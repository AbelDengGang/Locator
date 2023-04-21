package com.dr.libloc.sensor;

public interface SensorUpdateListener {
    void onSensorUpdate(DRSensor sender, SensorData data);
}
