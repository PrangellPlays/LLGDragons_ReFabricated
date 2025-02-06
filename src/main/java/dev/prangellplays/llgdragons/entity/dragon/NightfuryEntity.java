package dev.prangellplays.llgdragons.entity.dragon;

import dev.prangellplays.llgdragons.LLGDragonsClient;
import dev.prangellplays.llgdragons.entity.DragonEntity;
import dev.prangellplays.llgdragons.entity.FetchBallEntity;
import dev.prangellplays.llgdragons.entity.dragonability.nightfury.PlasmaBlastEntity;
import dev.prangellplays.llgdragons.entity.goal.DragonSitGoal;
import dev.prangellplays.llgdragons.entity.goal.DragonStillGoal;
import dev.prangellplays.llgdragons.init.LLGDragonsEntities;
import dev.prangellplays.llgdragons.init.LLGDragonsItems;
import dev.prangellplays.llgdragons.init.LLGDragonsSounds;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.*;
import net.minecraft.world.EntityView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;
import static net.minecraft.entity.attribute.EntityAttributes.GENERIC_FLYING_SPEED;
import static net.minecraft.entity.attribute.EntityAttributes.GENERIC_MOVEMENT_SPEED;

public class NightfuryEntity extends DragonEntity implements GeoEntity {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private static final Ingredient TAMING_INGREDIENT;

