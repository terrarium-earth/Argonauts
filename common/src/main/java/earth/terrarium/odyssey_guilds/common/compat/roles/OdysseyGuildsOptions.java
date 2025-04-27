package earth.terrarium.odyssey_guilds.common.compat.roles;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamresourceful.bytecodecs.base.ByteCodec;
import com.teamresourceful.bytecodecs.base.object.ObjectByteCodec;
import earth.terrarium.odyssey_guilds.OdysseyGuilds;
import earth.terrarium.prometheus.Prometheus;
import earth.terrarium.prometheus.api.roles.options.RoleOption;
import earth.terrarium.prometheus.api.roles.options.RoleOptionSerializer;

public record OdysseyGuildsOptions(int maxGuildMembers, int maxPartyMembers) implements RoleOption<OdysseyGuildsOptions> {

    public static final RoleOptionSerializer<OdysseyGuildsOptions> SERIALIZER = RoleOptionSerializer.of(
        Prometheus.id(OdysseyGuilds.MOD_ID),
        1,
        RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.optionalFieldOf("maxGuildMembers", OdysseyGuilds.DEFAULT_MAX_GUILD_MEMBERS).forGetter(OdysseyGuildsOptions::maxGuildMembers),
            Codec.INT.optionalFieldOf("maxPartyMembers", OdysseyGuilds.DEFAULT_MAX_PARTY_MEMBERS).forGetter(OdysseyGuildsOptions::maxPartyMembers)
        ).apply(instance, OdysseyGuildsOptions::new)),
        ObjectByteCodec.create(
            ByteCodec.INT.fieldOf(OdysseyGuildsOptions::maxGuildMembers),
            ByteCodec.INT.fieldOf(OdysseyGuildsOptions::maxPartyMembers),
            OdysseyGuildsOptions::new
        ),
        new OdysseyGuildsOptions(OdysseyGuilds.DEFAULT_MAX_GUILD_MEMBERS, OdysseyGuilds.DEFAULT_MAX_PARTY_MEMBERS)
    );

    @Override
    public RoleOptionSerializer<OdysseyGuildsOptions> serializer() {
        return SERIALIZER;
    }
}
