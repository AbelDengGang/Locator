package com.dr.libloc.attribute;

import java.util.List;

public class PositionPost {
    // 顺时针旋转角度 平面图只使用xangle
    private float xangle = 0;
    private float yangle = 0;
    private float zangle = 0;
    private float x = 0;
    private float y = 0;
    private float z = 0;
    private long timeMs;
    private List<DRMapCell> positionProbabilityCells = null;

    public String toXML(){
        return "<Track Obj=\"Car\" Time=\"" + timeMs + "\" x=\"" + x + "\" y=\"" + y + "\" z=\"" + z + "\" xangle=\"" + xangle + "\" yangle=\"" + yangle + "\" zangle=\"" + zangle + "\"/>";
    }

    public void fromXML(String track){
        String[] split = track.split(" ");
        for (String str: split){
            String[] spl = str.split("=");
            if(spl[0].equals("Time")){
                timeMs = Long.parseLong(spl[1].split("\"")[1]);
            }else if(spl[0].equals("x")){
                x = Float.parseFloat(spl[1].split("\"")[1]);
            }else if (spl[0].equals("y")){
                y = Float.parseFloat(spl[1].split("\"")[1]);
            }else if(spl[0].equals("z")){
                z = Float.parseFloat(spl[1].split("\"")[1]);
            }else if(spl[0].equals("xangle")){
                xangle = Float.parseFloat(spl[1].split("\"")[1]);
            }else if(spl[0].equals("yangle")){
                yangle = Float.parseFloat(spl[1].split("\"")[1]);
            }else if(spl[0].equals("zangle")){
                zangle = Float.parseFloat(spl[1].split("\"")[1]);
            }
        }
    }

    @Override
    public String toString() {
        return "PositionPost{" +
                "xangle=" + xangle +
                ", yangle=" + yangle +
                ", zangle=" + zangle +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", timeMs=" + timeMs +
                '}';
    }

    public List<DRMapCell> getPositionProbabilityCells() {
        return positionProbabilityCells;
    }

    public void setPositionProbabilityCells(List<DRMapCell> positionProbabilityCells) {
        this.positionProbabilityCells = positionProbabilityCells;
    }

    public long getTimeMs() {
        return timeMs;
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

    public void setTimeMs(long timeMs) {
        this.timeMs = timeMs;
    }

    public float getXangle() {
        return xangle;
    }

    public void setXangle(float xangle) {
        this.xangle = xangle;
    }

    public float getYangle() {
        return yangle;
    }

    public void setYangle(float yangle) {
        this.yangle = yangle;
    }

    public float getZangle() {
        return zangle;
    }

    public void setZangle(float zangle) {
        this.zangle = zangle;
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
}
