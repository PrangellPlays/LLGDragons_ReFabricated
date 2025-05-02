package dev.prangellplays.llgdragons.client.entity.nightfury.renderer.plasma_blast;

import dev.prangellplays.llgdragons.LLGDragons;
import dev.prangellplays.llgdragons.client.entity.nightfury.model.plasma_blast.PlasmaBlastModel;
import dev.prangellplays.llgdragons.entity.dragonability.nightfury.PlasmaBlastEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.util.RenderUtils;

public class PlasmaBlastRenderer extends GeoEntityRenderer<PlasmaBlastEntity> {
    public PlasmaBlastRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new PlasmaBlastModel());
        this.addRenderLayer(new PlasmaBlastGlowLayer(this));
    }

    @Override
    public Identifier getTexture(PlasmaBlastEntity animatable) {
        return new Identifier(LLGDragons.MOD_ID, "textures/entity/nightfury/plasma_blast.png");
    }

    @Override
    public void render(PlasmaBlastEntity entity, float entityYaw, float partialTick, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight) {
        poseStack.push();
        float yawAngle = MathHelper.lerp(partialTick, entity.prevYaw, entity.getYaw());
        float pitchAngle = MathHelper.lerp(partialTick, entity.prevPitch, entity.getPitch());
        poseStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(yawAngle));
        poseStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(pitchAngle * 24));
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        poseStack.pop();
    }

    public RenderLayer getRenderType(PlasmaBlastEntity animatable, Identifier texture, VertexConsumerProvider bufferSource, float partialTick) {
        return RenderLayer.getEntityTranslucent(texture, true);
    }
}