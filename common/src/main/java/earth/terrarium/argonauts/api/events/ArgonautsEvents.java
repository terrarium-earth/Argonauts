package earth.terrarium.argonauts.api.events;

import earth.terrarium.argonauts.api.teams.MemberStatus;
import earth.terrarium.argonauts.api.teams.guild.Guild;
import earth.terrarium.argonauts.api.teams.party.Party;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class ArgonautsEvents {

    private static final List<CreateGuildEvent> CREATE_GUILD_EVENT = new ArrayList<>();
    private static final List<RemoveGuildEvent> REMOVE_GUILD_EVENT = new ArrayList<>();
    private static final List<GuildChangedEvent> GUILD_CHANGED_EVENT = new ArrayList<>();
    private static final List<ModifyGuildMemberEvent> MODIFY_GUILD_MEMBER_EVENT = new ArrayList<>();
    private static final List<RemoveGuildMemberEvent> REMOVE_GUILD_MEMBER_EVENT = new ArrayList<>();

    private static final List<CreatePartyEvent> CREATE_PARTY_EVENT = new ArrayList<>();
    private static final List<RemovePartyEvent> REMOVE_PARTY_EVENT = new ArrayList<>();
    private static final List<PartyChangedEvent> PARTY_CHANGED_EVENT = new ArrayList<>();
    private static final List<ModifyPartyMemberEvent> MODIFY_PARTY_MEMBER_EVENT = new ArrayList<>();
    private static final List<RemovePartyMemberEvent> REMOVE_PARTY_MEMBER_EVENT = new ArrayList<>();

    private static final List<OnTeleport> ON_TELEPORT = new ArrayList<>();

    @FunctionalInterface
    public interface CreateGuildEvent {

        void createGuild(Level level, Guild guild);

        static void register(CreateGuildEvent listener) {
            CREATE_GUILD_EVENT.add(listener);
        }

        @ApiStatus.Internal
        static void fire(Level level, Guild guild) {
            for (var listener : CREATE_GUILD_EVENT) {
                listener.createGuild(level, guild);
            }
        }
    }

    @FunctionalInterface
    public interface RemoveGuildEvent {

        void removeGuild(Level level, Guild guild);

        static void register(RemoveGuildEvent listener) {
            REMOVE_GUILD_EVENT.add(listener);
        }

        @ApiStatus.Internal
        static void fire(Level level, Guild guild) {
            for (var listener : REMOVE_GUILD_EVENT) {
                listener.removeGuild(level, guild);
            }
        }
    }

    @FunctionalInterface
    public interface GuildChangedEvent {

        void guildChanged(Level level, Guild guild);

        static void register(GuildChangedEvent listener) {
            GUILD_CHANGED_EVENT.add(listener);
        }

        @ApiStatus.Internal
        static void fire(Level level, Guild guild) {
            for (var listener : GUILD_CHANGED_EVENT) {
                listener.guildChanged(level, guild);
            }
        }
    }

    @FunctionalInterface
    public interface ModifyGuildMemberEvent {

        /**
         * Called when the player is added to the guild or their guild status is changed.
         */
        void modifyGuildMember(Level level, Guild guild, UUID player, MemberStatus status);

        static void register(ModifyGuildMemberEvent listener) {
            MODIFY_GUILD_MEMBER_EVENT.add(listener);
        }

        @ApiStatus.Internal
        static void fire(Level level, Guild guild, UUID player, MemberStatus status) {
            for (var listener : MODIFY_GUILD_MEMBER_EVENT) {
                listener.modifyGuildMember(level, guild, player, status);
            }
        }
    }

    @FunctionalInterface
    public interface RemoveGuildMemberEvent {

        void removeGuildMember(Level level, Guild guild, UUID player);

        static void register(RemoveGuildMemberEvent listener) {
            REMOVE_GUILD_MEMBER_EVENT.add(listener);
        }

        @ApiStatus.Internal
        static void fire(Level level, Guild guild, UUID player) {
            for (var listener : REMOVE_GUILD_MEMBER_EVENT) {
                listener.removeGuildMember(level, guild, player);
            }
        }
    }

    @FunctionalInterface
    public interface CreatePartyEvent {

        void createParty(Level level, Party party);

        static void register(CreatePartyEvent listener) {
            CREATE_PARTY_EVENT.add(listener);
        }

        @ApiStatus.Internal
        static void fire(Level level, Party party) {
            for (var listener : CREATE_PARTY_EVENT) {
                listener.createParty(level, party);
            }
        }
    }

    @FunctionalInterface
    public interface RemovePartyEvent {

        void removeParty(Level level, Party party);

        static void register(RemovePartyEvent listener) {
            REMOVE_PARTY_EVENT.add(listener);
        }

        @ApiStatus.Internal
        static void fire(Level level, Party party) {
            for (var listener : REMOVE_PARTY_EVENT) {
                listener.removeParty(level, party);
            }
        }
    }

    @FunctionalInterface
    public interface PartyChangedEvent {

        void partyChanged(Level level, Party party);

        static void register(PartyChangedEvent listener) {
            PARTY_CHANGED_EVENT.add(listener);
        }

        @ApiStatus.Internal
        static void fire(Level level, Party party) {
            for (var listener : PARTY_CHANGED_EVENT) {
                listener.partyChanged(level, party);
            }
        }
    }

    @FunctionalInterface
    public interface ModifyPartyMemberEvent {

        /**
         * Called when the player is added to the party or their party status is changed.
         */
        void modifyPartyMember(Level level, Party party, UUID player, MemberStatus status);

        static void register(ModifyPartyMemberEvent listener) {
            MODIFY_PARTY_MEMBER_EVENT.add(listener);
        }

        @ApiStatus.Internal
        static void fire(Level level, Party party, UUID player, MemberStatus status) {
            for (var listener : MODIFY_PARTY_MEMBER_EVENT) {
                listener.modifyPartyMember(level, party, player, status);
            }
        }
    }

    @FunctionalInterface
    public interface RemovePartyMemberEvent {

        void removePartyMember(Level level, Party party, UUID player);

        static void register(RemovePartyMemberEvent listener) {
            REMOVE_PARTY_MEMBER_EVENT.add(listener);
        }

        @ApiStatus.Internal
        static void fire(Level level, Party party, UUID player) {
            for (var listener : REMOVE_PARTY_MEMBER_EVENT) {
                listener.removePartyMember(level, party, player);
            }
        }
    }

    @FunctionalInterface
    public interface OnTeleport {

        /**
         * @return false if the teleport command should be cancelled.
         */
        boolean onTeleport(ServerPlayer player, BlockPos pos);

        static void register(OnTeleport listener) {
            ON_TELEPORT.add(listener);
        }

        @ApiStatus.Internal
        static boolean fire(ServerPlayer player, BlockPos pos) {
            for (var listener : ON_TELEPORT) {
                if (!listener.onTeleport(player, pos)) {
                    return false;
                }
            }
            return true;
        }
    }
}
