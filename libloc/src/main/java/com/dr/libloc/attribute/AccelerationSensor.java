package com.dr.libloc.attribute;

import java.util.Arrays;

public class AccelerationSensor {
    private float x;
    private float y;
    private float z;
    private float[] values;


    @Override
    public String toString() {
        return "AccelerationSensor{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", values=" + Arrays.toString(values) +
                '}';
    }

    public float[] getValues() {
        return values;
    }

    public void setValues(float[] values) {
        this.values = values;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }
}
