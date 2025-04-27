package earth.terrarium.argonauts.api.teams.settings.types;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import earth.terrarium.argonauts.api.teams.settings.Setting;
import earth.terrarium.argonauts.common.utils.ModUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

public record OptionalGlobalPosSetting(String id, Optional<GlobalPos> value) implements Setting<Optional<GlobalPos>> {

    @Override
    public ArgumentBuilder<CommandSourceStack, ?> createArgument(String argument) {
        return Commands.literal("set");
    }

    @Override
    public Setting<Optional<GlobalPos>> getFromArgument(String argument, CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        return new OptionalGlobalPosSetting(id, Optional.of(GlobalPos.of(context.getSource().getLevel().dimension(), player.blockPosition())));
    }

    @Override
    public void serialize(CompoundTag tag) {
        if (value.isEmpty()) return;
        tag.put(id, ModUtils.writeGlobalPos(value.get()));
    }

    @Override
    public Setting<Optional<GlobalPos>> deserialize(CompoundTag tag) {
        return new OptionalGlobalPosSetting(id, Optional.ofNullable(ModUtils.readGlobalPos(tag.getCompound(id))));
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeOptional(value, FriendlyByteBuf::writeGlobalPos);
    }

    @Override
    public Setting<Optional<GlobalPos>> decode(FriendlyByteBuf buf) {
        return new OptionalGlobalPosSetting(id, buf.readOptional(FriendlyByteBuf::readGlobalPos));
    }

    @Override
    public boolean hidden() {
        return true;
    }

    @Override
    public String toString() {
        GlobalPos globalPos = value.orElse(null);
        if (globalPos == null) return "\"\"";
        BlockPos pos = globalPos.pos();
        return "[%s, %s, %s]".formatted(pos.getX(), pos.getY(), pos.getZ());
    }
}
