package jp.co.souki.util;

/** 引数3つのTriFunctionだけど、プリミティブでほしいので固定にする。int -> Integerの変換ってどれくらいかかるんだろう？ */
public interface MyFunction<T, R> {
    public R execute(T t, int x, int y);
}
