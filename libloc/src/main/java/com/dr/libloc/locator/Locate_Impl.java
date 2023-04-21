package com.dr.libloc.locator;

import com.dr.libloc.sensor.SensorData_ACCELEROMETER;

public abstract class Locate_Impl {
    public abstract void addRFIDInfo(RFID_Info rfidInfo);
    public abstract void addACCInfo(SensorData_ACCELEROMETER data);
}
