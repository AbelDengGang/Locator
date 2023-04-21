package com.dr.libloc.attribute;

public class SensorRecorderItem {

    private long timeInMS;
    private String ID;
    private String type;
    private String name;
    private String data;
    private String RSSI;
    private int antChan;
    private int waitTime = 0;

    public long getTimeInMS() {
        return timeInMS;
    }

    public void setTimeInMS(long timeInMS) {
        this.timeInMS = timeInMS;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "SensorRecorderItem{" +
                "timeInMS=" + timeInMS +
                ", ID='" + ID + '\'' +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", data='" + data + '\'' +
                ", RSSI='" + RSSI + '\'' +
                ", antChan=" + antChan +
                ", waitTime=" + waitTime +
                '}';
    }

    public String getRSSI() {
        return RSSI;
    }

    public void setRSSI(String RSSI) {
        this.RSSI = RSSI;
    }

    public int getAntChan() {
        return antChan;
    }

    public void setAntChan(int antChan) {
        this.antChan = antChan;
    }

    public int getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }
}
