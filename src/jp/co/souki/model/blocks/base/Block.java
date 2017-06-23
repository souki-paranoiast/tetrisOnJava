package jp.co.souki.model.blocks.base;

//
//  ブロックの形状毎にクラス作成してみる
//

import jp.co.souki.model.GameField;
import jp.co.souki.util.Common;
import jp.co.souki.util.Point;
import jp.co.souki.view.PointView;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ブロックの基本的なモデリング情報を保持します
 */
public abstract class Block<BLOCKS extends Block<BLOCKS>> extends OneBlock<Block> {

    // Activeブロックが時間経過で成長とか面白そう??
//    protected List<Relation> relationList = new ArrayList<>();

    public Block(int x, int y, Color color) {
        super(x, y, color);
        setActivate(true);
    }

    @Override
    public boolean canMoveDown(GameField gameField, int x, int y) {
        return canMove(gameField, x, y, 0, 1, true);
    }

    @Override
    public void moveDown(GameField gameField, int x, int y) {
        this.move(gameField, x, y, 0, 1, true);
    }

    @Override
    public boolean canMoveRight(GameField gameField, int x, int y) {
        return canMove(gameField, x, y, 1, 0, true);
    }

    @Override
    public void moveRight(GameField gameField, int x, int y) {
        this.move(gameField, x, y, 1, 0, true);
    }

    @Override
    public boolean canMoveLeft(GameField gameField, int x, int y) {
        return canMove(gameField, x, y, -1, 0, false);
    }

    @Override
    public void moveLeft(GameField gameField, int x, int y) {
        this.move(gameField, x, y, -1, 0, false);
    }

    /**
     * 基本的なActionとして、各ブロックグループで定義している関係性通りにブロックの形状を変化（= 回転）させることが可能かどうかを返します
     * @param gameField フィールド（盤面）
     * @param x 現在のX軸
     * @param y 現在のY軸
     * @return
     */
    @Override
    public boolean canAction(GameField gameField, int x, int y) {
        OneBlock target = gameField.field[y][x].getBlock();
        List<Relation> relationList = target.getRelationList();
        if (relationList.isEmpty()) {
            return true;
        }
        // 今の
        Relation currentRelation = relationList.get(target.getVersion());
        List<Point> currentAbsoluteGroup = new ArrayList<>(relationList.size() + 1);
        for (int[] r : currentRelation.relative) {
            currentAbsoluteGroup.add(new Point(x + r[1], y + r[0]));
        }
        currentAbsoluteGroup.add(new Point(x, y)); // 指定された点も入れておく

        // 次の
        Relation nextRelation = relationList.get(nextVersion(target.getVersion()));
        List<Point> nextAbsoluteGroup = new ArrayList<>(relationList.size() + 1);
        for (int[] r : nextRelation.relative) {
            nextAbsoluteGroup.add(new Point(x + r[1], y + r[0]));
        }
        nextAbsoluteGroup.add(new Point(x, y)); // 指定された点も入れておく

        for (Point nextPoint : nextAbsoluteGroup) {
            int nextX = nextPoint.x;
            int nextY = nextPoint.y;
            if (nextX < 0 || nextX >= gameField.col || nextY < 0 || nextY >= gameField.row) {
                return false;
            }
            if (gameField.field[nextY][nextX].isDefaultOneBlock()) {
                continue; // 当然OK
            }
            if (currentAbsoluteGroup.contains(nextPoint)) {
                continue; // 今動かそうとしているやつなのでOKでしょう。多分 TODO
            }
            // 1個でもダメならもうダメ
            return false;
        }
        return true;
    }

    /**
     * 基本的なActionとして、各ブロックグループで定義している関係性通りにブロックの形状を変化させます（= 回転します）。
     * 引数のフィールドの中身が書き換わります。
     * @param gameField フィールド（盤面）
     * @param x 現在のX軸
     * @param y 現在のY軸
     */
    @Override
    public void executeAction(GameField gameField, int x, int y) {
        OneBlock target = gameField.field[y][x].getBlock();
        List<Relation> relationList = target.getRelationList();
        if (relationList.isEmpty()) {
            return;
        }
        // 今の
        Relation currentRelation = relationList.get(target.getVersion());
        ArrayList<Point> currentAbsoluteGroup = new ArrayList<>(relationList.size() + 1);
        for (int[] r : currentRelation.relative) {
            currentAbsoluteGroup.add(new Point(x + r[1], y + r[0]));
        }
        currentAbsoluteGroup.add(new Point(x, y)); // 指定された点も入れておく

        // 次の
        int nextVersion = nextVersion(target.getVersion());
        Relation nextRelation = relationList.get(nextVersion);
        ArrayList<Point> nextAbsoluteGroup = new ArrayList<>(relationList.size() + 1);
        for (int[] r : nextRelation.relative) {
            nextAbsoluteGroup.add(new Point(x + r[1], y + r[0]));
        }
        nextAbsoluteGroup.add(new Point(x, y)); // 指定された点も入れておく

        // 次バージョンセット
        target.setVersion(nextVersion);
        // TODO 結局なんかすっごく微妙なコード
        // 一瞬空間から存在を消す
        int size = currentAbsoluteGroup.size();
        List<OneBlock> floatBlocks = new ArrayList<>(size);
        for (Point current : currentAbsoluteGroup) {
            int cx = current.x;
            int cy = current.y;
            floatBlocks.add(gameField.field[cy][cx].getBlock());
            gameField.field[cy][cx].setBlock(null);
        }
        for (int i=0; i<size; i++) {
            OneBlock b = floatBlocks.get(i);
            Point next = nextAbsoluteGroup.get(i);
            b.setY(next.y);
            b.setX(next.x);
            gameField.field[next.y][next.x].setBlock(b);
        }
        for (Point current : currentAbsoluteGroup) {
            int cx = current.x;
            int cy = current.y;
            if (gameField.field[cy][cx].getBlock() == null) {
                gameField.field[cy][cx].setBlock(new DefaultOneBlock(cx, cy));
            }
        }
    }


