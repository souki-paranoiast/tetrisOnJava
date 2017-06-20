package jp.co.souki.util;

@FunctionalInterface
public interface MyPredicate<T1> {
    public boolean test(T1 t1, int t2, int t3);
}
