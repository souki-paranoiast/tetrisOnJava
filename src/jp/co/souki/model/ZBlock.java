package jp.co.souki.model;

import java.awt.*;
import java.util.List;

/**
 * Zブロックをモデリングします。
 * <pre>
 *    ..
 *     ..
 * </pre>
 */
public class ZBlock extends Block<ZBlock> {
    public static final List<Relation> relationList = Relation.of(new int[][][] {
            {
                    {-1,  1},
//                  { 0,  0},
                    { 0,  1},
                    { 1,  0},
            },
            {
                    { 0, -1},
//                  { 0,  0},
                    { 1,  0},
                    { 1,  1},
            },
    });
    public ZBlock(int x, int y) {
        super(x, y, Color.RED);
    }

    @Override
    public List<Relation> getRelationList() {
        return relationList;
    }
}
