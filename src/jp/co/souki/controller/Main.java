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
        int interval = 120_000;
        for(int i=1; i<65535; i++) {
            System.out.println(i + " ... 起動");
            try {
                Thread.sleep(interval / 100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("interval = " + interval);
            screen.downBlock();
            if (i % 30 == 0) {
                if (interval > 50000) {
                    interval -= (700000 / i);
                } else {
                    interval -= (500000 / i);
                }
            }
            if (screen.isNotAllMove()) {
                screen.createBlock();
            }
        }
        System.out.println("くりあ");

    }
}
