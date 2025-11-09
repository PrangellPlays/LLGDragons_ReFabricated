package dev.prangellplays.llgdragons.util;

import dev.prangellplays.llgdragons.LLGDragons;
import dev.prangellplays.llgdragons.client.entity.nightfury.renderer.*;
import dev.prangellplays.llgdragons.client.entity.nightfury.renderer.aztec.AztecNightfuryEggRenderer;
import dev.prangellplays.llgdragons.client.entity.nightfury.renderer.aztec.AztecNightfuryRenderer;
import dev.prangellplays.llgdragons.client.entity.nightfury.renderer.plasma_blast.PlasmaBlastRenderer;
import dev.prangellplays.llgdragons.client.init.LLGDragonsEntityRenderer;
import dev.prangellplays.llgdragons.client.sound.DragonDiveSoundInstance;
import dev.prangellplays.llgdragons.client.sound.DragonSoundHandler;
import dev.prangellplays.llgdragons.entity.DragonEntity;
import dev.prangellplays.llgdragons.entity.dragon.nightfury.Egg.AztecNightfuryEggEntity;
import dev.prangellplays.llgdragons.entity.dragon.nightfury.AztecNightfuryEntity;
import dev.prangellplays.llgdragons.entity.dragon.nightfury.Egg.NightfuryEggEntity;
import dev.prangellplays.llgdragons.entity.dragon.nightfury.NightfuryEntity;
import dev.prangellplays.llgdragons.init.*;
import dev.prangellplays.llgdragons.item.DragosphereItem;
import dev.prangellplays.llgdragons.network.KeyInputC2SPacket;
import dev.prangellplays.llgdragons.network.KeyInputPacket;
import dev.prangellplays.llgdragons.network.KeyInputSyncPacket;
import dev.prangellplays.llgdragons.network.LLGDragonsServerPacket;
import dev.prangellplays.llgdragons.particle.PlasmaWaveParticle;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.Entity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class LLGDragonsRegistries {
    private static final Set<UUID> currentlyPlaying = new HashSet<>();

    public static void init() {
        LLGDragonsBlocks.init();
        LLGDragonsItems.init();
        LLGDragonsItemGroups.init();
        LLGDragonsEntities.init();
        LLGDragonsServerPacket.init();
        LLGDragonsSounds.init();
        KeyInputC2SPacket.init();

        DragosphereItem.DragosphereEventHandler.init();
        registerEntityAttributes();
    }

    public static void initClient() {
        KeyInputPacket.init();
        KeyInputSyncPacket.init();
        registerBlockRenderLayerMap();
        registerEntityRenderer();
        registerParticleFactory();
        registerModelPredicates();
        registerClientEvents();
    }

    private static void registerEntityAttributes() {
        FabricDefaultAttributeRegistry.register(LLGDragonsEntities.NIGHTFURY, NightfuryEntity.createDragonAttributes());
        FabricDefaultAttributeRegistry.register(LLGDragonsEntities.NIGHTFURY_EGG, NightfuryEggEntity.createDragonEggAttributes());
        FabricDefaultAttributeRegistry.register(LLGDragonsEntities.AZTEC_NIGHTFURY, AztecNightfuryEntity.createDragonAttributes());
        FabricDefaultAttributeRegistry.register(LLGDragonsEntities.AZTEC_NIGHTFURY_EGG, AztecNightfuryEggEntity.createDragonEggAttributes());
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
        EntityRendererRegistry.register(LLGDragonsEntities.NIGHTFURY_EGG, NightfuryEggRenderer::new);
        EntityRendererRegistry.register(LLGDragonsEntities.AZTEC_NIGHTFURY, AztecNightfuryRenderer::new);
        EntityRendererRegistry.register(LLGDragonsEntities.AZTEC_NIGHTFURY_EGG, AztecNightfuryEggRenderer::new);
        EntityRendererRegistry.register(LLGDragonsEntities.PLASMA_BLAST, PlasmaBlastRenderer::new);
    }

    private static void registerParticleFactory() {
        LLGDragonsParticles.init();
        ParticleFactoryRegistry.getInstance().register(LLGDragonsParticles.PLASMA_WAVE, PlasmaWaveParticle.Factory::new);
    }

    private static void registerModelPredicates() {
        ModelPredicateProviderRegistry.register(LLGDragonsItems.DRAGOSPHERE, LLGDragons.id("dragosphere_full"), (stack, world, entity, i) -> {
            return DragosphereItem.hasCapturedDragon(stack) ? 1.0F : 0.0F;
        });
    }

    private static void registerClientEvents() {
        ClientTickEvents.END_CLIENT_TICK.register(DragonSoundHandler::onClientTick);
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.world == null || client.player == null) return;

            for (Entity entity : client.world.getEntities()) {
                if (entity instanceof DragonEntity dragon) {
                    if (dragon.isClientBoosting() && dragon.isGoingDown()) {
                        if (!currentlyPlaying.contains(dragon.getUuid())) {
                            MinecraftClient.getInstance().getSoundManager().play(new DragonDiveSoundInstance(dragon));
                            currentlyPlaying.add(dragon.getUuid());
                        }
                    } else {
                        currentlyPlaying.remove(dragon.getUuid());
                    }
                }
            }
        });
    }
}
