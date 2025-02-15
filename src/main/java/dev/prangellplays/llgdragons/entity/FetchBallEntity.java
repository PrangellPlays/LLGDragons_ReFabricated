package dev.prangellplays.llgdragons.entity;

import dev.prangellplays.llgdragons.init.LLGDragonsEntities;
import dev.prangellplays.llgdragons.init.LLGDragonsItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public class FetchBallEntity extends ThrownItemEntity {
    public FetchBallEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public FetchBallEntity(World world, LivingEntity owner) {
        super(LLGDragonsEntities.FETCH_BALL, owner, world);
    }

    public FetchBallEntity(World world, double x, double y, double z) {
        super(LLGDragonsEntities.FETCH_BALL, x, y, z, world);
    }

    protected Item getDefaultItem() {
        return LLGDragonsItems.FETCH_BALL;
    }

    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
    }

    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (!this.getWorld().isClient) {
            this.getWorld().sendEntityStatus(this, (byte)3);
            this.discard();
        }

        ItemScatterer.spawn(this.getWorld(), this.getX(), this.getY(), this.getZ(), this.getDefaultItem().getDefaultStack());
    }
}