package dev.prangellplays.llgdragons.item;

import dev.prangellplays.llgdragons.client.screen.TellingBookScreen;
import dev.prangellplays.llgdragons.entity.DragonEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class TellingBookItem extends Item {
    public TellingBookItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        World world = user.getWorld();
        if (entity instanceof DragonEntity dragon && dragon.isTamed()) {
            if (world.isClient) {
                MinecraftClient.getInstance().setScreen(new TellingBookScreen());
            }
        }
        return super.useOnEntity(stack, user, entity, hand);
    }
}
