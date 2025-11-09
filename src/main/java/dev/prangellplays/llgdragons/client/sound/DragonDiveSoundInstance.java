package dev.prangellplays.llgdragons.client.sound;

import dev.prangellplays.llgdragons.entity.DragonEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;

public class DragonDiveSoundInstance extends MovingSoundInstance {
    private final DragonEntity dragon;
    private static final Random SOUND_RANDOM = SoundInstance.createRandom();
    public static final int field_32996 = 20;
    private int tickCount;

    public DragonDiveSoundInstance(DragonEntity dragon) {
        super(SoundEvents.ITEM_ELYTRA_FLYING, SoundCategory.PLAYERS, SOUND_RANDOM);
        this.dragon = dragon;
        this.repeat = true;
        this.repeatDelay = 0;
        this.volume = 1.0F;
    }

    public void tick() {
        ++this.tickCount;
        if (!this.dragon.isRemoved() && (this.tickCount <= 20 || this.dragon.isGoingDown() && this.dragon.canBoost() && this.dragon.isClientBoosting())) {
            this.x = (double)((float)this.dragon.getX());
            this.y = (double)((float)this.dragon.getY());
            this.z = (double)((float)this.dragon.getZ());
            float f = (float)this.dragon.getVelocity().lengthSquared();
            if ((double)f >= 1.0E-7) {
                this.volume = MathHelper.clamp(f / 4.0F, 0.0F, 1.0F);
            } else {
                this.volume = 0.0F;
            }

            if (this.tickCount < 20) {
                this.volume = 0.0F;
            } else if (this.tickCount < 40) {
                this.volume *= (float)(this.tickCount - 20) / 20.0F;
            }

            float g = 0.8F;
            if (this.volume > 0.8F) {
                this.pitch = 1.0F + (this.volume - 0.8F);
            } else {
                this.pitch = 1.0F;
            }

        } else {
            this.setDone();
        }
    }
}