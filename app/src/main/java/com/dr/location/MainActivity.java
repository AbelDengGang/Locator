package com.dr.location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;

import com.dr.libloc.locator.LocatorMgr;
import com.dr.libloc.sensor.DRSensorMgr;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private Button buttonMap;
    private Button buttonSerial;
    private Button buttonSensorRecorder;
    private Button buttonIMU;
    private AlertDialog mDialog;
    private MapFragment mapFragment;
    private RFIDWLR200Fragment rfidwlr200Fragment;
    private RecorderFragment recorderFragment;
    private IMUFragment imuFragment;
    private AdvFragment videoFragment;
    private FrameLayout frameLayoutVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        //屏幕保持常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide(); //隐藏标题栏
        }

//        requestPermission();

        buttonMap = (Button)findViewById(R.id.buttonMap);
        buttonSerial = (Button)findViewById(R.id.buttonSerial);
        buttonSensorRecorder = (Button)findViewById(R.id.buttonSensorRecorder);
        buttonIMU = (Button)findViewById(R.id.buttonIMU);
        frameLayoutVideo = (FrameLayout) findViewById(R.id.video_Fragment);
        buttonMap.setOnClickListener(this);
        buttonSerial.setOnClickListener(this);
        buttonSensorRecorder.setOnClickListener(this);
        buttonIMU.setOnClickListener(this);


        DRSensorMgr.createMgr(this);
        LocatorMgr.createLocator_RFID();
        mapFragment = new MapFragment();
        rfidwlr200Fragment = new RFIDWLR200Fragment();
        recorderFragment = new RecorderFragment();
        imuFragment = new IMUFragment();
        videoFragment = new AdvFragment();

        getSupportFragmentManager().beginTransaction().add(R.id.fragment_main, rfidwlr200Fragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.video_Fragment, videoFragment).commit();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonMap:
                videoFragment.startPlay();
                frameLayoutVideo.setVisibility(View.VISIBLE);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main, mapFragment).commit();
                break;
            case R.id.buttonSerial:
                videoFragment.stopPlay();
                frameLayoutVideo.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main, rfidwlr200Fragment).commit();
                break;
            case R.id.buttonSensorRecorder:
                videoFragment.stopPlay();
                frameLayoutVideo.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main, recorderFragment).commit();
                break;
            case R.id.buttonIMU:
                videoFragment.stopPlay();
                frameLayoutVideo.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main, imuFragment).commit();
                break;
        }
    }

    public void delayFun(){
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1){
            for(int i = 0; i < permissions.length; i++){
                //已授权
                if(grantResults[i] == 0){
                    continue;
                }

                if(ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i])){
                    //选择禁止
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("授权");
                    builder.setMessage("需要授权才可以使用");
                    builder.setPositiveButton("去允许", (dialog, which) -> {
                        if(mDialog != null && mDialog.isShowing()){
                            mDialog.dismiss();
                        }
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    });
                    mDialog = builder.create();
                    mDialog.setCanceledOnTouchOutside(false);
                    mDialog.show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 2){
            //应用设置页面返回后再次判断权限
            requestPermission();
        }
    }

    public void requestPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        //取消常亮
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }

    @Override
    protected void onPause() {
        super.onPause();
        onResume();
    }
}