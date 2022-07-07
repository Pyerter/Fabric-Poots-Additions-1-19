package net.pyerter.pootsadditions.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;

public class Util {
    public static int clamp(int value, int min, int max) {
        value = Math.max(value, min);
        value = Math.min(value, max);
        return value;
    }

    public static boolean blockPosEqual(BlockPos pos1, BlockPos pos2) {
        return pos1.asLong() == pos2.asLong();
    }

    public static BlockPos addBlockPos(BlockPos pos1, BlockPos pos2) {
        return new BlockPos(pos1.getX() + pos2.getX(), pos1.getY() + pos2.getY(), pos1.getZ() + pos2.getZ());
    }

    public static BlockPos addBlockPos(BlockPos pos, Direction dir) {
        return new BlockPos(pos.getX() + dir.getOffsetX(), pos.getY() + dir.getOffsetY(), pos.getZ() + dir.getOffsetZ());
    }

    public static Direction numbToDirection(int numb) {
        for (Direction dir: Direction.values()) {
            if (numb == dir.getHorizontal())
                return dir;
        }
        return Direction.UP;
    }

    public static Direction mirrorDirection(Direction dir) {
        switch (dir) {
            case DOWN: return Direction.UP;
            case UP: return Direction.DOWN;
            case NORTH: return Direction.SOUTH;
            case SOUTH: return Direction.NORTH;
            case EAST: return Direction.WEST;
            case WEST: return Direction.EAST;
        }
        return Direction.DOWN;
    }

    public static final Direction[] HORIZONTAL_DIRECTIONS = new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};

    public static boolean isNullOrBlank(String s) {
        return s == null || s.isBlank();
    }

    public static boolean pointInRectangle(double x, double y, int rectX, int rectY, int rectWidth, int rectHeight) {
        return (x >= rectX && x < rectX + rectWidth &&
                y >= rectY && y < rectY + rectHeight);
    }

    public static boolean pointInRectangleByCorners(double x, double y, int rectX, int rectY, int rectX2, int rectY2) {
        return pointInRectangle(x, y, rectX, rectY, rectX2 - rectX + 1, rectY2 - rectY + 1);
    }

    public static <T> ArrayList<T> arrayListOf(T ... args) {
        ArrayList<T> list = new ArrayList<>();
        for (T arg: args) {
            list.add(arg);
        }
        return list;
    }
}
