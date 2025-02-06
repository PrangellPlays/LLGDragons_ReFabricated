package dev.prangellplays.llgdragons.item;

import dev.prangellplays.llgdragons.entity.dragonability.nightfury.PlasmaBlastEntity;
import dev.prangellplays.llgdragons.init.LLGDragonsEntities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class PlasmaProjectileItem extends Item {
    public PlasmaProjectileItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        PlasmaBlastEntity plasmaBlastEntity = new PlasmaBlastEntity(LLGDragonsEntities.PLASMA_BLAST, world);
        plasmaBlastEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 1.5F, 1.0F);
        world.spawnEntity(plasmaBlastEntity);
        return super.use(world, user, hand);
    }
}
