package com.dr.libloc.sensor;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.dr.libloc.interfaces.RecorderInterface;
import com.dr.libloc.locator.Locator_RFID;
import com.dr.libloc.mapUtil.SensorRecoderReadXML;
import com.dr.libloc.mapUtil.SensorRecoderSaveXML;
import com.rfid_WULIANKEJIR200.cmd.ConstCode;

import java.util.Collection;


public class SensorRecorder implements SensorUpdateListener, RecorderInterface {

    private String TAG = SensorRecorder.class.toString();
    private Locator_RFID locator_rfid;
    private DRSensor_RFID_WULIANKJ_R200 drwR200;
    private DRSensor_ACCELEROMETER drSensor_accelerometer;
    private SensorRecoderSaveXML sensorRecoderSaveXML;
    private Collection<DRSensor> drSensors;
    private SensorData_RFID dataRFID;
    private SensorData_ACCELEROMETER sensorData_accelerometer;
    private SensorData_NATIVE_COMPASS sensorData_native_compass;
    private Context context;
    private String XMLPath;
    private SensorRecoderReadXML sensorRecoderReadXML;
    private String readXmlPath;
    private boolean buttonPlayBack = false;

    private String timeInMs = null;
    private String type = null;
    private int waitTime = 0;

    private Activity activity;
    // 指南针
    private boolean isCheckboxCompass;
    private DRSensor_NativeCompass drSensorNativeCompass;



    public SensorRecorder(Context context, Activity activity, String XMLPath) {
        this.context = context;
        this.XMLPath = XMLPath;
        this.activity = activity;
        drwR200 = DRSensorMgr.getMgr().getRFIDSensor();
        drSensors = DRSensorMgr.getMgr().getSensors();
        drSensor_accelerometer = DRSensorMgr.getMgr().getACCSensor();
        this.sensorRecoderSaveXML = new SensorRecoderSaveXML(context, XMLPath);
        isCheckboxCompass = false;
        drSensorNativeCompass = DRSensorMgr.getMgr().getCompassSensor();
    }

    @Override
    public void start(){
//        drwR200.regListener(this);
        drSensorNativeCompass.regListener(this);
        drSensorNativeCompass.start();
        for (DRSensor sensor: drSensors) {
            sensor.regListener(this);
        }
    }

    @Override
    public void stop(){
//        drwR200.rmListener(this);
        drSensor_accelerometer.rmListener(this);
        drSensor_accelerometer.stop();
        for (DRSensor sensor: drSensors) {
            sensor.regListener(this);
        }
    }

    private void stringXmlParser(String attrite){
        String[] split = attrite.split(" ");
        for (String str: split){
            String[] spl = str.split("=");
            if(spl[0].equals("type")){
                type = spl[1].split("\"")[1];
            }else if(spl[0].equals("timeInMS")){
                if(timeInMs != null){
                    waitTime =(int) (Long.parseLong(spl[1].split("\"")[1]) - Long.parseLong(timeInMs));
                }
                timeInMs = spl[1].split("\"")[1];
            }
        }
    }

    @Override
    public void playBack() {
//        drwR200.pause();
        for (DRSensor sensor: drSensors) {
            sensor.pause();
        }
        stop();
        String temp = null;
        while ((temp = sensorRecoderReadXML.readRecodeLine())!=null) {
            if(!temp.equals("true")){
                stringXmlParser(temp);
                try {
                    System.out.println(waitTime);
                    if(waitTime > 0){
                        Thread.sleep(waitTime);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(type != null){
                    switch (type){
                        case "RFID":
                            SensorData_RFID sensorData_rfid = new SensorData_RFID();
                            sensorData_rfid.fromXML(temp);
                            drwR200.playbackItem(sensorData_rfid);
                            break;
                        case "ACCELEROMETER":
                            SensorData_ACCELEROMETER sensorData_accelerometer = new SensorData_ACCELEROMETER();
                            sensorData_accelerometer.fromXML(temp);
                            drSensor_accelerometer.playbackItem(sensorData_accelerometer);
                            break;
                        case "COMPASS":
                            SensorData_NATIVE_COMPASS native_compass = new SensorData_NATIVE_COMPASS();
                            native_compass.fromXML(temp);
                            drSensor_accelerometer.playbackItem(native_compass);
                            break;
                    }
                }
            }
        }
        timeInMs = null;
        waitTime = 0;
//        drwR200.resume();
        for (DRSensor sensor: drSensors) {
            sensor.resume();
        }
        start();
    }

    @Override
    public void load() {
        try {
            sensorRecoderReadXML = new SensorRecoderReadXML(readXmlPath);
//            sensorRecoderReadXML.load();
            sensorRecoderReadXML.loadRecode();
        }catch (Exception e){
            Log.e(TAG, "Failed to read file :", e);
        }
    }

    @Override
    public void store() {
//        sensorRecoderSaveXML.saveXMLFile();
        sensorRecoderSaveXML.saveRecorder();
    }

    @Override
    public void onSensorUpdate(DRSensor sender, SensorData data) {
        if(data.getType().equals("SensorData_RFID")){
            dataRFID = (SensorData_RFID)  data;
            commandRFID();
        }else if(data.getType().equals("ACCELEROMETER")){
            sensorData_accelerometer = (SensorData_ACCELEROMETER) data;
            commandACCELEROMETER();
        }else if(data.getType().equals("COMPASS")){
            if(isCheckboxCompass){
                sensorData_native_compass = (SensorData_NATIVE_COMPASS) data;
                commandNATIVECOMPASS();
            }
        }

    }

    public void commandNATIVECOMPASS(){
        try {
            sensorRecoderSaveXML.addRecorder(sensorData_native_compass.toXML());
        }catch (Exception e){
            Log.e(TAG, "Error saving motion trace ", e);
        }
    }

    public void commandRFID(){
        try {
            switch (dataRFID.getCommandType()){
                case ConstCode.FRAME_CMD_READ_SINGLE:
                case ConstCode.FRAME_CMD_READ_MULTI:
                    sensorRecoderSaveXML.addRecorder(dataRFID.toXML());
                    break;
            }
        }catch (Exception e){
            Log.e(TAG, "Error saving motion trace ", e);
        }
    }

    public void commandACCELEROMETER(){
        try {
            sensorRecoderSaveXML.addRecorder(sensorData_accelerometer.toXML());
        }catch (Exception e){
            Log.e(TAG, "Error saving motion trace ", e);
        }
    }

    public void initRecorderFile(){
        sensorRecoderSaveXML.initRecorderFile();
    }

    public String getXMLPath() {
        return XMLPath;
    }

    public void setXMLPath(String XMLPath) {
        this.XMLPath = XMLPath;
        sensorRecoderSaveXML.setXMLPath(XMLPath);
    }

    public SensorRecoderSaveXML getSensorRecoderSaveXML() {
        return sensorRecoderSaveXML;
    }

    public void setSensorRecoderSaveXML(SensorRecoderSaveXML sensorRecoderSaveXML) {
        this.sensorRecoderSaveXML = sensorRecoderSaveXML;
    }

    public String getReadXmlPath() {
        return readXmlPath;
    }

    public void setReadXmlPath(String readXmlPath) {
        this.readXmlPath = readXmlPath;
    }

    public boolean isButtonPlayBack() {
        return buttonPlayBack;
    }

    public void setButtonPlayBack(boolean buttonPlayBack) {
        this.buttonPlayBack = buttonPlayBack;
    }

    public boolean isCheckboxCompass() {
        return isCheckboxCompass;
    }

    public void setCheckboxCompass(boolean checkboxCompass) {
        isCheckboxCompass = checkboxCompass;
    }
}
