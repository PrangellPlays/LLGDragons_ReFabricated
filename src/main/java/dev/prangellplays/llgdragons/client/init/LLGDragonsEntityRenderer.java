package dev.prangellplays.llgdragons.client.init;

import dev.prangellplays.llgdragons.init.LLGDragonsEntities;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingItemEntity;

public class LLGDragonsEntityRenderer {
    public static void init() {
        registerItemEntityRenders(
                LLGDragonsEntities.FETCH_BALL
        );
    }

    @SafeVarargs
    private static void registerItemEntityRenders(EntityType<? extends FlyingItemEntity>... entityTypes) {
        for (EntityType<? extends FlyingItemEntity> entityType : entityTypes) {
            registerItemEntityRender(entityType);
        }
    }

    private static <T extends Entity & FlyingItemEntity> void registerItemEntityRender(EntityType<T> entityType) {
        EntityRendererRegistry.register(entityType, FlyingItemEntityRenderer::new);
    }
}
