package jp.co.souki.model.blocks.appendix;

import jp.co.souki.model.blocks.base.Block;

import java.awt.*;
import java.util.List;

/**
 * PuyoPuyoのブロックをモデリングします
 * <pre>
 *      .
 *      .
 * </pre>
 */
public class PuyoBlock extends Block {
    private static Color myColor = new Color(64, 192, 164);
    public static final List<Relation> relationList = Relation.of(new int[][][] {
            {
//                  { 0,  0},
                    { 1,  0},
            },
            {
                    { 0, -1},
//                  { 0,  0},
            },
            {
                    {-1,  0},
//                  { 0,  0},
            },
            {
//                  { 0,  0},
                    { 0,  1},
            },
    });

    public PuyoBlock (int x, int y) {
        super(x, y, myColor);
    }

    @Override
    public List<Relation> getRelationList() {
        return relationList;
    }
}
