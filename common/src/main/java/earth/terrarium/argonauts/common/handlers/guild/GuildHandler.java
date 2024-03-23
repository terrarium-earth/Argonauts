package earth.terrarium.argonauts.common.handlers.guild;

import com.teamresourceful.resourcefullib.common.utils.CommonUtils;
import com.teamresourceful.resourcefullib.common.utils.SaveHandler;
import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.argonauts.api.guild.Guild;
import earth.terrarium.argonauts.api.guild.GuildApi;
import earth.terrarium.argonauts.common.compat.cadmus.CadmusIntegration;
import earth.terrarium.argonauts.common.compat.heracles.HeraclesIntegration;
import earth.terrarium.argonauts.common.handlers.base.MemberException;
import earth.terrarium.argonauts.common.handlers.base.members.Member;
import earth.terrarium.argonauts.common.handlers.guild.members.GuildMembers;
import earth.terrarium.argonauts.common.handlers.guild.settings.GuildSettings;
import earth.terrarium.argonauts.common.network.messages.ClientboundSyncGuildsPacket;
import earth.terrarium.argonauts.common.utils.EventUtils;
import earth.terrarium.argonauts.common.utils.ModUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;

public class GuildHandler extends SaveHandler implements GuildApi {

    private final Map<UUID, Guild> guilds = new HashMap<>();
    private final Map<UUID, UUID> playerGuilds = new HashMap<>();

    @Override
    public void loadData(CompoundTag tag) {
        for (String key : tag.getAllKeys()) {
            UUID id = UUID.fromString(key);
            CompoundTag guildTag = tag.getCompound(key);
            CompoundTag settingsTag = guildTag.getCompound("settings");
            CompoundTag membersTag = guildTag.getCompound("members");
            CompoundTag alliesTag = guildTag.getCompound("allies");
            ListTag fakePlayersTag = guildTag.getList("fakePlayers", Tag.TAG_STRING);
            GuildSettings settings = new GuildSettings();
            if (!settingsTag.getCompound("hq").isEmpty()) {
                settings.setHq(ModUtils.readGlobalPos(settingsTag.getCompound("hq")));
            }
            var name = Component.Serializer.fromJson(settingsTag.getString("name"));
            settings.setDisplayName(name == null ? CommonComponents.EMPTY : name);
            var motd = Component.Serializer.fromJson(settingsTag.getString("motd"));
            settings.setMotd(motd == null ? CommonComponents.EMPTY : motd);
            settings.setColor(ChatFormatting.getById(settingsTag.getByte("color")));
            settings.setAllowFakePlayers(settingsTag.getBoolean("allowFakePlayers"));
            GuildMembers members = new GuildMembers(ModUtils.readBasicProfile(guildTag.getCompound("owner")));
            membersTag.getAllKeys().forEach(memberTag ->
                members.add(ModUtils.readBasicProfile(membersTag.getCompound(memberTag)))
            );
            alliesTag.getAllKeys().forEach(allyTag ->
                members.ally(ModUtils.readBasicProfile(alliesTag.getCompound(allyTag)))
            );
            for (Tag tag1 : fakePlayersTag) {
                String uuidText = tag1.getAsString();
                UUID uuid = ModUtils.parseUuidOrNull(uuidText);
                if (uuid != null) {
                    members.fakePlayers().add(uuid);
                } else {
                    Argonauts.LOGGER.warn("Failed to parse fake player uuid of {} in guild {}", uuidText, id);
                }
            }
            Guild guild = new Guild(id, settings, members);
            guilds.put(id, guild);
            this.updateInternal();
        }
    }

