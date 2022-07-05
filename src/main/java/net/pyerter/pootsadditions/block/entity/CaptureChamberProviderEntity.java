package net.pyerter.pootsadditions.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.pyerter.pootsadditions.PootsAdditions;
import net.pyerter.pootsadditions.util.Util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class CaptureChamberProviderEntity extends BlockEntity {

    public static int UPDATE_PERIOD = 50;
    public static int MAX_LINKS = 4;
    public static int MAX_PRIORITY = 5;

    private int priority = 0;
    private int updateTimer = 0;
    private boolean initiallyUpdated = false;

    public CaptureChamberProviderEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CAPTURE_CHAMBER_PROVIDER, pos, state);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt("capture_chamber_provider.priority", priority);
        nbt.putInt("capture_chamber_provider.update_timer", updateTimer);
        nbt.putBoolean("capture_chamber_provider.initial_update", initiallyUpdated);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        priority = nbt.getInt("capture_chamber_provider.priority");
        updateTimer = nbt.getInt("capture_chamber_provider.update_timer");
        initiallyUpdated = nbt.getBoolean("capture_chamber_provider.initial_update");
    }

    public int getPriority() {
        return priority;
    }

    public int incrementPriority() {
        this.priority++;
        if (priority > MAX_PRIORITY)
            this.priority = 0;
        initiallyUpdated = false;
        this.markDirty();
        return priority;
    }

    public static void tick(World world, BlockPos pos, BlockState state, CaptureChamberProviderEntity entity) {
        /*if (!entity.initiallyUpdated) {
            searchAndLink(world, pos, state, entity);
            entity.updateTimer = 0;
            entity.initiallyUpdated = true;
        }*/

        entity.updateTimer += 1;
        if (entity.updateTimer >= UPDATE_PERIOD) {
            entity.updateTimer = 0;
            searchAndLink(world, pos, state, entity);
        }

        entity.markDirty();
    }

    public static void searchAndLink(World world, BlockPos pos, BlockState state, CaptureChamberProviderEntity entity) {
        BlockPos originalPosUp = new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ());
        BlockPos originalPosDown = new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ());
        searchAndLink(world, pos, state, entity, originalPosUp);
        searchAndLink(world, pos, state, entity, originalPosDown);
    }

    private static void searchAndLink(World world, BlockPos pos, BlockState state, CaptureChamberProviderEntity entity, BlockPos chamberPos) {
        BlockEntity targetChecker = world.getBlockEntity(chamberPos);
        if (targetChecker != null && targetChecker instanceof CaptureChamberEntity) {
            CaptureChamberEntity originalEntity = (CaptureChamberEntity) targetChecker;
            originalEntity.setReceivingPriority(entity.getPriority());
            List<BlockPos> checkedPositions = new ArrayList<>();
            checkedPositions.add(chamberPos);
            List<CaptureChamberEntity> providers = new ArrayList<>();
            providers.add(originalEntity);
            searchAndLinkBackTo(world, chamberPos, originalEntity, checkedPositions, providers);
        }
    }

    private static void searchAndLinkBackTo(World world, BlockPos receiverPos, CaptureChamberEntity receiver, List<BlockPos> checkedPositions, List<CaptureChamberEntity> providers) {
        LinkedList<BlockPos> queuedPos = new LinkedList<>();

        for (Direction dir: HORIZONTAL_DIRECTIONS) {
            BlockPos nextPos = new BlockPos(receiverPos.getX() + dir.getOffsetX(), receiverPos.getY(), receiverPos.getZ() + dir.getOffsetZ());
            queuedPos.add(nextPos);
            checkedPositions.add(nextPos);
        }

        CaptureChamberEntity ultimateReceiver = receiver;
        while (!queuedPos.isEmpty()) {
            BlockPos currentPos = queuedPos.removeFirst();

            BlockEntity currentEntity = world.getBlockEntity(currentPos);
            if (currentEntity == null || !(currentEntity instanceof CaptureChamberEntity))
                continue;

            CaptureChamberEntity currentChamber = (CaptureChamberEntity) currentEntity;
            if (!posIsCaptureChamber(world, currentChamber.getProvideTarget()))
                providers.add(currentChamber);
            if (currentChamber.getReceivingPriority() > ultimateReceiver.getReceivingPriority())
                ultimateReceiver = currentChamber;

            for (Direction dir: HORIZONTAL_DIRECTIONS) {
                BlockPos nextPos = new BlockPos(currentPos.getX() + dir.getOffsetX(), currentPos.getY(), currentPos.getZ() + dir.getOffsetZ());
                if (!listContainsPos(checkedPositions, nextPos) && manhattanDistanceHorizontal(receiverPos, nextPos) <= MAX_LINKS) {
                    queuedPos.add(nextPos);
                    checkedPositions.add(nextPos);
                }
            }
        }

        for (CaptureChamberEntity current: providers) {
            if (current != ultimateReceiver && current.getReceivingPriority() < ultimateReceiver.getReceivingPriority())
                current.setProvideTarget(ultimateReceiver.getPos());
        }
    }

    public static int manhattanDistanceHorizontal(BlockPos from, BlockPos to) {
        return Math.abs(to.getX() - from.getX()) + Math.abs(to.getZ() - from.getZ());
    }

    public static boolean listContainsPos(List<BlockPos> poses, BlockPos pos) {
        for (BlockPos current: poses) {
            if (current.getX() == pos.getX() && current.getY() == pos.getY() && current.getZ() == pos.getZ()) {
                return true;
            }
        }
        return false;
    }

    public static boolean posIsCaptureChamber(World world, BlockPos pos) {
        BlockEntity entity = world.getBlockEntity(pos);
        return entity instanceof CaptureChamberEntity;
    }

    public static final Direction[] HORIZONTAL_DIRECTIONS = new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
}
