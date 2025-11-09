package dev.prangellplays.llgdragons.client.entity.nightfury.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.prangellplays.llgdragons.LLGDragons;
import dev.prangellplays.llgdragons.client.entity.layer.DragonPassengerLayer;
import dev.prangellplays.llgdragons.client.entity.layer.DragonRiderLayer;
import dev.prangellplays.llgdragons.client.entity.layer.RenderDummyPlayer;
import dev.prangellplays.llgdragons.client.entity.nightfury.model.NightfuryModel;
import dev.prangellplays.llgdragons.data.nightfury.variant.NightfuryVariant;
import dev.prangellplays.llgdragons.entity.dragon.nightfury.NightfuryEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.*;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.util.RenderUtils;

public class NightfuryRenderer extends GeoEntityRenderer<NightfuryEntity> {
    public NightfuryRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new NightfuryModel());
        //this.addRenderLayer(new DragonPassengerLayer<>(this, "Rider"));
        //this.addRenderLayer(new DragonRiderLayer(this));
    }

    @Override
    public Identifier getTextureLocation(NightfuryEntity animatable) {
        NightfuryVariant variant = animatable.getVariant();
        return variant.getTexture();
    }

    @Override
    public void render(NightfuryEntity entity, float entityYaw, float partialTick, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight) {
        poseStack.push();
        float scale = entity.isBaby() ? 0.4f : 1.5f;
        poseStack.scale(scale, scale, scale);

        getGeoModel().getBone("Mouth").ifPresent(mouthBone -> {
            Matrix4f localMatrix = new Matrix4f(mouthBone.getModelSpaceMatrix());
            Vector3f offset = new Vector3f(localMatrix.m30(), localMatrix.m31(), localMatrix.m32());
            Vec3d worldPos = new Vec3d(offset.x(), offset.y(), offset.z()).add(entity.getX(), entity.getY(), entity.getZ());

            entity.setMouthWorldPos(worldPos);
        });

        if (entity.hasPassengers()) {
            Entity passenger = entity.getFirstPassenger();
            if (passenger instanceof PlayerEntity player) {
                // Get bone transform
                GeoBone mountBone = getGeoModel().getBone("Rider").orElse(null);
                if (mountBone != null) {
                    poseStack.push();

                    // Apply GeckoLib bone transforms
                    poseStack.translate(mountBone.getWorldPosition().x, mountBone.getWorldPosition().y, mountBone.getWorldPosition().z);
                    poseStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-mountBone.getRotY()));
                    poseStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-mountBone.getRotX()));
                    poseStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(mountBone.getRotZ()));

                    // Optional: offset/scale correction
                    poseStack.translate(0, -1.5F, 0);
                    poseStack.scale(1.0F, 1.0F, 1.0F);

                    RenderDummyPlayer dummy = new RenderDummyPlayer((ClientWorld) player.getWorld(), player.getGameProfile());
                    dummy.copyFrom(player);

                    PlayerEntityRenderer renderer = (PlayerEntityRenderer) MinecraftClient.getInstance().getEntityRenderDispatcher().getRenderer(dummy);

                    renderer.render(dummy, 0F, partialTick, poseStack, bufferSource, packedLight);
                    LLGDragons.LOGGER.warn("Rendered player successfully");

                    // Render player model manually
                    /*RenderSystem.disableDepthTest();
                    MinecraftClient.getInstance().getEntityRenderDispatcher().getRenderer(player).render(player, 0, partialTick, poseStack, bufferSource, packedLight);
                    RenderSystem.enableDepthTest();*/

                    poseStack.pop();
                }
            }
        }

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        poseStack.pop();
    }

    @Override
    public void renderRecursively(MatrixStack poseStack, NightfuryEntity animatable, GeoBone bone, RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        /*RenderUtils.translateMatrixToBone(poseStack, bone);
        RenderUtils.translateToPivotPoint(poseStack, bone);
        //rotateMatrixAroundDragonBoneOrHead(poseStack, bone, animatable, partialTick);
        RenderUtils.scaleMatrixForBone(poseStack, bone);
        if (bone.getName().equals("rider")) {
            Matrix4f poseState = new Matrix4f(poseStack.peek().getPositionMatrix());
            Matrix4f localMatrix = RenderUtils.invertAndMultiplyMatrices(poseState, this.entityRenderTranslations);
            animatable.riderMatrix = new Matrix4f(localMatrix);
            bone.setModelSpaceMatrix(RenderUtils.invertAndMultiplyMatrices(poseState, this.modelRenderTranslations));
            localMatrix.translate(new Vector3f(this.getPositionOffset(animatable, 1.0F).toVector3f()));
            bone.setLocalSpaceMatrix(localMatrix);
            Matrix4f worldState = new Matrix4f(localMatrix);
            worldState.translate(new Vector3f(animatable.getPos().toVector3f()));
            bone.setWorldSpaceMatrix(worldState);
        }

        if ("rider".equals(bone.getName())) {
            Matrix4f poseMatrix = new Matrix4f(poseStack.peek().getPositionMatrix());
            animatable.riderMatrix = poseMatrix;
        }*/

        super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
