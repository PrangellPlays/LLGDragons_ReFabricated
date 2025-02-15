package dev.prangellplays.llgdragons.item;

import dev.prangellplays.llgdragons.LLGDragons;
import dev.prangellplays.llgdragons.entity.DragonEntity;
import dev.prangellplays.llgdragons.entity.FetchBallEntity;
import dev.prangellplays.llgdragons.entity.dragon.NightfuryEntity;
import dev.prangellplays.llgdragons.init.LLGDragonsEntities;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class FetchBallItem extends Item {
    public FetchBallItem(Settings settings) {
        super(settings);
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        world.playSound((PlayerEntity) null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
        if (!world.isClient) {
            FetchBallEntity fetchBallEntity = new FetchBallEntity(world, user);
            fetchBallEntity.setItem(itemStack);
            fetchBallEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 1.5F, 1.0F);
            world.spawnEntity(fetchBallEntity);
        }

        user.incrementStat(Stats.USED.getOrCreateStat(this));
        if (!user.getAbilities().creativeMode) {
            itemStack.decrement(1);
        }

        return TypedActionResult.success(itemStack, world.isClient());
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        String entityId = EntityType.getId(entity.getType()).toString();
        if (!LLGDragons.dragosphereWhitelist.contains(entityId)) {
            return ActionResult.FAIL;
        } else {
            return ActionResult.SUCCESS;
        }
    }
}