package jp.co.souki.model;

import jp.co.souki.util.Common;

import java.awt.*;
import java.util.Collections;
import java.util.List;

/**
 * 背景として扱われます
 */
public final class DefaultOneBlock extends OneBlock<DefaultOneBlock> {
    public DefaultOneBlock(int x, int y) {
        super(x, y, Color.GRAY);
        super.setActivate(false);
    }

    @Override
    public List<Relation> getRelationList() {
        return Collections.emptyList();
    }

    @Override
    public boolean canAction(GameField gameField, int x, int y) {
        return false;
    }

    @Override
    public void executeAction(GameField gameField, int x, int y) {
        throw new RuntimeException("動きません");
    }

    @Override
    public boolean isActivate() {
        return false;
    }

    @Override
    public void setActivate(boolean isActivate) {
        Common.debug("設定しません");
    }
}
