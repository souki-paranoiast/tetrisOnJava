package jp.co.souki.controller;

import jp.co.souki.model.GameField;
import jp.co.souki.util.Common;
import jp.co.souki.view.Screen;

/**
 * テトリス
 * <pre>
 *     通常モード：java -jar ./tetrisOnJava.jar FALSE
 *     特殊モード：java -jar ./tetrisOnJava.jar
 * </pre>
 */
public class Main {
    // TODO 全体的に副作用が氾濫してるし引数で受け取ったものを更新したりしているので、可能な限りリファクタリングしていきたい
    public static void main(String ... args) {
        // 基本的には特殊ルール
        Common.setNormal(args != null && args.length != 0 && args[0].equalsIgnoreCase("FALSE"));

        GameField gameField = new GameField();
        Screen screen = new Screen(gameField);

        int interval = 120_000;
        String lastMessage = "クリア";
        for(int i=1; i<65535; i++) {
            System.out.println(i + " ... 起動");
            try {
                Thread.sleep(interval / 100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("interval = " + interval);
            screen.downBlock(false);
            if (i % 30 == 0) {
                if (interval > 50000) {
                    interval -= (700000 / i);
                } else {
                    interval -= (400000 / i);
                }
            }
            if (screen.isNotAllMove()) {
                screen.createBlock();
            }
            if (gameField.isGameOver()) {
                lastMessage = "げぇむおーばー";
                break;
            }
        }
        System.out.println(lastMessage);

    }
}
