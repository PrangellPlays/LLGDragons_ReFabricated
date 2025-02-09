package dev.prangellplays.llgdragons.item;

import dev.prangellplays.llgdragons.entity.dragonability.nightfury.PlasmaBlastEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class PlasmaProjectileItem extends Item {
    public PlasmaProjectileItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        PlasmaBlastEntity plasmaBlastEntity = new PlasmaBlastEntity(world, user, user.getX(), user.getY(), user.getZ());
        //plasmaBlastEntity.refreshPositionAndAngles(user.getX(), user.getY() + user.getBoundingBox().getYLength() * 0.65D + (user.getPitch() > 0F ? -user.getPitch() / 40F : -user.getPitch() / 80F), user.getZ() * 3D, user.getYaw(), user.getPitch());
        plasmaBlastEntity.setVelocity(user.getX(), user.getEyeY(), user.getZ(), 2, 1);
        world.spawnEntity(plasmaBlastEntity);
        return super.use(world, user, hand);
    }
}
