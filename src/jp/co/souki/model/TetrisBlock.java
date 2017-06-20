package jp.co.souki.model;

import java.awt.*;
import java.util.List;

/**
 * テトリスブロックをモデリングします
 * <pre>
 *      .
 *      .
 *      .
 *      .
 * </pre>
 */
public class TetrisBlock extends Block<TetrisBlock> {

    protected List<Relation> relationList = Relation.of(new int[][][] {
            {
                // 縦
                {-1,  0},
//              { 0,  0},
                { 1,  0},
                { 2,  0},
            },
            {
                 // 横
                { 0, -1},
//              { 0,  0},
                { 0,  1},
                { 0,  2},
            }
    });

    public TetrisBlock (int x, int y) {
        super(x, y, Color.CYAN);
    }

    @Override
    public List<Relation> getRelationList() {
        return relationList;
    }
}
