package com.dr.libloc.sensor;


import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.OrientationEventListener;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.SENSOR_SERVICE;
import static java.lang.Thread.sleep;

public class DRSensorMgr {
    private static DRSensorMgr instance;
    private Activity activity;
    private HashMap< String, DRSensor> sensors;
    private DRSensor_RFID_WULIANKJ_R200 rfidSensor;
    private DRSensor_NativeCompass nativeCompassSensor;
    private DRSensor_ACCELEROMETER accSensor;
    public static int orientation; // orientation > 350 || orientation < 10 :  0度：手机默认竖屏状态（home键在正下方）
                                    // orientation > 80 && orientation < 100 ;  90度：手机顺时针旋转90度横屏（home建在左侧）
                                    // orientation > 170 && orientation < 190 ;  180度：手机顺时针旋转180度竖屏（home键在上方）
                                    // orientation > 260 && orientation < 280 ;  270度：手机顺时针旋转270度横屏，（home键在右侧）

    private static final String TAG = DRSensorMgr.class.toString();

    private DRSensorMgr(Activity activity){
        this.activity = activity;
    }
    public static void createMgr(Activity activity){
        if(DRSensorMgr.instance != null) return;
        DRSensorMgr.instance = new DRSensorMgr(activity);
        instance.sensors = new HashMap<String,DRSensor>();
        DRSensorMgr.orientation = activity.getRequestedOrientation();
        OrientationEventListener orientationEventListener = new OrientationEventListener(activity) {
            @Override
            public void onOrientationChanged(int orientation) {
                DRSensorMgr.orientation = orientation;
            }
        };
        instance.createRFIDSensor();
        instance.createACCSensor();

    }

    public void addSensor(DRSensor sensor){
        String key = sensor.getType()+":"+sensor.getDRName();
        if(sensors.containsKey(key)){
            Log.e(TAG, key + "already in list!");
            return;
        }
        sensors.put(key,sensor);
    }

    public DRSensor getSensor(String type, String name){
        String key = type+":"+name;
        return sensors.get(key);
    }

    public Collection<DRSensor> getSensors(){
        return sensors.values();
    }

    private void createACCSensor(){
        accSensor = new DRSensor_ACCELEROMETER(activity,"ACCELEROMETER");
        addSensor(accSensor);
        accSensor.start();
    }

    public DRSensor_ACCELEROMETER getACCSensor(){
        return accSensor;
    }

    private void createRFIDSensor(){
        rfidSensor = new DRSensor_RFID_WULIANKJ_R200(activity,"WULIANKJ_R200");
        addSensor(rfidSensor);
        TimerTask rfid_startTask = new TimerTask() {
            @Override
            public void run() {
                // 读卡器启动需要一点时间，因此需要稍微等一下
                rfidSensor.start();
                try {
                    Thread.sleep(500);
                }
                catch(InterruptedException e)
                {
                    e.printStackTrace();
                }
                rfidSensor.sendReadMultiFrame(65535);
            }
        };
        Timer rfid_startTimer = new Timer();
        rfid_startTimer.schedule(rfid_startTask, 1000);
    }

    public DRSensor_RFID_WULIANKJ_R200 getRFIDSensor(){
        return rfidSensor;
    }

    public DRSensor_NativeCompass getCompassSensor(){
        // TODO check if support compass sensor
        // if not support , return null


        if (nativeCompassSensor != null) {
            return nativeCompassSensor;
        }

        nativeCompassSensor = new DRSensor_NativeCompass(activity, "COMPASS", "COMPASS");
        return nativeCompassSensor;
    }

    public static DRSensorMgr getMgr(){
        if (DRSensorMgr.instance == null){
            Log.e(TAG,"You must call DRSensorMgr.createMgr before call DRSensorMgr.getMgr!!!");
            return DRSensorMgr.instance;
        }
        return DRSensorMgr.instance;
    }
    public static List<Sensor> getSysSensor(Activity activity) {
        SensorManager sensorManager = (SensorManager) activity.getSystemService(SENSOR_SERVICE);

        List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
        Log.d("Sensor", "sensors " + sensors.toString());

        for (Sensor sensor : sensors) {
            Log.d("Sensor", getSensorStringType(sensor.getType()) );
        }

        return sensors;
    }

    public static String getSensorStringType(int typeCode){
        Field[] _fields=Sensor.class.getFields();
        for (Field _field : _fields) {
            try {
                if(_field.getGenericType().equals(int.class)
                        &&typeCode==_field.getInt(null)){
                    return  _field.getName();
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return  "Not Find Sensor Name TypeCode="+typeCode;
    }
}
