package dev.prangellplays.llgdragons.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.world.World;

public abstract class DragonEntity extends TameableEntity {
    public boolean still;
    protected DragonEntity(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
    }

    public boolean isStill() {
        return this.still;
    }

    public void setStill(boolean still) {
        this.still = still;
    }
}
