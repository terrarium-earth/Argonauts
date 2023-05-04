package earth.terrarium.argonauts.common.handlers.guild;

import earth.terrarium.argonauts.common.handlers.guild.members.GuildMembers;
import earth.terrarium.argonauts.common.handlers.guild.settings.GuildSettings;
import earth.terrarium.argonauts.common.utils.ModUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GuildHandler extends SavedData {

    private final Map<UUID, Guild> guilds = new HashMap<>();
    private final Map<UUID, UUID> playerGuilds = new HashMap<>();

    public void load(CompoundTag tag) {
        for (String key : tag.getAllKeys()) {
            UUID id = UUID.fromString(key);
            CompoundTag guildTag = tag.getCompound(key);
            CompoundTag settingsTag = guildTag.getCompound("settings");
            CompoundTag membersTag = guildTag.getCompound("members");
            GuildSettings settings = new GuildSettings();
            settings.setHq(ModUtils.readGlobalPos(settingsTag.getCompound("hq")));
            settings.setName(Component.Serializer.fromJson(settingsTag.getString("name")));
            settings.setIsPublic(settingsTag.getBoolean("public"));
            GuildMembers members = new GuildMembers(ModUtils.readBasicProfile(tag.getCompound("owner")));
            membersTag.getList("members", Tag.TAG_COMPOUND).forEach(memberTag ->
                members.add(ModUtils.readBasicProfile((CompoundTag) memberTag))
            );

        }
    }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag tag) {
        return null;
    }
}
