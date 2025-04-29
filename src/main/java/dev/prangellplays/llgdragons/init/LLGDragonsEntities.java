package dev.prangellplays.llgdragons.init;

import dev.prangellplays.llgdragons.LLGDragons;
import dev.prangellplays.llgdragons.entity.FetchBallEntity;
import dev.prangellplays.llgdragons.entity.dragon.nightfury.Egg.AztecNightfuryEggEntity;
import dev.prangellplays.llgdragons.entity.dragon.nightfury.AztecNightfuryEntity;
import dev.prangellplays.llgdragons.entity.dragon.nightfury.Egg.NightfuryEggEntity;
import dev.prangellplays.llgdragons.entity.dragon.nightfury.NightfuryEntity;
import dev.prangellplays.llgdragons.entity.dragonability.nightfury.PlasmaBlastEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;

public interface LLGDragonsEntities {
    Map<EntityType<? extends Entity>, Identifier> ENTITIES = new LinkedHashMap();
    EntityType<FetchBallEntity> FETCH_BALL = Registry.register(Registries.ENTITY_TYPE, LLGDragons.id("fetch_ball"), FabricEntityTypeBuilder.<FetchBallEntity>create(SpawnGroup.MISC, FetchBallEntity::new).dimensions(new EntityDimensions(0.25F, 0.25F, true)).trackRangeChunks(4).trackedUpdateRate(10).build());
    EntityType<NightfuryEntity> NIGHTFURY = Registry.register(Registries.ENTITY_TYPE, LLGDragons.id("nightfury"), FabricEntityTypeBuilder.create(SpawnGroup.MISC, NightfuryEntity::new).dimensions(new EntityDimensions(1.0f, 1.0f, true)).build());
    EntityType<NightfuryEggEntity> NIGHTFURY_EGG = Registry.register(Registries.ENTITY_TYPE, LLGDragons.id("nightfury_egg"), FabricEntityTypeBuilder.create(SpawnGroup.MISC, NightfuryEggEntity::new).dimensions(new EntityDimensions(1.0f, 1.0f, true)).build());
    EntityType<AztecNightfuryEntity> AZTEC_NIGHTFURY = Registry.register(Registries.ENTITY_TYPE, LLGDragons.id("aztec_nightfury"), FabricEntityTypeBuilder.create(SpawnGroup.MISC, AztecNightfuryEntity::new).dimensions(new EntityDimensions(1.0f, 1.0f, true)).build());
    EntityType<AztecNightfuryEggEntity> AZTEC_NIGHTFURY_EGG = Registry.register(Registries.ENTITY_TYPE, LLGDragons.id("aztec_nightfury_egg"), FabricEntityTypeBuilder.create(SpawnGroup.MISC, AztecNightfuryEggEntity::new).dimensions(new EntityDimensions(1.0f, 1.0f, true)).build());
    EntityType<PlasmaBlastEntity> PLASMA_BLAST = Registry.register(Registries.ENTITY_TYPE, LLGDragons.id("plasma_blast"), FabricEntityTypeBuilder.<PlasmaBlastEntity>create(SpawnGroup.MISC, PlasmaBlastEntity::new).dimensions(new EntityDimensions(0.5f, 0.5f, true)).trackRangeChunks(4).trackedUpdateRate(10).build());

    private static <T extends EntityType<? extends Entity>> T createEntity(String name, T entity) {
        ENTITIES.put(entity, new Identifier(LLGDragons.MOD_ID, name));
        return entity;
    }

    static void init() {
        ENTITIES.keySet().forEach((entityType) -> {
            Registry.register(Registries.ENTITY_TYPE, (Identifier) ENTITIES.get(entityType), entityType);
        });
    }
}