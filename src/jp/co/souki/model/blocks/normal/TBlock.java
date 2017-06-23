package jp.co.souki.model.blocks.normal;

import jp.co.souki.model.blocks.base.Block;

import java.awt.*;
import java.util.List;

/**
 * T字型のブロックをモデリングします
 * <pre>
 *      .
 *     ...
 * </pre>
 */
public class TBlock extends Block<TBlock> {

    public static final List<Relation> relationList = Relation.of(new int[][][] {
            {
                    // 上向き
                    { 0, -1},
//                  { 0,  0},
                    {-1, -0},
                    { 0,  1},
            },
            {
                    // 右向き
                    {-1,  0},
//                  { 0,  0},
                    { 1,  0},
                    { 0,  1},
            },
            {
                    // 下向き
                    { 0,  1},
//                  { 0,  0},
                    { 1,  0},
                    { 0, -1},
            },
            {
                    // 左向き
                    { 1,  0},
//                  { 0,  0},
                    { 0, -1},
                    {-1,  0},
            },
    });

    public TBlock (int x, int y) {
        super(x, y, Color.MAGENTA);
    }

    @Override
    public List<Relation> getRelationList() {
        return relationList;
    }
}
