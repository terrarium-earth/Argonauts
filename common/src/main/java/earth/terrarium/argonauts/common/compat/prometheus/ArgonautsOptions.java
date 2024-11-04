package earth.terrarium.argonauts.common.compat.prometheus;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamresourceful.bytecodecs.base.ByteCodec;
import com.teamresourceful.bytecodecs.base.object.ObjectByteCodec;
import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.prometheus.Prometheus;
import earth.terrarium.prometheus.api.roles.options.RoleOption;
import earth.terrarium.prometheus.api.roles.options.RoleOptionSerializer;
import net.minecraft.resources.ResourceLocation;

public record ArgonautsOptions(int maxGuildMembers, int maxPartyMembers) implements RoleOption<ArgonautsOptions> {

    public static final RoleOptionSerializer<ArgonautsOptions> SERIALIZER = RoleOptionSerializer.of(
        Prometheus.id(Argonauts.MOD_ID),
        1,
        RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.optionalFieldOf("maxGuildMembers", Argonauts.DEFAULT_MAX_GUILD_MEMBERS).forGetter(ArgonautsOptions::maxGuildMembers),
            Codec.INT.optionalFieldOf("maxPartyMembers", Argonauts.DEFAULT_MAX_PARTY_MEMBERS).forGetter(ArgonautsOptions::maxPartyMembers)
        ).apply(instance, ArgonautsOptions::new)),
        ObjectByteCodec.create(
            ByteCodec.INT.fieldOf(ArgonautsOptions::maxGuildMembers),
            ByteCodec.INT.fieldOf(ArgonautsOptions::maxPartyMembers),
            ArgonautsOptions::new
        ),
        new ArgonautsOptions(Argonauts.DEFAULT_MAX_GUILD_MEMBERS, Argonauts.DEFAULT_MAX_PARTY_MEMBERS)
    );

    @Override
    public RoleOptionSerializer<ArgonautsOptions> serializer() {
        return SERIALIZER;
    }
}
