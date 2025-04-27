package earth.terrarium.argonauts.api.teams.settings.types;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.teamresourceful.resourcefullib.common.color.Color;
import earth.terrarium.argonauts.api.teams.settings.Setting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public record ColorSettings(String id, Color value) implements Setting<Color> {
    public static final SimpleCommandExceptionType INVALID_COLOR = new SimpleCommandExceptionType(() -> "Invalid color");

    public ArgumentBuilder<CommandSourceStack, ?> createArgument(String argument) {
        return Commands.argument(argument, StringArgumentType.word());
    }

    public Setting<Color> getFromArgument(String argument, CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        String color = context.getArgument(argument, String.class);
        Color parsedColor = Color.tryParse(color);
        if (parsedColor == null) {
            throw INVALID_COLOR.create();
        } else {
            return new ColorSettings(this.id, parsedColor);
        }
    }

    public void serialize(CompoundTag tag) {
        tag.putString("color", value.toString());
    }

    public Setting<Color> deserialize(CompoundTag tag) {
        return new ColorSettings(this.id, Color.parse(tag.getString("color")));
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        Color.BYTE_CODEC.encode(value, buf);
    }

    @Override
    public Setting<Color> decode(FriendlyByteBuf buf) {
        return new ColorSettings(id, Color.BYTE_CODEC.decode(buf));
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
