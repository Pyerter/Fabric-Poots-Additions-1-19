package net.pyerter.pootsadditions.block.custom;

import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.pyerter.pootsadditions.block.entity.EngineeringStationEntity;
import net.pyerter.pootsadditions.block.entity.ModBlockEntities;
import net.pyerter.pootsadditions.util.Util;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.Set;

public class RedstoneMemorizerBlock extends Block {

    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;

    public static final int MAX_POWER = 15;
    public static final IntProperty MEMORIZED_POWER = IntProperty.of("memorized_power", 0, MAX_POWER);
    public static final IntProperty REDSTONE_POWER = Properties.POWER;
    public static final BooleanProperty LIT = Properties.LIT;

    private boolean givesPower = true;

    public RedstoneMemorizerBlock(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        builder.add(MEMORIZED_POWER);
        builder.add(REDSTONE_POWER);
        builder.add(LIT);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {

        return ActionResult.PASS;
    }

    @Override
    public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return getRedstonePower(state, direction);
    }

    @Override
    public int getStrongRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return getRedstonePower(state, direction);
    }

    public int getRedstonePower(BlockState state, Direction dir) {
        if (!this.givesPower)
            return 0;

        if (dir.getId() == state.get(FACING).getId() || dir.getId() == Util.mirrorDirection(state.get(FACING)).getId())
            return state.get(MEMORIZED_POWER);

        return 0;
    }

    private void update(World world, BlockPos pos, BlockState state) {
        int receivedRedstonePower = this.getSidedRedstonePower(world, pos, state);
        if ((Integer)state.get(REDSTONE_POWER) != receivedRedstonePower) {
            if (world.getBlockState(pos) == state) {
                int memorizedPower = state.get(MEMORIZED_POWER);
                if (memorizedPower == 0)
                    world.setBlockState(pos, (BlockState)state.with(REDSTONE_POWER, receivedRedstonePower).with(MEMORIZED_POWER, receivedRedstonePower).with(LIT, receivedRedstonePower > 0), 2);
                else if (memorizedPower == receivedRedstonePower) {
                    world.setBlockState(pos, (BlockState)state.with(REDSTONE_POWER, receivedRedstonePower).with(MEMORIZED_POWER, 0).with(LIT, false), 2);
                } else {
                    world.setBlockState(pos, (BlockState)state.with(REDSTONE_POWER, receivedRedstonePower), 2);
                }
            }

            Set<BlockPos> set = Sets.newHashSet();
            set.add(pos);
            Direction[] directions = Direction.values();
            int numbDirections = directions.length;

            for(int index = 0; index < numbDirections; ++index) {
                Direction direction = directions[index];
                set.add(pos.offset(direction));
            }

            Iterator blockPosIterator = set.iterator();

            while(blockPosIterator.hasNext()) {
                BlockPos blockPos = (BlockPos)blockPosIterator.next();
                world.updateNeighborsAlways(blockPos, this);
            }
        }

    }

    /*
    private int getReceivedRedstonePower(World world, BlockPos pos) {
        this.givesPower = false;
        int receivedSignal = world.getReceivedRedstonePower(pos);
        this.givesPower = true;
        int maxPower = 0;
        if (receivedSignal < 15) {
            for (Direction dir: Direction.values()) {
                BlockPos blockPos = pos.offset(dir);
                BlockState blockState = world.getBlockState(blockPos);
                maxPower = Math.max(maxPower, this.increasePower(blockState, dir));
            }
        }

        return Math.max(receivedSignal, maxPower - 1);
    }
    */

    private int getSidedRedstonePower(World world, BlockPos pos, BlockState state) {
        this.givesPower = false;
        int power1 = world.getStrongRedstonePower(pos.offset(state.get(FACING)), state.get(FACING).getOpposite());
        int power2 = world.getStrongRedstonePower(pos.offset(state.get(FACING).getOpposite()), state.get(FACING));
        this.givesPower = true;
        int receivedSignal = Math.max(power1, power2);

        int maxPower = 0;
        if (receivedSignal < 15) {
            for (Direction dir: Direction.values()) {
                if (dir == state.get(FACING) || dir == state.get(FACING).getOpposite()) {
                    BlockPos blockPos = pos.offset(dir);
                    BlockState blockState = world.getBlockState(blockPos);
                    maxPower = Math.max(maxPower, this.increasePower(blockState, dir));
                }
            }
        }

        return Math.max(receivedSignal, maxPower - 1);
    }

    private int increasePower(BlockState state, Direction dir) {
        return state.isOf(this) ? getRedstonePower(state, dir) : 0;
    }

    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (!world.isClient) {
            this.update(world, pos, state);
        }
    }

    private void updateOffsetNeighbors(World world, BlockPos pos) {
        Iterator var3 = Direction.Type.HORIZONTAL.iterator();

        Direction direction;
        while(var3.hasNext()) {
            direction = (Direction)var3.next();
            this.updateNeighbors(world, pos.offset(direction));
        }

        var3 = Direction.Type.HORIZONTAL.iterator();

        while(var3.hasNext()) {
            direction = (Direction)var3.next();
            BlockPos blockPos = pos.offset(direction);
            if (world.getBlockState(blockPos).isSolidBlock(world, blockPos)) {
                this.updateNeighbors(world, blockPos.up());
            } else {
                this.updateNeighbors(world, blockPos.down());
            }
        }

    }

    private void updateNeighbors(World world, BlockPos pos) {
        if (world.getBlockState(pos).isOf(this)) {
            world.updateNeighborsAlways(pos, this);
            Direction[] var3 = Direction.values();
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                Direction direction = var3[var5];
                world.updateNeighborsAlways(pos.offset(direction), this);
            }

        }
    }

    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        if (!oldState.isOf(state.getBlock()) && !world.isClient) {
            this.update(world, pos, state);
            Iterator var6 = Direction.Type.VERTICAL.iterator();

            while(var6.hasNext()) {
                Direction direction = (Direction)var6.next();
                world.updateNeighborsAlways(pos.offset(direction), this);
            }

            this.updateOffsetNeighbors(world, pos);
        }
    }

    public boolean emitsRedstonePower(BlockState state) {
        return this.givesPower;
    }

    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock())) {
            super.onStateReplaced(state, world, pos, newState, moved);
            if (!world.isClient) {
                Direction[] var6 = Direction.values();
                int var7 = var6.length;

                for(int var8 = 0; var8 < var7; ++var8) {
                    Direction direction = var6[var8];
                    world.updateNeighborsAlways(pos.offset(direction), this);
                }

                this.update(world, pos, state);
                this.updateOffsetNeighbors(world, pos);
            }
        }
    }

    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if ((Boolean)state.get(LIT) && !world.isReceivingRedstonePower(pos)) {
            world.setBlockState(pos, (BlockState)state.cycle(LIT), 2);
        }

    }

    public static int calculateLuminance(BlockState state) {
        if (state.getBlock() instanceof RedstoneMemorizerBlock) {
            return state.get(MEMORIZED_POWER);
        }
        return 0;
    }
}
