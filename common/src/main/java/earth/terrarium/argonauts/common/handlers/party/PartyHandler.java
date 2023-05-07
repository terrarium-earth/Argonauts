package earth.terrarium.argonauts.common.handlers.party;

import earth.terrarium.argonauts.common.handlers.base.MemberException;
import earth.terrarium.argonauts.common.handlers.chat.ChatHandler;
import earth.terrarium.argonauts.common.handlers.chat.ChatMessageType;
import earth.terrarium.argonauts.common.utils.ModUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;

public class PartyHandler {

    private static final Map<UUID, Party> PARTIES = new HashMap<>();
    private static final Map<UUID, UUID> PLAYER_PARTIES = new HashMap<>();

    public static UUID createParty(Player player) throws MemberException {
        if (PLAYER_PARTIES.containsKey(player.getUUID())) {
            throw MemberException.ALREADY_IN_PARTY;
        }
        UUID id = ModUtils.generate(Predicate.not(PARTIES::containsKey), UUID::randomUUID);
        PARTIES.put(id, new Party(id, player));
        PLAYER_PARTIES.put(player.getUUID(), id);
        player.displayClientMessage(Component.translatable("text.argonauts.member.party_create", player.getName().getString()), false);
        return id;
    }

    /**
     * Returns the party with the given id.
     */
    @Nullable
    public static Party get(UUID id) {
        return PARTIES.get(id);
    }

    /**
     * Returns the party the player is in.
     */
    @Nullable
    public static Party get(Player player) {
        return getPlayerParty(player.getUUID());
    }

    /**
     * Returns the party the player is in.
     */
    @Nullable
    public static Party getPlayerParty(UUID player) {
        return get(PLAYER_PARTIES.get(player));
    }

    public static void join(Party party, ServerPlayer player) throws MemberException {
        if (PLAYER_PARTIES.containsKey(player.getUUID())) {
            throw MemberException.ALREADY_IN_PARTY;
        } else if (party.ignored().has(player.getUUID())) {
            throw MemberException.NOT_ALLOWED_TO_JOIN_PARTY;
        } else if (party.isPublic() || party.members().isInvited(player.getUUID())) {
            for (ServerPlayer teamMember : player.server.getPlayerList().getPlayers()) {
                if (party.members().isMember(teamMember.getUUID())) {
                    teamMember.displayClientMessage(Component.translatable("text.argonauts.member.party_perspective_join", player.getName().getString(), party.members().getLeader().profile().getName()), false);
                }
            }

            party.members().add(player.getGameProfile());
            PLAYER_PARTIES.put(player.getUUID(), party.id());
            player.displayClientMessage(Component.translatable("text.argonauts.member.party_join", party.members().getLeader().profile().getName()), false);
        } else {
            throw MemberException.NOT_ALLOWED_TO_JOIN_PARTY;
        }
    }

    public static void remove(Party party, MinecraftServer server) {
        PARTIES.remove(party.id());
        party.members().forEach(member -> {
            if (PLAYER_PARTIES.get(member.profile().getId()) == party.id()) {
                PLAYER_PARTIES.remove(member.profile().getId());
            }
        });
        ChatHandler.remove(party, ChatMessageType.PARTY);
        ServerPlayer player = server.getPlayerList().getPlayer(party.members().getLeader().profile().getId());
        if (player == null) return;
        player.displayClientMessage(Component.translatable("text.argonauts.member.party_disband", player.getName().getString()), false);
    }

    public static void leave(UUID id, ServerPlayer player) throws MemberException {
        Party party = get(id);
        if (party == null) {
            throw MemberException.PARTY_DOES_NOT_EXIST;
        } else if (PLAYER_PARTIES.get(player.getUUID()) != id) {
            throw MemberException.PLAYER_IS_NOT_IN_PARTY;
        } else if (party.members().isLeader(player.getUUID())) {
            throw MemberException.CANT_REMOVE_PARTY_LEADER;
        }
        PLAYER_PARTIES.remove(player.getUUID());
        party.members().remove(player.getUUID());

        player.displayClientMessage(Component.translatable("text.argonauts.member.guild_leave", party.members().getLeader().profile().getName()), false);

        for (ServerPlayer teamMember : player.server.getPlayerList().getPlayers()) {
            if (party.members().isMember(teamMember.getUUID())) {
                teamMember.displayClientMessage(Component.translatable("text.argonauts.member.party_perspective_leave", player.getName().getString(), party.members().getLeader().profile().getName()), false);
            }
        }
    }
}
