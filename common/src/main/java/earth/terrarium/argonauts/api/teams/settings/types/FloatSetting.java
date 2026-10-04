package earth.terrarium.argonauts.api.teams.settings.types;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import earth.terrarium.argonauts.api.teams.settings.Setting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public record FloatSetting(String id, Float value) implements Setting<Float> {

    @Override
    public ArgumentBuilder<CommandSourceStack, ?> createArgument(String argument) {
        return Commands.argument(argument, FloatArgumentType.floatArg());
    }

    @Override
    public Setting<Float> getFromArgument(String argument, CommandContext<CommandSourceStack> context) {
        return new FloatSetting(id, FloatArgumentType.getFloat(context, argument));
    }

    @Override
    public void serialize(CompoundTag tag) {
        tag.putFloat(id, value);
    }

    @Override
    public Setting<Float> deserialize(CompoundTag tag) {
        return new FloatSetting(id, tag.getFloat(id));
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeFloat(value);
    }

    @Override
    public Setting<Float> decode(FriendlyByteBuf buf) {
        return new FloatSetting(id, buf.readFloat());
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
