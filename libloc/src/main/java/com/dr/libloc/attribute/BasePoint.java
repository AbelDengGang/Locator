package com.dr.libloc.attribute;

public class BasePoint {
    // 绘画伸缩倍数
    private float scaleY = 1f;
    private float scaleX = 1f;
    private float scaleZ = 1f;
    // 画布基础点
    private float x = 0;
    private float y = 0;
    private float z = 0;

    @Override
    public String toString() {
        return "basePoint{" +
                "scaleY=" + scaleY +
                ", scaleX=" + scaleX +
                ", scaleZ=" + scaleZ +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }

    public float getScaleY() {
        return scaleY;
    }

    public void setScaleY(float scaleY) {
        this.scaleY = this.scaleY*scaleY;
    }

    public float getScaleX() {
        return scaleX;
    }

    public void setScaleX(float scaleX) {
        this.scaleX = this.scaleX*scaleX;
    }

    public float getScaleZ() {
        return scaleZ;
    }

    public void setScaleZ(float scaleZ) {
        this.scaleZ = scaleZ;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public float getCX() {
        return x*100;
    }

    public float getCY() {
        return y*100;
    }

    public float getCZ() {
        return z*100;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }
}
