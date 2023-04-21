package com.dr.libloc.locator;


import com.dr.libloc.attribute.MapItem;
import com.dr.libloc.sensor.DRSensor;
import com.dr.libloc.sensor.DRSensor_ACCELEROMETER;
import com.dr.libloc.sensor.DRSensor_RFID_WULIANKJ_R200;
import com.dr.libloc.sensor.SensorData;
import com.dr.libloc.sensor.SensorData_ACCELEROMETER;
import com.dr.libloc.sensor.SensorData_RFID;
import com.rfid_WULIANKEJIR200.cmd.ConstCode;

public class Locator_RFID extends Locator {
    DRSensor_RFID_WULIANKJ_R200 sensorRFID;
    DRSensor_ACCELEROMETER      sensorACC;


    Locate_Impl locateImpl;  // who calculate the position and post
    @Override
    public void calPostionPost() {
        // TODO calculate post and position, and call notify() if updated
    }

    @Override
    public void onSensorUpdate(DRSensor sender, SensorData data) {
        if(data.getType().equals("SensorData_RFID")){
            onRFID((SensorData_RFID)  data);
        }else if(data.getType().equals("ACCELEROMETER")){
            onACCELEROMETER((SensorData_ACCELEROMETER) data);
        }
    }

    private void onRFID(SensorData_RFID data){
        switch (data.getCommandType()){
            // process valid data
            case ConstCode.FRAME_CMD_READ_SINGLE:
            case ConstCode.FRAME_CMD_READ_MULTI:
                if (DRMap != null) {
                    MapItem mapItem = DRMap.getItem(data.getID());
                    if (mapItem != null){
                        RFID_Info rfidInfo = new RFID_Info();
                        rfidInfo.data = data;
                        rfidInfo.mapItem = mapItem;
                        this.locateImpl.addRFIDInfo(rfidInfo);
                    }
                }
                break;
        }
    }

    private void onACCELEROMETER(SensorData_ACCELEROMETER data){
        this.locateImpl.addACCInfo(data);
    }

    public void start(){
        // to init the locator
//        locateImpl = new Locate_RFID_RegionCenter(this);
//        locateImpl = new Locate_RFID_MoveLocate(this);
        locateImpl = new Locate_RFID_orientation(this);
        if(drSensorMgr != null) {
            sensorRFID = drSensorMgr.getRFIDSensor();
            sensorACC = drSensorMgr.getACCSensor();
        }
        resume();
    }
    public  void pause(){

        // regist listerner to sensor
        if (sensorRFID != null) {
            this.sensorRFID.rmListener(this);
        }
        if (sensorACC != null) {
            this.sensorACC.rmListener(this);
        }
    }
    public  void resume(){
        // regist listerner to sensor
        if (sensorRFID != null) {
            this.sensorRFID.regListener(this);
        }
        if (sensorACC != null) {
            this.sensorACC.regListener(this);
        }
    }

    public void stop(){
        //remove listener from sensor
        pause();
    }

}
