package com.dr.location;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.dr.libloc.CompassViewer;
import com.dr.libloc.MapViewer;
import com.dr.libloc.UwbViewer;
import com.dr.libloc.attribute.AccelerationSensor;
import com.dr.libloc.attribute.MapItem;
import com.dr.libloc.attribute.PositionPost;
import com.dr.libloc.attribute.RefreshStatus;
import com.dr.libloc.drawUtil.MapItemDrawer_self;
import com.dr.libloc.locator.Locator;
import com.dr.libloc.locator.LocatorMgr;
import com.dr.libloc.locator.PositionPostUpdateListener;
import com.dr.libloc.sensor.DRSensor;
import com.dr.libloc.sensor.DRSensorMgr;
import com.dr.libloc.sensor.DRSensor_NativeCompass;
import com.dr.libloc.sensor.DRSensor_RFID_WULIANKJ_R200;
import com.dr.libloc.sensor.SensorData;
import com.dr.libloc.sensor.SensorData_ACCELEROMETER;
import com.dr.libloc.sensor.SensorData_NATIVE_COMPASS;
import com.dr.libloc.sensor.SensorData_RFID;
import com.dr.libloc.sensor.SensorUpdateListener;
import com.rfid_WULIANKEJIR200.cmd.ConstCode;

import java.util.Collection;

public class MapFragment extends Fragment implements PositionPostUpdateListener, SensorUpdateListener {

    private MapViewer mapViewer;
    private Locator locator;
    private DRSensor_RFID_WULIANKJ_R200 drwR200;
    private Collection<DRSensor> drSensors;
    private SensorData_RFID dataRFID;
    private SensorData_ACCELEROMETER sensorData_accelerometer;
    private SensorData_NATIVE_COMPASS sensorData_native_compass;
    private String TAG = MapFragment.class.toString();

    private OrientationEventListener orientationEventListener;

    float currentDistance = 0;
    float lastcurrentDistance = -1;
    float action_down_x;
    float action_down_y;
    float action_up_x = 0;
    float action_up_y = 0;
    boolean pointerboolen = true;
    float x1 = 1,y1 = 1,x2,y2;
    private UwbViewer uwbViewer;
    private CompassViewer directionViewer;
    private DRSensor_NativeCompass compassSensor;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapViewer = new MapViewer(getContext());
        uwbViewer = new UwbViewer(getContext());
        directionViewer = new CompassViewer(getContext());
        FrameLayout linearLayout = new FrameLayout(getContext());
        FrameLayout linearLayout_uwb = new FrameLayout(getContext());
        FrameLayout linearLayout_direction = new FrameLayout(getContext());
        linearLayout.setBackgroundColor(Color.WHITE);
        linearLayout_uwb.setBackgroundColor(Color.WHITE);
        linearLayout_direction.setBackgroundColor(Color.WHITE);
        linearLayout.addView(mapViewer);
        linearLayout_uwb.addView(uwbViewer);
        linearLayout_direction.addView(directionViewer);
        FrameLayout layout = (FrameLayout)view.findViewById(R.id.map_draw);
        FrameLayout uwbDraw = (FrameLayout)view.findViewById(R.id.uwb_draw);
        FrameLayout directLayout = (FrameLayout)view.findViewById(R.id.compass_draw);

        layout.addView(linearLayout);
        uwbDraw.addView(linearLayout_uwb);
        directLayout.addView(linearLayout_direction);

        locator = LocatorMgr.getLocator();
        locator.setDRMap(mapViewer.getDRMap());
        locator.start();

        drwR200 = DRSensorMgr.getMgr().getRFIDSensor();
        drSensors = DRSensorMgr.getMgr().getSensors();
        compassSensor = DRSensorMgr.getMgr().getCompassSensor();
        mapViewerTouchListener();

        orientationEventListener = new OrientationEventListener(getContext()) {
            @Override
            public void onOrientationChanged(int orientation) {
                // 平 放
//                if(orientation == OrientationEventListener.ORIENTATION_UNKNOWN){
//
//                }
                // 只检测是否有四个角度的改变
                if (orientation > 350 || orientation < 10) {
                    // 0度：手机默认竖屏状态（home键在正下方）
//                    Log.d(TAG, "下");
                } else if (orientation > 80 && orientation < 100) {
                    // 90度：手机顺时针旋转90度横屏（home建在左侧）
//                    Log.d(TAG, "左");
                    uwbViewer.setLeft(true);
                } else if (orientation > 170 && orientation < 190) {
                    // 180度：手机顺时针旋转180度竖屏（home键在上方）
//                    Log.d(TAG, "上");
                } else if (orientation > 260 && orientation < 280) {
                    // 270度：手机顺时针旋转270度横屏，（home键在右侧）
//                    Log.d(TAG, "右");
                    uwbViewer.setLeft(false);
                }
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        locator.regPPUpdateListener(this);
        compassSensor.regListener(this);
        compassSensor.start();
//        drwR200.regListener(this);
        for (DRSensor sensor: drSensors) {
            sensor.regListener(this);
        }
        orientationEventListener.enable();
    }

    public void onCompassStop(){
        compassSensor.rmListener(this);
        compassSensor.stop();
    }

    public void onCompassStart(){
        compassSensor.regListener(this);
        compassSensor.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        locator.rmPPUpdateListener(this);
        compassSensor.rmListener(this);
        compassSensor.stop();
//        drwR200.rmListener(this);
        for (DRSensor sensor: drSensors) {
            sensor.rmListener(this);
        }
        orientationEventListener.disable();
        mapViewer.setThread_flag(false);
        uwbViewer.setThread_flag(false);
        directionViewer.setThread_flag(false);
    }

    @Override
    public void onPPUpdate(Locator locator, PositionPost positionPost) {
        MapItem mapItem = new MapItemDrawer_self();
        mapItem.setPositionPost(positionPost);
        mapViewer.setItem(mapItem, RefreshStatus.refresh_map);
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
            sensorData_native_compass = (SensorData_NATIVE_COMPASS)data;
            directionViewer.setNative_compass(sensorData_native_compass);
        }
    }

