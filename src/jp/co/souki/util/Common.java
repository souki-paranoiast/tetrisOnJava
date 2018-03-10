package jp.co.souki.util;

public class Common {

    public final static boolean debugFlg = false;

    private static boolean isNormal = false;

    public static void debug(Object o) {
        if (debugFlg) {
            System.out.println(String.valueOf(o));
        }
    }
    public static boolean isNormal() {
        return isNormal;
    }
    public static void setNormal(boolean pIsNormal) {
        isNormal = pIsNormal;
    }

    @SuppressWarnings(value = "all")
    public static <T,R> R cast(T o) {
        return (R)o;
    }
}
