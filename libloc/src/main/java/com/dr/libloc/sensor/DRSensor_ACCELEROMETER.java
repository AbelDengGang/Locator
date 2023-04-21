package com.dr.libloc.sensor;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.dr.libloc.comm.COMM;

import java.util.Date;

import static android.content.Context.SENSOR_SERVICE;

public class DRSensor_ACCELEROMETER extends DRSensor implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mLinearAccelerometer;
    private Sensor mGyroscope;
    private Sensor mGravity;

    // 加速度传感器坐标系, 原点在安卓手机左下角,X轴向右为正，Y轴向上为正
    //
    //   ^ y
    //   |
    //   |
    //   |
    //   |----HEME BUTTON------> X



    public float[] getCalibration() {
        return calibration;
    }

    public void setCalibration(float Gx, float Gy, float Gz) {
        calibration[0] = Gx;
        calibration[1] = Gy;
        calibration[2] = Gz;
    }

    private float calibration[] = new float[3]; // calibration data for static
                                    // calibration[0]: Acceleration minus Gx on the x-axis
                                    // calibration[1]: Acceleration minus Gy on the y-axis
                                    // calibration[2]: Acceleration minus Gz on the z-axis
    static int calibNum = 10;
    private int calibCnt = 0;
    private float calibBuff[][] = new float[calibNum][3];
    private static final String TAG = DRSensor_ACCELEROMETER.class.toString();

    public void calibrateInit(){
        calibCnt = 0;
    };

    private void doCalib(){
        float totalX = 0;
        float totalY = 0;
        float totalZ = 0;
        for (float[] sample:calibBuff){
            totalX += sample[0];
            totalY += sample[1];
            totalZ += sample[2];
        }
        calibration[0] = totalX / calibNum;
        calibration[1] = totalY / calibNum;
        calibration[2] = totalZ / calibNum;
    }

    public DRSensor_ACCELEROMETER(Activity activity, String name) {
        super(activity, "ACCELEROMETER", name);
        calibration[0] = 0.0f;
        calibration[1] = 0.0f;
        calibration[2] = 0.0f;
        mSensorManager = (SensorManager)activity.getSystemService(SENSOR_SERVICE);

        mLinearAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        if (mLinearAccelerometer == null){
            Log.e(TAG, "Can't get LINEAR_ACCELERATION sensor!!!!!!");
        }

        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (mAccelerometer == null) {
            Log.e(TAG, "Can't get ACCELEROMETER sensor!!!!!!");
        }

        mGyroscope =   mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        if (mGyroscope == null){
            Log.e(TAG, "Can't get GYROSCOPE sensor!!!!!!");
        }

        mGravity =   mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        if (mGravity == null){
            Log.e(TAG, "Can't get GRAVITY sensor!!!!!!");
        }
    }

    @Override
    public void start() {
        resume();
    }

    @Override
    public void stop() {
        pause();
    }

    @Override
    public void pause() {
        if (mSensorManager != null) {

            mSensorManager.unregisterListener(this);
        }
    }

    @Override
    public void resume() {
        if (mSensorManager != null) {
            if (mLinearAccelerometer != null) {
                calibrateInit();
                mSensorManager.registerListener(this, mLinearAccelerometer,
                        SensorManager.SENSOR_DELAY_FASTEST);
            }


            // try to use LINEAR_ACCELERATION firstly,
            // if no LINEAR_ACCELERATION in system, use ACCELEROMETER
            // ACCELEROMETER need remove gravity
            if (mLinearAccelerometer == null) {
                if (mAccelerometer != null) {
                    calibrateInit();
                    mSensorManager.registerListener(this, mAccelerometer,
                            SensorManager.SENSOR_DELAY_FASTEST);
                }
            }

            if (mGyroscope != null){
                mSensorManager.registerListener(this, mGyroscope,
                        SensorManager.SENSOR_DELAY_FASTEST);
            }

            if (mGravity != null){
                mSensorManager.registerListener(this, mGyroscope,
                        SensorManager.SENSOR_DELAY_FASTEST);
            }
        }
    }

    @Override
    public void onNewData(COMM sender, byte[] data) {
    }

    @Override
    public void onCommEvent(COMM sender, Exception e, String msg, int code) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        Log.d(TAG,String.format(" %s : %d", sensor.getStringType(),sensor.getType()));
        switch(sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER: {
                Log.d(TAG, String.format("Before calib: Gx:%f,Gy:%f,Gz:%f",
                        event.values[0], event.values[1], event.values[2]));
                if (calibCnt < calibNum) {
                    calibBuff[calibCnt][0] = event.values[0];
                    calibBuff[calibCnt][1] = event.values[1];
                    calibBuff[calibCnt][2] = event.values[2];
                    calibCnt++;
                    if (calibCnt == calibNum) {
                        doCalib();
                    }
                    return;
                }
                event.values[0] = event.values[0] - calibration[0];
                event.values[1] = event.values[1] - calibration[1];
                event.values[2] = event.values[2] - calibration[2];
                Log.d(TAG, String.format("After calib: Gx:%f,Gy:%f,Gz:%f",
                        event.values[0], event.values[1], event.values[2]));
                SensorData_ACCELEROMETER data = new SensorData_ACCELEROMETER(event);
                Log.d(TAG, String.format("%d, %d",System.currentTimeMillis(),data.getTimeInMS()));
                notify(data);
            }
                break;
            case Sensor.TYPE_GYROSCOPE:
                break;
            case Sensor.TYPE_LINEAR_ACCELERATION: {
                    Log.d(TAG, String.format("Ax:%f,Ay:%f,Az:%f",
                            event.values[0], event.values[1], event.values[2]));
                    SensorData_ACCELEROMETER data = new SensorData_ACCELEROMETER(event);
                    Log.d(TAG, String.format("%d, %d",System.currentTimeMillis(),data.getTimeInMS()));
                    notify(data);
            }
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public boolean isLinearAccelerationSupport(){
        if (mLinearAccelerometer != null){
            return true;
        }
        return false;
    }

    public boolean isAccelerationSupport(){
        if (mAccelerometer != null){
            return true;
        }
        return false;
    }

    public boolean isGravitySupport(){
        if (mGravity != null){
            return true;
        }
        return false;
    }

    public boolean isGyroscopeupport(){
        if (mGyroscope != null){
            return true;
        }
        return false;
    }

}
