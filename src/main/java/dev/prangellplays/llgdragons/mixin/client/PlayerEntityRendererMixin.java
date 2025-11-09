package dev.prangellplays.llgdragons.mixin.client;

import dev.prangellplays.llgdragons.client.entity.nightfury.renderer.NightfuryRenderer;
import dev.prangellplays.llgdragons.entity.DragonEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({PlayerEntityRenderer.class})
public abstract class PlayerEntityRendererMixin {
    @Inject(method = {"render*"}, at = {@At("HEAD")})
    private void onRenderPlayer(AbstractClientPlayerEntity player, float entityYaw, float partialTicks, MatrixStack matrixStack, VertexConsumerProvider buffer, int packedLight, CallbackInfo ci) {
        /*if (player == MinecraftClient.getInstance().player) {
            Entity entity = player.getVehicle();
            if (entity instanceof DragonEntity) {
                DragonEntity dragon = (DragonEntity)entity;
                if (dragon.riderMatrix != null) {
                    EntityRenderer<?> renderer = MinecraftClient.getInstance().getEntityRenderDispatcher().getRenderer(dragon);
                    if (renderer instanceof NightfuryRenderer) {
                        NightfuryRenderer dragonRenderer = (NightfuryRenderer)renderer;
                        matrixStack.push();

                        Vec3d rideOffset = dragon.getRiderOffset(player, partialTicks);
                        matrixStack.translate(-rideOffset.getX(), -rideOffset.getY(), -rideOffset.getZ());
                        Matrix4f noScale = noScale(dragon);
                        matrixStack.translate(dragon.riderMatrix.m30(), dragon.riderMatrix.m31(), dragon.riderMatrix.m32());
                        Quaternionf rotation = new Quaternionf();
                        noScale.getNormalizedRotation(rotation);
                        matrixStack.multiply(rotation);
                        float fallFlyingTicks = 1.0F;
                        matrixStack.translate((double)0.0F, (rideOffset.getY() - (double)dragon.getLayerYOffset()) * (double)fallFlyingTicks, (double)0.0F);
                        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MathHelper.lerpAngleDegrees(partialTicks, dragon.prevBodyYaw, dragon.bodyYaw) + 180.0F));
                    }
                }
            }
        }*/
    }

    @Inject(method = "render*", at = @At("TAIL"))
    private void afterRenderPlayer(AbstractClientPlayerEntity player, float entityYaw, float partialTicks, MatrixStack matrixStack, VertexConsumerProvider buffer, int packedLight, CallbackInfo ci) {
        /*Entity entity = player.getVehicle();
        if (entity instanceof DragonEntity dragon && dragon.riderMatrix != null) {
            matrixStack.pop();
        }*/
    }

    @Unique
    private static @NotNull Matrix4f noScale(DragonEntity dragon) {
        Matrix4f noScale = new Matrix4f(dragon.riderMatrix);
        float scaleX = (float)Math.sqrt((double)(noScale.m00() * noScale.m00() + noScale.m01() * noScale.m01() + noScale.m02() * noScale.m02()));
        float scaleY = (float)Math.sqrt((double)(noScale.m10() * noScale.m10() + noScale.m11() * noScale.m11() + noScale.m12() * noScale.m12()));
        float scaleZ = (float)Math.sqrt((double)(noScale.m20() * noScale.m20() + noScale.m21() * noScale.m21() + noScale.m22() * noScale.m22()));
        noScale.m00(noScale.m00() / scaleX);
        noScale.m01(noScale.m01() / scaleX);
        noScale.m02(noScale.m02() / scaleX);
        noScale.m10(noScale.m10() / scaleY);
        noScale.m11(noScale.m11() / scaleY);
        noScale.m12(noScale.m12() / scaleY);
        noScale.m20(noScale.m20() / scaleZ);
        noScale.m21(noScale.m21() / scaleZ);
        noScale.m22(noScale.m22() / scaleZ);
        return noScale;
    }
}
