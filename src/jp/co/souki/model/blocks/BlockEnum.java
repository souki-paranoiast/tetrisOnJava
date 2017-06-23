package jp.co.souki.model.blocks;

import jp.co.souki.model.blocks.appendix.DiamondBlock;
import jp.co.souki.model.blocks.appendix.GrowthBlock;
import jp.co.souki.model.blocks.appendix.PuyoBlock;
import jp.co.souki.model.blocks.base.OneBlock;
import jp.co.souki.model.blocks.normal.*;
import jp.co.souki.util.Common;

import java.util.Arrays;
import java.util.List;

public enum BlockEnum {
    /** 通常 */
    NORMAL(JBlock.class, LBlock.class, SBlock.class, SquareBlock.class, TBlock.class, TetrisBlock.class, ZBlock.class),

    // TODO 特殊系が多いとゲームにならないので重みづけとか必要かも
//    /** 通常 + α */
//    APPENDIX(JBlock.class, LBlock.class, SBlock.class, SquareBlock.class, TBlock.class, TetrisBlock.class, ZBlock.class, DiamondBlock.class, PuyoBlock.class, GrowthBlock.class),
    /** 通常 + α（重みづけ） */
    APPENDIX(JBlock.class, JBlock.class
            , LBlock.class, LBlock.class
            , SBlock.class, SBlock.class
            , SquareBlock.class, SquareBlock.class
            , TBlock.class, TBlock.class
            , TetrisBlock.class, TetrisBlock.class
            , ZBlock.class, ZBlock.class
            , DiamondBlock.class
            , PuyoBlock.class, PuyoBlock.class
            , GrowthBlock.class
    ),
    ;
    public final List<? extends OneBlock> clazzList;
    private BlockEnum(Class<? extends OneBlock> ... clazzAry) {
        clazzList = Arrays.asList(Common.cast(clazzAry));
    }
}
