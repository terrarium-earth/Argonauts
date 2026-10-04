package earth.terrarium.argonauts.api.teams.settings.types;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import earth.terrarium.argonauts.api.teams.settings.Setting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public record IntegerSetting(String id, Integer value) implements Setting<Integer> {

    @Override
    public ArgumentBuilder<CommandSourceStack, ?> createArgument(String argument) {
        return Commands.argument(argument, IntegerArgumentType.integer());
    }

    @Override
    public Setting<Integer> getFromArgument(String argument, CommandContext<CommandSourceStack> context) {
        return new IntegerSetting(id, IntegerArgumentType.getInteger(context, argument));
    }

    @Override
    public void serialize(CompoundTag tag) {
        tag.putInt(id, value);
    }

    @Override
    public Setting<Integer> deserialize(CompoundTag tag) {
        return new IntegerSetting(id, tag.getInt(id));
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(value);
    }

    @Override
    public Setting<Integer> decode(FriendlyByteBuf buf) {
        return new IntegerSetting(id, buf.readVarInt());
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
