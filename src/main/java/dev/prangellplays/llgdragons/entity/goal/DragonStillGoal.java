package dev.prangellplays.llgdragons.entity.goal;

import dev.prangellplays.llgdragons.entity.DragonEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.TameableEntity;

import java.util.EnumSet;

public class DragonStillGoal extends Goal {
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