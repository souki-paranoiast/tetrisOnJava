package jp.co.souki.model;

import java.awt.*;
import java.util.List;

/**
 * Lブロックをモデリングします。
 * <pre>
 *     .
 *     .
 *     ..
 * </pre>
 */
public class LBlock extends Block<LBlock> {
    public static final List<Relation> relationList = Relation.of(new int[][][] {
            {
                    {-2,  0},
                    {-1,  0},
//                  { 0,  0},
                    { 0,  1},
            },
            {
//                  { 0,  0},
                    { 0,  1},
                    { 0,  2},
                    { 1,  0},
            },
            {
                    { 0, -1},
//                  { 0,  0},
                    { 1,  0},
                    { 2,  0},
            },
            {
                    {-1,  0},
                    { 0, -2},
                    { 0, -1},
//                  { 0,  0},
            },
    });
    public LBlock (int x, int y) {
        super(x, y, Color.ORANGE);
    }

    @Override
    public List<Relation> getRelationList() {
        return relationList;
    }
}
