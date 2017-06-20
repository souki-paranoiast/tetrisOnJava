package jp.co.souki.view;

import jp.co.souki.model.GameField;
import jp.co.souki.model.SubOneBlock;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Screen extends JFrame {
    /** ゲームフィールド（Model全般） */
    private final GameField gameField;

    /** メインパネル */
    private final JPanel panel;

    public Screen(GameField gameField) {
        this.gameField = gameField;
        panel = new JPanel();

        panel.setLayout(new GridLayout(gameField.row - gameField.tateBanpei, gameField.col));
        for (PointView[] pvs : gameField.field) {
            for (PointView pv : pvs) {
                if (pv.id <= gameField.tateBanpei * gameField.col)  {
                    continue; // 番兵分は表示対象外
                }
                panel.add(pv);
            }
        }
        this.setBounds(600, 20, 40 * gameField.col, 40 * (gameField.row - gameField.tateBanpei));
        this.registEvent();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        add(panel);
        setVisible(true);
    }

    public void createBlock() {
        int x = gameField.col / 2, y = gameField.tateBanpei - 1;
        if (!gameField.field[y][x].isDefaultOneBlock()) { // 何らかのブロック
            if (!(gameField.field[y][x].getBlock() instanceof SubOneBlock)) { // SubOneの場合は、ひとまず見ないようにする
                if (!gameField.field[y][x].getBlock().canMoveDown(gameField, x, y)) {
                    this.gameOver();
                    return;
                }
            }
        }
        gameField.createBlock(x, y);
        repaintAll();
    }

    public void gameOver() {
        gameField.gameOver();
        repaintAll();
    }
    // イベントと時間で同時起動があり得るかも
    public synchronized void downBlock() {
        gameField.downBlock();
        gameField.deleteBlock();
        repaintAll();
    }
    public void moveRight() {
        gameField.moveRight();
        repaintAll();
    }
    public void moveLeft() {
        gameField.moveLeft();
        repaintAll();
    }
    public void blockAction() {
        gameField.action();
        repaintAll();
    }
    public void repaintAll() {
        for (PointView[] pvs : gameField.field) {
            for (PointView pv : pvs) {
                pv.repaintBackground();
            }
        }
    }

    /** 全てがActiveではない場合にTRUEを返します */
    public boolean isNotAllMove() {
        for (PointView[] pvs : gameField.field) {
            for (PointView pv : pvs) {
                if (pv.getBlock().isActivate()) {
                    return false;
                }
            }
        }
        return true;
    }

    private void registEvent() {
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_ENTER :
                        blockAction();
                        break;
                        // TODO
//                    case KeyEvent.VK_UP :
//                        break;
                    case KeyEvent.VK_RIGHT :
                        moveRight();
                        break;
                    case KeyEvent.VK_DOWN :
                        downBlock();
                        break;
                    case KeyEvent.VK_LEFT :
                        moveLeft();
                        break;
                }
            }
        });
    }
}
