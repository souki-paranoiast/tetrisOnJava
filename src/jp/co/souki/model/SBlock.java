package jp.co.souki.model;

import java.awt.*;
import java.util.List;

/**
 * Sブロックをモデリングします。
 * <pre>
 *     ..
 *    ..
 * </pre>
 */
public class SBlock extends Block<SBlock> {
    public static final List<Relation> relationList = Relation.of(new int[][][] {
            {
                    {-1,  0},
                    {-1,  1},
                    { 0, -1},
//                  { 0,  0},
            },
            {
                    {-1,  0},
//                  { 0,  0},
                    { 0,  1},
                    { 1,  1},
            },
    });
    public SBlock(int x, int y) {
        super(x, y, Color.GREEN);
    }

    @Override
    public List<Relation> getRelationList() {
        return relationList;
    }
}
