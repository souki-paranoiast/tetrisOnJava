package jp.co.souki.model;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class OneBlock<BLOCK extends OneBlock> {

    /** 動作対象のブロックであるか */
    private boolean activate;

    /** ブロックの色 */
    protected Color color;

    /** Actionのバージョン（通常、回転の現在形状を示す） */
    protected int version;

    // ブロックごとに持ちたくないなあ。。。
    /** X軸（横） */
    private int x;

    /** Y軸（縦） */
    private int y;

    public OneBlock(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.version = 0;
        setActivate(false);
    }

    /**
     * グループ（ブロックの塊）を保持する代表ブロックであるかどうかを返却します。<br>
     * （= グループを保持するか）
     * @return
     */
    public boolean hasRelation() {
        // TODO これはどうしよう。サブ系かどうかのフラグとかを保持するか専用のクラスにするか。
        return !getRelationList().isEmpty();
    }

    /**
     * 保持するグループ（ブロックの塊）を返却します。<br>
     * 保持しない場合、空のリストを返す必要があります。
     * @return
     */
    public abstract List<Block.Relation> getRelationList();

    /**
     * 主点となる点を指定して、そのブロックが下に移動できるかどうかを返します<br>
     *     注意：移動できない場合、このメソッドは{@link Block#setActivate(boolean)}にfalseを渡して実行します
     * @param gameField フィールド（盤面）
     * @param x 現在のX軸
     * @param y 現在のY軸
     * @return
     */
    public boolean canMoveDown(GameField gameField, int x, int y) {
        return canMoveDown(gameField, x, y, true);
    }

    /**
     * @see OneBlock#canMoveDown(GameField, int, int)
     * @param gameField
     * @param x
     * @param y
     * @return
     */
    public boolean canMoveDown(GameField gameField, int x, int y, boolean dry) {
        int nextY = y + 1;
        if (gameField.row <= nextY) {
            if (dry) {
                gameField.field[y][x].getBlock().setActivate(false);
            }

            return false;
        }

        if (!gameField.field[nextY][x].isDefaultOneBlock()) {
            if (dry) {
                gameField.field[y][x].getBlock().setActivate(false);
            }
            return false;
        }
        return true;
    }

    /**
     * 主点となる点を指定して、そのブロックを下に移動します
     * @param gameField フィールド（盤面）
     * @param x 現在のX軸
     * @param y 現在のY軸
     * @return
     */
    public void moveDown(GameField gameField, int x, int y) {
        OneBlock current = gameField.field[y][x].getBlock();
        current.setY(current.getY() + 1);
        gameField.field[y + 1][x].setBlock(current);
        gameField.field[y][x].setBlock(new DefaultOneBlock(x, y));
    }


    /**
     * 主点となる点を指定して、そのブロックが右に移動できるかどうかを返します
     * @param gameField フィールド（盤面）
     * @param x 現在のX軸
     * @param y 現在のY軸
     * @return
     */
    public boolean canMoveRight(GameField gameField, int x, int y) {
        int nextX = x + 1;
        if (gameField.col <= nextX) {
            return false;
        }
        if (!gameField.field[y][nextX].isDefaultOneBlock()) {
            return false;
        }
        return true;
    }

    /**
     * 主点となる点を指定して、そのブロックを右に移動します
     * @param gameField フィールド（盤面）
     * @param x 現在のX軸
     * @param y 現在のY軸
     * @return
     */
    public void moveRight(GameField gameField, int x, int y) {
        OneBlock current = gameField.field[y][x].getBlock();
        current.setX(current.getX() + 1);
        gameField.field[y][x + 1].setBlock(current);
        gameField.field[y][x].setBlock(new DefaultOneBlock(x, y));
    }

    /**
     * 主点となる点を指定して、そのブロックが左に移動できるかどうかを返します
     * @param gameField フィールド（盤面）
     * @param x 現在のX軸
     * @param y 現在のY軸
     * @return
     */
    public boolean canMoveLeft(GameField gameField, int x, int y) {
        int nextX = x - 1;
        if (0 > nextX) {
            return false;
        }
        if (!gameField.field[y][nextX].isDefaultOneBlock()) {
            return false;
        }
        return true;
    }

    /**
     * 主点となる点を指定して、そのブロックを左に移動します
     * @param gameField フィールド（盤面）
     * @param x 現在のX軸
     * @param y 現在のY軸
     * @return
     */
    public void moveLeft(GameField gameField, int x, int y) {
        OneBlock current = gameField.field[y][x].getBlock();
        current.setX(current.getX() - 1);
        gameField.field[y][x - 1].setBlock(current);
        gameField.field[y][x].setBlock(new DefaultOneBlock(x, y));
    }

    /**
     * 主点となる点を指定して、そのブロックのActionが実行可能かどうかを返します
     * @param gameField フィールド（盤面）
     * @param x 現在のX軸
     * @param y 現在のY軸
     * @return
     */
    public abstract boolean canAction(GameField gameField, int x, int y);

    /**
     * 主点となる点を指定して、そのブロックのActionが実行します<br>
     *     注意：引数の値に対して副作用を含みます
     * @param gameField フィールド（盤面）
     * @param x 現在のX軸
     * @param y 現在のY軸
     */
    public abstract void executeAction(GameField gameField, int x, int y);

    /**
     * フィールド上に存在するとき、動作対象のブロックであるかを返します
     * @return TRUE：動作対象のブロック
     */
    public boolean isActivate() {
        return activate;
    }

    /**
     * 動作対象のブロックとするかを設定します。
     * @param isActivate TRUE：動作対象のブロックにする
     */
    public void setActivate(boolean isActivate) {
        activate = isActivate;
    }

    /**
     * ブロックの色を返却します。
     * @return
     */
    public Color getColor() {
        return color;
    }

    /**
     * ブロックの色を設定します。
     * @param color
     */
    public void setColor(Color color) {
        this.color = color;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        int size = getRelationList().size();
        if (size == 0 || version == 0) {
            this.version = 0;
            return;
        }
        this.version = version;
    }
    public int nextVersion(int currentVersion) {
        if (!hasRelation()) {
            return 0;
        }
        int size = getRelationList().size();
        if (size == 0 || size == 1) {
            return 0;
        }
        int next = currentVersion + 1;
        if (size > next) {
            return next;
        }
        return (next % size);
    }

    /**
     * ブロックの位置関係性を示します<br>
     *  通常、基礎となる１点から複数の点を示す位置関係の複合でブロックが存在します
     */
    public static class Relation {
        /** 回転の連番 */
        public int version;

        /**
         * 相対的ポジション。イミュータブルで使用されるべきです<br>
         * <code>relative.length</code>で取得できるサイズが基礎以外の点ブロックの数です<br>
         * <code>relative[0] = y[縦]
         * <code>relative[1] = x[横] </code>
         */
        public final int[][] relative;
        public Relation(int version, int[][] relative) {
            this.version = version;
            this.relative = relative;
        }

        /** 一般的な意味合いと異なるけど、リストを作成します。{y, x}の配列を引数にします */
        public static List<Relation> of (int[][][] relations) {
            // zipWithIndex ...
//            return Arrays.stream(relations)
//                    .map(relation -> relation)
            int length = relations.length;
            List list = new ArrayList<>(length);
            for (int i=0; i<length; i++) {
                list.add(new Relation(i, relations[i]));
            }
            return list;
        }
        @Override public String toString() {
            return String.format("current version=%s, releation=%s", version, Arrays.toString(relative));
        }
    }
}
