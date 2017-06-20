package jp.co.souki.util;

/** 引数3つのTriConsumerだけど、プリミティブでほしいので固定にする。int -> Integerの変換ってどれくらいかかるんだろう？ */
public interface MyConsumer<T> {
    public void apply(T t, int x, int y);
}
