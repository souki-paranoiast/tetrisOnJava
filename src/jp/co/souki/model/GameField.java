package jp.co.souki.model;

import jp.co.souki.model.blocks.BlockEnum;
import jp.co.souki.model.blocks.base.Block;
import jp.co.souki.model.blocks.base.DefaultOneBlock;
import jp.co.souki.model.blocks.base.OneBlock;
import jp.co.souki.model.blocks.base.SubOneBlock;
import jp.co.souki.util.Common;
import jp.co.souki.util.MyConsumer;
import jp.co.souki.util.MyPredicate;
import jp.co.souki.view.PointView;

import java.awt.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ゲームのフィールド情報をモデリングします<br>
 * 画面とデータとの連携部分になります<br>
 * メモ：row == y, col == x
 * 引数はy, x の順番でとるべきだろうか。。。
 */
public class GameField {

    public final PointView[][] field;

    public final int row = 20;

    public final int col = 10;

    private boolean isGameOver = false;

    /** 点数 */
    public int score = 0;

    public static final int[] points = {10, 42, 105, 250, 390, 720, 1270};
    /**
     * 縦の番兵
     */
    public final int tateBanpei = 3; // 2よりは大きくないと多分落ちる

    public GameField() {
        field = new PointView[row][col];
        int i = 0;
        for (int ri = 0; ri < row; ri++) {
            for (int ci = 0; ci < col; ci++) {
                field[ri][ci] = new PointView(++i, new DefaultOneBlock(ci, ri));
            }
        }
    }

    public boolean isGameOver() {
        return isGameOver;
    }
    /**
     * ブロックを生成する。
     *
     * @param x
     * @param y
     */
    public void createBlock(int x, int y) {
        setTetrisBlocks(x, y);
    }

    /**
     * ブロックを消去する
     */
    public void deleteBlock() {
        System.out.println("deleteBlock ... ");
        // TODO Activeの制御は実行者だな・・
        for (int y=0; y<field.length; y++) {
            for (int x=0; x<field[y].length; x++) {
                if (field[y][x].isDefaultOneBlock()) {
                    continue;
                }
                if (field[y][x].getBlock().isActivate()) {
                    return;
                }
            }
        }

        // 最大row回数分（再帰とかでも面白いかもだけど、ひとまず思いついた方で実装）
        List<Integer> delList = new ArrayList<>(4); // 基本形


        // 配列を逆順にするAPI無いのか。。 というかこのあたりアルゴリズムの基礎力が無いことを思い知らされる
        for (int ri = field.length - 1; ri >= 0; ri--) {
            if (Arrays.stream(field[ri]).allMatch(p -> !p.isDefaultOneBlock())) {
                delList.add(Integer.valueOf(ri));
            }
        }
        int delListSize = delList.size();
        if (delListSize == 0) {
            return;
        }

        int addScore;
        if (delListSize > points.length) {
            int diff = delListSize - points.length;
            addScore = (int)(points[points.length - 1] * 1.8 * diff);
        } else {
            addScore = points[delListSize - 1];
        }
        score += addScore;

        int[][] moveRelation = makeRelation(delList);
        for (int[] loop : moveRelation) { // TODO リファクタリング
            int current = loop[0];
            int next = loop[1];
            if (next == -1) { // 削除
                for (int ci = field[current].length - 1; ci >= 0; ci--) {
                    field[current][ci].setBlock(new DefaultOneBlock(ci, current));
                }
            } else if (next == current) {
                // 移動無し
            } else {
                for (int ci = field[current].length - 1; ci >= 0; ci--) {
                    OneBlock b = field[current][ci].getBlock();
                    b.setY(next);
                    field[next][ci].setBlock(b);
                    field[current][ci].setBlock(new DefaultOneBlock(ci, current));
                }
            }
        }
        for (int ri = delListSize - 1; ri >= 0; ri--) {
            System.out.println("newBackground -> " + ri);
            for (int ci = field[ri].length - 1; ci >= 0; ci--) {
                field[ri][ci].setBlock(new DefaultOneBlock(ci, ri));
            }
        }
    }

    /**
     * ゲームオーバー
     */
    public void gameOver() {
        Color gameOverColor = new Color(255, 64, 64);
        for (PointView[] pvs : field) {
            for (PointView pv : pvs) {
                if (!pv.isDefaultOneBlock()) {
                    pv.getBlock().setColor(gameOverColor);
                }
            }
        }
        isGameOver = true;
    }

    /**
     * ブロックを下に移動します。下への移動は各ブロックの実装によります
     */
    public void downBlock(boolean isAction) {
        System.out.println("downBlock ... ");
        this.moveSpecifiedDirectionReversedPosition(
                (gameField, x, y) -> field[y][x].getBlock().canMoveDown(this, x, y),
                (gameField, x, y) -> field[y][x].getBlock().moveDown(this, x, y),
                isAction
        );
    }

    /**
     * ブロックを右に移動します。右への移動は各ブロックの実装によります。
     */
    public void moveRight() {
        System.out.print("R ");
        this.moveSpecifiedDirectionReversedPosition(
                (gameField, x, y) -> field[y][x].getBlock().canMoveRight(this, x, y),
                (gameField, x, y) -> field[y][x].getBlock().moveRight(this, x, y),
                false
        );
    }

