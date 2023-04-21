package com.dr.libloc.attribute;

public class DRRoadVertex {
    float x;
    float y;
    float z;
    String name;


    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
