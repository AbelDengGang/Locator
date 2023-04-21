package com.dr.libloc.sensor;

import android.os.SystemClock;

public class SensorData {
    protected String type;
    protected String DRname;
    protected byte[] data;

    public long getTimeInMS() {
        return timeInMS;
    }

    protected long timeInMS;


    public SensorData(String type, String name) {
        timeInMS = System.currentTimeMillis();
        this.DRname = name;
        this.type = type;

    }

    public String getType() {
        return type;
    }

    /*
    Event.timestamp use same  base as SystemClock.elapsedRealtimeNanos().
    https://developer.android.google.cn/reference/android/hardware/SensorEvent#timestamp
    To convert Event.timestamp, have to calculate the offset of now to Event.timestamp,
    then apply the offset to System.currentTimeMillis();
         | current UTC by call                | Event.timestamp's UTC
         | System.currentTimeMillis()         |
         |-----------offset ------------------|
    <------------------------------------------------------------ boot time
         | currnt nano seconds by call        | nano seconds of
         |SystemClock.elapsedRealtimeNanos(). |Event.timestamp

         offset = (SystemClock.elapsedRealtimeNanos() -  Event.timestamp) / 1_000_000L
         Event.timestamp's UTC = System.currentTimeMillis() - offset

     */
    public long eventTimeToUTCMS(long eventTimeNanos){
        // Capture current timestamp for bose UTC and nano second
        // must call the two function as close as possiable
        long currentUTC_ms = System.currentTimeMillis();
        long currentNano = SystemClock.elapsedRealtimeNanos();
        long offsetMS = (currentNano - eventTimeNanos) / 1_000_000L;
        long eventTimeUTC = currentUTC_ms -  offsetMS;

        return eventTimeUTC;
    }
    public String getDRName() {
        return DRname;
    }

    public byte[] getData() {
        return data;
    }

    public String toXML(){return "<item/>";};

    public void fromXML(String xmlItem){};
}
