package jp.co.souki.controller;

import jp.co.souki.model.GameField;
import jp.co.souki.view.Screen;

/**
 * テトリス
 */
public class Main {
    // TODO 全体的に副作用が氾濫してるし引数で受け取ったものを更新したりしているので、可能な限りリファクタリングしていきたい
    public static void main(String ... args) {
        GameField gameField = new GameField();
        Screen screen = new Screen(gameField);
        for(int i=0; i<65535; i++) {
            System.out.println(i + " ... 起動");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (i % 4 == 0) {
                screen.downBlock();
            }
            if (screen.isNotAllMove()) {
                screen.createBlock();
            }
        }
        System.out.println("くりあ");

    }
}
