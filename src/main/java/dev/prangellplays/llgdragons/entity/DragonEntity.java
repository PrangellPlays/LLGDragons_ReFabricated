package dev.prangellplays.llgdragons.entity;

import dev.prangell.lumicode.entity.ai.LumicodePathFinderNavigateGround;
import dev.prangellplays.llgdragons.LLGDragonsClient;
import dev.prangellplays.llgdragons.init.LLGDragonsItems;
import dev.prangellplays.llgdragons.init.LLGDragonsSounds;
import dev.prangellplays.llgdragons.util.DragonCameraManager;
import dev.prangellplays.llgdragons.util.LLGDragonsTags;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.ServerConfigHandler;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.*;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector4f;
import software.bernie.geckolib.animatable.GeoEntity;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static net.minecraft.entity.attribute.EntityAttributes.GENERIC_FLYING_SPEED;
import static net.minecraft.entity.attribute.EntityAttributes.GENERIC_MOVEMENT_SPEED;

public abstract class DragonEntity extends TameableEntity implements GeoEntity {
    private static final TrackedData<Optional<UUID>> OWNER_UUID = DataTracker.registerData(DragonEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);

    private static final TrackedData<Boolean> GENDER = DataTracker.registerData(DragonEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    private static final TrackedData<Integer> DAY_COUNT_AGE = DataTracker.registerData(DragonEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> AGE_INT = DataTracker.registerData(DragonEntity.class, TrackedDataHandlerRegistry.INTEGER);

    public static final TrackedData<ItemStack> FAVOURITE_FOOD = DataTracker.registerData(DragonEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);

    public static final Ingredient TAMING_INGREDIENT;

    private static final TrackedData<Boolean> SLEEPING = DataTracker.registerData(DragonEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> HAS_SADDLE = DataTracker.registerData(DragonEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    private static final TrackedData<Boolean> FLYING = DataTracker.registerData(DragonEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    public static final TrackedData<Boolean> START_FLYING = DataTracker.registerData(DragonEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    public static final TrackedData<Boolean> CLIENT_START_FLYING = DataTracker.registerData(DragonEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    public static final TrackedData<Boolean> CLIENT_END_FLYING = DataTracker.registerData(DragonEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    private static final TrackedData<Boolean> GOING_UP = DataTracker.registerData(DragonEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> GOING_DOWN = DataTracker.registerData(DragonEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    private static final TrackedData<Boolean> STILL = DataTracker.registerData(DragonEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    private static final TrackedData<ItemStack> MOUTH_STACK = DataTracker.registerData(DragonEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);

    public static final TrackedData<Byte> TURNING_STATE = DataTracker.registerData(DragonEntity.class, TrackedDataHandlerRegistry.BYTE);

    public static final TrackedData<Boolean> MOVING_BACKWARDS = DataTracker.registerData(DragonEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    public static final TrackedData<Integer> PRIMARY_ATTACK_COOLDOWN = DataTracker.registerData(DragonEntity.class, TrackedDataHandlerRegistry.INTEGER);

    public static final Predicate<ItemEntity> PICKABLE_DROP_FILTER = (itemEntity) -> !itemEntity.cannotPickup() && itemEntity.isAlive();
    public static final Predicate<Entity> FOLLOWABLE_DROP_FILTER = (entity) -> entity.isAlive();

    public static final List<Item> VANILLA_FOODS;

    public int day_count_age;
    public static final int base_day_length = 24000;
    public int age_int;
    public static ItemStack favouriteFood;
    private boolean sitting;
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
    public int diveSoundChance;
    public boolean still;
    private boolean readyToPlay = false;
    public boolean isSecondaryAttackPressed = false;
    public boolean isPrimaryAttackPressed = false;
    protected float rotationProgress;
    public static final int TRANSITION_TICKS = 10;
    protected int primaryAttackDuration = 20;
    protected int baseSecondaryAttackCooldown = 20;
    protected int basePrimaryAttackCooldown = 20;
    public Matrix4f mouthMatrix = null;
    private Vec3d mouthWorldPos = null;
    public @Nullable Matrix4f riderMatrix = null;

    protected DragonEntity(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected EntityNavigation createNavigation(World world) {
        return new LumicodePathFinderNavigateGround(this, world);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        if (this.getOwnerUuid() != null) {
            nbt.putUuid("owner", this.getOwnerUuid());
        }
        nbt.putBoolean("gender", this.isFemale());
        nbt.putInt("day_count_age", this.dataTracker.get(DAY_COUNT_AGE));
        nbt.putInt("age_int", this.dataTracker.get(AGE_INT));
        nbt.putString("favourite_food", Registries.ITEM.getId(getFavouriteFood()).toString());
        nbt.putBoolean("flying", this.flying);
        nbt.putBoolean("saddled", this.dataTracker.get(HAS_SADDLE));
        nbt.putBoolean("still", this.dataTracker.get(STILL));
        nbt.putBoolean("sleeping", this.isDragonSleeping());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        UUID uUID2;
        if (nbt.containsUuid("owner")) {
            uUID2 = nbt.getUuid("owner");
        } else {
            String string = nbt.getString("owner");
            uUID2 = ServerConfigHandler.getPlayerUuidByName(this.getServer(), string);
        }
        if (uUID2 != null) {
            try {
                this.setOwnerUuid(uUID2);
                this.setTamed(true);
            } catch (Throwable var4) {
                this.setTamed(false);
            }
        }
        this.setGender(nbt.getBoolean("gender"));
        this.day_count_age = nbt.getInt("day_count_age");
        this.dataTracker.set(DAY_COUNT_AGE, this.day_count_age);
        this.age_int = nbt.getInt("age_int");
        this.dataTracker.set(AGE_INT, this.age_int);
        if (nbt.contains("favourite_food")) {
            Identifier id = new Identifier(nbt.getString("favourite_food"));
            ItemStack item = Registries.ITEM.get(id).getDefaultStack();
            this.dataTracker.set(FAVOURITE_FOOD, item);
        }
        this.flying = nbt.getBoolean("flying");
        this.dataTracker.set(FLYING, this.flying);
        this.saddled = nbt.getBoolean("saddled");
        this.dataTracker.set(HAS_SADDLE, this.saddled);
        this.still = nbt.getBoolean("still");
        this.dataTracker.set(STILL, this.still);
        this.setIsDragonSleeping(nbt.getBoolean("sleeping"));
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(OWNER_UUID, Optional.empty());

        this.dataTracker.startTracking(GENDER, false);

        this.dataTracker.startTracking(DAY_COUNT_AGE, 0);
        this.dataTracker.startTracking(AGE_INT, 24000);

        this.dataTracker.startTracking(FAVOURITE_FOOD, Items.COOKED_PORKCHOP.getDefaultStack());

        this.dataTracker.startTracking(SLEEPING, false);
        this.dataTracker.startTracking(HAS_SADDLE, false);

        this.dataTracker.startTracking(FLYING, false);
        this.dataTracker.startTracking(CLIENT_END_FLYING, false);
        this.dataTracker.startTracking(START_FLYING, false);
        this.dataTracker.startTracking(CLIENT_START_FLYING, false);

        this.dataTracker.startTracking(GOING_UP, false);
        this.dataTracker.startTracking(GOING_DOWN, false);

        this.dataTracker.startTracking(STILL, false);

        this.dataTracker.startTracking(MOUTH_STACK, new ItemStack(Items.AIR));

        this.dataTracker.startTracking(TURNING_STATE, (byte)0);//1 - left, 2 - right, 0 - straight

        this.dataTracker.startTracking(MOVING_BACKWARDS, false);

        this.dataTracker.startTracking(PRIMARY_ATTACK_COOLDOWN, 0);
    }

    public boolean isFemale() {
        return this.dataTracker.get(GENDER);
    }

    public void setGender(boolean male) {
        this.dataTracker.set(GENDER, male);
    }

    public int getDayCountAge() {
        return this.dataTracker.get(DAY_COUNT_AGE);
    }

    public void setDayCountAge(int age) {
        this.dataTracker.set(DAY_COUNT_AGE, age);
    }

    public void getAgeInt() {
        this.dataTracker.get(AGE_INT);
    }

    public void setAgeInt(int age) {
        this.dataTracker.set(AGE_INT, age);
    }

    public Item getFavouriteFood() {
        return this.dataTracker.get(FAVOURITE_FOOD).getItem();
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

    public boolean isNocturnal() {
        return false;
    }

    public boolean isDragonSleeping() {
        return this.dataTracker.get(SLEEPING);
    }

    public void setIsDragonSleeping(boolean sleeping) {
        this.dataTracker.set(SLEEPING, sleeping);
    }

    protected void sleepMechanics() {
        if (!getWorld().isClient()) { //&& this.isTame()) {      -   cause wild dragons that were already sleeping to never wake up
            if (isSitting()) {
                setIsDragonSleeping(isNocturnal() ? getWorld().isDay() : !getWorld().isDay());
            } else {
                setIsDragonSleeping(false);
            }
        }
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

    public boolean isPrimaryAttack() {
        return getPrimaryAttackCooldown() > getMaxPrimaryAttackCooldown() - primaryAttackDuration;
    }

    public void setPrimaryAttackCooldown(int state) {
        dataTracker.set(PRIMARY_ATTACK_COOLDOWN, state);
    }

    public int getPrimaryAttackCooldown() {
        return  dataTracker.get(PRIMARY_ATTACK_COOLDOWN);
    }

    public int getMaxSecondaryAttackCooldown() {
        return (int) (baseSecondaryAttackCooldown);
    }

    public int getMaxPrimaryAttackCooldown() {
        return  (int) (basePrimaryAttackCooldown);
    }

    public byte getTurningState() {
        return dataTracker.get(TURNING_STATE);
    }
    public void setTurningState(byte state) {
        dataTracker.set(TURNING_STATE, state);
    }

    public boolean isMoving() {
        return getVelocity().getZ() != 0 || getVelocity().getX() != 0;
    }

    public boolean isMovingBackwards() {
        return dataTracker.get(MOVING_BACKWARDS);
    }

    public void setMovingBackwards(boolean state) {
        dataTracker.set(MOVING_BACKWARDS, state);
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
    public boolean canBeLeashedBy(PlayerEntity player) {
        return false;
    }

    public boolean boosting() {
        return MinecraftClient.getInstance().options.sprintKey.isPressed();
    }

    public boolean canBoost() {
        return false;
    }

    public void setMouthMatrix(Matrix4f matrix) {
        this.mouthMatrix = new Matrix4f(matrix);
    }

    public Vec3d getMouthWorldPos() {
        if (mouthWorldPos == null) {
            return this.getPos().add(0, this.getStandingEyeHeight(), 0);
        }
        return mouthWorldPos;
    }

    public void setMouthWorldPos(Vec3d pos) {
        this.mouthWorldPos = pos;
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
                    this.setOwner(player);
                    this.navigation.recalculatePath();
                    this.setTarget(null);
                    this.getWorld().sendEntityStatus(this, (byte) 7);
                    /*setSitting(true);
                    setInSittingPose(true);*/
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
            } else if (this.isBreedingItem(itemstack) && !this.isBaby()) {
                //this.setBreedingAge(600);
                this.setLoveTicks(600);
                if (!player.getAbilities().creativeMode) {
                    itemstack.decrement(1);
                }

                spawnItemParticles(itemstack, 16);
                this.playSound(SoundEvents.ENTITY_GENERIC_EAT, 0.5F + 0.5F * (float)this.random.nextInt(2), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
                if (item != Items.CAKE) {
                    this.heal((float) item.getFoodComponent().getHunger());
                } else {
                    this.heal(7.0F);
                }
                this.lovePlayer(player);
                return ActionResult.SUCCESS;
            } else if (this.isBreedingItem(itemstack) && this.isBaby()) {
                return ActionResult.FAIL;
            }

            if (item == LLGDragonsItems.FETCH_BALL) {
                setSitting(false);
                setInSittingPose(false);
                setStill(false);
                setReadyToPlay(true);
            }

            if (item == Items.SADDLE && !this.isBaby()) {
                this.getWorld().playSoundFromEntity((PlayerEntity) null, this, SoundEvents.ENTITY_HORSE_SADDLE, SoundCategory.NEUTRAL, 0.8F, 1.0F);
                if (!player.isCreative()) {
                    itemstack.decrement(1);
                }
                this.dataTracker.set(HAS_SADDLE, true);
                return ActionResult.SUCCESS;
            }

            if (item == Items.SHEARS && !this.isBaby()) {
                this.getWorld().playSoundFromEntity((PlayerEntity) null, this, SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.NEUTRAL, 0.8F, 1.0F);
                if (!player.isCreative()) {
                    itemstack.damage(1, player, (p) -> {p.sendToolBreakStatus(player.getActiveHand());});
                }
                this.dataTracker.set(HAS_SADDLE, false);
                player.giveItemStack(Items.SADDLE.getDefaultStack());
                return ActionResult.SUCCESS;
            }
        }

        if(isTamed() && hand == Hand.MAIN_HAND && !isTamingItem(itemstack) && !isBreedingItem(itemstack) && item != LLGDragonsItems.FETCH_BALL && item != itemForSitting && item != LLGDragonsItems.DRAGOSPHERE && item != LLGDragonsItems.STILLNESS_STAFF && item != LLGDragonsItems.TELLING_BOOK && this.dataTracker.get(HAS_SADDLE) && !this.isBaby()) {
            if (isInSittingPose()) {
                boolean sitting = !isInSittingPose();
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
            boolean sitting = !isInSittingPose();
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
        for (int i = 0; i < count; ++i) {
            Vec3d particleOrigin = this.getMouthWorldPos();
            if (stack.isOf(Items.CAKE)) {
                this.getWorld().addParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.CAKE.getDefaultState()), particleOrigin.x, particleOrigin.y, particleOrigin.z, particleOrigin.x, particleOrigin.y + 0.05, particleOrigin.z);
            } else {
                this.getWorld().addParticle(new ItemStackParticleEffect(ParticleTypes.ITEM, stack), particleOrigin.x, particleOrigin.y, particleOrigin.z, particleOrigin.x, particleOrigin.y + 0.05, particleOrigin.z);
            }
        }
    }

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
                if (this.isGoingDown() && this.canBoost() && this.boosting()) {

                } else if (this.isGoingDown() && this.canBoost() && !this.boosting()) {
                    this.resetFlapSoundDelay();
                    this.getWorld().playSound(this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_ENDER_DRAGON_FLAP, this.getSoundCategory(), 1.0F, 0.2F + this.random.nextFloat() * 0.3F, false);
                } else {
                    this.resetFlapSoundDelay();
                    this.getWorld().playSound(this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_ENDER_DRAGON_FLAP, this.getSoundCategory(), 1.0F, 0.2F + this.random.nextFloat() * 0.3F, false);
                }
            }
            if (this.isAlive() && this.random.nextInt(10) < this.diveSoundChance++) {
                if (this.isGoingDown() && this.canBoost() && this.boosting()) {
                    this.resetDiveSoundDelay();
                    //this.getWorld().playSound(this.getX(), this.getY(), this.getZ(), SoundEvents.ITEM_ELYTRA_FLYING, this.getSoundCategory(), 0.6F, 0.2F + this.random.nextFloat() * 0.3F, false);
                }
            }

            if (!isFlying() || this.isNearGround() || !this.isGoingDown() && this.random.nextInt(10) > this.diveSoundChance++) {
                MinecraftClient.getInstance().getSoundManager().stopSounds(SoundEvents.ITEM_ELYTRA_FLYING.getId(), this.getSoundCategory());
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

        if (this.age % 1200 == 0) {
            sleepMechanics();
            if (this.isDragonSleeping()) this.getNavigation().stop();
        }

        if (getPrimaryAttackCooldown() > 0) setPrimaryAttackCooldown(getPrimaryAttackCooldown() - 1);

        if (getWorld().isClient() && getControllingPassenger() == MinecraftClient.getInstance().player) {
            isSecondaryAttackPressed = LLGDragonsClient.dragonAttackSecondary.wasPressed();
            isPrimaryAttackPressed = LLGDragonsClient.dragonAttackPrimary.wasPressed();
        }
        if (getControllingPassenger() == null) updateInputs();

        this.setAgeInt(dataTracker.get(AGE_INT) - 1);

        if (dataTracker.get(AGE_INT) == 0 || dataTracker.get(AGE_INT) <= -24000) {
            setDayCountAge(this.dataTracker.get(DAY_COUNT_AGE) + 1);
            this.setAgeInt(base_day_length);
        }

        if (this.dataTracker.get(DAY_COUNT_AGE) >= 6) {
            this.setBaby(false);
        }

        if (this.dataTracker.get(DAY_COUNT_AGE) < 6) {
            this.setBaby(true);
        }
    }

    public int getMinFlapSoundDelay() {
        if (this.canBoost() && this.boosting()) {
            return 10;
        }
        return 30;
    }

    public int getMinDiveSoundDelay() {
        return 210;
    }

    public void resetDiveSoundDelay() {
        this.diveSoundChance = -this.getMinDiveSoundDelay();
    }

    public void resetFlapSoundDelay() {
        this.flapSoundChance = -this.getMinFlapSoundDelay();
    }

    @Override
    public int getMinAmbientSoundDelay() {
        return 320;
    }

    @Override
    public float getSoundPitch() {
        if (this.isBaby()) {
            return 1.3f;
        } else {
            return 1.0F;
        }
    }

    protected float getSoundVolume() {
        return 1.0F;
    }

    @Override
    public void travel(Vec3d vec3) {
        if (this.isInAir()) {
            if (this.isLogicalSideForUpdatingMovement()) {
                float boost = this.canBoost() && this.boosting() ? 2 : 1;
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
                if (this.canBoost() && this.boosting()) {
                    moveY = -2;
                } else {
                    moveY = -1;
                }
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
        return !this.isOnGround() && !this.isTouchingWater();
    }

    public boolean isStill() {
        return this.still;
    }

    public boolean hasLocalDriver() {
        return this.getControllingPassenger() instanceof PlayerEntity player && player.isMainPlayer();
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

    @Override
    public boolean cannotDespawn() {
        return this.isTamed();
    }

    @Override
    public @Nullable EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        this.setGender(this.getRandom().nextBoolean());
        this.setDayCountAge(6);
        this.setBaby(false);
        this.setAgeInt(base_day_length);
        this.chooseFavouriteFood();
        this.dataTracker.set(FAVOURITE_FOOD, favouriteFood);
        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }

    @Override
    public void onSpawnPacket(EntitySpawnS2CPacket packet) {
        this.setAgeInt(base_day_length);
        super.onSpawnPacket(packet);
    }

    public boolean isBreedingItem(ItemStack stack) {
        Item favourite = this.getFavouriteFood();
        return stack.isOf(favourite);
    }

    public void chooseFavouriteFood() {
        Random random = new Random();
        favouriteFood = VANILLA_FOODS.get(random.nextInt(VANILLA_FOODS.size())).getDefaultStack();
    }

    public boolean hasStackInMouth() {
        return getStackInMouth() != null && getStackInMouth().getItem() != Items.AIR;
    }

    public boolean hasStackInMouth(ItemStack stack) {
        return hasStackInMouth(stack.getItem());
    }

    public boolean hasStackInMouth(Item item) {
        return hasStackInMouth() && getStackInMouth().getItem() == item;
    }

    public boolean hasStackInMouth(TagKey<Item> tag) {
        return hasStackInMouth() && getStackInMouth().isIn(tag);
    }

    public ItemStack getStackInMouth() {
        return this.dataTracker.get(MOUTH_STACK);
    }

    public void setStackInMouth(ItemStack itemStack) {
        if(itemStack == null) {
            itemStack = new ItemStack(Items.AIR);
        }

        this.dataTracker.set(MOUTH_STACK, itemStack);
    }

    public boolean isReadyToPlay() {
        return readyToPlay;
    }

    public void setReadyToPlay(boolean readyToPlay) {
        this.readyToPlay = readyToPlay;
    }

    public void dropStackInMouth() {
        if(hasStackInMouth()) {
            ItemScatterer.spawn(this.getWorld(), this.getX(), this.getY(), this.getZ(), getStackInMouth());
            setStackInMouth(null);
        }
    }

    @Override
    protected void addPassenger(Entity passenger) {
        super.addPassenger(passenger);
        if (this.hasLocalDriver()) {
            DragonCameraManager.onDragonMount();
        }
    }

    @Override
    protected void removePassenger(Entity passenger) {
        super.removePassenger(passenger);
        if (this.hasLocalDriver()) {
            DragonCameraManager.onDragonDismount();
        }
    }

    private void updateRotationProgress() {
        switch (getTurningState()) {
            case 1 -> {
                if (rotationProgress < TRANSITION_TICKS) rotationProgress++;
            }
            case 2 -> {
                if (rotationProgress > -TRANSITION_TICKS) rotationProgress--;
            }
            default -> {
                if (rotationProgress != 0) {
                    if (rotationProgress > 0) rotationProgress--;
                    else  rotationProgress++;
                }
            }
        }
    }

    public void updateInputs() {
    }

    @Override
    public void dismountVehicle() {
        super.dismountVehicle();
    }

    static {
        List<Item> foodPool = new ArrayList<>();

        foodPool.add(Items.APPLE);
        foodPool.add(Items.BAKED_POTATO);
        foodPool.add(Items.BEEF);
        foodPool.add(Items.BEETROOT);
        foodPool.add(Items.BREAD);
        foodPool.add(Items.CARROT);
        foodPool.add(Items.CHICKEN);
        foodPool.add(Items.CHORUS_FRUIT);
        foodPool.add(Items.COD);
        foodPool.add(Items.COOKED_BEEF);
        foodPool.add(Items.COOKED_CHICKEN);
        foodPool.add(Items.COOKED_COD);
        foodPool.add(Items.COOKED_MUTTON);
        foodPool.add(Items.COOKED_PORKCHOP);
        foodPool.add(Items.COOKED_RABBIT);
        foodPool.add(Items.COOKED_SALMON);
        foodPool.add(Items.COOKIE);
        foodPool.add(Items.DRIED_KELP);
        foodPool.add(Items.GOLDEN_CARROT);
        foodPool.add(Items.MELON_SLICE);
        foodPool.add(Items.MUTTON);
        foodPool.add(Items.PORKCHOP);
        foodPool.add(Items.PUMPKIN_PIE);
        foodPool.add(Items.RABBIT);
        foodPool.add(Items.SALMON);
        foodPool.add(Items.SWEET_BERRIES);
        foodPool.add(Items.GLOW_BERRIES);
        foodPool.add(Items.TROPICAL_FISH);
        foodPool.add(Items.CAKE);

        VANILLA_FOODS = List.copyOf(foodPool);
    }

    public static class DragonSitGoal extends Goal {
        private final DragonEntity dragonEntity;

        public DragonSitGoal(DragonEntity dragonEntity) {
            this.dragonEntity = dragonEntity;
            this.setControls(EnumSet.of(Goal.Control.JUMP, Goal.Control.MOVE));
        }

        @Override
        public boolean shouldContinue() {
            return this.dragonEntity.isInSittingPose();
        }

        @Override
        public boolean canStart() {
            if (!this.dragonEntity.isTamed()) {
                return false;
            } else if (this.dragonEntity.isInsideWaterOrBubbleColumn()) {
                return false;
            } else if (!this.dragonEntity.isOnGround()) {
                return false;
            } else {
                if (this.dragonEntity.getOwner() == null) {
                    // Has to be true cause if Owner is not there
                    // Only if owner is there, dragon can get set to walk
                    return true;
                } else {
                    return this.dragonEntity.isInSittingPose();
                }
            }
        }

        @Override
        public void start() {
            this.dragonEntity.getNavigation().stop();
            this.dragonEntity.setSitting(true);
        }

        @Override
        public void stop() {
            this.dragonEntity.setSitting(false);
        }
    }

    public static class DragonStillGoal extends Goal {
        private final DragonEntity dragonEntity;

        public DragonStillGoal(DragonEntity dragonEntity) {
            this.dragonEntity = dragonEntity;
            this.setControls(EnumSet.of(Control.JUMP, Control.MOVE));
        }

        @Override
        public boolean shouldContinue() {
            return this.dragonEntity.isStill();
        }

        @Override
        public boolean canStart() {
            if (!this.dragonEntity.isTamed()) {
                return false;
            } else if (this.dragonEntity.isInsideWaterOrBubbleColumn()) {
                return false;
            } else if (!this.dragonEntity.isOnGround()) {
                return false;
            } else {
                if (this.dragonEntity.getOwner() == null) {
                    // Has to be true cause if Owner is not there
                    // Only if owner is there, dragon can get set to walk
                    return true;
                } else {
                    return this.dragonEntity.isStill();
                }
            }
        }

        @Override
        public void start() {
            this.dragonEntity.getNavigation().stop();
            this.dragonEntity.lookAtEntity(dragonEntity, 0, 0);
            this.dragonEntity.setStill(true);
        }

        @Override
        public void stop() {
            this.dragonEntity.setStill(false);
        }
    }

    public static class DragonSleepGoal extends Goal {
        private final DragonEntity dragonEntity;

        public DragonSleepGoal(DragonEntity dragonEntity) {
            this.dragonEntity = dragonEntity;
            this.setControls(EnumSet.of(Goal.Control.JUMP, Goal.Control.MOVE));
        }

        @Override
        public boolean shouldContinue() {
            return this.dragonEntity.isDragonSleeping();
        }

        @Override
        public boolean canStart() {
            if (!this.dragonEntity.isTamed()) {
                return false;
            } else if (this.dragonEntity.isInsideWaterOrBubbleColumn()) {
                return false;
            } else if (!this.dragonEntity.isOnGround()) {
                return false;
            } else {
                if (this.dragonEntity.getOwner() == null) {
                    // Has to be true cause if Owner is not there
                    // Only if owner is there, dragon can get set to walk
                    return true;
                } else {
                    return this.dragonEntity.isDragonSleeping();
                }
            }
        }

        @Override
        public void start() {
            this.dragonEntity.getNavigation().stop();
            this.dragonEntity.setIsDragonSleeping(true);
        }

        @Override
        public void stop() {
            this.dragonEntity.setIsDragonSleeping(false);
        }
    }

    @Environment(EnvType.CLIENT)
    public boolean isClientBoosting() {
        return this.boosting();
    }

    /*public Vec3d getRiderOffset(Entity entity, float partialTick) {
        float x = this.getPassengersRidingXOffset();
        float y = (float)((this.isRemoved() ? (double)0.01F : this.getPassengersRidingOffset()) + getVehicleAttachmentPoint(entity).y);
        if (this.getPassengerList().size() > 1) {
            int i = this.getPassengerList().indexOf(entity);
            if (i == 0) {
                x = 0.2F;
            } else {
                x = -0.6F;
            }

            if (entity instanceof AnimalEntity) {
                x += 0.2F;
            }
        }

        Vec3d vec3 = new Vec3d((double)x, (double)y, (double)0.0F);
        vec3 = vec3.rotateY(-this.getYaw(partialTick) * ((float)Math.PI / 180F) - ((float)Math.PI / 2F));
        return new Vec3d(vec3.x, vec3.y, vec3.z);
    }

    public Vec3d getVehicleAttachmentPoint(Entity vehicle) {
        if (vehicle instanceof LivingEntity living) {
            return new Vec3d(0.0, living.getHeight() * 0.75, 0.0);
        }
        return Vec3d.ZERO;
    }

    public abstract float getPassengersRidingXOffset();

    public abstract float getYRideOffset();

    public abstract float getLayerYOffset();

    public double getPassengersRidingOffset() {
        return (double)this.getDimensions(EntityPose.STANDING).height * (double)this.getYRideOffset();
    }*/
}
