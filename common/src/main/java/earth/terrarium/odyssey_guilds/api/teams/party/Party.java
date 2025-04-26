package earth.terrarium.odyssey_guilds.api.teams.party;

import com.teamresourceful.bytecodecs.base.ByteCodec;
import com.teamresourceful.bytecodecs.base.object.ObjectByteCodec;
import com.teamresourceful.bytecodecs.defaults.MapCodec;
import com.teamresourceful.resourcefullib.common.color.Color;
import earth.terrarium.odyssey_guilds.api.teams.Member;
import earth.terrarium.odyssey_guilds.api.teams.MemberStatus;
import earth.terrarium.odyssey_guilds.api.teams.Team;
import earth.terrarium.odyssey_guilds.api.teams.permissions.MemberPermissionsApi;
import earth.terrarium.odyssey_guilds.api.teams.settings.Setting;
import earth.terrarium.odyssey_guilds.api.teams.settings.types.ColorSettings;
import earth.terrarium.odyssey_guilds.api.teams.settings.types.StringSetting;
import earth.terrarium.odyssey_guilds.common.settings.Settings;
import earth.terrarium.olympus.client.constants.MinecraftColors;
import net.minecraft.network.chat.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Parties are a form of team, designed for temporary outings and collaborations. They are not persistent across logins and are intended for short-term use.
 *
 * @param id       The party ID
 * @param members  A map of members to their corresponding permissions and member status
 * @param settings A map of setting IDs to their corresponding values
 */
public record Party(
    UUID id,
    Map<UUID, Member> members,
    Map<String, Setting<?>> settings
) implements Team {

    public static final ByteCodec<Party> BYTE_CODEC = ObjectByteCodec.create(
        ByteCodec.UUID.fieldOf(Party::id),
        new MapCodec<>(ByteCodec.UUID, Member.BYTE_CODEC).fieldOf(Party::members),
        new MapCodec<>(ByteCodec.STRING, Setting.BYTE_CODEC).fieldOf(Party::settings),
        Party::new
    );

    public Party(UUID creator, String name) {
        this(UUID.randomUUID(), new HashMap<>(), new HashMap<>());
        this.members.put(creator, new Member(MemberStatus.OWNER, MemberPermissionsApi.API.getPartyPermissions()));
        this.settings.put(Settings.DISPLAY_NAME.id(), new StringSetting(Settings.DISPLAY_NAME.id(), name));
        this.settings.put(Settings.COLOR.id(), new ColorSettings(Settings.COLOR.id(), MinecraftColors.GREEN));
    }

    @Override
    public Member getOrCreateMember(UUID player) {
        return this.members.computeIfAbsent(player, id -> new Member(MemberStatus.MEMBER, MemberPermissionsApi.API.getPartyPermissions()));
    }

    @Override
    public boolean isPublic() {
        return Settings.PUBLIC.get(this);
    }

    @Override
    public Component displayName() {
        return Component.literal(Settings.DISPLAY_NAME.get(this)).withColor(this.color().getValue());
    }

    @Override
    public Color color() {
        return Settings.COLOR.get(this);
    }

    @Override
    public String type() {
        return "party";
    }
}