    public void commandRFID(){
        try {
            switch (dataRFID.getCommandType()){
                case ConstCode.FRAME_CMD_READ_SINGLE:
                case ConstCode.FRAME_CMD_READ_MULTI:
                    mapViewer.setSensorData_rfid(dataRFID, RefreshStatus.refresh_map);
                    break;
            }
        }catch (Exception e){
            Log.e(TAG, "Error saving motion trace ", e);
        }
    }

    public void commandACCELEROMETER(){
        try {
            float[] value = sensorData_accelerometer.getValue();
            AccelerationSensor alts = new AccelerationSensor();
            alts.setX(value[0]);
            alts.setY(value[1]);
            alts.setZ(value[2]);
            uwbViewer.setAccelerationSensor(alts);
        }catch (Exception e){
            Log.e(TAG, "Error saving motion trace ", e);
        }
    }

    public void mapViewerTouchListener(){
        mapViewer.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        if(event.getPointerCount() == 1){
//                            System.out.println("action down");
                            action_down_x = event.getX();
                            action_down_y = event.getY();
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:

                        System.out.println(String.format("%d,%f", event.getPointerId(0),event.getY()));

                        if(event.getPointerCount() == 2){
                            pointerboolen = false;
                            float offsetX = event.getX(0) - event.getX(1);
                            float offsetY = event.getY(0) - event.getY(1);
                            currentDistance = (float) Math.sqrt(offsetX*offsetX+offsetY*offsetY);
                            if(lastcurrentDistance < 0){
                                lastcurrentDistance = currentDistance;
                            }else{
                                if(currentDistance - lastcurrentDistance > 5){
//                                    action_up_x = action_up_x*(1f/1.1f);
//                                    action_up_y = action_up_y*(1f/1.1f);
                                    x1 = x1*1.1f;
                                    y1 = y1*1.1f;
                                    x2 = (event.getX(0) + event.getX(1))/2;
                                    y2 = (event.getY(0) + event.getY(1))/2;
//                                    action_up_x = action_up_x*1.1f - ;
//                                    action_up_y = action_up_y*1.1f - ;
                                    mapViewer.setScale(1.1f, (event.getX(0) + event.getX(1))/2, (event.getY(0) + event.getY(1))/2, RefreshStatus.refresh_map);
                                }else if(lastcurrentDistance - currentDistance > 5){
//                                    action_up_x = action_up_x*1.1f;
//                                    action_up_y = action_up_y*1.1f;
//                                    action_up_x = action_up_x - x1;
//                                    action_up_y = action_up_y - y1;
                                    x2 = (event.getX(0) + event.getX(1))/2;
                                    y2 = (event.getY(0) + event.getY(1))/2;
                                    x1 = x1*(1f/1.1f);
                                    y1 = y1*(1f/1.1f);
                                    mapViewer.setScale(1f/1.1f, (event.getX(0) + event.getX(1))/2, (event.getY(0) + event.getY(1))/2, RefreshStatus.refresh_map);
                                }
                                lastcurrentDistance = currentDistance;
                            }
                        }else if(event.getPointerCount() == 1){
                            if(pointerboolen == true){
                                mapViewer.setFxy((event.getX() - action_down_x) + action_up_x,(event.getY() - action_down_y) + action_up_y, RefreshStatus.refresh_map);
                            }
                        }

                        break;
                    case MotionEvent.ACTION_UP:
                        pointerboolen = true;
                        if(event.getPointerCount() == 2){
                            action_up_x = action_up_x*x1 - x2;
                            action_up_y = action_up_y*y1 - y2;
                        }else{
                            action_up_x = (event.getX() - action_down_x) + action_up_x;
                            action_up_y = (event.getY() - action_down_y) + action_up_y;
                        }

                        break;
                }
                return true;
            }
        });
    }
}