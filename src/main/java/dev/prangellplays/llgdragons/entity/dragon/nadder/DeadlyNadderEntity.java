package dev.prangellplays.llgdragons.entity.dragon.nadder;

import dev.prangellplays.llgdragons.entity.DragonEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.EntityView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;

public class DeadlyNadderEntity extends DragonEntity implements GeoEntity {
    public DeadlyNadderEntity(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public @Nullable PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }

    @Override
    public EntityView method_48926() {
        return null;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return null;
    }

    /*@Override
    public float getPassengersRidingXOffset() {
        return 0.0F;
    }

    @Override
    public float getYRideOffset() {
        return 0.5F;
    }

    @Override
    public float getLayerYOffset() {
        return 2.5F;
    }*/
}
