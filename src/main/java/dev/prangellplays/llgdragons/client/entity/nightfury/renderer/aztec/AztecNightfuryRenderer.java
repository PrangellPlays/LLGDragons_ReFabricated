package dev.prangellplays.llgdragons.client.entity.nightfury.renderer.aztec;

import dev.prangellplays.llgdragons.LLGDragons;
import dev.prangellplays.llgdragons.client.entity.nightfury.model.aztec.AztecNightfuryModel;
import dev.prangellplays.llgdragons.entity.dragon.nightfury.AztecNightfuryEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class AztecNightfuryRenderer extends GeoEntityRenderer<AztecNightfuryEntity> {
    public AztecNightfuryRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new AztecNightfuryModel());
        this.addRenderLayer(new AztecNightfuryRuneLayer(this));
    }

    @Override
    public Identifier getTexture(AztecNightfuryEntity animatable) {
        return new Identifier(LLGDragons.MOD_ID, "textures/entity/nightfury/nightfury.png");
    }

    @Override
    public void render(AztecNightfuryEntity entity, float entityYaw, float partialTick, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight) {
        if (entity.isBaby()) {
            poseStack.scale(0.4f, 0.4f, 0.4f);
        }

        poseStack.scale(1.5f, 1.5f, 1.5f);
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    public RenderLayer getRenderType(AztecNightfuryEntity animatable, Identifier texture, VertexConsumerProvider bufferSource, float partialTick) {
        return RenderLayer.getEntityTranslucent(texture, true);
    }
}