    @Override
    public void saveData(CompoundTag tag) {
        guilds.forEach((uuid, guild) -> {
            CompoundTag guildTag = new CompoundTag();
            CompoundTag settingsTag = new CompoundTag();
            CompoundTag membersTag = new CompoundTag();
            CompoundTag alliesTag = new CompoundTag();
            ListTag fakePlayersTag = new ListTag();
            settingsTag.put("hq", guild.settings().hq().isPresent() ? ModUtils.writeGlobalPos(guild.settings().hq().get()) : new CompoundTag());
            settingsTag.putString("name", Component.Serializer.toJson(guild.settings().displayName()));
            settingsTag.putString("motd", Component.Serializer.toJson(guild.settings().motd()));
            settingsTag.putByte("color", (byte) guild.settings().color().getId());
            settingsTag.putBoolean("allowFakePlayers", guild.settings().allowFakePlayers());
            guildTag.put("settings", settingsTag);
            guildTag.put("owner", ModUtils.writeBasicProfile(guild.members().getLeader().profile()));
            guild.members().forEach(member -> membersTag.put(member.profile().getId().toString(), ModUtils.writeBasicProfile(member.profile())));
            guild.members().allies().forEach(member -> alliesTag.put(member.profile().getId().toString(), ModUtils.writeBasicProfile(member.profile())));
            guild.members().fakePlayers().forEach(fakePlayer -> fakePlayersTag.add(StringTag.valueOf(fakePlayer.toString())));
            guildTag.put("members", membersTag);
            guildTag.put("allies", alliesTag);
            guildTag.put("fakePlayers", fakePlayersTag);
            tag.put(uuid.toString(), guildTag);
        });
    }

    public static GuildHandler read(MinecraftServer server) {
        return read(server.overworld().getDataStorage(), HandlerType.create(GuildHandler::new), "argonauts_guilds");
    }

    @Override
    public void createGuild(ServerPlayer player, Component displayName) throws MemberException {
        var data = read(player.server);
        if (data.playerGuilds.containsKey(player.getUUID())) {
            throw MemberException.ALREADY_IN_GUILD;
        }
        UUID id = CommonUtils.generate(Predicate.not(data.guilds::containsKey), UUID::randomUUID);
        Guild guild = new Guild(id, player);
        guild.settings().setDisplayName(displayName);
        data.guilds.put(id, guild);
        data.playerGuilds.put(player.getUUID(), id);
        ModUtils.sendToAllClientPlayers(new ClientboundSyncGuildsPacket(Set.of(guild), Set.of()), player.server);
        player.displayClientMessage(CommonUtils.serverTranslatable("text.argonauts.member.guild_create", guild.settings().displayName().getString()), false);

        EventUtils.created(guild, player);
        if (Argonauts.isCadmusLoaded()) {
            CadmusIntegration.addToCadmusTeam(player, id.toString());
        }
        if (Argonauts.isHeraclesLoaded()) {
            HeraclesIntegration.updateHeraclesChanger(player);
        }
    }

    @Nullable
    @Override
    public Guild get(MinecraftServer server, UUID id) {
        return read(server).guilds.get(id);
    }

    @Nullable
    @Override
    public Guild get(ServerPlayer player) {
        return getPlayerGuild(player.server, player.getUUID());
    }

    @Nullable
    @Override
    public Guild getPlayerGuild(MinecraftServer server, UUID player) {
        var data = read(server);
        return data.guilds.get(data.playerGuilds.get(player));
    }

    @Override
    public Collection<Guild> getAll(MinecraftServer server) {
        return read(server).guilds.values();
    }

    @Override
    public void tryJoin(Guild guild, ServerPlayer player) throws MemberException {
        if (guild.isPublic() || guild.members().isInvited(player.getUUID())) {
            join(guild, player);
        } else {
            throw MemberException.NOT_ALLOWED_TO_JOIN_GUILD;
        }
    }

