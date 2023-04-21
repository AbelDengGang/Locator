package com.dr.location;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Looper;
import android.preference.PreferenceActivity;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;

import com.dr.libloc.locator.LocatorRecorder;
import com.dr.libloc.sensor.SensorRecorder;
import com.dr.libloc.toast.ToastText;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class RecorderFragment extends Fragment implements View.OnClickListener{


    private Spinner spinner;
    private Spinner spinnerLocator;
    private Button beginRecorderButton;
    private Button playbackRecordButton;

    private String XMLpath = null;
    private String locatorXMLPath = null;
    private String fileName = "recorder";
    private String locatorFileName = "recorder";
    private String readXmlPath;
    private String readLocatorXMLPath;

    private SensorRecorder sensorRecorder;
    private LocatorRecorder locatorRecorder;
    private List<String> list;
    private ArrayAdapter<String> adapter;
    private ArrayAdapter<String> adapterLocator;
    private String[] selectFilePath;
    private String[] selectLocatorFilePath;
    private String selectFileName = "";
    private int threadCount = 0;
    private boolean isCheckboxCompass;

    private CheckBox checkboxCompass;

    private SensorManager mSensorMgr;// 声明一个传感管理器对象

    private String TAG = RecorderFragment.class.toString();
    private File externalFile;
    private MapFragment fragment;

    public RecorderFragment() {
        isCheckboxCompass = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recorder, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        selectFilePath = new String[2];
        selectLocatorFilePath = new String[2];
        selectFilePath[0] = "请选择文件";
        selectLocatorFilePath[0] = "请先选择第一个文件";
        selectFilePath[1] = "不播放";

        mSensorMgr = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);

        beginRecorderButton = (Button) view.findViewById(R.id.beginRecorder);
        playbackRecordButton = (Button) view.findViewById(R.id.playbackRecord);

        checkboxCompass = (CheckBox) view.findViewById(R.id.checkbox_compass);
        checkboxCompass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isCheckboxCompass = isChecked;
                sensorRecorder.setCheckboxCompass(isChecked);
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
                    //两个传感器都存在的情况
                } else {
                    checkboxCompass.setChecked(false);
                    ToastText.toastCompass(getContext());
                }
            }
        });


        spinner = (Spinner) view.findViewById(R.id.spinnerPullDown);
        spinnerLocator = (Spinner) view.findViewById(R.id.spinnerPullDownLocator);
        list = new ArrayList<>();
        getAllRecorderXMl();

        //添加选择事件监听
        spinner.setOnItemSelectedListener(new SpinnerSelectedListener());
        spinnerLocator.setOnItemSelectedListener(new SpinnerSelectedLocatorListener());
        beginRecorderButton.setOnClickListener(this);
        playbackRecordButton.setOnClickListener(this);

        //设置默认值