    private static final TrackedData<Boolean> GENDER = DataTracker.registerData(NightfuryEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> SLEEPING = DataTracker.registerData(NightfuryEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> HAS_SADDLE = DataTracker.registerData(NightfuryEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    private static final TrackedData<Boolean> FLYING = DataTracker.registerData(NightfuryEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    public static final TrackedData<Boolean> START_FLYING = DataTracker.registerData(NightfuryEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    public static final TrackedData<Boolean> CLIENT_START_FLYING = DataTracker.registerData(NightfuryEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    public static final TrackedData<Boolean> CLIENT_END_FLYING = DataTracker.registerData(NightfuryEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    private static final TrackedData<Boolean> GOING_UP = DataTracker.registerData(NightfuryEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> GOING_DOWN = DataTracker.registerData(NightfuryEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    private static final TrackedData<Boolean> STILL = DataTracker.registerData(NightfuryEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    private int startFlyingTimer = 0;
    private float dragonSideSpeed = 0.0F;
    private float dragonForwardSpeed = 0.0F;
    private float turningFloat;
    private int onGroundTicker;
    public boolean flying;
    public boolean saddled;
    private int startFlyingTime = 0;
    public static final int GROUND_CLEARENCE_THRESHOLD = 3;
    private boolean nearGround;
    public int flapSoundChance;
    public boolean still;

    public NightfuryEntity(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createNightfuryAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 60.0D)
                .add(GENERIC_MOVEMENT_SPEED, 0.3f)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 4.0f)
                .add(GENERIC_FLYING_SPEED, 1f);
                //.add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 4);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("flying", this.flying);
        nbt.putBoolean("saddled", this.dataTracker.get(HAS_SADDLE));
        nbt.putBoolean("still", this.dataTracker.get(STILL));
        //nbt.putBoolean("still_facing", this.dataTracker.get(STILL_FACING));
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.flying = nbt.getBoolean("flying");
        this.dataTracker.set(FLYING, this.flying);
        this.saddled = nbt.getBoolean("saddled");
        this.dataTracker.set(HAS_SADDLE, this.saddled);
        this.still = nbt.getBoolean("still");
        this.dataTracker.set(STILL, this.still);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(GENDER, false);
        this.dataTracker.startTracking(SLEEPING, false);
        this.dataTracker.startTracking(HAS_SADDLE, false);

        this.dataTracker.startTracking(FLYING, false);
        this.dataTracker.startTracking(CLIENT_END_FLYING, false);
        this.dataTracker.startTracking(START_FLYING, false);
        this.dataTracker.startTracking(CLIENT_START_FLYING, false);

        this.dataTracker.startTracking(GOING_UP, false);
        this.dataTracker.startTracking(GOING_DOWN, false);

        this.dataTracker.startTracking(STILL, false);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new DragonStillGoal(this));
        this.goalSelector.add(2, new DragonSitGoal(this));
        this.goalSelector.add(3, new TemptGoal(this, 0.75f, TAMING_INGREDIENT, false));
        this.goalSelector.add(4, new WanderAroundFarGoal(this, 0.75f, 0.2f));
        this.goalSelector.add(5, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.add(6, new LookAroundGoal(this));
    }

    public boolean canFly() {
        return !this.isBaby();
    }

    public boolean shouldFly() {
        if (this.isInAir()) return !this.isOnGround(); // more natural landings
        return this.canFly() && !this.isTouchingWater() && !this.isNearGround();
    }

    public boolean isInAir() {
        return this.flying;
    }

    public void setFlying(boolean flying) {
        this.flying = flying;
    }

    public boolean isNearGround() {
        return this.nearGround;
    }

    public boolean boosting() {
        return MinecraftClient.getInstance().options.sprintKey.isPressed();
    }

    public void setStill(boolean still) {
        this.still = still;
    }

    public boolean isGoingUp() {
        return this.dataTracker.get(GOING_UP);
    }

    public void setGoingUp(boolean goingUp) {
        this.dataTracker.set(GOING_UP, goingUp);
    }

    public boolean isGoingDown() {
        return this.dataTracker.get(GOING_DOWN);
    }

    public void setGoingDown(boolean goingDown) {
        this.dataTracker.set(GOING_DOWN, goingDown);
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return LLGDragonsEntities.NIGHTFURY.create(world);
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getStackInHand(hand);
        Item item = itemstack.getItem();

        Item itemForSitting = Items.STICK;
        Item itemForStillness = LLGDragonsItems.STILLNESS_STAFF;

        if(isTamingItem(itemstack) && !isTamed()) {
            if(this.getWorld().isClient()) {
                return ActionResult.CONSUME;
            } else {
                if (!player.getAbilities().creativeMode) {
                    itemstack.decrement(1);
                }

                spawnItemParticles(itemstack, 16);
                this.playSound(SoundEvents.ENTITY_GENERIC_EAT, 0.5F + 0.5F * (float)this.random.nextInt(2), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);

                if (this.random.nextInt(3) == 0) {
                    super.setOwner(player);
                    this.navigation.recalculatePath();
                    this.setTarget(null);
                    this.getWorld().sendEntityStatus(this, (byte) 7);
                    setSitting(true);
                    setInSittingPose(true);
                }

                return ActionResult.SUCCESS;
            }
        }

        if (isTamed()) {
            if (this.isTamingItem(itemstack)) {
                if (!player.getAbilities().creativeMode) {
                    itemstack.decrement(1);
                }

                spawnItemParticles(itemstack, 16);
                this.playSound(SoundEvents.ENTITY_GENERIC_EAT, 0.5F + 0.5F * (float)this.random.nextInt(2), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
                this.heal((float)item.getFoodComponent().getHunger());
                return ActionResult.SUCCESS;
            }

            if (item == Items.SADDLE) {
                this.getWorld().playSoundFromEntity((PlayerEntity) null, this, SoundEvents.ENTITY_HORSE_SADDLE, SoundCategory.NEUTRAL, 0.8F, 1.0F);
                if (!player.isCreative()) {
                    itemstack.decrement(1);
                }
                this.dataTracker.set(HAS_SADDLE, true);
                return ActionResult.SUCCESS;
            }
        }

        if(isTamed() && hand == Hand.MAIN_HAND && !isTamingItem(itemstack) && item != LLGDragonsItems.FETCH_BALL && item != itemForSitting && item != LLGDragonsItems.DRAGOSPHERE && item != LLGDragonsItems.STILLNESS_STAFF && item != LLGDragonsItems.TELLING_BOOK && this.dataTracker.get(HAS_SADDLE)) {
            if (isSitting()) {
                boolean sitting = !isSitting();
                setSitting(sitting);
                setInSittingPose(sitting);
                setRiding(player);
            } else if (isStill()) {
                boolean still = !isStill();
                setStill(still);
                setRiding(player);
            }
            setRiding(player);

            return ActionResult.SUCCESS;
        }

        if(isTamed() && hand == Hand.MAIN_HAND && item == itemForSitting) {
            boolean sitting = !isSitting();
            setSitting(sitting);
            setInSittingPose(sitting);
            player.sendMessage(Text.of("Sitting: " + sitting), true);
            if (this.getWorld().isClient && !this.isSilent()) {
                if (this.isAlive()) {
                    this.getWorld().playSound(this.getX(), this.getY(), this.getZ(), SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, this.getSoundCategory(), 1.0F, 0.2F + this.random.nextFloat() * 0.3F, false);
                }
            }

            return ActionResult.SUCCESS;
        }

        if(isTamed() && hand == Hand.MAIN_HAND && item == itemForStillness) {
            boolean still = !isStill();
            setStill(still);
            player.sendMessage(Text.of("Still: " + still), true);
            if (this.getWorld().isClient && !this.isSilent()) {
                if (this.isAlive()) {
                    this.getWorld().playSound(this.getX(), this.getY(), this.getZ(), SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, this.getSoundCategory(), 1.0F, 0.2F + this.random.nextFloat() * 0.3F, false);
                }
            }

            return ActionResult.SUCCESS;
        }

        return super.interactMob(player, hand);
    }

    private void spawnItemParticles(ItemStack stack, int count) {
        for(int i = 0; i < count; ++i) {
            Vec3d vec3d = new Vec3d(((double)this.random.nextFloat() - 0.5) * 0.1, Math.random() * 0.1 + 0.1, 0.0);
            vec3d = vec3d.rotateX(-this.getPitch() * 0.017453292F);
            vec3d = vec3d.rotateY(-this.getYaw() * 0.017453292F);
            double d = (double)(-this.random.nextFloat()) * 0.6 - 0.3;
            Vec3d vec3d2 = new Vec3d(((double)this.random.nextFloat() - 0.5) * 0.3, d, 0.6);
            vec3d2 = vec3d2.rotateX(-this.getPitch() * 0.017453292F);
            vec3d2 = vec3d2.rotateY(-this.getYaw() * 0.017453292F);
            vec3d2 = vec3d2.add(this.getX(), this.getEyeY(), this.getZ());
            this.getWorld().addParticle(new ItemStackParticleEffect(ParticleTypes.ITEM, stack), vec3d2.x, vec3d2.y, vec3d2.z, vec3d.x, vec3d.y + 0.05, vec3d.z);
        }

    }

    @Override
    public EntityView method_48926() {
        return this.getWorld();
    }

    @Override
    protected int computeFallDamage(float fallDistance, float damageMultiplier) {
        return 0;
    }

    @Override
    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        return false;
    }

    @Override
    public boolean isFireImmune() {
        return true;
    }

    @Override
    public boolean isClimbing() {
        return false;
    }

    @Override
    public boolean canFreeze() {
        return false;
    }

    @Override
    public boolean isPushable() {
        return !this.hasPassengers();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    private PlayState predicate(AnimationState<NightfuryEntity> nightfuryEntityAnimationState) {
        if (this.flying) {
            if (this.boosting()) {
                if (this.isGoingUp() && this.isInAir()) {
                    nightfuryEntityAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.nightfury.flyingupboost", Animation.LoopType.LOOP));
                } else if (this.isGoingDown() && this.isInAir()) {
                    nightfuryEntityAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.nightfury.flyingdownboost", Animation.LoopType.LOOP));
                } else {
                    nightfuryEntityAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.nightfury.flyingboost", Animation.LoopType.LOOP));
                }
            } else {
                if (this.isGoingUp() && this.isInAir()) {
                    nightfuryEntityAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.nightfury.flyingup", Animation.LoopType.LOOP));
                } else if (this.isGoingDown() && this.isInAir()) {
                    nightfuryEntityAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.nightfury.flyingdown", Animation.LoopType.LOOP));
                } else {
                    nightfuryEntityAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.nightfury.flying", Animation.LoopType.LOOP));
                }
            }
        } else if (nightfuryEntityAnimationState.isMoving()) {
            nightfuryEntityAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.nightfury.walk", Animation.LoopType.LOOP));
        } else {
            nightfuryEntityAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.nightfury.idle", Animation.LoopType.LOOP));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    /* RIDEABLE */

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        return (LivingEntity) this.getFirstPassenger();
    }

    private void setRiding(PlayerEntity player) {
        this.setSitting(false);
        this.setInSittingPose(false);
        this.setStill(false);

        player.setYaw(this.getYaw());
        player.setPitch(this.getPitch());
        player.startRiding(this);
    }

    @Override
    public void tick() {
        super.tick();
        PlayerEntity player = (PlayerEntity) this.getControllingPassenger();
        this.nearGround = this.isOnGround() || !this.getWorld().isSpaceEmpty(this, new Box(this.getX(), this.getY(), this.getZ(), this.getX(), this.getY() - (GROUND_CLEARENCE_THRESHOLD * this.getScaleFactor()), this.getZ()));
        boolean flying = this.shouldFly();
        if (flying != this.isInAir()) {
            this.setFlying(flying);
        }

        if (this.flying && !this.hasLocalDriver()) {
            this.setFlying(false);
        }

        if (this.getWorld().isClient && !this.isSilent() && this.flying) {
            if (this.isAlive() && this.random.nextInt(10) < this.flapSoundChance++) {
                this.resetFlapSoundDelay();
                this.getWorld().playSound(this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_ENDER_DRAGON_FLAP, this.getSoundCategory(), 1.0F, 0.2F + this.random.nextFloat() * 0.3F, false);
            }
        }

        if (this.getWorld().isClient) {
            if (MinecraftClient.getInstance().options.jumpKey.isPressed()) {
                this.setGoingUp(true);
            } else if (!MinecraftClient.getInstance().options.jumpKey.isPressed()) {
                this.setGoingUp(false);
            }

            if (LLGDragonsClient.descend.isPressed()) {
                this.setGoingDown(true);
            } else if (!LLGDragonsClient.descend.isPressed()) {
                this.setGoingDown(false);
            }
        }

        if (this.getWorld().isClient) {
            if (LLGDragonsClient.dragonAttackPrimary.isPressed()) {
                if (!this.getWorld().isClient()) {
                    PlasmaBlastEntity plasmaBlastEntity = new PlasmaBlastEntity(LLGDragonsEntities.PLASMA_BLAST, this.getWorld());
                    plasmaBlastEntity.setVelocity(this, this.getPitch(), this.getYaw(), 0.0F, 1.5F, 1.0F);
                    this.getWorld().spawnEntity(plasmaBlastEntity);
                }
            }
        }
    }

    public int getMinFlapSoundDelay() {
        if (this.boosting()) {
            return 10;
        }
        return 30;
    }

    private void resetFlapSoundDelay() {
        this.flapSoundChance = -this.getMinFlapSoundDelay();
    }

    @Override
    public int getMinAmbientSoundDelay() {
        return 320;
    }

    protected SoundEvent getAmbientSound() {
        return LLGDragonsSounds.NIGHTFURY_ROAR;
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return LLGDragonsSounds.NIGHTFURY_ROAR;
    }

    @Override
    public float getSoundPitch() {
        return 1.0F;
    }

    protected float getSoundVolume() {
        return 1.0F;
    }

    @Override
    public void travel(Vec3d vec3) {
        if (this.isInAir()) {
            if (this.isLogicalSideForUpdatingMovement()) {
                float boost = this.boosting() ? 2 : 1;
                this.updateVelocity(this.getMovementSpeed() * boost, vec3);
                this.move(MovementType.SELF, this.getVelocity());
                this.setVelocity(this.getVelocity().multiply(0.0f));
            }
            this.updateLimbs(true);
        } else super.travel(vec3);
    }

    @Override
    protected Vec3d getControlledMovementInput(PlayerEntity player, Vec3d move) {
        double moveSideways = player.sidewaysSpeed;
        double moveY = move.y;
        double moveForward = player.forwardSpeed;
        if (this.hasLocalDriver()) {
            moveForward = moveForward < 0 ? moveForward / 3 : moveForward;
        }
        if (this.isInAir() && this.hasLocalDriver()) {
            if (MinecraftClient.getInstance().options.jumpKey.isPressed()) {
                moveY = 1;
            } else if (LLGDragonsClient.descend.isPressed()) {
                moveY = -1;
            }
        }

        float speed = this.getSaddledSpeed(player);
        float verticalSpeed = speed - 0.6f;
        return new Vec3d(moveSideways * speed, moveY * verticalSpeed, moveForward * speed);
    }

    @Override
    protected void tickControlled(PlayerEntity player, Vec3d movementInput) {
        // rotate head to match driver.
        float yaw = player.headYaw;
        /*if (movementInput.z > 0) // rotate in the direction of the drivers controls
            yaw += (float) MathHelper.atan2(player.forwardSpeed, player.sidewaysSpeed) * (180f / (float) Math.PI) - 90;*/
        this.headYaw = yaw;
        this.setPitch(player.getPitch() * 0.68f);
        // rotate body towards the head
        this.setYaw(MathHelper.clampAngle(this.headYaw, this.getYaw(), 10));
        if (this.isLogicalSideForUpdatingMovement())
            if (!this.flying && MinecraftClient.getInstance().options.jumpKey.isPressed()) {
                this.flying = true;
                this.getDataTracker().set(FLYING, true);
                this.startFlyingTimer = 0;
                this.startFlyingTime = (int) this.getWorld().getTime();
            }
    }

    @Override
    protected float getSaddledSpeed(PlayerEntity driver) {
        return (float) this.getAttributeValue(this.isInAir() ? GENERIC_FLYING_SPEED : GENERIC_MOVEMENT_SPEED);
    }

    @Override
    public Vec3d updatePassengerForDismount(LivingEntity passenger) {
        Direction direction = this.getMovementDirection();
        if (direction.getAxis() == Direction.Axis.Y) {
            return super.updatePassengerForDismount(passenger);
        }
        int[][] is = Dismounting.getDismountOffsets(direction);
        BlockPos blockPos = this.getBlockPos();
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        for (EntityPose entityPose : passenger.getPoses()) {
            Box box = passenger.getBoundingBox(entityPose);
            for (int[] js : is) {
                mutable.set(blockPos.getX() + js[0], blockPos.getY(), blockPos.getZ() + js[1]);
                double d = this.getWorld().getDismountHeight(mutable);
                if (!Dismounting.canDismountInBlock(d)) continue;
                Vec3d vec3d = Vec3d.ofCenter(mutable, d);
                if (!Dismounting.canPlaceEntityAt(this.getWorld(), passenger, box.offset(vec3d))) continue;
                passenger.setPose(entityPose);
                return vec3d;
            }
        }
        return super.updatePassengerForDismount(passenger);
    }

    public boolean isTamingItem(ItemStack stack) {
        return TAMING_INGREDIENT.test(stack);
    }

    static {
        TAMING_INGREDIENT = Ingredient.ofItems(new ItemConvertible[]{Items.COD, Items.SALMON});
    }

    public PlayerEntity getRidingPlayer() {
        if (this.getControllingPassenger() instanceof PlayerEntity player) {
            return player;
        }

        return null;
    }

    public boolean isFlying() {
        return this.dataTracker.get(FLYING);
    }

    public boolean isStill() {
        return this.still;
    }

    public boolean hasLocalDriver() {
        return this.getControllingPassenger() instanceof PlayerEntity player && player.isMainPlayer();
    }

    public double getMountedHeightOffset() {
        if (this.isGoingUp() && this.isFlying() && !this.isOnGround()) {
            return super.getMountedHeightOffset() - 0.2f;
        } else if (this.isGoingDown() && this.isFlying() && !this.isOnGround()) {
            return super.getMountedHeightOffset() + 1.32;
        } else {
            return super.getMountedHeightOffset() + 0.62;
        }
    }

    @Override
    public int getMaxLookYawChange() {
        if (this.isStill()) {
            return 0;
        }
        return 10;
    }

    @Override
    public int getMaxLookPitchChange() {
        if (this.isStill()) {
            return 0;
        }
        return 40;
    }

    @Override
    public int getMaxHeadRotation() {
        if (this.isStill()) {
            return 0;
        }
        return 75;
    }
}
