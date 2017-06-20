package jp.co.souki.view;

import jp.co.souki.model.DefaultOneBlock;
import jp.co.souki.model.OneBlock;

import javax.swing.JLabel;
import javax.swing.border.BevelBorder;

public class PointView<BLOCKS extends OneBlock> extends JLabel {
    public final int id;
    private BLOCKS block;
    public PointView(int id, BLOCKS block) {
        this.id = id;
        this.block = block;
        setBorder(new BevelBorder(BevelBorder.RAISED));
        setOpaque(true);
        setText(String.valueOf(id));
        repaintBackground();
    }

    public void repaintBackground() {
        setBackground(block.getColor());
        repaint();
    }

    public BLOCKS getBlock() {
        return block;
    }
    public void setBlock(BLOCKS block) {
        this.block = block;
    }
    /** デフォルトブロック（背景）であるかどうかを返します */
    public boolean isDefaultOneBlock() {
        return block instanceof DefaultOneBlock;
    }

    @Override public String toString() {
        return String.format("x:%2s, y:%2s, act:%2s", block.getX(), block.getY(), block.isActivate() ? "〇" : "×");
    }
}
