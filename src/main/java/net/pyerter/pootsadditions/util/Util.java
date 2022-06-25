package net.pyerter.pootsadditions.util;

public class Util {
    public static int clamp(int value, int min, int max) {
        value = Math.max(value, min);
        value = Math.min(value, max);
        return value;
    }
}
