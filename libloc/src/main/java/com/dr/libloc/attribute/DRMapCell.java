package com.dr.libloc.attribute;

public class DRMapCell {
    private float bx; // begin x position in map
    private float by; // begin y position in map
    private static float cellSize = 0.1f;
    private float probability;

    public DRMapCell() {
        probability = 0.0f;
    }

    public float getCmBx() {
        return bx*100;
    }

    public void setBx(float bx) {
        this.bx = bx;
    }

    public float getBx() {
        return bx;
    }

    public float getBy() {
        return by;
    }

    public float getCmBy() {
        return by*100;
    }

    public void setBy(float by) {
        this.by = by;
    }

    public static float getCellSize() {
        return cellSize;
    }

    public static float getCmCellSize() {
        return cellSize*100;
    }

    public static void setCellSize(float cellSize) {
        DRMapCell.cellSize = cellSize;
    }

    public float getProbability() {
        return probability;
    }

    public void setProbability(float probability) {
        this.probability = probability;
    }
}