//        spinner.setVisibility(View.VISIBLE);
//                    String state = Environment.getExternalStorageState();
        externalFile = getContext().getExternalFilesDir( null );

        XMLpath = externalFile.toString() + "/" + fileName + ".xml";
        locatorXMLPath = externalFile.toString() + "/" + locatorFileName + "locator_L.xml";

        this.sensorRecorder = new SensorRecorder(getContext(), getActivity(), XMLpath);

        locatorRecorder = new LocatorRecorder(getContext(),locatorXMLPath);

        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        fragment = new MapFragment();
        transaction.replace(R.id.fragment_main_01, fragment).commit();

    }

    @Override
    public void onResume() {
        super.onResume();
        sensorRecorder.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        sensorRecorder.stop();
        fragment.onStop();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.beginRecorder:
                beginRecorder_B();
                break;
            case R.id.playbackRecord:
                playbackRecord_B();
                break;
        }
    }

    public void playbackRecord_B(){
        if(readXmlPath.replace(" ", "").equals("/storage/emulated/0/Android/data/com.dr.location/files/请选择文件")){
            ToastText.toastChooseFile(getContext());
        }else{
            if(beginRecorderButton.getText().equals("开始记录")){
                fragment.onCompassStop();
                new Thread("Playback sensor record thread"){
                    @Override
                    public void run() {
                        super.run();
                        sensorRecorder.setReadXmlPath(readXmlPath);
                        sensorRecorder.load();
                        sensorRecorder.playBack();
                        threadCount += 1;
                        threadEndToast();
                    }
                }.start();

                new Thread("Playback locator record thread"){
                    @Override
                    public void run() {
                        super.run();
                        locatorRecorder.setReadXmlPath(readLocatorXMLPath);
                        locatorRecorder.load();
                        locatorRecorder.playBack();
                        threadCount += 1;
                        System.out.println(threadCount);
                        threadEndToast();
                    }
                }.start();

            }else {
                ToastText.toastEndRecorder(getContext());
            }
        }
    }

    public void threadEndToast(){
        if(threadCount == 2){
            threadCount = 0;
            fragment.onCompassStart();
            ToastText.toastEndPlayback(getContext());
        }
    }

    public void beginRecorder_B(){
        if(beginRecorderButton.getText().equals("开始记录")){
            inputEditText();
        }else if(beginRecorderButton.getText().equals("结束记录")){
            beginRecorderButton.setText("开始记录");
            sensorRecorder.store();
            locatorRecorder.store();
        }
        getAllRecorderXMl();
    }

    public void getAllRecorderXMl(){

        list.clear();
        File externalFile = getContext().getExternalFilesDir( null );
        String path = externalFile.toString();
        File file = new File(path);
        File[] files = file.listFiles();
        for (int i = 0 ;i < files.length; i++){
            if(!files[i].isDirectory()){
                String[] spl = files[i].getName().split("\\.");
                if(spl[spl.length-1].equals("xml")){
                    if(!(files[i].getName().contains("locator_L"))){
                        list.add(files[i].getName());
                    }
                }
            }
        }
        selectFilePath = new String[list.size()+2];
        selectFilePath[0] = "请选择文件";
        selectFilePath[1] = "不播放";
        for (int i = 0; i < list.size(); i++){
            selectFilePath[i+2] = list.get(i);
        }

        adapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, selectFilePath);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
    public void getAllRecorderLocatorXMl(){
        list.clear();
        File externalFile = getContext().getExternalFilesDir( null );
        String path = externalFile.toString();
        File file = new File(path);
        File[] files = file.listFiles();
        for (int i = 0 ;i < files.length; i++){
            if(!files[i].isDirectory()){
                String[] spl = files[i].getName().split("\\.");
                if(spl[spl.length-1].equals("xml")){
                    if(selectFileName == null){
                        return;
                    }
                    if(selectFileName.equals("不播放")){
                        if((files[i].getName().contains("locator_L"))){
                            list.add(files[i].getName());
                        }
                    }else{
                        if((files[i].getName().contains(selectFileName.split("\\.")[0]))){
                            if((files[i].getName().contains("locator_L"))){
                                list.add(files[i].getName());
                            }
                        }
                    }
                }
            }
        }
        selectLocatorFilePath = new String[list.size()+1];
        selectLocatorFilePath[0] = "不播放";
        for (int i = 0; i < list.size(); i++){
            selectLocatorFilePath[i+1] = list.get(i);
        }

        adapterLocator = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, selectLocatorFilePath);
        adapterLocator.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerLocator.setAdapter(adapterLocator);
    }


    public void inputEditText(){
        long time=System.currentTimeMillis();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd-HHmmss");
        Date date=new Date(time);
        String name=format.format(date);
        final EditText inputServer = new EditText(getContext());
        inputServer.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
        inputServer.setText(name);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("请输入文件名字").setIcon(R.drawable.ic_launcher_background).setView(inputServer).setNegativeButton("取消",null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
                fileName = inputServer.getText().toString();
                locatorFileName = inputServer.getText().toString();
                XMLpath = externalFile.toString() + "/" + fileName + ".xml";
                locatorXMLPath = externalFile.toString() + "/" + locatorFileName + "locator_L.xml";
                sensorRecorder.setXMLPath(XMLpath);
                locatorRecorder.setXmlPath(locatorXMLPath);
                sensorRecorder.initRecorderFile();
                locatorRecorder.initRecorderFile();
                beginRecorderButton.setText("结束记录");
            }
        });
        builder.show();
    }

    class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener{

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            readXmlPath = externalFile.toString() + "/" + selectFilePath[position];
            selectFileName = selectFilePath[position];
            getAllRecorderLocatorXMl();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    }
    class SpinnerSelectedLocatorListener implements AdapterView.OnItemSelectedListener{

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            readLocatorXMLPath = externalFile.toString() + "/" + selectLocatorFilePath[position];
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    }

}