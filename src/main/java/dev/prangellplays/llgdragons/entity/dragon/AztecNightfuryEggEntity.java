package dev.prangellplays.llgdragons.entity.dragon;

import com.google.common.collect.ImmutableList;
import dev.prangellplays.llgdragons.LLGDragons;
import dev.prangellplays.llgdragons.init.LLGDragonsEntities;
import dev.prangellplays.llgdragons.init.LLGDragonsItems;
import dev.prangellplays.llgdragons.init.LLGDragonsSounds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.ServerConfigHandler;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Arm;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;

import java.util.Optional;
import java.util.UUID;

public class AztecNightfuryEggEntity extends MobEntity implements GeoEntity {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    private PlayState predicate(AnimationState<AztecNightfuryEggEntity> aztecNightfuryEntityAnimationState) {
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    protected static final TrackedData<Optional<UUID>> OWNER_UNIQUE_ID = DataTracker.registerData(AztecNightfuryEggEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);
    private static final TrackedData<Integer> DRAGON_AGE = DataTracker.registerData(AztecNightfuryEggEntity.class, TrackedDataHandlerRegistry.INTEGER);

    public AztecNightfuryEggEntity(EntityType<AztecNightfuryEggEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public static DefaultAttributeContainer.Builder createAztecNightfuryEggAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 10.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0D);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("dragonAge", this.getDragonAge());
        try {
            if (this.getOwnerId() == null) {
                nbt.putString("ownerUUID", "");
            } else {
                nbt.putString("ownerUUID", this.getOwnerId().toString());
            }
        } catch (Exception e) {
            LLGDragons.LOGGER.error("An error occurred while trying to read the NBT data of a dragon egg", e);
        }
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound tag) {
        super.readCustomDataFromNbt(tag);
        this.setDragonAge(tag.getInt("dragonAge"));
        String s;

        if (tag.contains("ownerUUID", 8)) {
            s = tag.getString("ownerUUID");
        } else {
            String s1 = tag.getString("owner");
            UUID converedUUID = ServerConfigHandler.getPlayerUuidByName(this.getServer(), s1);
            s = converedUUID == null ? s1 : converedUUID.toString();
        }
        if (!s.isEmpty()) {
            this.setOwnerId(UUID.fromString(s));
        }
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.getDataTracker().startTracking(DRAGON_AGE, 0);
        this.getDataTracker().startTracking(OWNER_UNIQUE_ID, Optional.empty());
    }

    public UUID getOwnerId() {
        return this.dataTracker.get(OWNER_UNIQUE_ID).orElse(null);
    }

    public void setOwnerId(UUID p_184754_1_) {
        this.dataTracker.set(OWNER_UNIQUE_ID, Optional.ofNullable(p_184754_1_));
    }

    @Override
    public boolean isInvulnerableTo(DamageSource i) {
        return i.getAttacker() != null && super.isInvulnerableTo(i);
    }

    public int getDragonAge() {
        return this.getDataTracker().get(DRAGON_AGE);
    }

    public void setDragonAge(int i) {
        this.getDataTracker().set(DRAGON_AGE, i);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.getWorld().isClient) {
            this.updateEggCondition();
        }
    }

    public void updateEggCondition() {
        this.setDragonAge(this.getDragonAge() + 1);

        if (this.getDragonAge() > 500) {
            AztecNightfuryEntity dragon = LLGDragonsEntities.AZTEC_NIGHTFURY.create(this.getWorld());

            if (this.hasCustomName()) {
                assert dragon != null;
                dragon.setCustomName(this.getCustomName());
            }

            assert dragon != null;
            dragon.setGender(this.getRandom().nextBoolean());
            dragon.setPosition(this.getBlockPos().getX() + 0.5, this.getBlockPos().getY() + 1, this.getBlockPos().getZ() + 0.5);
            dragon.setBaby(true);

            if (!this.getWorld().isClient()) {
                this.getWorld().spawnEntity(dragon);
            }

            if (this.hasCustomName()) {
                dragon.setCustomName(this.getCustomName());
            }

            this.getWorld().playSound(this.getX(), this.getY() + this.getStandingEyeHeight(), this.getZ(), LLGDragonsSounds.DRAGON_EGG_HATCH, this.getSoundCategory(), 2.5F, 1.0F, false);
            this.remove(RemovalReason.DISCARDED);
        }
    }

    @Override
    public SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return null;
    }

    @Override
    public Iterable<ItemStack> getArmorItems() {
        return ImmutableList.of();
    }

    @Override
    public ItemStack getEquippedStack(EquipmentSlot slotIn) {
        return ItemStack.EMPTY;
    }

    @Override
    public void equipStack(EquipmentSlot slotIn, ItemStack stack) {

    }

    @Override
    public boolean damage(DamageSource var1, float var2) {
        if (var1.isIn(DamageTypeTags.IS_FIRE))
            return false;
        if (!this.getWorld().isClient && !var1.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY) && !this.isRemoved()) {
            this.dropItem(this.getItem().getItem(), 1);
        }
        this.remove(RemovalReason.KILLED);
        return true;
    }

    private ItemStack getItem() {
        return new ItemStack(LLGDragonsItems.AZTEC_NIGHTFURY_EGG);
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public Arm getMainArm() {
        return Arm.RIGHT;
    }

    @Override
    protected void pushAway(Entity entity) {
    }

    public void onPlayerPlace(PlayerEntity player) {
        this.setOwnerId(player.getUuid());
    }

    @Override
    public boolean cannotDespawn() {
        return true;
    }
}
