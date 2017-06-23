package jp.co.souki.model.blocks.base;

import jp.co.souki.model.GameField;
import jp.co.souki.util.Point;

import java.util.List;

/**
 * ブロックのグループに属するその他ポイント
 */
public class SubOneBlock<PARENT extends OneBlock> extends OneBlock {
    private final PARENT parent;
    public SubOneBlock(int x, int y, PARENT parent) {
        super(x, y, parent.getColor());
        this.parent = parent;
        super.setActivate(false);
    }

    public OneBlock getParent() {
        return parent;
    }

    @Override
    public boolean hasRelation() {
        return false;
    }
    @Override
    public List<Point> getRelationList() {
        throw new RuntimeException("グループ持ってないってばぁ");
    }

    @Override
    public boolean canMoveDown(GameField gameField, int x, int y) {
        // こいつ自身では動かない
        return false;
    }

    @Override
    public void moveDown(GameField gameField, int x, int y) {
        throw new RuntimeException("動きません");
    }

    @Override
    public boolean canMoveRight(GameField gameField, int x, int y) {
        return false;
    }

    @Override
    public void moveRight(GameField gameField, int x, int y) {
        throw new RuntimeException("動きません");
    }

    @Override
    public boolean canMoveLeft(GameField gameField, int x, int y) {
        return false;
    }

    @Override
    public void moveLeft(GameField gameField, int x, int y) {
        throw new RuntimeException("動きません");
    }

    @Override
    public boolean canAction(GameField gameField, int x, int y) {
        return false;
    }

    @Override
    public void executeAction(GameField gameField, int x, int y) {
        throw new RuntimeException("動きません");
    }

}
