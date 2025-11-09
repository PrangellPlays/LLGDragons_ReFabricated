package dev.prangellplays.llgdragons.mixin.server;

import dev.prangellplays.llgdragons.entity.DragonEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Inject(method = "dismountVehicle", at = @At("HEAD"), cancellable = true)
    public void preventDismount(CallbackInfo ci) {
        Entity vehicle = ((PlayerEntity)(Object)this).getVehicle();
        if (vehicle instanceof DragonEntity dragon && dragon.isFlying()) {
            // Cancel dismount if dragon is flying
            ci.cancel();
        }
    }
}
