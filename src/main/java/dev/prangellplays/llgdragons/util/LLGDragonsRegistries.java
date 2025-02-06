package dev.prangellplays.llgdragons.util;

import dev.prangellplays.llgdragons.client.entity.nightfury.NightfuryRenderer;
import dev.prangellplays.llgdragons.client.entity.nightfury.PlasmaBlastRenderer;
import dev.prangellplays.llgdragons.client.init.LLGDragonsEntityRenderer;
import dev.prangellplays.llgdragons.entity.dragon.NightfuryEntity;
import dev.prangellplays.llgdragons.entity.dragonability.nightfury.PlasmaBlastEntity;
import dev.prangellplays.llgdragons.init.LLGDragonsBlocks;
import dev.prangellplays.llgdragons.init.LLGDragonsEntities;
import dev.prangellplays.llgdragons.init.LLGDragonsParticles;
import dev.prangellplays.llgdragons.particle.PlasmaWaveParticle;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.client.render.RenderLayer;

public class LLGDragonsRegistries {
    public static void init() {
        registerEntityAttributes();
    }

    public static void initClient() {
        registerBlockRenderLayerMap();
        registerEntityRenderer();
        registerParticleFactory();
    }

    private static void registerEntityAttributes() {
        FabricDefaultAttributeRegistry.register(LLGDragonsEntities.NIGHTFURY, NightfuryEntity.createNightfuryAttributes());
    }

    private static void registerBlockRenderLayerMap() {
        BlockRenderLayerMap.INSTANCE.putBlock(LLGDragonsBlocks.NIGHTFURY_HEAD, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(LLGDragonsBlocks.HTTYD_NIGHTFURY_HEAD, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(LLGDragonsBlocks.ALBINO_NIGHTFURY_HEAD, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(LLGDragonsBlocks.WHITE_NIGHTFURY_HEAD, RenderLayer.getCutout());
    }

    private static void registerEntityRenderer() {
        LLGDragonsEntityRenderer.init();
        EntityRendererRegistry.register(LLGDragonsEntities.NIGHTFURY, NightfuryRenderer::new);
        EntityRendererRegistry.register(LLGDragonsEntities.PLASMA_BLAST, PlasmaBlastRenderer::new);
    }

    private static void registerParticleFactory() {
        LLGDragonsParticles.init();
        ParticleFactoryRegistry.getInstance().register(LLGDragonsParticles.PLASMA_WAVE, PlasmaWaveParticle.Factory::new);
    }
}
