package jp.co.souki.model.blocks.normal;

import jp.co.souki.model.blocks.base.Block;

import java.awt.*;
import java.util.List;

/**
 * Jブロックをモデリングします。
 * <pre>
 *     .
 *     .
 *    ..
 * </pre>
 */
public class JBlock extends Block<JBlock> {
    public static final List<Relation> relationList = Relation.of(new int[][][] {
            {
                    {-2,  0},
                    {-1,  0},
//                  { 0,  0},
                    { 0, -1},
            },
            {
                    {-1,  0},
//                  { 0,  0},
                    { 0,  1},
                    { 0,  2},
            },
            {
//                  { 0,  0},
                    { 0,  1},
                    { 1,  0},
                    { 2,  0},
            },
            {
                    { 0, -2},
                    { 0, -1},
//                  { 0,  0},
                    { 1,  0},
            },
    });
    public JBlock(int x, int y) {
        super(x, y, Color.BLUE);
    }

    @Override
    public List<Relation> getRelationList() {
        return relationList;
    }
}
