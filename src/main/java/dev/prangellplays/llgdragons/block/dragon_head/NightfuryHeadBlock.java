package dev.prangellplays.llgdragons.block.dragon_head;

import dev.prangellplays.llgdragons.block.DragonHeadBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class NightfuryHeadBlock extends DragonHeadBlock {
    private static final VoxelShape NORTH_AXIS_SHAPE = VoxelShapes.cuboid(0, 0, 0, 1, 0.35, 1.15);
    private static final VoxelShape EAST_AXIS_SHAPE = VoxelShapes.cuboid(-0.15, 0, 0, 1, 0.35, 1);
    private static final VoxelShape SOUTH_AXIS_SHAPE = VoxelShapes.cuboid(0, 0, -0.15, 1, 0.35, 1);
    private static final VoxelShape WEST_AXIS_SHAPE = VoxelShapes.cuboid(0, 0, 0, 1.15, 0.35, 1);
    public NightfuryHeadBlock(Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction direction = state.get(FACING);

        return switch (direction) {
            case DOWN -> null;
            case UP -> null;
            case NORTH -> NORTH_AXIS_SHAPE;
            case EAST -> EAST_AXIS_SHAPE;
            case SOUTH -> SOUTH_AXIS_SHAPE;
            case WEST -> WEST_AXIS_SHAPE;
        };
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction direction = state.get(FACING);

        return switch (direction) {
            case DOWN -> null;
            case UP -> null;
            case NORTH -> NORTH_AXIS_SHAPE;
            case EAST -> EAST_AXIS_SHAPE;
            case SOUTH -> SOUTH_AXIS_SHAPE;
            case WEST -> WEST_AXIS_SHAPE;
        };
    }
}
