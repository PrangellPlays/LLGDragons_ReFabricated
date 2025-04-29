package dev.prangellplays.llgdragons.item;

import dev.prangellplays.llgdragons.LLGDragons;
import dev.prangellplays.llgdragons.entity.DragonEntity;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.OverlayMessageS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class DragosphereItem  extends Item {
    private static final Logger LOGGER = LogManager.getLogger();
    public DragosphereItem(Settings settings) {
        super(settings);
    }

    public ActionResult captureDragon(ItemStack stack, PlayerEntity player, LivingEntity target, Hand hand) {
        if (!player.getWorld().isClient) {
            String entityId = EntityType.getId(target.getType()).toString();

            if (target instanceof DragonEntity dragon && dragon.isOwner(player)) {
                if (!LLGDragons.dragosphereWhitelist.contains(entityId)) {
                    if (player instanceof ServerPlayerEntity serverPlayerEntity) {
                        serverPlayerEntity.networkHandler.sendPacket(new OverlayMessageS2CPacket(Text.translatable("item.llgdragons.dragosphere.cannot_capture")));
                    }
                    return ActionResult.FAIL;
                }

                if (player.getItemCooldownManager().isCoolingDown(this)) {
                    return ActionResult.PASS;
                }

                NbtCompound nbt = stack.getOrCreateNbt();

                if (nbt.contains("CapturedDragon")) {
                    return ActionResult.FAIL;
                }

                try {
                    NbtCompound entityTag = new NbtCompound();
                    target.writeNbt(entityTag);

                    nbt.putString("CapturedDragonType", entityId);
                    nbt.put("CapturedDragon", entityTag);

                    target.remove(Entity.RemovalReason.DISCARDED);
                } catch (Exception e) {
                    LOGGER.error("Error capturing dragon: " + e.getMessage());
                    return ActionResult.FAIL;
                }

                player.getWorld().playSound((PlayerEntity) null, player.getX(), player.getY(), player.getZ(), SoundEvents.BLOCK_END_PORTAL_FRAME_FILL, SoundCategory.NEUTRAL, 0.5F, 0.4F / (player.getWorld().getRandom().nextFloat() * 0.4F + 0.8F));

                player.getItemCooldownManager().set(this, 20);

                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.PASS;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        NbtCompound nbt = stack.getNbt();

        if (player.getItemCooldownManager().isCoolingDown(this)) {
            return TypedActionResult.pass(stack);
        }

        if (nbt != null && nbt.contains("CapturedDragon") && nbt.contains("CapturedDragonType")) {
            if (!world.isClient) {
                try {
                    NbtCompound entityTag = nbt.getCompound("CapturedDragon");
                    String entityTypeString = nbt.getString("CapturedDragonType");

                    Optional<EntityType<?>> optionalEntityType = Registries.ENTITY_TYPE.getOrEmpty(new Identifier(entityTypeString));
                    if (optionalEntityType.isPresent()) {
                        EntityType<?> entityType = optionalEntityType.get();
                        LivingEntity entity = (LivingEntity) entityType.create(world);
                        if (entity != null) {
                            entity.readNbt(entityTag);

                            HitResult hitResult = player.raycast(5.0D, 1.0F, false); // 5 blocks distance

                            if (hitResult.getType() == HitResult.Type.BLOCK) {
                                BlockHitResult blockHitResult = (BlockHitResult) hitResult;
                                BlockPos targetPos = blockHitResult.getBlockPos().offset(blockHitResult.getSide());

                                entity.refreshPositionAndAngles(
                                        targetPos.getX() + 0.5,
                                        targetPos.getY(),
                                        targetPos.getZ() + 0.5,
                                        player.getYaw(),
                                        player.getPitch()
                                );

                                world.spawnEntity(entity);

                                nbt.remove("CapturedDragon");
                                nbt.remove("CapturedDragonType");

                                stack.setNbt(nbt);
                            }
                        } else {
                            LOGGER.error("Failed to create dragon from captured data.");
                        }
                    } else {
                        LOGGER.error("Dragon type not found in registry: " + entityTypeString);
                    }
                } catch (Exception e) {
                    LOGGER.error("Error releasing dragon: " + e.getMessage());
                }

                player.getItemCooldownManager().set(this, 20);

                return TypedActionResult.success(stack);
            }
        }

        return TypedActionResult.pass(stack);
    }

    @Override
    public Text getName(ItemStack stack) {
        NbtCompound nbt = stack.getNbt();
        if (nbt != null && nbt.contains("CapturedDragonType")) {
            return Text.translatable("item.llgdragons.dragosphere.full").formatted(Formatting.ITALIC);
        } else {
          return Text.translatable("item.llgdragons.dragosphere.empty");
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        NbtCompound nbt = stack.getNbt();
        if (nbt != null && nbt.contains("CapturedDragonType")) {
            if (nbt != null && nbt.getCompound("CapturedDragon").contains("CustomName")) {
                tooltip.add(Text.translatable("item.llgdragons.dragosphere.name").formatted(Formatting.GRAY).append(Text.literal(": ").formatted(Formatting.GRAY)).append(Text.literal(nbt.getCompound("CapturedDragon").getString("CustomName").toString().substring(9, nbt.getCompound("CapturedDragon").getString("CustomName").toString().length() - 2)).formatted(Formatting.GREEN)));
            } else if (nbt != null && !nbt.getCompound("CapturedDragon").contains("CustomName")) {
                tooltip.add(Text.translatable("item.llgdragons.dragosphere.name").formatted(Formatting.GRAY).append(Text.literal(":").formatted(Formatting.GRAY)));
            }
            MutableText entityText = Text.translatable("item.llgdragons.dragosphere.species").setStyle(Style.EMPTY.withColor(Formatting.GRAY));
            entityText.append(Text.literal(": ").setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
            MutableText entityAppend = Text.translatable("item.llgdragons.dragosphere.no_dragon").setStyle(Style.EMPTY.withColor(Formatting.DARK_RED));
            if (nbt != null && nbt.contains("CapturedDragon") && nbt.contains("CapturedDragonType")) {
                try {
                    EntityType<?> type = Registries.ENTITY_TYPE.get(new Identifier(nbt.getString("CapturedDragonType")));
                    entityAppend = type.getName().copy().setStyle(Style.EMPTY.withColor(Formatting.GREEN));
                } catch (Exception ignored) {}
            }
            entityText.append(entityAppend);
            tooltip.add(entityText);
            if (nbt != null && nbt.getCompound("CapturedDragon").contains("gender") && nbt.getCompound("CapturedDragon").getInt("gender") == 1) {
                tooltip.add(Text.translatable("item.llgdragons.dragosphere.gender").formatted(Formatting.GRAY).append(Text.literal(": ").formatted(Formatting.GRAY).append(Text.translatable("item.llgdragons.dragosphere.female").formatted(Formatting.GREEN))));
            } else {
                tooltip.add(Text.translatable("item.llgdragons.dragosphere.gender").formatted(Formatting.GRAY).append(Text.literal(": ").formatted(Formatting.GRAY).append(Text.translatable("item.llgdragons.dragosphere.male").formatted(Formatting.GREEN))));
            }
            if (nbt.getCompound("CapturedDragon").getInt("day_count_age") < 6) {
                tooltip.add(Text.translatable("item.llgdragons.dragosphere.age").formatted(Formatting.GRAY).append(Text.literal(": ").formatted(Formatting.GRAY).append(Text.literal(String.valueOf(nbt.getCompound("CapturedDragon").getInt("day_count_age"))).append(Text.literal(" ")).append(Text.translatable("item.llgdragons.dragosphere.days").append(Text.literal(" (").append(Text.translatable("item.llgdragons.telling_book.age.baby").append(Text.literal(")"))))).formatted(Formatting.GREEN))));
            } else {
                tooltip.add(Text.translatable("item.llgdragons.dragosphere.age").formatted(Formatting.GRAY).append(Text.literal(": ").formatted(Formatting.GRAY).append(Text.literal(String.valueOf(nbt.getCompound("CapturedDragon").getInt("day_count_age"))).append(Text.literal(" ")).append(Text.translatable("item.llgdragons.dragosphere.days").append(Text.literal(" (").append(Text.translatable("item.llgdragons.telling_book.age.adult").append(Text.literal(")"))))).formatted(Formatting.GREEN))));
            }
            tooltip.add(Text.translatable("item.llgdragons.dragosphere.favourite_food").formatted(Formatting.GRAY).append(Text.literal(": ").formatted(Formatting.GRAY).append(Text.translatable(String.valueOf(nbt.getCompound("CapturedDragon").getString("favourite_food"))).formatted(Formatting.GREEN))));
            //tooltip.add(Text.translatable("item.llgdragons.dragosphere.captured").formatted(Formatting.GRAY).append(Text.literal(": ").formatted(Formatting.GRAY)).append(Text.literal(entityTypeString).formatted(Formatting.GREEN)));
        } else {
            tooltip.add(Text.translatable("item.llgdragons.dragosphere.no_dragon").formatted(Formatting.DARK_RED));
        }
    }

    public static boolean hasCapturedDragon(ItemStack stack) {
        if (!stack.hasNbt()) {
            return false;
        } else {
            return stack.getNbt() != null && stack.getNbt().contains("CapturedDragonType");
        }
    }

    public class DragosphereEventHandler {
        public static void init() {
            UseEntityCallback.EVENT.register((PlayerEntity player, World world, Hand hand, Entity entity, EntityHitResult hitResult) -> {
                ItemStack stack = player.getStackInHand(hand);

                if (entity instanceof PlayerEntity) {
                    return ActionResult.PASS;
                }

                if (entity instanceof LivingEntity targetEntity) {

                    if (stack.getItem() instanceof DragosphereItem dragosphere) {
                        ActionResult result = dragosphere.captureDragon(stack, player, targetEntity, hand);
                        if (result == ActionResult.SUCCESS) {
                            return ActionResult.SUCCESS;
                        }
                    }
                }
                return ActionResult.PASS;
            });
        }
    }
}