    @Override
    public void join(Guild guild, ServerPlayer player) throws MemberException {
        var data = read(player.server);
        if (data.playerGuilds.containsKey(player.getUUID())) {
            throw MemberException.ALREADY_IN_GUILD;
        }
        for (Member member : guild.members()) {
            ServerPlayer serverPlayer = player.server.getPlayerList().getPlayer(member.profile().getId());
            if (serverPlayer == null) continue;
            serverPlayer.displayClientMessage(CommonUtils.serverTranslatable("text.argonauts.member.guild_perspective_join", player.getName().getString(), guild.settings().displayName().getString()), false);
        }

        guild.members().add(player.getGameProfile());
        data.playerGuilds.put(player.getUUID(), guild.id());
        ModUtils.sendToAllClientPlayers(new ClientboundSyncGuildsPacket(Set.of(guild), Set.of()), player.server);
        player.displayClientMessage(CommonUtils.serverTranslatable("text.argonauts.member.guild_join", guild.settings().displayName().getString()), false);

        EventUtils.joined(guild, player);
        if (Argonauts.isCadmusLoaded()) {
            CadmusIntegration.addToCadmusTeam(player, guild.id().toString());
        }
        if (Argonauts.isHeraclesLoaded()) {
            HeraclesIntegration.updateHeraclesChanger(player);
        }
    }

    @Override
    public void leave(UUID id, ServerPlayer player) throws MemberException {
        var data = read(player.server);
        Guild guild = get(player.server, id);
        if (guild == null) {
            throw MemberException.GUILD_DOES_NOT_EXIST;
        } else if (data.playerGuilds.get(player.getUUID()) != id) {
            throw MemberException.PLAYER_IS_NOT_IN_GUILD;
        } else if (guild.members().isLeader(player.getUUID())) {
            throw MemberException.YOU_CANT_REMOVE_GUILD_OWNER;
        }
        guild.members().remove(player.getUUID());

        player.displayClientMessage(CommonUtils.serverTranslatable("text.argonauts.member.guild_leave", guild.settings().displayName().getString()), false);

        for (Member member : guild.members()) {
            ServerPlayer serverPlayer = player.server.getPlayerList().getPlayer(member.profile().getId());
            if (serverPlayer == null) continue;
            serverPlayer.displayClientMessage(CommonUtils.serverTranslatable("text.argonauts.member.guild_perspective_leave", player.getName().getString(), guild.settings().displayName().getString()), false);
        }
        data.playerGuilds.remove(player.getUUID());
        ModUtils.sendToAllClientPlayers(new ClientboundSyncGuildsPacket(Set.of(guild), Set.of()), player.server);

        EventUtils.left(guild, player);
        if (Argonauts.isCadmusLoaded()) {
            CadmusIntegration.removeFromCadmusTeam(player, id.toString());
        }
        if (Argonauts.isHeraclesLoaded()) {
            HeraclesIntegration.updateHeraclesChanger(player);
        }
    }

    @Override
    public void disband(Guild guild, MinecraftServer server) {
        ServerPlayer player = server.getPlayerList().getPlayer(guild.members().getLeader().profile().getId());
        if (player == null) return;
        player.displayClientMessage(CommonUtils.serverTranslatable("text.argonauts.member.guild_disband", guild.settings().displayName().getString()), false);
        EventUtils.disbanned(guild);
        remove(false, guild, server);
    }

    @Override
    public void remove(boolean force, Guild guild, MinecraftServer server) {
        var data = read(server);
        data.guilds.remove(guild.id());
        ModUtils.sendToAllClientPlayers(new ClientboundSyncGuildsPacket(Set.of(), Set.of(guild.id())), server);
        EventUtils.removed(force, guild);
        if (Argonauts.isCadmusLoaded()) {
            CadmusIntegration.disbandCadmusTeam(guild, server);
        }
        if (Argonauts.isHeraclesLoaded()) {
            guild.members().forEach(m -> HeraclesIntegration.updateHeraclesChanger(server.overworld(), m.profile().getId()));
        }
        data.updateInternal();
    }

    private void updateInternal() {
        playerGuilds.clear();
        guilds.values().forEach(team ->
            team.members().forEach(member ->
                playerGuilds.put(member.profile().getId(), team.id())));
    }

    @Override
    public boolean isDirty() {
        return true;
    }
}