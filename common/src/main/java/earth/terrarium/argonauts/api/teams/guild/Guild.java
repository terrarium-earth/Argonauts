package earth.terrarium.argonauts.api.teams.guild;

import com.teamresourceful.bytecodecs.base.ByteCodec;
import com.teamresourceful.bytecodecs.base.object.ObjectByteCodec;
import com.teamresourceful.bytecodecs.defaults.MapCodec;
import com.teamresourceful.resourcefullib.common.color.Color;
import earth.terrarium.argonauts.api.teams.Member;
import earth.terrarium.argonauts.api.teams.MemberStatus;
import earth.terrarium.argonauts.api.teams.Team;
import earth.terrarium.argonauts.api.teams.permissions.MemberPermissionsApi;
import earth.terrarium.argonauts.api.teams.settings.Setting;
import earth.terrarium.argonauts.api.teams.settings.types.ColorSettings;
import earth.terrarium.argonauts.api.teams.settings.types.StringSetting;
import earth.terrarium.argonauts.common.settings.Settings;
import earth.terrarium.argonauts.common.utils.ModUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Guilds are a more permanent form of team. They are persistent across logins and feature role management, setting headquarters, displaying messages of the day, and more. Guilds also integrate with Cadmus for chunk claiming as a team, and Heracles for completing quests together.
 *
 * @param id       The guild ID
 * @param members  A map of members to their corresponding permissions and member status
 * @param settings A map of setting IDs to their corresponding values
 */
public record Guild(
    UUID id,
    Map<UUID, Member> members,
    Map<String, Setting<?>> settings
) implements Team {

    public static final ByteCodec<Guild> BYTE_CODEC = ObjectByteCodec.create(
        ByteCodec.UUID.fieldOf(Guild::id),
        new MapCodec<>(ByteCodec.UUID, Member.BYTE_CODEC).fieldOf(Guild::members),
        new MapCodec<>(ByteCodec.STRING, Setting.BYTE_CODEC).fieldOf(Guild::settings),
        Guild::new
    );

    public Guild(UUID creator, String name) {
        this(UUID.randomUUID(), new HashMap<>(), new HashMap<>());
        this.members.put(creator, new Member(MemberStatus.OWNER, MemberPermissionsApi.API.getGuildPermissions()));
        this.settings.put(Settings.DISPLAY_NAME.id(), new StringSetting(Settings.DISPLAY_NAME.id(), name));
        this.settings.put(Settings.COLOR.id(), new ColorSettings(Settings.COLOR.id(), ModUtils.uuidToColor(this.id)));
    }

    @Override
    public Member getOrCreateMember(UUID player) {
        return this.members.computeIfAbsent(player, id -> new Member(MemberStatus.MEMBER, MemberPermissionsApi.API.getGuildPermissions()));
    }

    @Override
    public boolean isPublic() {
        return Settings.PUBLIC.get(this);
    }

    @Override
    public Component displayName() {
        return Component.literal(Settings.DISPLAY_NAME.get(this)).withColor(this.color().getValue());
    }

    @Override
    public Color color() {
        return Settings.COLOR.get(this);
    }

    @Override
    public String type() {
        return "guild";
    }

    /**
     * Gets the allies of the team.
     *
     * @param level the level
     * @return the allies
     */
    public List<Player> allies(Level level) {
        return this.members().entrySet().stream()
            .filter(entry -> entry.getValue().status().isAllied())
            .map(Map.Entry::getKey)
            .map(level::getPlayerByUUID)
            .filter(Objects::nonNull)
            .toList();
    }

    /**
     * Checks if the player is an ally of the team.
     *
     * @param player the player
     * @return if the player is an ally
     */
    public boolean isAllied(UUID player) {
        Member member = this.members().get(player);
        return member != null && member.status().isAllied();
    }

    /**
     * Gets the fake players of the team. Use sparingly.
     *
     * @return the fake players
     */
    public Set<UUID> getFakePlayers() {
        return this.members().entrySet().stream()
            .filter(entry -> entry.getValue().status().isFakePlayer())
            .map(Map.Entry::getKey)
            .collect(Collectors.toSet());
    }

    /**
     * Checks if the player is a member or a fake player of the team.
     *
     * @param player the player
     * @return if the player is a member
     */
    public boolean isMemberOrFakePlayer(UUID player) {
        Member member = this.members().get(player);
        return member != null && (member.status().isMember() || member.status().isFakePlayer());
    }
}
