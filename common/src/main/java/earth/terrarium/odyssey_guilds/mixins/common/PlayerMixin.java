package earth.terrarium.odyssey_guilds.mixins.common;

import earth.terrarium.odyssey_guilds.api.teams.guild.GuildApi;
import earth.terrarium.odyssey_guilds.api.teams.party.PartyApi;
import earth.terrarium.odyssey_guilds.common.settings.Settings;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerMixin extends Entity {

    public PlayerMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "canHarmPlayer", at = @At("HEAD"), cancellable = true)
    public void odyssey_guilds$canHarmPlayer(Player other, CallbackInfoReturnable<Boolean> cir) {
        if (this.level().isClientSide()) return;

        PartyApi.API.getPlayerParty(this.getUUID()).ifPresent(party -> {
            if (party.isMember(other.getUUID()) && !Settings.FRIENDLY_FIRE.get(party)) {
                cir.setReturnValue(false);
            }
        });

        GuildApi.API.getPlayerGuild(this.level(), this.getUUID()).ifPresent(guild -> {
            if (guild.isMember(other.getUUID()) && !Settings.FRIENDLY_FIRE.get(guild)) {
                cir.setReturnValue(false);
            }
        });
    }
}
