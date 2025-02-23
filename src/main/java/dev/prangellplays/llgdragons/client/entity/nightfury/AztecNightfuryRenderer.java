package dev.prangellplays.llgdragons.client.entity.nightfury;

import dev.prangellplays.llgdragons.LLGDragons;
import dev.prangellplays.llgdragons.entity.dragon.AztecNightfuryEntity;
import dev.prangellplays.llgdragons.entity.dragon.NightfuryEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class AztecNightfuryRenderer extends GeoEntityRenderer<AztecNightfuryEntity> {
    public AztecNightfuryRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new AztecNightfuryModel());
    }

    @Override
    public Identifier getTextureLocation(AztecNightfuryEntity animatable) {
        return new Identifier(LLGDragons.MOD_ID, "textures/entity/nightfury/aztec_nightfury.png");
    }

    @Override
    public void render(AztecNightfuryEntity entity, float entityYaw, float partialTick, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight) {
        if (entity.isBaby()) {
            poseStack.scale(0.4f, 0.4f, 0.4f);
        }

        poseStack.scale(1.5f, 1.5f, 1.5f);
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
