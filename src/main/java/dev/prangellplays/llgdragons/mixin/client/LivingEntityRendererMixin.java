package dev.prangellplays.llgdragons.mixin.client;

import dev.prangellplays.llgdragons.client.entity.layer.DragonPassengerLayer;
import dev.prangellplays.llgdragons.entity.DragonEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity>{
    @Inject(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "HEAD"), cancellable = true)
    private void cancelRender(T entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (entity.getVehicle() instanceof DragonEntity dragon) {
            if (entity instanceof PlayerEntity player && dragon.getPassengerList().contains(player)) {
                ci.cancel();
            }
        }
    }
}
