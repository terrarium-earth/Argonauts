package earth.terrarium.odyssey_guilds.common.compat.prometheus;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamresourceful.bytecodecs.base.ByteCodec;
import com.teamresourceful.bytecodecs.base.object.ObjectByteCodec;
import earth.terrarium.odyssey_guilds.OdysseyGuilds;
import earth.terrarium.prometheus.Prometheus;
import earth.terrarium.prometheus.api.roles.options.RoleOption;
import earth.terrarium.prometheus.api.roles.options.RoleOptionSerializer;

public record ArgonautsOptions(int maxGuildMembers, int maxPartyMembers) implements RoleOption<ArgonautsOptions> {

    public static final RoleOptionSerializer<ArgonautsOptions> SERIALIZER = RoleOptionSerializer.of(
        Prometheus.id(OdysseyGuilds.MOD_ID),
        1,
        RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.optionalFieldOf("maxGuildMembers", OdysseyGuilds.DEFAULT_MAX_GUILD_MEMBERS).forGetter(ArgonautsOptions::maxGuildMembers),
            Codec.INT.optionalFieldOf("maxPartyMembers", OdysseyGuilds.DEFAULT_MAX_PARTY_MEMBERS).forGetter(ArgonautsOptions::maxPartyMembers)
        ).apply(instance, ArgonautsOptions::new)),
        ObjectByteCodec.create(
            ByteCodec.INT.fieldOf(ArgonautsOptions::maxGuildMembers),
            ByteCodec.INT.fieldOf(ArgonautsOptions::maxPartyMembers),
            ArgonautsOptions::new
        ),
        new ArgonautsOptions(OdysseyGuilds.DEFAULT_MAX_GUILD_MEMBERS, OdysseyGuilds.DEFAULT_MAX_PARTY_MEMBERS)
    );

    @Override
    public RoleOptionSerializer<ArgonautsOptions> serializer() {
        return SERIALIZER;
    }
}
