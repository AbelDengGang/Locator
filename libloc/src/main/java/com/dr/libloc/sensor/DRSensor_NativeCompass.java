package com.dr.libloc.sensor;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.SystemClock;
import android.util.Log;

import com.dr.libloc.comm.COMM;

import java.util.List;

public class DRSensor_NativeCompass extends DRSensor implements SensorEventListener {

    private String TAG = DRSensor_NativeCompass.class.toString();
    private float[] mAcceValues;  //加速度变更值的数组
    private float[] mMagnValues;  //磁场强度变更值的数组
    private float[] values;
    private long timeMs;
    private SensorManager mSensorMgr;// 声明一个传感管理器对象
    private boolean isregSensorCompass;

    public DRSensor_NativeCompass(Activity activity, String type, String name) {
        super(activity, "COMPASS", name);
        timeMs = 0;
        mSensorMgr = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        isregSensorCompass = false;
    }
    // 计算指南针的方向
    private void calculateOrientation() {
        values = new float[3];
        float[] R = new float[9];
        SensorManager.getRotationMatrix(R, null, mAcceValues, mMagnValues);
        SensorManager.getOrientation(R, values);
        values[0] = (float) Math.toDegrees(values[0]);
        SensorData_NATIVE_COMPASS data = new SensorData_NATIVE_COMPASS(values, timeMs);
        Log.d(TAG, String.format("%d, %d",System.currentTimeMillis(),data.getTimeInMS()));
        notify(data);
        // 设置罗盘视图中的指南针方向
//        if (values[0] >= -10 && values[0] < 10) {
////            tv_direction.setText("手机上部方向是正北");
//            direction = "正北";
//        } else if (values[0] >= 10 && values[0] < 80) {
////            tv_direction.setText("手机上部方向是东北");
//            direction = "东北";
//        } else if (values[0] >= 80 && values[0] <= 100) {
////            tv_direction.setText("手机上部方向是正东");
//            direction = "正东";
//        } else if (values[0] >= 100 && values[0] < 170) {
////            tv_direction.setText("手机上部方向是东南");
//            direction = "东南";
//        } else if ((values[0] >= 170 && values[0] <= 180)
//                || (values[0]) >= -180 && values[0] < -170) {
////            tv_direction.setText("手机上部方向是正南");
//            direction = "正南";
//        } else if (values[0] >= -170 && values[0] < -100) {
////            tv_direction.setText("手机上部方向是西南");
//            direction = "西南";
//        } else if (values[0] >= -100 && values[0] < -80) {
////            tv_direction.setText("手机上部方向是正西");
//            direction = "正西";
//        } else if (values[0] >= -80 && values[0] < -10) {
////            tv_direction.setText("手机上部方向是西北");
//            direction = "西北";
//        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) { // 加速度变更事件
            mAcceValues = event.values;
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) { // 磁场强度变更事件
            mMagnValues = event.values;
        }
        if (mAcceValues != null && mMagnValues != null) {
            long currentUTC_ms = System.currentTimeMillis();
            long currentNano = SystemClock.elapsedRealtimeNanos();
            long offsetMS = (currentNano - event.timestamp) / 1_000_000L;
            timeMs = currentUTC_ms -  offsetMS;
            calculateOrientation(); // 加速度和磁场强度两个都有了，才能计算磁极的方向
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void start() {
        if(!isregSensorCompass){
            resume();
            isregSensorCompass = true;
        }
    }

    @Override
    public void stop() {
        if(isregSensorCompass){
            pause();
            isregSensorCompass = false;
        }
    }

    @Override
    public void pause() {
        if(mSensorMgr != null){
            mSensorMgr.unregisterListener(this);
        }
    }

    @Override
    public void resume() {
        if(mSensorMgr != null){
            int suitable = 0;
            // 获取当前设备支持的传感器列表
            List<Sensor> sensorList = mSensorMgr.getSensorList(Sensor.TYPE_ALL);
            for (Sensor sensor : sensorList) {
                if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) { // 找到加速度传感器
                    suitable += 1;
                } else if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) { // 找到磁场传感器
                    suitable += 10;
                }
            }
            if (suitable / 10 > 0 && suitable % 10 > 0) {
                // 给加速度传感器注册传感监听器
                mSensorMgr.registerListener(this,
                        mSensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                        SensorManager.SENSOR_DELAY_NORMAL);
                // 给磁场传感器注册传感监听器
                mSensorMgr.registerListener(this,
                        mSensorMgr.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                        SensorManager.SENSOR_DELAY_NORMAL);
            }
        }
    }

    @Override
    public void onNewData(COMM sender, byte[] data) {

    }

    @Override
    public void onCommEvent(COMM sender, Exception e, String msg, int code) {

    }
}
