package dev.prangellplays.llgdragons.client.entity.nightfury;

import dev.prangellplays.llgdragons.LLGDragons;
import dev.prangellplays.llgdragons.entity.dragon.AztecNightfuryEggEntity;
import dev.prangellplays.llgdragons.entity.dragon.NightfuryEggEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class AztecNightfuryEggRenderer extends GeoEntityRenderer<AztecNightfuryEggEntity> {
    public AztecNightfuryEggRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new AztecNightfuryEggModel());
    }

    @Override
    public Identifier getTextureLocation(AztecNightfuryEggEntity animatable) {
        return new Identifier(LLGDragons.MOD_ID, "textures/entity/nightfury/aztec_nightfury_egg.png");
    }

    @Override
    public void render(AztecNightfuryEggEntity entity, float entityYaw, float partialTick, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight) {
        poseStack.scale(1.1f, 1.1f, 1.1f);
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
