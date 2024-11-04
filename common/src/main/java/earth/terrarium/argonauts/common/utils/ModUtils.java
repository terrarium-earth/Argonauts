package earth.terrarium.argonauts.common.utils;

import com.mojang.datafixers.util.Pair;
import com.teamresourceful.resourcefullib.common.color.Color;
import com.teamresourceful.resourcefullib.common.exceptions.NotImplementedException;
import com.teamresourceful.resourcefullib.common.utils.CommonUtils;
import dev.architectury.injectables.annotations.ExpectPlatform;
import earth.terrarium.olympus.client.constants.MinecraftColors;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

public class ModUtils {

    public static final Pattern SPECIAL_COLOR_PATTERN = Pattern.compile("&&([0-9a-fklmnor])");

    public static Color uuidToColor(UUID id) {
        return MinecraftColors.COLORS[Math.abs(id.hashCode()) % MinecraftColors.COLORS.length];
    }

    public static Component translatableWithStyle(String key, Object... args) {
        for (int i = 0; i < args.length; ++i) {
            if (!(args[i] instanceof MutableComponent component)) continue;
            if (component.getStyle().getColor() == null) continue;

            ChatFormatting color = ChatFormatting.getByName(component.getStyle().getColor().toString());
            if (color != null) {
                args[i] = "§" + color.getChar() + component.getString();
            }
        }

        return Component.literal(CommonUtils.serverTranslatable(key, args).getString());
    }

    @Nullable
    public static GlobalPos readGlobalPos(CompoundTag tag) {
        ResourceLocation key = ResourceLocation.tryParse(tag.getString("dimension"));
        if (key == null) return null;
        BlockPos pos = BlockPos.of(tag.getLong("pos"));
        ResourceKey<Level> level = ResourceKey.create(Registries.DIMENSION, key);
        return GlobalPos.of(level, pos);
    }

    public static CompoundTag writeGlobalPos(GlobalPos pos) {
        CompoundTag tag = new CompoundTag();
        tag.putString("dimension", pos.dimension().location().toString());
        tag.putLong("pos", pos.pos().asLong());
        return tag;
    }

    public static String formatTextColors(String text) {
        return SPECIAL_COLOR_PATTERN
            .matcher(text)
            .replaceAll(result -> "§" + result.group(1));
    }

    @ExpectPlatform
    public static List<Pair<UUID, Component>> getFakePlayers() {
        throw new NotImplementedException();
    }

    @ExpectPlatform
    public static Component getParsedComponent(Component component, ServerPlayer player) {
        throw new NotImplementedException();
    }
}
