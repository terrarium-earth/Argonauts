package earth.terrarium.argonauts.api.teams.settings.types;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import earth.terrarium.argonauts.api.teams.settings.Setting;
import earth.terrarium.argonauts.common.utils.ModUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public record StringSetting(String id, String value) implements Setting<String> {

    @Override
    public ArgumentBuilder<CommandSourceStack, ?> createArgument(String argument) {
        return Commands.argument(argument, StringArgumentType.greedyString());
    }

    @Override
    public Setting<String> getFromArgument(String argument, CommandContext<CommandSourceStack> context) {
        return new StringSetting(id, ModUtils.formatTextColors(StringArgumentType.getString(context, argument)));
    }

    @Override
    public void serialize(CompoundTag tag) {
        tag.putString(id, value);
    }

    @Override
    public Setting<String> deserialize(CompoundTag tag) {
        return new StringSetting(id, tag.getString(id));
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(value);
    }

    @Override
    public Setting<String> decode(FriendlyByteBuf buf) {
        return new StringSetting(id, buf.readUtf());
    }

    @Override
    public String toStringCommand() {
        return value;
    }

    @Override
    public String toString() {
        return value.isEmpty() ? "\"\"" : value;
    }
}
