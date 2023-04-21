package com.dr.libloc.attribute;

public class CanvasArguments {

    // 滑动平移量
    private float fx = 0;
    private float fy = 0;
    // 地图的缩放
    private float scale = 1f;
    // 缩放点（两手指中心点）
    private float scx = 0;
    private float scy = 0;

    public float getFx() {
        return fx;
    }

    public void setFx(float fx) {
        this.fx = fx;
    }

    public float getFy() {
        return fy;
    }

    public void setFy(float fy) {
        this.fy = fy;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = this.scale*scale;
    }

    public float getScx() {
        return scx;
    }

    public void setScx(float scx) {
        this.scx = scx;
    }

    public float getScy() {
        return scy;
    }

    public void setScy(float scy) {
        this.scy = scy;
    }
}
