package dev.prangellplays.llgdragons.client.entity.nightfury.renderer;

import dev.prangellplays.llgdragons.client.entity.nightfury.model.NightfuryModel;
import dev.prangellplays.llgdragons.data.nightfury.NightfuryVariant;
import dev.prangellplays.llgdragons.entity.dragon.nightfury.NightfuryEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class NightfuryRenderer extends GeoEntityRenderer<NightfuryEntity> {
    public NightfuryRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new NightfuryModel());
    }

    @Override
    public Identifier getTextureLocation(NightfuryEntity animatable) {
        NightfuryVariant variant = animatable.getVariant();
        return variant.getTexture();
    }

    @Override
    public void render(NightfuryEntity entity, float entityYaw, float partialTick, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight) {
        if (entity.isBaby()) {
            poseStack.scale(0.4f, 0.4f, 0.4f);
        }

        poseStack.scale(1.5f, 1.5f, 1.5f);
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
