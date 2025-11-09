package dev.prangellplays.llgdragons.entity.dragon.nightfury.Egg;

import dev.prangellplays.llgdragons.LLGDragons;
import dev.prangellplays.llgdragons.data.nightfury.variant.NightfuryVariant;
import dev.prangellplays.llgdragons.entity.DragonEggEntity;
import dev.prangellplays.llgdragons.entity.dragon.nightfury.NightfuryEntity;
import dev.prangellplays.llgdragons.init.LLGDragonsEntities;
import dev.prangellplays.llgdragons.init.LLGDragonsItems;
import dev.prangellplays.llgdragons.init.LLGDragonsSounds;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

import static dev.prangellplays.llgdragons.entity.DragonEntity.*;

public class NightfuryEggEntity extends DragonEggEntity implements GeoEntity {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private static final TrackedData<Integer> VARIANT_ID = DataTracker.registerData(NightfuryEggEntity.class, TrackedDataHandlerRegistry.INTEGER);

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    private PlayState predicate(AnimationState<NightfuryEggEntity> dragonEggEntityAnimationState) {
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public NightfuryEggEntity(EntityType<? extends NightfuryEggEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public static DefaultAttributeContainer.Builder createDragonEggAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 10.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0D);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(VARIANT_ID, NightfuryVariant.toId(this.getWorld().getRegistryManager(), NightfuryVariant.NIGHTFURY));
    }

    public void setVariant(NightfuryVariant variant, DynamicRegistryManager registryManager) {
        int id = NightfuryVariant.toId(registryManager, variant);
        this.dataTracker.set(VARIANT_ID, id);
    }

    public NightfuryVariant getVariant(DynamicRegistryManager registryManager) {
        int id = this.dataTracker.get(VARIANT_ID);
        return NightfuryVariant.fromId(registryManager, id);
    }

    public void updateEggCondition() {
        this.setDragonAge(this.getDragonAge() + 1);

        if (this.getDragonAge() > 500) {
            DynamicRegistryManager registryManager = this.getWorld().getRegistryManager();
            NightfuryVariant variant = this.getVariant(registryManager);
            NightfuryEntity dragon = LLGDragonsEntities.NIGHTFURY.create(this.getWorld());

            if (this.hasCustomName()) {
                assert dragon != null;
                dragon.setCustomName(this.getCustomName());
            }

            assert dragon != null;
            dragon.setVariant(variant);
            dragon.setGender(this.getRandom().nextBoolean());
            dragon.setDayCountAge(0);
            dragon.setBaby(true);
            dragon.setAgeInt(base_day_length);
            dragon.chooseFavouriteFood();
            dragon.getDataTracker().set(FAVOURITE_FOOD, favouriteFood);
            dragon.setPosition(this.getBlockPos().getX() + 0.5, this.getBlockPos().getY() + 1, this.getBlockPos().getZ() + 0.5);

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


    public ItemStack getItem() {
        return new ItemStack(LLGDragonsItems.NIGHTFURY_EGG);
    }
}
