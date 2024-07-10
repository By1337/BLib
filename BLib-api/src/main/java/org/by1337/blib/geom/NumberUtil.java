package org.by1337.blib.geom;

public class NumberUtil {
    public static int floor(double num) {
        final int floor = (int) num;
        return floor == num ? floor : floor - (int) (Double.doubleToRawLongBits(num) >>> 63);
    }
    public static double square(double num) {
        return num * num;
    }
}
