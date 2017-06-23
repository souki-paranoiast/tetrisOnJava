package jp.co.souki.model.blocks.appendix;

import jp.co.souki.model.GameField;
import jp.co.souki.model.blocks.base.Block;
import jp.co.souki.model.blocks.base.OneBlock;
import jp.co.souki.model.blocks.base.SubOneBlock;
import jp.co.souki.util.Common;
import jp.co.souki.util.Point;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

// TODO 一番やりたかったことなのに結構面倒で泣ける。Actionを変えると制御できない気がするなぁ。。
/**
 * 成長していくブロックをモデリングします。左右に動くと、それぞれ1段階だけ成長します。
 * <pre>
 *       .
 *      .
 * </pre>
 */
public class GrowthBlock extends Block {

    private boolean isLeftGrowth = false;

    private boolean isRightGrowth = false;

    private static Color myColor = Color.BLACK;

    //// ATTENTION !!? 右に動いたら左が成長し、左の場合は逆。
    public static final List<Relation> normalRelationList = Relation.of(new int[][][] {
            {
//                  { 0,  0},
                    { 1, -1},
            },
    });
    public static final List<Relation> leftGrownRelationList = Relation.of(new int[][][] {
            {
                    { 0, -1},
//                  { 0,  0},
                    { 1, -2},
                    { 1, -1},
            },
    });
    public static final List<Relation> rightGrownRelationList = Relation.of(new int[][][] {
            {
//                  { 0,  0},
                    { 0,  1},
                    { 1, -1},
                    { 1,  0},
            },
    });
    public static final List<Relation> BothGrownRelationList = Relation.of(new int[][][] {
            {
                    { 0, -1},
//                  { 0,  0},
                    { 0,  1},
                    { 1, -2},
                    { 1, -1},
                    { 1,  0},
            },
    });

    public GrowthBlock(int x, int y) {
        super(x, y, myColor);
    }

    @Override
    public List<Relation> getRelationList() {
        if (isLeftGrowth) {
            if (isRightGrowth) {
                return BothGrownRelationList;
            }
            return leftGrownRelationList;
        }
        if (isRightGrowth) {
            return rightGrownRelationList;
        }
        return normalRelationList;
    }

    /**
     * 右に移動します。初回移動時は、元の位置にブロックを残します（= 左側が成長します）
     * @param gameField
     * @param x
     * @param y
     */
    @Override
    public void moveRight(GameField gameField, int x, int y) {
        this.move(gameField, x, y, 1, 0, true);
        if (!isLeftGrowth) { // 右に動いたら左が成長する
            // まだ動いてないなら、（デフォルトの形状でいう）元の位置にブロックを生成する
            List<Point> absoluteGroup = new ArrayList<>();
            OneBlock targetBlock = gameField.field[y][x + 1].getBlock(); // 動かす対象になっていたブロックかも？
            if (targetBlock instanceof SubOneBlock) { // TODO 元々のブロックがどこに行くか確定的でない気がするので一応見ておく
                targetBlock = ((SubOneBlock)targetBlock).getParent();
            }
            Relation relation = Common.cast(normalRelationList.get(targetBlock.getVersion()));
            for (int[] r : relation.relative) {
                absoluteGroup.add(new Point(x + r[1], y + r[0]));
            }
            absoluteGroup.add(new Point(x, y)); // これで元の位置が全部

            for (Point point : absoluteGroup) {
                gameField.field[point.y][point.x].setBlock(new SubOneBlock(point.x, point.y, targetBlock));
            }
            isLeftGrowth = true;
        }
    }

    /**
     * 左に移動します。初回移動時は、元の位置にブロックを残します（= 右側が成長します）
     * @param gameField
     * @param x
     * @param y
     */
    @Override
    public void moveLeft(GameField gameField, int x, int y) {
        this.move(gameField, x, y, -1, 0, false);
        if (!isRightGrowth) { // 左に動いたら右が成長する
            // まだ動いてないなら、（デフォルトの形状でいう）元の位置にブロックを生成する
            List<Point> absoluteGroup = new ArrayList<>();
            OneBlock targetBlock = gameField.field[y][x - 1].getBlock(); // 動かす対象になっていたブロックかも？
            if (targetBlock instanceof SubOneBlock) { // TODO 元々のブロックがどこに行くか確定的でない気がするので一応見ておく
                targetBlock = ((SubOneBlock)targetBlock).getParent();
            }
            Relation relation = Common.cast(normalRelationList.get(targetBlock.getVersion()));
            for (int[] r : relation.relative) {
                absoluteGroup.add(new Point(x + r[1], y + r[0]));
            }
            absoluteGroup.add(new Point(x, y)); // これで元の位置が全部

            for (Point point : absoluteGroup) {
                gameField.field[point.y][point.x].setBlock(new SubOneBlock(point.x, point.y, targetBlock));
            }
            isRightGrowth = true;
        }
    }

    /**
     * 動けるけど動きません
     * @param gameField フィールド（盤面）
     * @param x 現在のX軸
     * @param y 現在のY軸
     * @return
     */
    @Override
    public boolean canAction(GameField gameField, int x, int y) {
        return true;
    }

    /**
     * 動きません
     * @param gameField フィールド（盤面）
     * @param x 現在のX軸
     * @param y 現在のY軸
     */
    @Override
    public void executeAction(GameField gameField, int x, int y) {
        // TODO とりあえずこの子はAction自体は存在しないとする
    }
}