    /**
     * ブロックを左に移動します。左への移動は各ブロックの実装によります。
     */
    public void moveLeft() {
        System.out.print("L ");
        this.moveSpecifiedDirection(
                (gameField, x, y) -> field[y][x].getBlock().canMoveLeft(this, x, y),
                (gameField, x, y) -> field[y][x].getBlock().moveLeft(this, x, y)
        );
    }

    /**
     * Actionを実行します
     */
    public void action() {
        System.out.print("act ");
        for (int ri = 0; ri < field.length; ri++) {
            PointView[] s = field[ri];
            for (int ci = 0; ci < s.length; ci++) {
                if (!field[ri][ci].isDefaultOneBlock() && field[ri][ci].getBlock().isActivate()) {
                    if (field[ri][ci].getBlock().canAction(this, ci, ri)) {
                        field[ri][ci].getBlock().executeAction(this, ci, ri);
                        return;
                    }
                }
            }
        }
    }

    /**
     * 指定された方向へ移動します。
     *
     * @param predicate 条件
     * @param consumer  実アクション
     */
    private void moveSpecifiedDirection(MyPredicate<GameField> predicate, MyConsumer<GameField> consumer) {
        // 右下から見る。この辺り読みづらいからStream使いたいなぁ
        for (int ri = 0; ri < field.length; ri++) {
            PointView[] s = field[ri];
            for (int ci = 0; ci < s.length; ci++) {
                PointView p = s[ci];
                if (!p.getBlock().isActivate() || !p.getBlock().hasRelation()) {
                    continue;
                }
                if (!predicate.test(this, ci, ri)) {
                    continue;
                }
                consumer.apply(this, ci, ri);
            }
        }
    }

    /**
     * 指定された方向へ移動します。右下から見ます
     *
     * @param predicate 条件
     * @param consumer  実アクション
     * @param isAction Actionによる起動かどうか。後付けParam
     */
    private void moveSpecifiedDirectionReversedPosition(MyPredicate<GameField> predicate, MyConsumer<GameField> consumer, boolean isAction) {
        // 右下から見る。この辺り読みづらいからStream使いたいなぁ
        for (int ri = field.length - 1; ri >= 0; ri--) {
            PointView[] s = field[ri];
            for (int ci = s.length - 1; ci >= 0; ci--) {
                PointView p = s[ci];
                if (!p.getBlock().isActivate() || !p.getBlock().hasRelation()) {
                    continue;
                }
                if (!predicate.test(this, ci, ri)) {
                    continue;
                }
                consumer.apply(this, ci, ri);
                if (isAction) {
                    score++;
                }
            }
        }
    }

    private void setTetrisBlocks(int x, int y) {
        List<? extends OneBlock> blockList;
        if (Common.isNormal()) {
            blockList = BlockEnum.NORMAL.clazzList;
        } else {
            blockList = BlockEnum.APPENDIX.clazzList;
        }
        int size = blockList.size();
        int defaultNum = 99;
        int rand = ((int) (Math.random() * size));
        rand = (rand > (size - 1)) ? defaultNum : rand;

        Class<? extends OneBlock> clazz = Common.cast(blockList.get(rand));
        setBlocks(clazz, x, y);
    }

    private <BLOCKS extends OneBlock> void setBlocks(Class<BLOCKS> clazz, int x, int y) {

        try {
            Constructor<BLOCKS> bConst = clazz.getConstructor(int.class, int.class);
            BLOCKS blocks = bConst.newInstance(x, y);
            field[y][x].setBlock(blocks);

            Block.Relation relation = (Block.Relation) blocks.getRelationList().get(0);

            for (int[] position : relation.relative) {
                int subX = x + position[1];
                int subY = y + position[0];
                if (!field[subY][subX].isDefaultOneBlock()) {
                    gameOver();
                }
            }
            for (int[] position : relation.relative) {
                int subX = x + position[1];
                int subY = y + position[0];

                field[subY][subX].setBlock(new SubOneBlock(subX, subY, blocks));
            }
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    /**
     * 現在の行が、次に何行目になるかをマッピングします。ゴミコード。
     * @return [行]{current, next}を表す。消える場合は-1.
     */
    private int[][] makeRelation(List<Integer> delList) {
        int[][] ret = new int[row][2];
        for (int i = row - 1; i >= 0; i--) {
            int next = i;
            for (int del : delList) {
                if (i == del) {
                    next = -1;
                    break;
                } else if (i < del) {
                    next++;
                }
            }
            ret[row - i - 1] = new int[]{i, next}; // 逆順にしておく
        }
        return ret;
    }
    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(300);
        for (int ri = 0; ri < field.length; ri++) {
            PointView[] s = field[ri];
            for (int ci = 0; ci < s.length; ci++) {
                sb.append(String.format("%2d:%2d = %5s", ri, ci, field[ri][ci].getBlock().isActivate()));
                sb.append('\n');
            }
        }
        return sb.toString();
    }
}
