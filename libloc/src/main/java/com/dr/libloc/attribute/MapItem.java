package com.dr.libloc.attribute;

import com.dr.libloc.interfaces.MapItemDrawer;

import org.w3c.dom.Element;

import java.util.Map;

public abstract class MapItem implements MapItemDrawer{

    private float width;
    private float heigth;
    private float length;
    private float wight;
    private String type;
    private String name;
    private PositionPost positionPost;
    private BasePoint basePoint;
    private AccelerationSensor accelerationSensor = null;
    private Map<String, DRRoad> drRoadMap;
    private Element attribute;
    private boolean isHighlight = false;
    private String drawColor = "#FFFFFF";
    private String antChan = "0";

    public AccelerationSensor getAccelerationSensor() {
        return accelerationSensor;
    }

    public void setAccelerationSensor(AccelerationSensor accelerationSensor) {
        this.accelerationSensor = accelerationSensor;
    }

    public String getAntChan() {
        return antChan;
    }

    public void setAntChan(String antChan) {
        this.antChan = antChan;
    }

    public String getDrawColor() {
        return drawColor;
    }

    public void setDrawColor(String drawColor) {
        if(isHighlight != true){
            this.drawColor = drawColor;
        }else {
            this.drawColor = "#33ff00";
        }
    }

    public boolean isHighlight() {
        return isHighlight;
    }

    public void setHighlight(boolean highlight) {
        isHighlight = highlight;
    }

    public Element getAttribute() {
        return attribute;
    }

    public void setAttribute(Element attribute) {
        this.attribute = attribute;
    }

    public float getWightMeter() {
        return wight;
    }

    public void setWight(float wight) {
        this.wight = wight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public PositionPost getPositionPost() {
        return positionPost;
    }

    public void setPositionPost(PositionPost positionPost) {
        this.positionPost = positionPost;
    }

    public BasePoint getBasePoint() {
        return basePoint;
    }

    public void setBasePoint(BasePoint basePoint) {
        this.basePoint = basePoint;
    }

    protected MapItem() {
    }

    public float getCWidth() {
        return width*100;
    }

    public float getCHeigth() {
        return heigth*100;
    }

    public float getCLength() {
        return length*100;
    }

    public float getCWight() {
        return wight*100;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setHeigth(float heigth) {
        this.heigth = heigth;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public float getWidth() {
        return width;
    }

    public float getHeigth() {
        return heigth;
    }

    public float getLength() {
        return length;
    }

    public float getWight() {
        return wight;
    }

    @Override
    public String toString() {
        return "MapItem{" +
                "width=" + width +
                ", heigth=" + heigth +
                ", length=" + length +
                ", wight=" + wight +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", positionPost=" + positionPost +
                ", basePoint=" + basePoint +
                ", accelerationSensor=" + accelerationSensor +
                ", drRoadMap=" + drRoadMap +
                ", attribute=" + attribute +
                ", isHighlight=" + isHighlight +
                ", drawColor='" + drawColor + '\'' +
                ", antChan='" + antChan + '\'' +
                '}';
    }

    public Map<String, DRRoad> getDrRoadMap() {
        return drRoadMap;
    }

    public void setDrRoadMap(Map<String, DRRoad> drRoadMap) {
        this.drRoadMap = drRoadMap;
    }
}
