package dev.prangellplays.llgdragons.entity.dragon.nightfury;

import dev.prangellplays.llgdragons.LLGDragons;
import dev.prangellplays.llgdragons.data.nightfury.variant.NightfuryVariant;
import dev.prangellplays.llgdragons.entity.DragonEntity;
import dev.prangellplays.llgdragons.entity.dragon.nightfury.Egg.NightfuryEggEntity;
import dev.prangellplays.llgdragons.entity.dragonability.nightfury.PlasmaBlastEntity;
import dev.prangellplays.llgdragons.init.LLGDragonsEntities;
import dev.prangellplays.llgdragons.init.LLGDragonsItems;
import dev.prangellplays.llgdragons.init.LLGDragonsSounds;
import dev.prangellplays.llgdragons.util.LLGDragonsTags;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static net.minecraft.entity.attribute.EntityAttributes.GENERIC_FLYING_SPEED;
import static net.minecraft.entity.attribute.EntityAttributes.GENERIC_MOVEMENT_SPEED;

public class NightfuryEntity extends DragonEntity implements VariantHolder<NightfuryVariant>, GeoEntity {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    private static final TrackedData<Integer> VARIANT_ID = DataTracker.registerData(NightfuryEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private final Map<String, Vec3d> clientLocatorCache = new ConcurrentHashMap<>();

    public NightfuryEntity(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(VARIANT_ID, NightfuryVariant.toId(this.getWorld().getRegistryManager(), NightfuryVariant.NIGHTFURY));
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        this.getWorld().getRegistryManager().get(LLGDragons.NIGHTFURY_VARIANT_KEY)
                .getEntry(NightfuryVariant.toId(this.getWorld().getRegistryManager(), this.getVariant()))
                .flatMap(RegistryEntry.Reference::getKey)
                .ifPresent(key -> nbt.putString("variant", key.getValue().toString()));
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        NightfuryVariant variant = NightfuryVariant.fromNbt(this.getWorld().getRegistryManager(), nbt);
        if(variant != null) this.setVariant(variant);
    }

    public static DefaultAttributeContainer.Builder createDragonAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 60.0D)
                .add(GENERIC_MOVEMENT_SPEED, 0.3f)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 4.0f)
                .add(GENERIC_FLYING_SPEED, 1f);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new DragonStillGoal(this));
        this.goalSelector.add(1, new DragonSleepGoal(this));
        this.goalSelector.add(2, new DragonSitGoal(this));
        this.goalSelector.add(3, new NightFuryMateGoal(this, 1.0D));
        this.goalSelector.add(4, new TemptGoal(this, 0.75f, TAMING_INGREDIENT, false));
        this.goalSelector.add(5, new DragonFetchGoal(this));
        this.goalSelector.add(6, new WanderAroundFarGoal(this, 0.75f, 0.2f));
        this.goalSelector.add(7, new LookAroundGoal(this));
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        //NightfuryVariant variant = this.getVariant();

        this.setGender(this.getRandom().nextBoolean());
        this.setDayCountAge(0);
        this.setBaby(true);
        this.setAgeInt(base_day_length);
        this.chooseFavouriteFood();
        this.dataTracker.set(FAVOURITE_FOOD, favouriteFood);
        //this.setVariant(variant);
        return LLGDragonsEntities.NIGHTFURY.create(world);
    }

    @Override
    public EntityView method_48926() {
        return this.getWorld();
    }

    @Override
    public boolean canBoost() {
        return true;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    private PlayState predicate(AnimationState<NightfuryEntity> dragonEntityAnimationState) {
        if (!this.isBaby()) {
            if (this.flying) {
                if (this.canBoost() && this.boosting()) {
                    if (this.isGoingUp() && this.isInAir()) {
                        dragonEntityAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.nightfury.flyingupboost", Animation.LoopType.LOOP));
                    } else if (this.isGoingDown() && this.isInAir()) {
                        dragonEntityAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.nightfury.flyingdownboost", Animation.LoopType.LOOP));
                    } else {
                        dragonEntityAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.nightfury.flyingboost", Animation.LoopType.LOOP));
                    }
                } else {
                    if (this.isGoingUp() && this.isInAir()) {
                        dragonEntityAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.nightfury.flyingup", Animation.LoopType.LOOP));
                    } else if (this.isGoingDown() && this.isInAir()) {
                        dragonEntityAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.nightfury.flyingdown", Animation.LoopType.LOOP));
                    } else {
                        dragonEntityAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.nightfury.flying", Animation.LoopType.LOOP));
                    }
                }
            } else if (dragonEntityAnimationState.isMoving()) {
                dragonEntityAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.nightfury.walk", Animation.LoopType.LOOP));
            } else if (this.isTouchingWater()) {
                dragonEntityAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.nightfury.flyingdown", Animation.LoopType.LOOP));
            } else if (this.isInSittingPose()) {
                dragonEntityAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.nightfury.sit", Animation.LoopType.LOOP));
            } else if (!hasControllingPassenger() && this.isDragonSleeping()) {
                dragonEntityAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.nightfury.sleep", Animation.LoopType.LOOP));
            } else {
                dragonEntityAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.nightfury.idle", Animation.LoopType.LOOP));
            }
        } else {
            if (dragonEntityAnimationState.isMoving()) {
                dragonEntityAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.nightfury.walk", Animation.LoopType.LOOP));
            } else if (this.isInSittingPose()) {
                dragonEntityAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.nightfury.sit", Animation.LoopType.LOOP));
            } else if (!hasControllingPassenger() && this.isDragonSleeping()) {
                dragonEntityAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.nightfury.sleep", Animation.LoopType.LOOP));
            } else {
                dragonEntityAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.nightfury.idlebaby", Animation.LoopType.LOOP));
            }
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void tick() {
        super.tick();

        if (hasControllingPassenger()) {
            if (isPrimaryAttackPressed && getPrimaryAttackCooldown() == 0) {
                shoot();
                setPrimaryAttackCooldown(getMaxPrimaryAttackCooldown());
                this.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 15, 4, false, false, false), this);
            }
        }

        if (!this.getWorld().isClient) return;
    }

    public void shoot() {
        if (getWorld().isClient()) return;
        setPrimaryAttackCooldown(getMaxPrimaryAttackCooldown());
        float progress = rotationProgress / TRANSITION_TICKS;
        float yaw = 0;
        boolean check = isFlying() && isMoving() && !isMovingBackwards();
        if (hasControllingPassenger()) {
            yaw = check ? 90 : 45;
            yaw *= progress;
        }
        for (int i = 0; i < 1; ++i) {
            PlasmaBlastEntity projectileEntity = new PlasmaBlastEntity(getWorld(), this, 0, 0, 0);
            projectileEntity.setPosition(this.getX(), this.getY() + 1, this.getZ());
            projectileEntity.setVelocity(this, this.getPitch(), this.getYaw(), 0.5f, 3.0f, 5.0f);
            //projectileEntity.addVelocity(Vec3d.fromPolar(this.getPitch(), this.getYaw()));
            getWorld().spawnEntity(projectileEntity);
        }
    }

    protected SoundEvent getAmbientSound() {
        return LLGDragonsSounds.NIGHTFURY_ROAR;
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return LLGDragonsSounds.NIGHTFURY_ROAR;
    }

    public boolean canBreedWith(NightfuryEntity other) {
        if (other == this) {
            return false;
        }
        if (other.getClass() != this.getClass()) {
            return false;
        }
        if (!this.isInLove() || !other.isInLove()) {
            return false;
        }

        return this.isFemale() != other.isFemale();
    }

    public NightfuryEggEntity createEgg(NightfuryEntity nightfury) {
        NightfuryEggEntity dragon = new NightfuryEggEntity(LLGDragonsEntities.NIGHTFURY_EGG, this.getWorld());
        dragon.setPosition(MathHelper.floor(this.getX()) + 0.5, MathHelper.floor(this.getY()) + 1, MathHelper.floor(this.getZ()) + 0.5);
        return dragon;
    }

    @Override
    public NightfuryVariant getVariant() {
        return NightfuryVariant.fromId(this.getWorld().getRegistryManager(), this.dataTracker.get(VARIANT_ID));
    }

    @Override
    public void setVariant(NightfuryVariant variant) {
        int id = NightfuryVariant.toId(this.getWorld().getRegistryManager(), variant);
        this.dataTracker.set(VARIANT_ID, id);
    }

    public static class NightFuryMateGoal extends Goal {
        final World theWorld;
        final double moveSpeed;
        private final NightfuryEntity dragon;
        int spawnBabyDelay;
        private NightfuryEntity targetMate;

        public NightFuryMateGoal(NightfuryEntity dragon, double speedIn) {
            this.dragon = dragon;
            this.theWorld = dragon.getWorld();
            this.moveSpeed = speedIn;
            this.setControls(EnumSet.of(Control.MOVE));
        }

        @Override
        public boolean canStart() {
            if (!this.dragon.isInLove() || this.dragon.isInSittingPose()) return false;
            else {
                this.targetMate = this.getNearbyMate();
                return this.targetMate != null;
            }
        }

        public boolean continueExecuting() {
            return this.targetMate.isAlive() && this.targetMate.isInLove() && this.spawnBabyDelay < 60;
        }

        @Override
        public void stop() {
            this.targetMate = null;
            this.spawnBabyDelay = 0;
        }

        @Override
        public void tick() {
            this.dragon.getLookControl().lookAt(this.targetMate, 10.0F, this.dragon.getMaxLookPitchChange());
            this.dragon.getNavigation().startMovingTo(this.targetMate.getX(), this.targetMate.getY(), this.targetMate.getZ(), this.moveSpeed);
            this.dragon.setFlying(false);
            ++this.spawnBabyDelay;
            if (this.spawnBabyDelay >= 60 && this.dragon.distanceTo(this.targetMate) < 35)
                this.spawnBaby();
        }

        private NightfuryEntity getNearbyMate() {
            List<? extends NightfuryEntity> list = this.theWorld.getNonSpectatingEntities(this.dragon.getClass(), this.dragon.getBoundingBox().expand(180.0D, 180.0D, 180.0D));
            double d0 = Double.MAX_VALUE;
            NightfuryEntity mate = null;
            for (NightfuryEntity partner : list)
                if (this.dragon.canBreedWith(partner)) {
                    double d1 = this.dragon.squaredDistanceTo(partner);
                    if (d1 < d0) {
                        mate = partner;
                        d0 = d1;
                    }
                }
            return mate;
        }

        private void spawnBaby() {
            /*NightfuryEggCombination combination = NightfuryEggCombinations.getMatchingCombination(theWorld.getRegistryManager(), (RegistryEntry<NightfuryVariant) this.dragon.getVariant(), (RegistryEntry<NightfuryVariant>) targetMate.getVariant());
            Registry<NightfuryVariant> variantRegistry = this.theWorld.getRegistryManager().get(LLGDragons.NIGHTFURY_VARIANT_KEY);
            NightfuryEggCombination combo = NightfuryEggCombinations.getMatchingCombination(theWorld.getRegistryManager(),
                    (RegistryEntry<NightfuryVariant>) this.dragon.getVariant(), (RegistryEntry<NightfuryVariant>) this.targetMate.getVariant()
            );
            RegistryEntry<NightfuryVariant> variantEntry;
            Random random = this.dragon.getRandom();

            if (combo != null && !combo.getResultEggs().isEmpty()) {
                Identifier id = combo.getResultEggs().get(random.nextInt(combo.getResultEggs().size()));
                variantEntry = variantRegistry.getEntry(RegistryKey.of(LLGDragons.NIGHTFURY_VARIANT_KEY, id))
                        .orElseThrow(() -> new IllegalStateException("Unknown variant: " + id));
            } else {
                variantEntry = (RegistryEntry<NightfuryVariant>) this.dragon.getVariant();
            }

            int offspringCount = combination.getOffspringCount();

            for (int i = 0; i < offspringCount; i++) {

                NightfuryEggEntity egg = this.dragon.createEgg(this.targetMate);
                if (egg == null) continue;

                System.out.println("Breeding " + dragon.getVariant().texture + " x " + targetMate.getVariant().texture);
                System.out.println("Chosen offspring variant: " + variantEntry);
                egg.setVariant((NightfuryVariant) variantEntry, egg.getWorld().getRegistryManager());

                if (i == 0) {
                    this.dragon.setBreedingAge(6000);
                    this.targetMate.setBreedingAge(6000);
                    this.dragon.resetLoveTicks();
                    this.targetMate.resetLoveTicks();
                }

                double offsetX = (random.nextDouble() - 0.5) * 2.0;
                double offsetZ = (random.nextDouble() - 0.5) * 2.0;
                double posX = (!this.dragon.isFemale() ? this.targetMate.getX() : this.dragon.getX()) + offsetX;
                double posY = (!this.dragon.isFemale() ? this.targetMate.getY() : this.dragon.getY()) - 1;
                double posZ = (!this.dragon.isFemale() ? this.targetMate.getZ() : this.dragon.getZ()) + offsetZ;

                egg.refreshPositionAndAngles(posX, posY + 1F, posZ, 0.0F, 0.0F);
                this.theWorld.spawnEntity(egg);

                for (int p = 0; p < 17; ++p) {
                    double d0 = random.nextGaussian() * 0.02D;
                    double d1 = random.nextGaussian() * 0.02D;
                    double d2 = random.nextGaussian() * 0.02D;
                    double d3 = random.nextDouble() * egg.getWidth() * 2.0D - egg.getWidth();
                    double d4 = 0.5D + random.nextDouble() * egg.getHeight();
                    double d5 = random.nextDouble() * egg.getWidth() * 2.0D - egg.getWidth();
                    this.theWorld.addParticle(ParticleTypes.HEART, egg.getX() + d3, egg.getY() + d4, egg.getZ() + d5, d0, d1, d2);
                }
            }

            if (this.theWorld.getGameRules().getBoolean(GameRules.DO_MOB_LOOT)) {
                this.theWorld.spawnEntity(new ExperienceOrbEntity(this.theWorld, this.dragon.getX(), this.dragon.getY(), this.dragon.getZ(), random.nextInt(15) + 10));
            }*/
        }
    }

    /*public double getMountedHeightOffset() {
        if (this.isGoingUp() && this.isFlying() && !this.isOnGround()) {
            return super.getMountedHeightOffset() - 0.2f;
        } else if (this.isGoingDown() && this.isFlying() && !this.isOnGround()) {
            return super.getMountedHeightOffset() + 1.32;
        } else {
            return super.getMountedHeightOffset() + 0.62;
        }
    }*/

    public static class DragonFetchGoal extends Goal {
        private final DragonEntity dragonEntity;
        private ThrownItemEntity fetchBallEntity;
        private ItemEntity itemEntity;
        private boolean failed;

        public DragonFetchGoal(DragonEntity dragonEntity) {
            this.dragonEntity = dragonEntity;
            this.setControls(EnumSet.of(Goal.Control.JUMP, Goal.Control.MOVE, Goal.Control.LOOK));
        }

        @Override
        public boolean shouldContinue() {
            return !canStop();
        }

        @Override
        public boolean canStart() {
            if (failed) {
                return false;
            }

            if (!this.dragonEntity.isTamed()) {
                return false;
            }

            if (this.dragonEntity.isBaby()) {
                return false;
            }

            if (this.dragonEntity.isInsideWaterOrBubbleColumn()) {
                return false;
            }

            if (!this.dragonEntity.isOnGround()) {
                return false;
            }

            if (this.dragonEntity.hasStackInMouth()) {
                return false;
            }

            if (this.dragonEntity.getOwner() == null) {
                return false;
            }

            if (!this.dragonEntity.isReadyToPlay()) {
                return false;
            }

            List<ThrownItemEntity> list = this.dragonEntity.getWorld().getEntitiesByClass(ThrownItemEntity.class, this.dragonEntity.getBoundingBox().expand(8.0D, 8.0D, 8.0D), DragonEntity.FOLLOWABLE_DROP_FILTER);

            if (list.size() == 0) {
                return false;
            }

            this.fetchBallEntity = list.get(0);
            return this.fetchBallEntity != null;
        }

        @Override
        public boolean canStop() {
            if (failed) {
                return true;
            }

            if (this.fetchBallEntity != null || this.itemEntity != null || this.dragonEntity.hasStackInMouth(LLGDragonsItems.FETCH_BALL)) {
                return false;
            }

            return true;
        }

        @Override
        public void start() {
            this.dragonEntity.getNavigation().stop();
            this.dragonEntity.getNavigation().startMovingTo(fetchBallEntity, 1.5);
        }

        @Override
        public void stop() {
            this.dragonEntity.setReadyToPlay(false);
            this.fetchBallEntity = null;
            this.itemEntity = null;
            this.failed = false;
        }

        @Override
        public void tick() {
            if (this.dragonEntity.isTouchingWater() && this.dragonEntity.getFluidHeight(FluidTags.WATER) > this.dragonEntity.getSwimHeight() || this.dragonEntity.isInLava()) {
                if (this.dragonEntity.getRandom().nextFloat() < 0.8F) {
                    this.dragonEntity.getJumpControl().setActive();
                }
            }

            if (this.fetchBallEntity != null) {
                if (this.fetchBallEntity.isRemoved()) {
                    List<ItemEntity> list = this.dragonEntity.getWorld().getEntitiesByClass(ItemEntity.class, this.dragonEntity.getBoundingBox().expand(20.0D, 8.0D, 20.0D), DragonEntity.PICKABLE_DROP_FILTER);

                    for (ItemEntity itemEntity : list) {
                        if (itemEntity.getStack().isIn(LLGDragonsTags.Items.FETCH_BALL)) {
                            this.itemEntity = itemEntity;
                            break;
                        }
                    }

                    if (this.itemEntity == null) {
                        fail();
                    }

                    this.fetchBallEntity = null;
                    return;
                }

                if (this.dragonEntity.getNavigation().isIdle()) {
                    this.dragonEntity.getNavigation().startMovingTo(fetchBallEntity, 1.3);
                }
                return;
            }

            if (this.itemEntity != null) {
                if (this.itemEntity.isRemoved()) {
                    if (!this.dragonEntity.hasStackInMouth(LLGDragonsItems.FETCH_BALL)) {
                        fail();
                    }

                    this.itemEntity = null;
                    return;
                }

                if (this.dragonEntity.squaredDistanceTo(itemEntity) < 3) {
                    this.dragonEntity.getNavigation().stop();

                    if (this.dragonEntity.hasStackInMouth()) {
                        this.dragonEntity.dropStackInMouth();
                    }

                    this.dragonEntity.setStackInMouth(this.itemEntity.getStack());
                    this.itemEntity.remove(Entity.RemovalReason.KILLED);
                    this.dragonEntity.getNavigation().startMovingTo(this.dragonEntity.getOwner(), 1);
                    return;
                }

                if (this.dragonEntity.getNavigation().isIdle()) {
                    this.dragonEntity.getNavigation().startMovingTo(itemEntity, 1.5);
                }
                return;
            }

            if (this.dragonEntity.hasStackInMouth(LLGDragonsItems.FETCH_BALL)) {
                if (this.dragonEntity.getNavigation().isIdle()) {
                    this.dragonEntity.getNavigation().startMovingTo(this.dragonEntity.getOwner(), 1);
                }

                if (this.dragonEntity.squaredDistanceTo(this.dragonEntity.getOwner()) < 3) {
                    this.dragonEntity.getNavigation().stop();
                    this.dragonEntity.dropStackInMouth();

                    this.itemEntity = null;
                    this.fetchBallEntity = null;
                }

                return;
            }
        }

        private void fail() {
            this.failed = true;
            this.dragonEntity.getWorld().playSound(null, this.dragonEntity.getX(), this.dragonEntity.getY(), this.dragonEntity.getZ(), LLGDragonsSounds.NIGHTFURY_ROAR, SoundCategory.NEUTRAL, 0.4f, 1.05f);
        }
    }

    public void setClientLocatorPosition(String name, Vec3d pos) {
        if (name == null || pos == null) return;
        this.clientLocatorCache.put(name, pos);
    }

    public Vec3d getClientLocatorPosition(String name) {
        if (name == null) return null;
        return this.clientLocatorCache.get(name);
    }
}