package earth.terrarium.argonauts.common.guild;

import com.teamresourceful.resourcefullib.common.utils.SaveHandler;
import earth.terrarium.argonauts.api.teams.Member;
import earth.terrarium.argonauts.api.teams.MemberStatus;
import earth.terrarium.argonauts.api.teams.guild.Guild;
import earth.terrarium.argonauts.api.teams.settings.Setting;
import earth.terrarium.argonauts.api.teams.settings.TeamSettingsApi;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class GuildSaveData extends SaveHandler {

    private static final GuildSaveData CLIENT_SIDE = new GuildSaveData();
    private final Map<UUID, Guild> guilds = new HashMap<>();
    private final Map<UUID, Guild> guildsByPlayer = new HashMap<>();

    @Override
    public void loadData(CompoundTag tag) {
        CompoundTag guildsTag = tag.getCompound("guilds");
        guildsTag.getAllKeys().forEach(id -> {
            CompoundTag guildTag = guildsTag.getCompound(id);
            Map<UUID, Member> members = new HashMap<>();
            Map<String, Setting<?>> settings = new HashMap<>();

            CompoundTag membersTag = guildTag.getCompound("members");
            membersTag.getAllKeys().forEach(memberId -> {
                UUID uuid = UUID.fromString(memberId);
                CompoundTag memberTag = membersTag.getCompound(memberId);
                MemberStatus status = MemberStatus.valueOf(memberTag.getString("status").toUpperCase(Locale.ROOT));
                CompoundTag permissionsTag = memberTag.getCompound("permissions");
                Object2BooleanMap<String> permissions = new Object2BooleanOpenHashMap<>();
                permissionsTag.getAllKeys().forEach(permission -> permissions.put(permission, permissionsTag.getBoolean(permission)));
                members.put(uuid, new Member(status, permissions));
            });

            CompoundTag settingsTag = guildTag.getCompound("settings");
            settingsTag.getAllKeys().forEach(settingId -> {
                Setting<?> setting = TeamSettingsApi.API.getDefaultValue(settingId).deserialize(settingsTag);
                settings.put(settingId, setting);
            });

            Guild guild = new Guild(UUID.fromString(id), members, settings);
            this.guilds.put(guild.id(), guild);
            members.forEach((memberId, member) -> {
                if (member.status().isMember()) {
                    this.guildsByPlayer.put(memberId, guild);
                }
            });
        });
    }

    @Override
    public void saveData(CompoundTag tag) {
        CompoundTag guildsTag = new CompoundTag();
        this.guilds.forEach((id, guild) -> {
            CompoundTag guildTag = new CompoundTag();

            CompoundTag membersTag = new CompoundTag();
            guild.members().forEach((memberId, member) -> {
                if (!member.status().isInvited()) {
                    CompoundTag memberTag = new CompoundTag();
                    memberTag.putString("status", member.status().name().toLowerCase(Locale.ROOT));
                    CompoundTag permissionsTag = new CompoundTag();
                    member.permissions().forEach(permissionsTag::putBoolean);
                    memberTag.put("permissions", permissionsTag);
                    membersTag.put(memberId.toString(), memberTag);
                }
            });
            guildTag.put("members", membersTag);

            CompoundTag settingsTag = new CompoundTag();
            guild.settings().forEach((name, setting) -> setting.serialize(settingsTag));
            guildTag.put("settings", settingsTag);

            guildsTag.put(id.toString(), guildTag);
        });
        tag.put("guilds", guildsTag);
    }

    public static GuildSaveData read(Level level) {
        return read(level, HandlerType.create(CLIENT_SIDE, GuildSaveData::new), "argonauts_guilds");
    }

    public Map<UUID, Guild> guilds() {
        return this.guilds;
    }

    public Map<UUID, Guild> guildsByPlayer() {
        return this.guildsByPlayer;
    }
}
