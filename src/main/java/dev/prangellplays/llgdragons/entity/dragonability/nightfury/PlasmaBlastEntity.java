package dev.prangellplays.llgdragons.entity.dragonability.nightfury;

import dev.prangellplays.llgdragons.init.LLGDragonsEntities;
import dev.prangellplays.llgdragons.init.LLGDragonsParticles;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

public class PlasmaBlastEntity extends ExplosiveProjectileEntity implements GeoEntity {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    public PlasmaBlastEntity(EntityType<? extends ExplosiveProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Environment(EnvType.CLIENT)
    public PlasmaBlastEntity(World world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        this(LLGDragonsEntities.PLASMA_BLAST, world);
        this.refreshPositionAndAngles(x, y, z, this.getYaw(), this.getPitch());
        this.setVelocity(velocityX, velocityY, velocityZ);
    }

    public PlasmaBlastEntity(World world, LivingEntity owner, double velocityX, double velocityY, double velocityZ) {
        super(LLGDragonsEntities.PLASMA_BLAST, owner, velocityX, velocityY, velocityZ, world);
        Vec3d newVec3d = this.getVelocity().normalize().add(this.random.nextGaussian() * 0.1D, -this.random.nextDouble() * 0.1D, this.random.nextGaussian() * 0.1D);
        this.setVelocity(newVec3d);
    }

    @Override
    protected float getDrag() {
        return 0.9F;
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    @Environment(EnvType.CLIENT)
    public boolean shouldRender(double distance) {
        return distance < 16384.0D;
    }

    @Override
    public float getBrightnessAtEyes() {
        return 1.0F;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    private PlayState predicate(AnimationState<PlasmaBlastEntity> plasmaBlastEntityAnimationState) {

        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    private DamageSource plamsmaBlastDamageSource(Entity entity) {
        return entity.getDamageSources().dragonBreath();
    }
    @Override
    public void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        Entity entity = this.getOwner();
        Entity hittedEntity = entityHitResult.getEntity();
        if (!this.getWorld().isClient() && entity != null && hittedEntity instanceof LivingEntity) {
            hittedEntity.damage(plamsmaBlastDamageSource(this), 3.0F);
            this.getWorld().createExplosion(this, this.getX(), this.getY(), this.getZ(), 5, World.ExplosionSourceType.MOB);
            this.getWorld().addImportantParticle(LLGDragonsParticles.PLASMA_WAVE, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
            this.discard();
        }

    }

    @Override
    public void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        if (!this.getWorld().isClient()) {
            Entity entity = this.getOwner();
            if (entity == null || !(entity instanceof MobEntity) || this.getWorld().getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) {
                this.getWorld().createExplosion(this, this.getX(), this.getY(), this.getZ(), 5, World.ExplosionSourceType.MOB);
                this.getWorld().addImportantParticle(LLGDragonsParticles.PLASMA_WAVE, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
            }
            this.discard();
        }
    }
}
