package earth.terrarium.argonauts.common.commands.guild;

import com.mojang.brigadier.CommandDispatcher;
import earth.terrarium.argonauts.api.guild.Guild;
import earth.terrarium.argonauts.api.guild.GuildApi;
import earth.terrarium.argonauts.common.commands.base.CommandHelper;
import earth.terrarium.argonauts.common.constants.ConstantComponents;
import earth.terrarium.argonauts.common.handlers.base.MemberException;
import earth.terrarium.argonauts.common.handlers.base.MemberPermissions;
import earth.terrarium.argonauts.common.handlers.base.members.Member;
import earth.terrarium.argonauts.common.menus.guild.GuildMembersContent;
import earth.terrarium.argonauts.common.network.NetworkHandler;
import earth.terrarium.argonauts.common.network.messages.ClientboundOpenGuildMemberMenuPacket;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

public final class GuildMemberCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        CommandHelper.register(dispatcher,
            "guild",
            "members",
            player -> openMembersScreen(player, -1));
    }

    public static void openMembersScreen(ServerPlayer player, int selected) throws MemberException {
        if (!NetworkHandler.CHANNEL.canSendToPlayer(player, ClientboundOpenGuildMemberMenuPacket.TYPE))
            throw MemberException.NOT_INSTALLED_ON_CLIENT;

        Guild guild = GuildApi.API.get(player);
        if (guild == null) throw MemberException.YOU_ARE_NOT_IN_GUILD;

        Member member = guild.getMember(player);

        NetworkHandler.CHANNEL.sendToPlayer(new ClientboundOpenGuildMemberMenuPacket(
            new GuildMembersContent(
                guild.id(),
                selected,
                guild.members().allMembers(),
                member.hasPermission(MemberPermissions.MANAGE_MEMBERS),
                member.hasPermission(MemberPermissions.MANAGE_PERMISSIONS)),
            ConstantComponents.GUILD_MEMBERS_TITLE), player);
    }
}
