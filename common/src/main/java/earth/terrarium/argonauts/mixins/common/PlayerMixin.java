package earth.terrarium.argonauts.mixins.common;

import earth.terrarium.argonauts.api.party.Party;
import earth.terrarium.argonauts.api.party.PartyApi;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerMixin {

    @Inject(method = "canHarmPlayer", at = @At("HEAD"), cancellable = true)
    public void argonauts$canHarmPlayer(Player other, CallbackInfoReturnable<Boolean> cir) {
        Player player = (Player) (Object) this;
        if (player.level().isClientSide()) return;
        Party playerParty = PartyApi.API.getPlayerParty(player.getGameProfile().getId());
        if (playerParty == null) return;
        Party otherParty = PartyApi.API.getPlayerParty(other.getGameProfile().getId());
        if (otherParty == null) return;
        if (playerParty.id().equals(otherParty.id()) && !playerParty.friendlyFireEnabled()) {
            cir.setReturnValue(false);
        }
    }
}
