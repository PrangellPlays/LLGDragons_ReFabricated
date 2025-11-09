package dev.prangellplays.llgdragons.client.entity.layer;

import dev.prangellplays.llgdragons.entity.dragon.nightfury.NightfuryEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;
import software.bernie.geckolib.util.RenderUtils;

import java.util.Objects;

public class DragonRiderLayer extends GeoRenderLayer<NightfuryEntity> {
    private static final float PASSENGER_SEAT0_X = 0.0f, PASSENGER_SEAT0_Y = -3.0f, PASSENGER_SEAT0_Z = 0.0f;
    private BakedGeoModel lastBakedModel;

    public DragonRiderLayer(GeoRenderer<NightfuryEntity> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void preRender(MatrixStack poseStack, NightfuryEntity animatable, BakedGeoModel bakedModel, RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        enableTrackingForBones(bakedModel);
        super.preRender(poseStack, animatable, bakedModel, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);
    }

    private void enableTrackingForBones(BakedGeoModel bakedModel) {
        bakedModel.getBone("Rider").ifPresent(b -> b.setTrackingMatrices(true));
    }

    @Override
    public void render(MatrixStack poseStack, NightfuryEntity animatable, BakedGeoModel bakedModel, RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        if (this.lastBakedModel != null) {
            // Sample seat 0 (driver) position from passengerBone1
            this.lastBakedModel.getBone("passengerBone1").ifPresent(b -> {
                Vec3d world = transformLocator(b, PASSENGER_SEAT0_X, PASSENGER_SEAT0_Y, PASSENGER_SEAT0_Z);
                if (world != null) {
                    animatable.setClientLocatorPosition("passengerSeat0", world);
                }
            });
        }
        super.render(poseStack, animatable, bakedModel, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);
    }

    private Vec3d transformLocator(GeoBone bone, float px, float py, float pz) {
        if (bone == null || bone.getWorldSpaceMatrix() == null) return null;

        float lx = px / 16f;
        float ly = py / 16f;
        float lz = pz / 16f;
        org.joml.Matrix4f worldMat = new org.joml.Matrix4f(bone.getWorldSpaceMatrix());
        org.joml.Vector4f in = new org.joml.Vector4f(lx, ly, lz, 1f);
        org.joml.Vector4f out = worldMat.transform(in);
        return new Vec3d(out.x(), out.y(), out.z());
    }
}
