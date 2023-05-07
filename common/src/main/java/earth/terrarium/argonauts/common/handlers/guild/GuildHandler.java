package earth.terrarium.argonauts.common.handlers.guild;

import earth.terrarium.argonauts.common.handlers.base.MemberException;
import earth.terrarium.argonauts.common.handlers.chat.ChatHandler;
import earth.terrarium.argonauts.common.handlers.chat.ChatMessageType;
import earth.terrarium.argonauts.common.handlers.guild.members.GuildMembers;
import earth.terrarium.argonauts.common.handlers.guild.settings.GuildSettings;
import earth.terrarium.argonauts.common.utils.ModUtils;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;

public class GuildHandler extends SavedData {

    private final Map<UUID, Guild> guilds = new HashMap<>();
    private final Map<UUID, UUID> playerGuilds = new HashMap<>();

    public GuildHandler() {
    }

    public GuildHandler(CompoundTag tag) {
        for (String key : tag.getAllKeys()) {
            UUID id = UUID.fromString(key);
            CompoundTag guildTag = tag.getCompound(key);
            CompoundTag settingsTag = guildTag.getCompound("settings");
            CompoundTag membersTag = guildTag.getCompound("members");
            GuildSettings settings = new GuildSettings();
            settings.setHq(ModUtils.readGlobalPos(settingsTag.getCompound("hq")));
            settings.setDisplayName(Component.Serializer.fromJson(settingsTag.getString("name")));
            settings.setMotd(Component.Serializer.fromJson(settingsTag.getString("motd")));
            GuildMembers members = new GuildMembers(ModUtils.readBasicProfile(guildTag.getCompound("owner")));
            membersTag.getList("members", Tag.TAG_COMPOUND).forEach(memberTag ->
                members.add(ModUtils.readBasicProfile((CompoundTag) memberTag))
            );
            Guild guild = new Guild(id, settings, members);
            guilds.put(id, guild);
            this.updateInternal();
        }
    }

    @Override
    @NotNull
    public CompoundTag save(@NotNull CompoundTag tag) {
        guilds.forEach((uuid, guild) -> {
            CompoundTag guildTag = new CompoundTag();
            CompoundTag settingsTag = new CompoundTag();
            CompoundTag membersTag = new CompoundTag();
            settingsTag.put("hq", ModUtils.writeGlobalPos(guild.settings().hq()));
            settingsTag.putString("name", Component.Serializer.toJson(guild.settings().displayName()));
            settingsTag.putString("motd", Component.Serializer.toJson(guild.settings().motd()));
            guildTag.put("settings", settingsTag);
            guildTag.put("owner", ModUtils.writeBasicProfile(guild.members().getLeader().profile()));
            guild.members().forEach(member -> membersTag.put(member.profile().getId().toString(), ModUtils.writeBasicProfile(member.profile())));
            guildTag.put("members", membersTag);
            tag.put(uuid.toString(), guildTag);
        });
        return tag;
    }

    @Override
    public boolean isDirty() {
        return true;
    }

    public static GuildHandler read(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(GuildHandler::new, GuildHandler::new, "argonauts_guilds");
    }

    public static UUID createGuild(ServerPlayer player) throws MemberException {
        var data = read(player.server);
        if (data.playerGuilds.containsKey(player.getUUID())) {
            throw MemberException.ALREADY_IN_GUILD;
        }
        UUID id = ModUtils.generate(Predicate.not(data.guilds::containsKey), UUID::randomUUID);
        Guild guild = new Guild(id, player);
        guild.settings().setHq(GlobalPos.of(player.level.dimension(), player.blockPosition()));
        guild.settings().setDisplayName(Component.translatable("text.argonauts.guild_name", player.getName().getString()));
        data.guilds.put(id, guild);
        data.playerGuilds.put(player.getUUID(), id);
        player.displayClientMessage(Component.translatable("text.argonauts.member.guild_create", guild.settings().displayName().getString()), false);
        return id;
    }

    /**
     * Returns the guild with the given id.
     */
    @Nullable
    public static Guild get(MinecraftServer server, UUID id) {
        return read(server).guilds.get(id);
    }

    /**
     * Returns the guild the player is in.
     */
    @Nullable
    public static Guild get(ServerPlayer player) {
        return getPlayerGuild(player.server, player.getUUID());
    }

    /**
     * Returns the guild the player is in.
     */
    @Nullable
    public static Guild getPlayerGuild(MinecraftServer server, UUID player) {
        var data = read(server);
        return data.guilds.get(data.playerGuilds.get(player));
    }

    public static void join(Guild guild, ServerPlayer player) throws MemberException {
        var data = read(player.server);
        if (data.playerGuilds.containsKey(player.getUUID())) {
            throw MemberException.ALREADY_IN_GUILD;
        } else if (guild.isPublic() || guild.members().isInvited(player.getUUID())) {
            guild.members().add(player.getGameProfile());
            data.playerGuilds.put(player.getUUID(), guild.id());
            player.displayClientMessage(Component.translatable("text.argonauts.member.guild_join", guild.settings().displayName().getString()), false);
        } else {
            throw MemberException.NOT_ALLOWED_TO_JOIN_GUILD;
        }
    }

    public static void remove(Guild guild, MinecraftServer server) {
        var data = read(server);
        data.guilds.remove(guild.id());
        guild.members().forEach(member -> {
            if (data.playerGuilds.get(member.profile().getId()) == guild.id()) {
                data.playerGuilds.remove(member.profile().getId());
            }
        });
        ServerPlayer player = server.getPlayerList().getPlayer(guild.members().getLeader().profile().getId());
        if (player == null) return;
        player.displayClientMessage(Component.translatable("text.argonauts.member.guild_disband", guild.settings().displayName().getString()), false);
    }

    public static void remove(UUID id, ServerPlayer player) throws MemberException {
        var data = read(player.server);
        Guild guild = get(player.server, id);
        if (guild == null) {
            throw MemberException.GUILD_DOES_NOT_EXIST;
        } else if (data.playerGuilds.get(player.getUUID()) != id) {
            throw MemberException.PLAYER_IS_NOT_IN_GUILD;
        } else if (guild.members().isLeader(player.getUUID())) {
            throw MemberException.CANT_REMOVE_GUILD_OWNER;
        }
        data.playerGuilds.remove(player.getUUID());
        guild.members().remove(player.getUUID());
    }

    private void updateInternal() {
        playerGuilds.clear();
        guilds.values().forEach(team ->
            team.members().forEach(member ->
                playerGuilds.put(member.profile().getId(), team.id())));
    }
}