package jp.co.souki.util;

public class Common {

    public final static boolean debugFlg = false;

    public static void debug(Object o) {
        if (debugFlg) {
            System.out.println(String.valueOf(o));
        }
    }

    public static <T> T cast(Object o) {
        return (T)o;
    }
}
