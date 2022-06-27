package net.pyerter.pootsadditions.util;

import java.util.ArrayList;

public class Util {
    public static int clamp(int value, int min, int max) {
        value = Math.max(value, min);
        value = Math.min(value, max);
        return value;
    }

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
