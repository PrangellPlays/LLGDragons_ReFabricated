package dev.prangellplays.llgdragons.client.entity.nightfury.renderer.plasma_blast;

import dev.prangellplays.llgdragons.LLGDragons;
import dev.prangellplays.llgdragons.entity.dragonability.nightfury.PlasmaBlastEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class PlasmaBlastGlowLayer extends GeoRenderLayer<PlasmaBlastEntity> {
    private static final Identifier id = new Identifier(LLGDragons.MOD_ID, "textures/entity/nightfury/plasma_blast.png");

    public PlasmaBlastGlowLayer(GeoEntityRenderer<PlasmaBlastEntity> entityRendererIn) {
        super(entityRendererIn);
    }


    @Override
    public void render(MatrixStack matrixStackIn, PlasmaBlastEntity entitylivingbaseIn, BakedGeoModel bakedModel, RenderLayer renderType, VertexConsumerProvider bufferIn, VertexConsumer buffer, float partialTicks, int packedLightIn, int packedOverlay) {
        RenderLayer glowRenderType = RenderLayer.getEyes(id);
        this.getRenderer().reRender(this.getDefaultBakedModel(entitylivingbaseIn), matrixStackIn, bufferIn, entitylivingbaseIn, glowRenderType, bufferIn.getBuffer(glowRenderType), partialTicks, packedLightIn, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
    }
}