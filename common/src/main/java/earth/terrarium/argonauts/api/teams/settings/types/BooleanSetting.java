package earth.terrarium.argonauts.api.teams.settings.types;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import earth.terrarium.argonauts.api.teams.settings.Setting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public record BooleanSetting(String id, Boolean value) implements Setting<Boolean> {

    @Override
    public ArgumentBuilder<CommandSourceStack, ?> createArgument(String argument) {
        return Commands.argument(argument, BoolArgumentType.bool());
    }

    @Override
    public Setting<Boolean> getFromArgument(String argument, CommandContext<CommandSourceStack> context) {
        return new BooleanSetting(id, BoolArgumentType.getBool(context, argument));
    }

    @Override
    public void serialize(CompoundTag tag) {
        tag.putBoolean(id, value);
    }

    @Override
    public Setting<Boolean> deserialize(CompoundTag tag) {
        return new BooleanSetting(id, tag.getBoolean(id));
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(value);
    }

    @Override
    public Setting<Boolean> decode(FriendlyByteBuf buf) {
        return new BooleanSetting(id, buf.readBoolean());
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
