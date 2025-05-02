package dev.prangellplays.llgdragons.entity.dragonability.nightfury;

import dev.prangellplays.llgdragons.init.LLGDragonsEntities;
import dev.prangellplays.llgdragons.init.LLGDragonsParticles;
import dev.prangellplays.llgdragons.world.PlasmaBlastExplosion;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

import java.util.Random;

public class PlasmaBlastEntity extends ExplosiveProjectileEntity implements GeoEntity {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    @Override
    protected float getDrag() {
        return 1.0F;
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

    public PlasmaBlastEntity(EntityType<? extends ExplosiveProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public PlasmaBlastEntity(World world, LivingEntity owner, double velocityX, double velocityY, double velocityZ) {
        super(LLGDragonsEntities.PLASMA_BLAST, owner, velocityX, velocityY, velocityZ, world);
    }

    public PlasmaBlastEntity(World world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        super(LLGDragonsEntities.PLASMA_BLAST, x, y, z, velocityX, velocityY, velocityZ, world);
    }

    protected void onEntityHit(EntityHitResult entityHitResult) {
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

    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        if (!this.getWorld().isClient()) {
            Entity entity = this.getOwner();
            if (entity == null || !(entity instanceof MobEntity) || this.getWorld().getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) {
                createPlasmaExplosion(this, this.getX(), this.getY(), this.getZ(), 5, World.ExplosionSourceType.MOB);
                //this.getWorld().addImportantParticle(LLGDragonsParticles.PLASMA_WAVE, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
            }
            this.discard();
        }
    }

    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (!this.getWorld().isClient) {
            this.discard();
        }

    }

    public boolean canHit() {
        return false;
    }

    public boolean damage(DamageSource source, float amount) {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.age > 300) {
            discard();
        }
    }

    public PlasmaBlastExplosion createPlasmaExplosion(@Nullable Entity entity, double x, double y, double z, float power, World.ExplosionSourceType explosionSourceType) {
        return this.createPlasmaExplosion(entity, (DamageSource)null, (ExplosionBehavior)null, x, y, z, power, false, explosionSourceType);
    }

    public PlasmaBlastExplosion createPlasmaExplosion(@Nullable Entity entity, double x, double y, double z, float power, boolean createFire, World.ExplosionSourceType explosionSourceType) {
        return this.createPlasmaExplosion(entity, (DamageSource)null, (ExplosionBehavior)null, x, y, z, power, createFire, explosionSourceType);
    }

    public PlasmaBlastExplosion createPlasmaExplosion(@Nullable Entity entity, @Nullable DamageSource damageSource, @Nullable ExplosionBehavior behavior, Vec3d pos, float power, boolean createFire, World.ExplosionSourceType explosionSourceType) {
        return this.createPlasmaExplosion(entity, damageSource, behavior, pos.getX(), pos.getY(), pos.getZ(), power, createFire, explosionSourceType);
    }

    public PlasmaBlastExplosion createPlasmaExplosion(@Nullable Entity entity, @Nullable DamageSource damageSource, @Nullable ExplosionBehavior behavior, double x, double y, double z, float power, boolean createFire, World.ExplosionSourceType explosionSourceType) {
        return this.createPlasmaExplosion(entity, damageSource, behavior, x, y, z, power, createFire, explosionSourceType, true);
    }

    public PlasmaBlastExplosion createPlasmaExplosion(@Nullable Entity entity, @Nullable DamageSource damageSource, @Nullable ExplosionBehavior behavior, double x, double y, double z, float power, boolean createFire, World.ExplosionSourceType explosionSourceType, boolean particles) {
        Explosion.DestructionType var10000;
        switch (explosionSourceType) {
            case NONE -> var10000 = Explosion.DestructionType.KEEP;
            case BLOCK -> var10000 = getWorld().getDestructionType(GameRules.BLOCK_EXPLOSION_DROP_DECAY);
            case MOB -> var10000 = getWorld().getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING) ? getWorld().getDestructionType(GameRules.MOB_EXPLOSION_DROP_DECAY) : Explosion.DestructionType.KEEP;
            case TNT -> var10000 = getWorld().getDestructionType(GameRules.TNT_EXPLOSION_DROP_DECAY);
            default -> throw new IncompatibleClassChangeError();
        }

        Explosion.DestructionType destructionType = var10000;
        PlasmaBlastExplosion explosion = new PlasmaBlastExplosion(getWorld(), entity, damageSource, behavior, x, y, z, power, createFire, destructionType);
        explosion.collectBlocksAndDamageEntities();
        explosion.affectWorld(particles);
        return explosion;
    }
}
