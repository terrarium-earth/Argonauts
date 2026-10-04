package earth.terrarium.argonauts.api.teams;

import com.teamresourceful.resourcefullib.common.color.Color;
import earth.terrarium.argonauts.api.teams.settings.Setting;
import earth.terrarium.argonauts.common.permissions.Permissions;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public interface Team {

    /**
     * Gets the ID of the team.
     *
     * @return the ID
     */
    UUID id();

    /**
     * Gets the members of the team.
     *
     * @return the members and their status
     */
    Map<UUID, Member> members();

    /**
     * Gets or creates a member of the team.
     *
     * @param player the player
     * @return the member
     */
    Member getOrCreateMember(UUID player);

    /**
     * Gets the settings of the team.
     *
     * @return the settings
     */
    Map<String, Setting<?>> settings();

    /**
     * Checks if the team is public.
     *
     * @return if the team is public
     */
    boolean isPublic();

    /**
     * Gets the display name of the team.
     *
     * @return the display name
     */
    Component displayName();

    /**
     * Gets the color of the team.
     *
     * @return the color
     */
    Color color();

    /**
     * Gets the type of the team. GUILD for guilds, PARTY for parties.
     *
     * @return the type
     */
    String type();

    /**
     * Gets the online members of the team.
     *
     * @param level the level
     * @return the online members
     */
    default List<Player> onlineMembers(Level level) {
        return this.members().entrySet().stream()
            .filter(entry -> entry.getValue().status().isMember())
            .map(Map.Entry::getKey)
            .map(level::getPlayerByUUID)
            .filter(Objects::nonNull)
            .toList();
    }

    /**
     * Gets the number of members on the team.
     *
     * @return the members count
     */
    default int realMembersCount() {
        return this.members().values()
            .stream()
            .filter(member -> member.status().isMember())
            .mapToInt(member -> 1)
            .sum();
    }

    /**
     * Checks if the player has the permission.
     *
     * @param player     the player
     * @param permission the permission
     * @return True if the player has the permission, or false if aren't a member or don't have the permission.
     */
    default boolean hasPermission(UUID player, String permission) {
        Member member = this.members().get(player);
        return member != null && member.status().isMember() && (member.isOwner() ||
            member.hasPermission(Permissions.OPERATOR) ||
            member.hasPermission(permission));
    }

    /**
     * Checks if the player can manage the members of the team.
     *
     * @param player the player
     * @return if the player can manage the members
     */
    default boolean canManageMembers(UUID player) {
        return hasPermission(player, Permissions.MANAGE_MEMBERS);
    }

    /**
     * Checks if the player can manage the settings of the team.
     *
     * @param player the player
     * @return if the player can manage the settings
     */
    default boolean canManageSettings(UUID player) {
        return hasPermission(player, Permissions.MANAGE_SETTINGS);
    }

    /**
     * Checks if the player can manage the permissions of the team.
     *
     * @param player the player
     * @return if the player can manage the permissions
     */
    default boolean canManagePermissions(UUID player) {
        return hasPermission(player, Permissions.MANAGE_PERMISSIONS);
    }

    /**
     * Gets the owner of the team. Use sparingly.
     *
     * @return the owner
     * @throws IllegalStateException if the owner is not found.
     */
    default UUID getOwner() {
        return this.members().entrySet().stream()
            .filter(entry -> entry.getValue().status().isOwner())
            .map(Map.Entry::getKey)
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("Team has no owner"));
    }

    /**
     * Checks if the player is the owner of the team.
     *
     * @param player the player
     * @return if the player is the owner
     */
    default boolean isOwner(UUID player) {
        Member member = this.members().get(player);
        return member != null && member.isOwner();
    }

    /**
     * Checks if the player is a member of the team.
     *
     * @param player the player
     * @return if the player is a member
     */
    default boolean isMember(UUID player) {
        Member member = this.members().get(player);
        return member != null && member.status().isMember();
    }

    /**
     * Checks if the player is invited to the team.
     *
     * @param player the player
     * @return if the player is invited
     */
    default boolean isInvited(UUID player) {
        Member member = this.members().get(player);
        return member != null && member.status().isInvited();
    }
}