    @Override
    public boolean hasRelation() {
        return !getRelationList().isEmpty();
    }

    /**
     * 移動できるかどうか。
     *     注意：このメソッドは移動できなかった場合、副作用があります（処理の速度的に多少早くなるため）
     * @param gameField フィールド
     * @param x 現在のX軸
     * @param y 現在のY軸
     * @param addX 移動X軸の距離。移動無しなら0。マイナス可
     * @param addY 移動Y軸の距離。移動無しなら0。マイナス可
     * @param isStartPositionRightBottom 右下から判定するならTRUE。FALSEなら左上から
     * @return 移動できるならTRUE
     */
    protected boolean canMove(GameField gameField, int x, int y, int addX, int addY, boolean isStartPositionRightBottom) {
        if (!hasRelation()) {
            throw new RuntimeException("グループがあるブロックの実装なのでぇ。。。");
        }
        PointView pointView = gameField.field[y][x];
        if (!pointView.getBlock().hasRelation()) {
            return false;
        }
        if (!pointView.getBlock().isActivate()) {
            return false;
        }

        // TODO 何故かキャストしないとObject認定される よくわかってない。多分getBlock()で戻りの型をちゃんと定義してないからかな
        Block block = (Block)pointView.getBlock(); // この時点で確定じゃないとおかしい
        List<Point> absoluteGroup = new ArrayList<>();
        Relation relation = Common.cast(pointView.getBlock().getRelationList().get(block.getVersion()));
        for (int[] r : relation.relative) {
            absoluteGroup.add(new Point(x + r[1], y + r[0]));
        }
        absoluteGroup.add(new Point(x, y)); // 指定された点も入れておく

        boolean allStop = false;
        for (Point point : absoluteGroup) {
            int nextX = point.x + addX;
            int nextY = point.y + addY;
            if (nextX < 0 || nextX >= gameField.col || nextY < 0 || nextY >= gameField.row) {
                allStop = true;
                break;
            }
            if (gameField.field[nextY][nextX].isDefaultOneBlock()) {
                continue; // 当然OK
            }
            Point wk = new Point(nextX, nextY);
            if (absoluteGroup.contains(wk)) {
                continue; // 今動かそうとしているやつなのでOKでしょう。多分 TODO
            }
            // 1個でもダメならもうダメ
            allStop = true;
            break;
        }
        if (allStop) {
            // でも横軸の移動なら停止はしないことにする。クソコード
            if (addX == 0) {
                absoluteGroup.stream().forEach(p -> gameField.field[p.y][p.x].getBlock().setActivate(false));
            }
            return false;
        }
        return true;
    }

    /**
     * 移動します。
     * @param gameField フィールド
     * @param x 現在のX軸
     * @param y 現在のY軸
     * @param addX 移動X軸の距離。移動無しなら0。マイナス可
     * @param addY 移動Y軸の距離。移動無しなら0。マイナス可
     * @param isStartPositionRightBottom 右下から判定するならTRUE。FALSEなら左上から
     */
    protected void move(GameField gameField, int x, int y, int addX, int addY, boolean isStartPositionRightBottom) {
        Block block = (Block)gameField.field[y][x].getBlock(); // この時点で確定じゃないとおかしい
        List<Point> group = new ArrayList<>();
        Relation relation = Common.cast(gameField.field[y][x].getBlock().getRelationList().get(block.getVersion()));
        for (int[] r : relation.relative) {
            group.add(new Point(x + r[1], y + r[0]));
        }
        group.add(new Point(x, y)); // 指定された点も入れておく

        // TODO このあたりの分岐なくすために回転と同じようなロジックにした方が良いかも。速度との兼ね合いか
        if (isStartPositionRightBottom) {
            group = group.stream().sorted(Comparator.comparing(Point::y).thenComparing(Point::x).reversed()).collect(Collectors.toCollection(ArrayList::new));
        } else {
            group = group.stream().sorted(Comparator.comparing(Point::y).thenComparing(Point::x)).collect(Collectors.toCollection(ArrayList::new));
        }

        for (Point point : group) {
            int currentX =  point.x;
            int currentY =  point.y;

            OneBlock current = gameField.field[currentY][currentX].getBlock();
            current.setX(current.getX() + addX);
            current.setY(current.getY() + addY);
            gameField.field[currentY + addY][currentX + addX].setBlock(current);
            gameField.field[currentY][currentX].setBlock(new DefaultOneBlock(currentX, currentY));
        }
    }
}
