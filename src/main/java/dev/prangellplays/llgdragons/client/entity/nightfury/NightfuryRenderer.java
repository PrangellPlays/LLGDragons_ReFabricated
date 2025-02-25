package dev.prangellplays.llgdragons.client.entity.nightfury;

import dev.prangellplays.llgdragons.LLGDragons;
import dev.prangellplays.llgdragons.entity.dragon.NightfuryEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class NightfuryRenderer extends GeoEntityRenderer<NightfuryEntity> {
    public NightfuryRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new NightfuryModel());
    }

    @Override
    public Identifier getTextureLocation(NightfuryEntity animatable) {
        return new Identifier(LLGDragons.MOD_ID, "textures/entity/nightfury/nightfury.png");
    }

    @Override
    public void render(NightfuryEntity entity, float entityYaw, float partialTick, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight) {
        if (entity.day_count_age < 3) {
            poseStack.scale(0.4f, 0.4f, 0.4f);
        }
        if (entity.day_count_age < 4) {
            poseStack.scale(0.6f, 0.6f, 0.6f);
        }
        if (entity.day_count_age < 5) {
            poseStack.scale(1.0f, 1.0f, 1.0f);
        }
        if (entity.day_count_age < 6) {
            poseStack.scale(1.3f, 1.3f, 1.3f);
        }

        poseStack.scale(1.5f, 1.5f, 1.5f);
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
