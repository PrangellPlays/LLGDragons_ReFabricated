package dev.prangellplays.llgdragons.client.entity.nightfury;

import dev.prangellplays.llgdragons.LLGDragons;
import dev.prangellplays.llgdragons.entity.dragonability.nightfury.PlasmaBlastEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class PlasmaBlastRenderer extends GeoEntityRenderer<PlasmaBlastEntity> {
    public PlasmaBlastRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new PlasmaBlastModel());
    }

    @Override
    public Identifier getTextureLocation(PlasmaBlastEntity animatable) {
        return new Identifier(LLGDragons.MOD_ID, "textures/entity/nightfury/plasma_blast.png");
    }

    @Override
    public void render(PlasmaBlastEntity entity, float entityYaw, float partialTick, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}