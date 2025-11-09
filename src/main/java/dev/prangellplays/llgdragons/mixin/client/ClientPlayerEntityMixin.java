package dev.prangellplays.llgdragons.mixin.client;

import dev.prangellplays.llgdragons.entity.DragonEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {
    @Inject(method = "dismountVehicle", at = @At("HEAD"), cancellable = true)
    public void preventDismount(CallbackInfo ci) {
        ClientPlayerEntity clientPlayer = ((ClientPlayerEntity)(Object)this);
        Entity vehicle = clientPlayer.getVehicle();
        if (vehicle instanceof DragonEntity dragon && dragon.isFlying()) {
            // Cancel dismount if the dragon is flying
            ci.cancel();
        }
    }
}
