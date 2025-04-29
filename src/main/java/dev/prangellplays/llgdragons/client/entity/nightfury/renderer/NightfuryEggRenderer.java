package dev.prangellplays.llgdragons.client.entity.nightfury.renderer;

import dev.prangellplays.llgdragons.LLGDragons;
import dev.prangellplays.llgdragons.client.entity.nightfury.model.NightfuryEggModel;
import dev.prangellplays.llgdragons.entity.dragon.nightfury.Egg.NightfuryEggEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class NightfuryEggRenderer extends GeoEntityRenderer<NightfuryEggEntity> {
    public NightfuryEggRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new NightfuryEggModel());
    }

    @Override
    public Identifier getTextureLocation(NightfuryEggEntity animatable) {
        return new Identifier(LLGDragons.MOD_ID, "textures/entity/nightfury/nightfury_egg.png");
    }

    @Override
    public void render(NightfuryEggEntity entity, float entityYaw, float partialTick, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight) {
        poseStack.scale(1.1f, 1.1f, 1.1f);
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
