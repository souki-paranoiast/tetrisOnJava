package jp.co.souki.view;

import jp.co.souki.model.GameField;
import jp.co.souki.model.blocks.base.SubOneBlock;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Screen extends JFrame {
    /** ゲームフィールド（Model全般） */
    private final GameField gameField;

    /** メインパネル */
    private final JPanel panel;

    private final JPanel subPanel;

    public final JLabel scoreLabel;

    public Screen(GameField gameField) {
        this.gameField = gameField;
        panel = new JPanel();
        subPanel = new JPanel();
        scoreLabel = new JLabel();

        panel.setLayout(new GridLayout(gameField.row - gameField.tateBanpei, gameField.col));
        for (PointView[] pvs : gameField.field) {
            for (PointView pv : pvs) {
                if (pv.id <= gameField.tateBanpei * gameField.col)  {
                    continue; // 番兵分は表示対象外
                }
                panel.add(pv);
            }
        }
        int width = (40 * gameField.col);
        int height = 40 * (gameField.row - gameField.tateBanpei);
//        panel.setBounds(0, 0, width, height);

        subPanel.setBackground(Color.LIGHT_GRAY);
        scoreLabel.setOpaque(true);
        scoreLabel.setVerticalAlignment(SwingConstants.CENTER);
        scoreLabel.setVerticalTextPosition(SwingConstants.CENTER);
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        scoreLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        setScore(0); // これをやらないとパネルサイズが表示後に代わって気持ち悪い。そもそもサイズが変わるのが気持ち悪い
        subPanel.setLayout(new GridLayout(1,1));
        subPanel.add(scoreLabel);
//        subPanel.setSize(150, height);

        this.setBounds(600, 20, width + 80, height);
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = (width / (width + 80.0));
        gbc.weighty = 1.0d;
        gbc.fill = GridBagConstraints.BOTH;
        layout.setConstraints(panel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = (80 / (width + 80.0));
        gbc.weighty = 1.0d;
        gbc.fill = GridBagConstraints.BOTH;
        layout.setConstraints(subPanel, gbc); // サンプル持ってきた書き方だけど気持ち悪いよなぁ。。

        this.setLayout(layout);

        this.registEvent();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        add(panel);
        add(subPanel);
        setVisible(true);
    }

    public void createBlock() {
        int x = gameField.col / 2 - 1, y = 2;
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
    public synchronized void downBlock(boolean isAction) {
        gameField.downBlock(isAction);
        gameField.deleteBlock();
        setScore(gameField.score);
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
                    case KeyEvent.VK_SPACE :
                        blockAction();
                        break;
                        // TODO
//                    case KeyEvent.VK_UP :
//                    case KeyEvent.VK_W :
//                        break;
                    case KeyEvent.VK_RIGHT :
                    case KeyEvent.VK_D :
                        moveRight();
                        break;
                    case KeyEvent.VK_DOWN :
                    case KeyEvent.VK_S :
                        downBlock(true);
                        break;
                    case KeyEvent.VK_LEFT :
                    case KeyEvent.VK_A :
                        moveLeft();
                        break;
                }
            }
        });
    }

    public void setScore(int score) {
        this.scoreLabel.setText(String.format("点数 ： %05d", score));
        this.scoreLabel.repaint();
    }
}
