package jp.co.souki.model.blocks.appendix;

import jp.co.souki.model.blocks.base.Block;

import java.awt.*;
import java.util.List;

/**
 * ダイアモンドのブロックをモデリングします
 * <pre>
 *      .
 *     . .
 *      .
 * </pre>
 */
public class DiamondBlock extends Block {
    private static Color myColor = new Color(228, 228, 255);
    public static final List<Relation> relationList = Relation.of(new int[][][] {
            {
                    // 回転無しとする（でも基準点は一応一番上
//                  { 0,  0},
                    { 1, -1},
                    { 2,  0},
                    { 1,  1},
            },
    });

    public DiamondBlock (int x, int y) {
        super(x, y, myColor);
    }

    @Override
    public List<Relation> getRelationList() {
        return relationList;
    }
}
