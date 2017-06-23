package jp.co.souki.model.blocks.normal;

import jp.co.souki.model.blocks.base.Block;

import java.awt.*;
import java.util.List;

/**
 * 四角型のブロックをモデリングします
 * <pre>
 *     ..
 *     ..
 * </pre>
 */
public class SquareBlock extends Block<SquareBlock> {

    public static final List<Relation> relationList = Relation.of(new int[][][] {
            {
//                  { 0,  0},
                    { 0,  1},
                    { 1,  0},
                    { 1,  1},
            },
    });

    public SquareBlock (int x, int y) {
        super(x, y, Color.YELLOW);
    }

    @Override
    public List<Relation> getRelationList() {
        return relationList;
    }
}
