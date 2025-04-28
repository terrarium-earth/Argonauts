package earth.terrarium.odyssey_allies.common.compat.roles;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamresourceful.bytecodecs.base.ByteCodec;
import com.teamresourceful.bytecodecs.base.object.ObjectByteCodec;
import earth.terrarium.odyssey_allies.OdysseyAllies;
import earth.terrarium.prometheus.Prometheus;
import earth.terrarium.prometheus.api.roles.options.RoleOption;
import earth.terrarium.prometheus.api.roles.options.RoleOptionSerializer;

public record AlliesOptions(int maxGuildMembers, int maxPartyMembers) implements RoleOption<AlliesOptions> {

    public static final RoleOptionSerializer<AlliesOptions> SERIALIZER = RoleOptionSerializer.of(
        Prometheus.id(OdysseyAllies.MOD_ID),
        1,
        RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.optionalFieldOf("maxGuildMembers", OdysseyAllies.DEFAULT_MAX_GUILD_MEMBERS).forGetter(AlliesOptions::maxGuildMembers),
            Codec.INT.optionalFieldOf("maxPartyMembers", OdysseyAllies.DEFAULT_MAX_PARTY_MEMBERS).forGetter(AlliesOptions::maxPartyMembers)
        ).apply(instance, AlliesOptions::new)),
        ObjectByteCodec.create(
            ByteCodec.INT.fieldOf(AlliesOptions::maxGuildMembers),
            ByteCodec.INT.fieldOf(AlliesOptions::maxPartyMembers),
            AlliesOptions::new
        ),
        new AlliesOptions(OdysseyAllies.DEFAULT_MAX_GUILD_MEMBERS, OdysseyAllies.DEFAULT_MAX_PARTY_MEMBERS)
    );

    @Override
    public RoleOptionSerializer<AlliesOptions> serializer() {
        return SERIALIZER;
    }
}
