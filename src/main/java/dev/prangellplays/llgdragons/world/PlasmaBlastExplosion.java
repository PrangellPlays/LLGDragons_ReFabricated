package dev.prangellplays.llgdragons.world;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import dev.prangellplays.llgdragons.init.LLGDragonsParticles;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.nbt.NbtList;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.explosion.EntityExplosionBehavior;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class PlasmaBlastExplosion extends Explosion {
    private static final ExplosionBehavior DEFAULT_BEHAVIOR = new ExplosionBehavior();
    private static final int field_30960 = 16;
    private final boolean createFire;
    private final Explosion.DestructionType destructionType;
    private final Random random;
    private final World world;
    private final double x;
    private final double y;
    private final double z;
    @Nullable
    private final Entity entity;
    private final float power;
    private final DamageSource damageSource;
    private final ExplosionBehavior behavior;
    private final ObjectArrayList<BlockPos> affectedBlocks;
    private final Map<PlayerEntity, Vec3d> affectedPlayers;

    public PlasmaBlastExplosion(World world, @Nullable Entity entity, double x, double y, double z, float power, List<BlockPos> affectedBlocks) {
        this(world, entity, x, y, z, power, false, Explosion.DestructionType.DESTROY_WITH_DECAY, affectedBlocks);
    }

    public PlasmaBlastExplosion(World world, @Nullable Entity entity, double x, double y, double z, float power, boolean createFire, Explosion.DestructionType destructionType, List<BlockPos> affectedBlocks) {
        this(world, entity, x, y, z, power, createFire, destructionType);
        this.affectedBlocks.addAll(affectedBlocks);
    }

    public PlasmaBlastExplosion(World world, @Nullable Entity entity, double x, double y, double z, float power, boolean createFire, Explosion.DestructionType destructionType) {
        this(world, entity, (DamageSource)null, (ExplosionBehavior)null, x, y, z, power, createFire, destructionType);
    }

    public PlasmaBlastExplosion(World world, @Nullable Entity entity, @Nullable DamageSource damageSource, @Nullable ExplosionBehavior behavior, double x, double y, double z, float power, boolean createFire, Explosion.DestructionType destructionType) {
        super(world, entity, x, y, z, power, createFire, destructionType);
        this.random = Random.create();
        this.affectedBlocks = new ObjectArrayList();
        this.affectedPlayers = Maps.newHashMap();
        this.world = world;
        this.entity = entity;
        this.power = power;
        this.x = x;
        this.y = y;
        this.z = z;
        this.createFire = createFire;
        this.destructionType = destructionType;
        this.damageSource = damageSource == null ? world.getDamageSources().explosion(this) : damageSource;
        this.behavior = behavior == null ? this.chooseBehavior(entity) : behavior;
    }

    private ExplosionBehavior chooseBehavior(@Nullable Entity entity) {
        return (ExplosionBehavior)(entity == null ? DEFAULT_BEHAVIOR : new EntityExplosionBehavior(entity));
    }

    public static float getExposure(Vec3d source, Entity entity) {
        Box box = entity.getBoundingBox();
        double d = (double)1.0F / ((box.maxX - box.minX) * (double)2.0F + (double)1.0F);
        double e = (double)1.0F / ((box.maxY - box.minY) * (double)2.0F + (double)1.0F);
        double f = (double)1.0F / ((box.maxZ - box.minZ) * (double)2.0F + (double)1.0F);
        double g = ((double)1.0F - Math.floor((double)1.0F / d) * d) / (double)2.0F;
        double h = ((double)1.0F - Math.floor((double)1.0F / f) * f) / (double)2.0F;
        if (!(d < (double)0.0F) && !(e < (double)0.0F) && !(f < (double)0.0F)) {
            int i = 0;
            int j = 0;

            for(double k = (double)0.0F; k <= (double)1.0F; k += d) {
                for(double l = (double)0.0F; l <= (double)1.0F; l += e) {
                    for(double m = (double)0.0F; m <= (double)1.0F; m += f) {
                        double n = MathHelper.lerp(k, box.minX, box.maxX);
                        double o = MathHelper.lerp(l, box.minY, box.maxY);
                        double p = MathHelper.lerp(m, box.minZ, box.maxZ);
                        Vec3d vec3d = new Vec3d(n + g, o, p + h);
                        if (entity.getWorld().raycast(new RaycastContext(vec3d, source, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, entity)).getType() == HitResult.Type.MISS) {
                            ++i;
                        }

                        ++j;
                    }
                }
            }

            return (float)i / (float)j;
        } else {
            return 0.0F;
        }
    }

    public void collectBlocksAndDamageEntities() {
        this.world.emitGameEvent(this.entity, GameEvent.EXPLODE, new Vec3d(this.x, this.y, this.z));
        Set<BlockPos> set = Sets.newHashSet();
        int i = 16;

        for(int j = 0; j < 16; ++j) {
            for(int k = 0; k < 16; ++k) {
                for(int l = 0; l < 16; ++l) {
                    if (j == 0 || j == 15 || k == 0 || k == 15 || l == 0 || l == 15) {
                        double d = (double)((float)j / 15.0F * 2.0F - 1.0F);
                        double e = (double)((float)k / 15.0F * 2.0F - 1.0F);
                        double f = (double)((float)l / 15.0F * 2.0F - 1.0F);
                        double g = Math.sqrt(d * d + e * e + f * f);
                        d /= g;
                        e /= g;
                        f /= g;
                        float h = this.power * (0.7F + this.world.random.nextFloat() * 0.6F);
                        double m = this.x;
                        double n = this.y;
                        double o = this.z;

                        for(float p = 0.3F; h > 0.0F; h -= 0.22500001F) {
                            BlockPos blockPos = BlockPos.ofFloored(m, n, o);
                            BlockState blockState = this.world.getBlockState(blockPos);
                            FluidState fluidState = this.world.getFluidState(blockPos);
                            if (!this.world.isInBuildLimit(blockPos)) {
                                break;
                            }

                            Optional<Float> optional = this.behavior.getBlastResistance(this, this.world, blockPos, blockState, fluidState);
                            if (optional.isPresent()) {
                                h -= ((Float)optional.get() + 0.3F) * 0.3F;
                            }

                            if (h > 0.0F && this.behavior.canDestroyBlock(this, this.world, blockPos, blockState, h)) {
                                set.add(blockPos);
                            }

                            m += d * (double)0.3F;
                            n += e * (double)0.3F;
                            o += f * (double)0.3F;
                        }
                    }
                }
            }
        }

        this.affectedBlocks.addAll(set);
        float q = this.power * 2.0F;
        int k = MathHelper.floor(this.x - (double)q - (double)1.0F);
        int l = MathHelper.floor(this.x + (double)q + (double)1.0F);
        int r = MathHelper.floor(this.y - (double)q - (double)1.0F);
        int s = MathHelper.floor(this.y + (double)q + (double)1.0F);
        int t = MathHelper.floor(this.z - (double)q - (double)1.0F);
        int u = MathHelper.floor(this.z + (double)q + (double)1.0F);
        List<Entity> list = this.world.getOtherEntities(this.entity, new Box((double)k, (double)r, (double)t, (double)l, (double)s, (double)u));
        Vec3d vec3d = new Vec3d(this.x, this.y, this.z);

        for(int v = 0; v < list.size(); ++v) {
            Entity entity = (Entity)list.get(v);
            if (!entity.isImmuneToExplosion()) {
                double w = Math.sqrt(entity.squaredDistanceTo(vec3d)) / (double)q;
                if (w <= (double)1.0F) {
                    double x = entity.getX() - this.x;
                    double y = (entity instanceof TntEntity ? entity.getY() : entity.getEyeY()) - this.y;
                    double z = entity.getZ() - this.z;
                    double aa = Math.sqrt(x * x + y * y + z * z);
                    if (aa != (double)0.0F) {
                        x /= aa;
                        y /= aa;
                        z /= aa;
                        double ab = (double)getExposure(vec3d, entity);
                        double ac = ((double)1.0F - w) * ab;
                        entity.damage(this.getDamageSource(), (float)((int)((ac * ac + ac) / (double)2.0F * (double)7.0F * (double)q + (double)1.0F)));
                        double ad;
                        if (entity instanceof LivingEntity) {
                            LivingEntity livingEntity = (LivingEntity)entity;
                            ad = ProtectionEnchantment.transformExplosionKnockback(livingEntity, ac);
                        } else {
                            ad = ac;
                        }

                        x *= ad;
                        y *= ad;
                        z *= ad;
                        Vec3d vec3d2 = new Vec3d(x, y, z);
                        entity.setVelocity(entity.getVelocity().add(vec3d2));
                        if (entity instanceof PlayerEntity) {
                            PlayerEntity playerEntity = (PlayerEntity)entity;
                            if (!playerEntity.isSpectator() && (!playerEntity.isCreative() || !playerEntity.getAbilities().flying)) {
                                this.affectedPlayers.put(playerEntity, vec3d2);
                            }
                        }
                    }
                }
            }
        }

    }

    public void affectWorld(boolean particles) {
        if (this.world.isClient) {
            this.world.playSound(this.x, this.y, this.z, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0F, (1.0F + (this.world.random.nextFloat() - this.world.random.nextFloat()) * 0.2F) * 0.7F, false);
        }

        boolean bl = this.shouldDestroy();
        if (particles) {
            if (!(this.power < 2.0F) && bl) {
                this.world.addParticle(LLGDragonsParticles.PLASMA_WAVE, this.x, this.y, this.z, (double)1.0F, (double)0.0F, (double)0.0F);
            } else {
                this.world.addParticle(LLGDragonsParticles.PLASMA_WAVE, this.x, this.y, this.z, (double)1.0F, (double)0.0F, (double)0.0F);
            }
        }

        if (bl) {
            ObjectArrayList<Pair<ItemStack, BlockPos>> objectArrayList = new ObjectArrayList();
            boolean bl2 = this.getCausingEntity() instanceof PlayerEntity;
            Util.shuffle(this.affectedBlocks, this.world.random);
            ObjectListIterator var5 = this.affectedBlocks.iterator();

            while(var5.hasNext()) {
                BlockPos blockPos = (BlockPos)var5.next();
                BlockState blockState = this.world.getBlockState(blockPos);
                Block block = blockState.getBlock();
                if (!blockState.isAir()) {
                    BlockPos blockPos2 = blockPos.toImmutable();
                    this.world.getProfiler().push("explosion_blocks");
                    if (block.shouldDropItemsOnExplosion(this)) {
                        World blockEntity = this.world;
                        if (blockEntity instanceof ServerWorld) {
                            ServerWorld serverWorld = (ServerWorld)blockEntity;
                            BlockEntity be = blockState.hasBlockEntity() ? this.world.getBlockEntity(blockPos) : null;
                            LootContextParameterSet.Builder builder = (new LootContextParameterSet.Builder(serverWorld)).add(LootContextParameters.ORIGIN, Vec3d.ofCenter(blockPos)).add(LootContextParameters.TOOL, ItemStack.EMPTY).addOptional(LootContextParameters.BLOCK_ENTITY, be).addOptional(LootContextParameters.THIS_ENTITY, this.entity);
                            if (this.destructionType == Explosion.DestructionType.DESTROY_WITH_DECAY) {
                                builder.add(LootContextParameters.EXPLOSION_RADIUS, this.power);
                            }

                            blockState.onStacksDropped(serverWorld, blockPos, ItemStack.EMPTY, bl2);
                            blockState.getDroppedStacks(builder).forEach((stack) -> tryMergeStack(objectArrayList, stack, blockPos2));
                        }
                    }

                    this.world.setBlockState(blockPos, Blocks.AIR.getDefaultState(), 3);
                    block.onDestroyedByExplosion(this.world, blockPos, this);
                    this.world.getProfiler().pop();
                }
            }

            var5 = objectArrayList.iterator();

            while(var5.hasNext()) {
                Pair<ItemStack, BlockPos> pair = (Pair)var5.next();
                Block.dropStack(this.world, (BlockPos)pair.getSecond(), (ItemStack)pair.getFirst());
            }
        }

        if (this.createFire) {
            ObjectListIterator var13 = this.affectedBlocks.iterator();

            while(var13.hasNext()) {
                BlockPos blockPos3 = (BlockPos)var13.next();
                if (this.random.nextInt(3) == 0 && this.world.getBlockState(blockPos3).isAir() && this.world.getBlockState(blockPos3.down()).isOpaqueFullCube(this.world, blockPos3.down())) {
                    this.world.setBlockState(blockPos3, AbstractFireBlock.getState(this.world, blockPos3));
                }
            }
        }

    }

    public boolean shouldDestroy() {
        return this.destructionType != Explosion.DestructionType.KEEP;
    }

    private static void tryMergeStack(ObjectArrayList<Pair<ItemStack, BlockPos>> stacks, ItemStack stack, BlockPos pos) {
        int i = stacks.size();

        for(int j = 0; j < i; ++j) {
            Pair<ItemStack, BlockPos> pair = (Pair)stacks.get(j);
            ItemStack itemStack = (ItemStack)pair.getFirst();
            if (ItemEntity.canMerge(itemStack, stack)) {
                ItemStack itemStack2 = ItemEntity.merge(itemStack, stack, 16);
                stacks.set(j, Pair.of(itemStack2, (BlockPos)pair.getSecond()));
                if (stack.isEmpty()) {
                    return;
                }
            }
        }

        stacks.add(Pair.of(stack, pos));
    }

    public DamageSource getDamageSource() {
        return this.damageSource;
    }

    public Map<PlayerEntity, Vec3d> getAffectedPlayers() {
        return this.affectedPlayers;
    }

    @Nullable
    public LivingEntity getCausingEntity() {
        if (this.entity == null) {
            return null;
        } else {
            Entity entity = this.entity;
            if (entity instanceof TntEntity) {
                TntEntity tntEntity = (TntEntity)entity;
                return tntEntity.getOwner();
            } else {
                entity = this.entity;
                if (entity instanceof LivingEntity) {
                    LivingEntity livingEntity = (LivingEntity)entity;
                    return livingEntity;
                } else {
                    entity = this.entity;
                    if (entity instanceof ProjectileEntity) {
                        ProjectileEntity projectileEntity = (ProjectileEntity)entity;
                        entity = projectileEntity.getOwner();
                        if (entity instanceof LivingEntity) {
                            LivingEntity livingEntity2 = (LivingEntity)entity;
                            return livingEntity2;
                        }
                    }

                    return null;
                }
            }
        }
    }

    @Nullable
    public Entity getEntity() {
        return this.entity;
    }

    public void clearAffectedBlocks() {
        this.affectedBlocks.clear();
    }

    public List<BlockPos> getAffectedBlocks() {
        return this.affectedBlocks;
    }
}