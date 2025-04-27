package earth.terrarium.argonauts.api.teams.settings;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.teamresourceful.bytecodecs.base.ByteCodec;
import com.teamresourceful.bytecodecs.defaults.PassthroughCodec;
import earth.terrarium.argonauts.api.teams.Team;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public interface Setting<T> {

    /**
     * Gets the value of the setting.
     *
     * @return The value of the setting.
     */
    T value();

    /**
     * Gets the id of the setting.
     *
     * @return The id of the setting.
     */
    String id();

    /**
     * Creates a brigadier argument for the setting.
     *
     * @param argument The name of the argument.
     * @return The brigadier argument.
     */
    ArgumentBuilder<CommandSourceStack, ?> createArgument(String argument);

    /**
     * Gets the setting from a brigadier argument.
     *
     * @param argument The name of the argument.
     * @param context  The command context.
     * @return The setting.
     * @throws CommandSyntaxException If the setting could not be created.
     */
    Setting<T> getFromArgument(String argument, CommandContext<CommandSourceStack> context) throws CommandSyntaxException;

    /**
     * Serializes the setting to NBT.
     *
     * @param tag The tag.
     */
    void serialize(CompoundTag tag);

    /**
     * Deserializes the setting from NBT.
     *
     * @param tag The tag.
     * @return The setting.
     */
    Setting<T> deserialize(CompoundTag tag);

    /**
     * Encodes the setting to network.
     *
     * @param buf The buffer.
     */
    void encode(FriendlyByteBuf buf);

    /**
     * Decodes the setting from network.
     *
     * @param buf The buffer.
     * @return The setting.
     */
    Setting<T> decode(FriendlyByteBuf buf);

    /**
     * Whether the setting should be hidden in the settings screen.
     *
     * @return Whether the setting should be hidden.
     */
    default boolean hidden() {
        return false;
    }

    /**
     * Gets the string command representation of the setting for the settings screen.
     *
     * @return The string command representation of the setting.
     */
    default String toStringCommand() {
        return this.toString();
    }

    /**
     * Gets the value of the setting for a specific team.
     *
     * @param team The team.
     * @return The value of the setting.
     */
    default T get(Team team) {
        return TeamSettingsApi.API.<T>getSetting(team, id()).value();
    }

    ByteCodec<Setting<?>> BYTE_CODEC = new PassthroughCodec<>((buf, setting) -> {
        FriendlyByteBuf friendlyByteBuf = (FriendlyByteBuf) buf;
        friendlyByteBuf.writeUtf(setting.id());
        setting.encode(friendlyByteBuf);
    }, buf -> {
        FriendlyByteBuf friendlyByteBuf = (FriendlyByteBuf) buf;
        return TeamSettingsApi.API.getDefaultValue(friendlyByteBuf.readUtf()).decode(friendlyByteBuf);
    });
}
