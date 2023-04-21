package com.dr.libloc.locator;

import com.dr.libloc.sensor.DRSensorMgr;

public class LocatorMgr {
    private static Locator locator;

    public static void createLocator_RFID(){
        LocatorMgr.locator = new Locator_RFID();
    }

    public static Locator getLocator() {
        return locator;
    }

}
