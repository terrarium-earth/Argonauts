package earth.terrarium.argonauts.common.commands.guild;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import earth.terrarium.argonauts.api.guild.Guild;
import earth.terrarium.argonauts.api.guild.GuildApi;
import earth.terrarium.argonauts.common.handlers.base.MemberException;
import net.minecraft.server.level.ServerPlayer;

public final class GuildCommandHelper {
    public static Guild getGuildOrThrow(ServerPlayer player, boolean otherPlayer) throws CommandSyntaxException {
        Guild guild = GuildApi.API.get(player);
        if (guild == null) throw new SimpleCommandExceptionType(otherPlayer ?
            MemberException.PLAYER_IS_NOT_IN_GUILD.message() :
            MemberException.YOU_ARE_NOT_IN_GUILD.message()).create();
        return guild;
    }
}
