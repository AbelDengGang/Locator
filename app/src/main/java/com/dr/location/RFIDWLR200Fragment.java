package com.dr.location;

import android.hardware.Sensor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dr.libloc.sensor.DRSensor;
import com.dr.libloc.sensor.DRSensorMgr;
import com.dr.libloc.sensor.SensorData;
import com.dr.libloc.sensor.SensorData_RFID;
import com.dr.libloc.sensor.SensorUpdateListener;
import com.dr.libloc.sensor.DRSensor_RFID_WULIANKJ_R200;
import com.rfid_WULIANKEJIR200.cmd.ConstCode;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RFIDWLR200Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RFIDWLR200Fragment extends Fragment implements View.OnClickListener, SensorUpdateListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private DRSensor_RFID_WULIANKJ_R200 sensor;
    private static final String TAG = "RFIDWLR200Fragment";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView tv_info;
    private TextView tv_print;
    private Button btn_setup;
    private Button btn_set_select;
    private Button btn_read_data;
    private Button btn_write_data;
    private Button btn_lock;
    private EditText et_loop_num;
    private EditText et_pa_power;
    private EditText et_pointer;
    private EditText et_mask;
    private EditText et_kill_password;
    private EditText et_access_password;
    private EditText et_ld;
    private EditText et_sa;
    private EditText et_dl;
    private EditText et_data;
    private EditText et_access_password2;
    private EditText et_channel_index;
    private Spinner spn_region;
    private Spinner spn_select_mode;
    private Spinner spn_target;
    private Spinner spn_action;
    private Spinner spn_mem_bank;
    private Spinner spn_men_bakk2;
    private Spinner spn_dr;
    private Spinner spn_m;
    private Spinner spn_t_rext;
    private Spinner spn_sel;
    private Spinner spn_session;
    private Spinner spn_target_q;
    private Spinner spn_q;


    public RFIDWLR200Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RFIDWLR200Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RFIDWLR200Fragment newInstance(String param1, String param2) {
        RFIDWLR200Fragment fragment = new RFIDWLR200Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rfidr200, container, false);
        et_loop_num = view.findViewById(R.id.et_loop_num);
        et_pa_power = view.findViewById(R.id.et_pa_power);
        et_pointer = view.findViewById(R.id.et_pointer);
        et_mask = view.findViewById(R.id.et_mask);
        et_kill_password = view.findViewById(R.id.et_kill_password);
        et_access_password = view.findViewById(R.id.et_access_password);
        et_ld = view.findViewById(R.id.et_ld);
        et_sa = view.findViewById(R.id.et_sa);
        et_dl = view.findViewById(R.id.et_dl);
        et_data = view.findViewById(R.id.et_data);
        et_access_password2 = view.findViewById(R.id.et_access_password2);
        et_channel_index = view.findViewById(R.id.et_channel_index);

        spn_region = view.findViewById(R.id.spn_region);
        spn_select_mode = view.findViewById(R.id.spn_select_mode);
        spn_target = view.findViewById(R.id.spn_target);
        spn_action = view.findViewById(R.id.spn_action);
        spn_mem_bank = view.findViewById(R.id.spn_mem_bank);
        spn_men_bakk2 = view.findViewById(R.id.spn_mem_bank2);
        spn_dr = view.findViewById(R.id.spn_dr);
        spn_m = view.findViewById(R.id.spn_m);
        spn_t_rext = view.findViewById(R.id.spn_t_rext);
        spn_sel = view.findViewById(R.id.spn_sel);
        spn_session = view.findViewById(R.id.spn_session);
        spn_target_q = view.findViewById(R.id.spn_target_q);
        spn_q = view.findViewById(R.id.spn_q);

        tv_info = view.findViewById(R.id.tv_info);
        tv_print = view.findViewById(R.id.tv_print);
        tv_print.setMovementMethod(ScrollingMovementMethod.getInstance());

        btn_setup = view.findViewById(R.id.btn_setup);
        btn_set_select = view.findViewById(R.id.btn_set_select);
        btn_read_data = view.findViewById(R.id.btn_read_data);
        btn_write_data = view.findViewById(R.id.btn_write_data);
        btn_lock = view.findViewById(R.id.btn_lock);
        btn_setup.setOnClickListener(this);
        autoSetOnClickListener(view,R.id.btn_clear);
        autoSetOnClickListener(view,R.id.btn_firmware_version);
        autoSetOnClickListener(view,R.id.btn_software_version);
        autoSetOnClickListener(view,R.id.btn_manufacturers_info);
        autoSetOnClickListener(view,R.id.btn_read_single);
        autoSetOnClickListener(view,R.id.btn_read_multi);
        autoSetOnClickListener(view,R.id.btn_stop_read);
        autoSetOnClickListener(view,R.id.btn_set_region);
        autoSetOnClickListener(view,R.id.btn_get_pa_power);
        autoSetOnClickListener(view,R.id.btn_set_pa_power);
        autoSetOnClickListener(view,R.id.btn_set_select_mode);
        autoSetOnClickListener(view,R.id.btn_set_select);
        autoSetOnClickListener(view,R.id.btn_kill);
        autoSetOnClickListener(view,R.id.btn_lock);
        autoSetOnClickListener(view,R.id.btn_read_data);
        autoSetOnClickListener(view,R.id.btn_write_data);
        autoSetOnClickListener(view,R.id.btn_set_channel);
        autoSetOnClickListener(view,R.id.btn_get_channel);
        autoSetOnClickListener(view,R.id.btn_set_q);
        autoSetOnClickListener(view,R.id.btn_get_q);
        autoSetOnClickListener(view,R.id.btn_set_fhss_auto);
        autoSetOnClickListener(view,R.id.btn_set_fhss_cancel);
        //sensor = new DRSensor_RFID_WULIANKJ_R200(getActivity(),"WULIANKJ_R200");
        sensor = DRSensorMgr.getMgr().getRFIDSensor();
        sensor.regListener(this);
        List<Sensor> sensors =  DRSensorMgr.getSysSensor(getActivity());
        for (Sensor sensor : sensors) {
            String sensorMsg = sensor.toString() + " : " + DRSensorMgr.getSensorStringType(sensor.getType()) + "\n";
            appendPrintPanel(sensorMsg);
        }

        return view;
    }

    private void autoSetOnClickListener(View view,int id) {
        view.findViewById(id).setOnClickListener(this);
    }


    /**
     * 往输出面板追加消息并自动滚到底部
     */
    private void appendPrintPanel(String msg) {
        tv_print.append(msg);
        int offset = tv_print.getLineCount() * tv_print.getLineHeight();
        if (offset > tv_print.getHeight()) {
            tv_print.scrollTo(0, offset - tv_print.getHeight());
        }
    }

    private static final int HANDLER_WAHT_PRINT_TAG_DATA = 0;
    private Handler mainHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case HANDLER_WAHT_PRINT_TAG_DATA:
                    appendPrintPanel((String) msg.obj);
                    break;
            }
        }
    };
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_setup: // 设置串口
                break;
            case R.id.btn_clear: // 清除控制台信息
                tv_print.setText("");
                tv_print.scrollTo(0, 0);
                break;
            case R.id.btn_firmware_version: /* 获取固件版本 */
                //sendPacket(Commands.buildGetFirmwareVersionFrame());
                Log.e(TAG,"btn_firmware_version");
                sensor.sendGetFirmwareVersionCmd();
                break;
            case R.id.btn_software_version: /* 获取软件版本 */
                Log.e(TAG,"btn_software_version");
                sensor.sendGetSoftwareVersionCmd();
                //sendPacket(Commands.buildGetSoftwareVersionFrame());
                break;
            case R.id.btn_manufacturers_info: /* 获取制造商信息 */
                Log.e(TAG,"btn_manufacturers_info");
                sensor.sendGetManufacturesInfoCmd();
                //sendPacket(Commands.buildGetManufacturersInfoFrame());
                break;
            case R.id.btn_read_single: /* 单读 */
                // TODO 单读
                Log.e(TAG,"btn_read_single");
                sensor.sendReadSingleCmd();
                break;
            case R.id.btn_read_multi: /* 多读 */
                // TODO 多读
//                try {
//                    String loopStr = et_loop_num.getText().toString();
//                    int loopNum = Integer.valueOf(loopStr);
//                    sendPacket(Commands.buildReadMultiFrame(loopNum));
//                } catch (NumberFormatException e) {
//                    Toast.makeText(this, "请检查轮询次数是否正确", Toast.LENGTH_LONG).show();
//                }
                Log.e(TAG,"btn_read_multi");
                String loopStr = et_loop_num.getText().toString();
                int loopNum = Integer.valueOf(loopStr);
                sensor.sendReadMultiFrame(loopNum);
                break;
            case R.id.btn_stop_read: /* 停止多读 */
                Log.e(TAG,"btn_read_multi");
                sensor.sendStopReadCmd();
                // sendPacket(Commands.buildStopReadFrame());
                break;
            case R.id.btn_set_region: /* 设置区域 */
                // TODO 设置区域
                String regionStr = (String) spn_region.getSelectedItem();
                Log.e(TAG,"btn_set_region "+regionStr);

                switch (regionStr) {
                    case "中国900MHz":
                        sensor.sendSetRegionCmd(ConstCode.FRAME_PARAM_REGION_CHINA_900_MHZ);
                        //sendPacket(Commands.buildSetRegionFrame(ConstCode.FRAME_PARAM_REGION_CHINA_900_MHZ));
                        break;
                    case "中国800MHz":
                        sensor.sendSetRegionCmd(ConstCode.FRAME_PARAM_REGION_CHINA_800_MHZ);
                        //sendPacket(Commands.buildSetRegionFrame(ConstCode.FRAME_PARAM_REGION_CHINA_800_MHZ));
                        break;
                    case "美国":
                        sensor.sendSetRegionCmd(ConstCode.FRAME_PARAM_REGION_USA);
                        //sendPacket(Commands.buildSetRegionFrame(ConstCode.FRAME_PARAM_REGION_USA));
                        break;
                    case "欧洲":
                        sensor.sendSetRegionCmd(ConstCode.FRAME_PARAM_REGION_EUROPE);
                        //sendPacket(Commands.buildSetRegionFrame(ConstCode.FRAME_PARAM_REGION_EUROPE));
                        break;
                    case "韩国":
                        sensor.sendSetRegionCmd(ConstCode.FRAME_PARAM_REGION_KOREA);
                        //sendPacket(Commands.buildSetRegionFrame(ConstCode.FRAME_PARAM_REGION_KOREA));
                        break;
                }
                break;
            case R.id.btn_get_pa_power: /* 获取发射功率 */
                Log.e(TAG,"btn_get_pa_power ");
                sensor.sendGetPaPowerCmd();
//                sendPacket(Commands.buildGetPaPowerFrame());
                break;
            case R.id.btn_set_pa_power:
                Log.e(TAG,"btn_set_pa_power ");
                try {
                    String powerStr = et_pa_power.getText().toString();
                    int power = Integer.valueOf(powerStr);
                    sensor.sendSetPaPowerCmd(power);
                    //sendPacket(Commands.buildSetPaPowerFrame(power));
                } catch (NumberFormatException e) {
                    //Toast.makeText(this, "请检查发射功率值是否正确", Toast.LENGTH_LONG).show();
                    Log.e(TAG,e.toString());
                }
                break;
            case R.id.btn_set_select_mode: /* 设置SELECT MODE */
                // TODO 设置SELECT MODE
//                byte selectMode = 0x00;
//                switch ((String)spn_select_mode.getSelectedItem()) {
//                    case "0x00(选定)":
//                        selectMode = 0x00;
//                        break;
//                    case "0x01(不选定)":
//                        selectMode = 0x01;
//                        break;
//                    case "0x02(盘存之外)":
//                        selectMode = 0x02;
//                        break;
//                }
//                sendPacket(Commands.buildSetSelectModelFrame(selectMode));
                break;
            case R.id.btn_set_select: /* 设置SELECT */
                // TODO 设置SELECT
//                byte targetByte = 0x00;
//                switch ((String) spn_target.getSelectedItem()) {
//                    case "S0(000)":
//                        targetByte = 0x00;
//                        break;
//                    case "S1(001)":
//                        targetByte = 0x01;
//                        break;
//                    case "S2(010)":
//                        targetByte = 0x0A;
//                        break;
//                    case "S3(011)":
//                        targetByte = 0x0B;
//                        break;
//                    case "SL(100)":
//                        targetByte = 0x64;
//                        break;
//                    case "RFU(101)":
//                        targetByte = 0x65;
//                        break;
//                    case "RFU(110)":
//                        targetByte = 0x6E;
//                        break;
//                    case "RFU(111)":
//                        targetByte = 0x6F;
//                        break;
//                }
//                byte actionByte = 0x00;
//                switch ((String) spn_action.getSelectedItem()) {
//                    case "000":
//                        actionByte = 0x00;
//                        break;
//                    case "001":
//                        actionByte = 0x01;
//                        break;
//                    case "010":
//                        actionByte = 0x0A;
//                        break;
//                    case "011":
//                        actionByte = 0x0B;
//                        break;
//                    case "100":
//                        actionByte = 0x64;
//                        break;
//                    case "101":
//                        actionByte = 0x65;
//                        break;
//                    case "110":
//                        actionByte = 0x6E;
//                        break;
//                    case "111":
//                        actionByte = 0x6F;
//                        break;
//                }
//                byte memBankByte = 0x00;
//                switch ((String) spn_mem_bank.getSelectedItem()) {
//                    case "RFU":
//                        memBankByte = ConstCode.FRAME_PARAM_MEM_BANK_RFU;
//                        break;
//                    case "EPC":
//                        memBankByte = ConstCode.FRAME_PARAM_MEM_BANK_EPC;
//                        break;
//                    case "TID":
//                        memBankByte = ConstCode.FRAME_PARAM_MEM_BANK_TID;
//                        break;
//                    case "User":
//                        memBankByte = ConstCode.FRAME_PARAM_MEM_BANK_USER;
//                        break;
//                }
//                try {
//                    String pointerHexStr = et_pointer.getText().toString().replace(" ", "");
//                    long pointerVal = Long.valueOf(pointerHexStr, 16);
//                    byte[] maskBytes = ByteArrayUtils.toBytes(et_mask.getText().toString());
//
//                    sendPacket(Commands.buildSetSelectFrame(targetByte, actionByte, memBankByte, pointerVal, false, maskBytes));
//
//                } catch (NumberFormatException e) {
//                    Toast.makeText(this, "十六进制输入错误", Toast.LENGTH_LONG).show();
//                }
                break;
            case R.id.btn_kill: /* 灭活 */
                // TOD 灭活
//                try {
//                    byte[] killPwd = ByteArrayUtils.toBytes(et_kill_password.getText().toString());
//                    sendPacket(Commands.buildKillFrame(killPwd));
//                } catch (NumberFormatException e) {
//                    Toast.makeText(this, "十六进制输入错误", Toast.LENGTH_LONG).show();
//                }
                break;
            case R.id.btn_lock: /* Lock */
                // TODO Lock
//                try {
//                    if (this.checkPreSelect() && preSetSelectTask == null) {
//                        preSetSelectTask = new PreSetSelectTask(TaskType.LOCK);
//                        preSetSelectTask.start();
//                        return;
//                    }
//
//                    preSetSelectTask = null;
//
//                    byte[] accessPwd = ByteArrayUtils.toBytes(et_access_password.getText().toString());
//                    String dlHexStr = et_ld.getText().toString().replace(" ", "");
//                    int ld = Integer.valueOf(dlHexStr, 16);
//                    sendPacket(Commands.buildLockFrame(accessPwd, ld));
//                } catch (NumberFormatException e) {
//                    Toast.makeText(this, "十六进制输入错误", Toast.LENGTH_LONG).show();
//                }
                break;
            case R.id.btn_read_data: /* 读取数据 */
            case R.id.btn_write_data: /* 写入数据 */
                // TOD0  写入数据
//                try {
//                    // 如果满足"前置Select"条件（Mask都不为零），并且当前没有运行任务
//                    if (this.checkPreSelect() && preSetSelectTask == null) {
//                        preSetSelectTask = new PreSetSelectTask(v.getId() == R.id.btn_read_data ? TaskType.READ_DATA : TaskType.WRITE_DATA);
//                        preSetSelectTask.start();
//                        return;
//                    }
//
//                    preSetSelectTask = null;
//
//                    byte memBankByte2 = 0;
//                    switch ((String) spn_men_bakk2.getSelectedItem()) {
//                        case "RFU":
//                            memBankByte2 = ConstCode.FRAME_PARAM_MEM_BANK_RFU;
//                            break;
//                        case "EPC":
//                            memBankByte2 = ConstCode.FRAME_PARAM_MEM_BANK_EPC;
//                            break;
//                        case "TID":
//                            memBankByte2 = ConstCode.FRAME_PARAM_MEM_BANK_TID;
//                            break;
//                        case "User":
//                            memBankByte2 = ConstCode.FRAME_PARAM_MEM_BANK_USER;
//                            break;
//                    }
//                    byte[] accessPwd2 = ByteArrayUtils.toBytes(et_access_password2.getText().toString());
//                    int sa = Integer.valueOf(et_sa.getText().toString().replace(" ", ""), 16);
//                    int dl = Integer.valueOf(et_dl.getText().toString().replace(" ", ""), 16);
//
//                    if (v.getId() == R.id.btn_read_data) {
//                        sendPacket(Commands.buildReadDataFrame(accessPwd2, memBankByte2, sa, dl));
//                    } else if (v.getId() == R.id.btn_write_data) {
//                        byte[] data = ByteArrayUtils.toBytes(et_data.getText().toString());
//                        sendPacket(Commands.buildWriteDataFrame(accessPwd2, memBankByte2, sa, dl, data));
//                    }
//                } catch (NumberFormatException e) {
//                    e.printStackTrace();
//                    Toast.makeText(this, "十六进制输入错误", Toast.LENGTH_LONG).show();
//                }
                break;
            case R.id.btn_set_channel: /* 设置工作信道 */
                Log.e(TAG,"btn_set_channel ");
                try {
                    int ch = Integer.valueOf(et_channel_index.getText().toString().replace(" ", ""), 16);
                    sensor.sendSetChannelCmd(ch);
                    //sendPacket(Commands.buildSetChannelFrame((byte) (ch & 0xFF)));
                } catch (NumberFormatException e) {
                    //Toast.makeText(this, "十六进制输入错误", Toast.LENGTH_LONG).show();
                    Log.e(TAG,e.toString());
                }
                break;
            case R.id.btn_get_channel: /* 获取工作信道 */
                Log.e(TAG,"btn_set_channel ");
                sensor.sendGetChannelCmd();
//                sendPacket(Commands.buildGetChannelFrame());
                break;
            case R.id.btn_get_q: /* 获取固件Q值 */
                Log.e(TAG,"btn_get_q");
                sensor.sendGetQueryCmd();
//                sendPacket(Commands.buildGetQueryFrame());
                break;
            case R.id.btn_set_q: /* 设置固件Q值 */
                // TODO 设置固件Q值
//                int sel = 0;
//                switch ((String) spn_sel.getSelectedItem()) {
//                    case "ALL(00)":
//                        sel = 0;
//                        break;
//                    case "ALL(01)":
//                        sel = 1;
//                        break;
//                    case "~SL(10)":
//                        sel = 2;
//                        break;
//                    case "SL(11)":
//                        sel = 3;
//                        break;
//                }
//                int session = 0;
//                switch ((String) spn_session.getSelectedItem()) {
//                    case "S0":
//                        session = 0;
//                        break;
//                    case "S1":
//                        session = 1;
//                        break;
//                    case "S2":
//                        session = 2;
//                        break;
//                    case "S3":
//                        session = 3;
//                        break;
//                }
//                int target = 0;
//                switch ((String) spn_target_q.getSelectedItem()) {
//                    case "A":
//                        target = 0;
//                        break;
//                    case "B":
//                        target = 1;
//                        break;
//                }
//                int q = Integer.valueOf((String) spn_q.getSelectedItem());
//                sendPacket(Commands.buildSetQueryFrame(0, 0, 1, sel, session, target, q));
                break;
            case R.id.btn_set_fhss_auto: /* 设置自动跳频模式 */
                Log.e(TAG,"btn_set_fhss_auto");
                sensor.sendSetEnFhssCmd();
//                sendPacket(Commands.buildSetFhssFrame(true));
                break;
            case R.id.btn_set_fhss_cancel: /* 取消自动跳频模式 */
                Log.e(TAG,"btn_set_fhss_cancel");
                sensor.sendSetCancelFhssCmd();
//                sendPacket(Commands.buildSetFhssFrame(false));
                break;
        }

    }

    @Override
    public void onSensorUpdate(DRSensor sender, SensorData data) {
        SensorData_RFID dataRfid = (SensorData_RFID)data;
        Log.d(TAG,dataRfid.toString());

        // 在非UI线程接收信息，给UI发送消息，用于更新显示,这里只是一条打印信息
        Message message = Message.obtain();
        message.what = HANDLER_WAHT_PRINT_TAG_DATA;
        message.obj = String.format("REC -> %s\n", dataRfid.toString());
        mainHandler.sendMessage(message);

//        // -------------------------------------
//        // 检查是不是Select响应结果，并且“前置Select”任务存在时，调用任务函数尝试进行下一步操作
//        if (paramHashMap.containsKey("set_select_result_code") && preSetSelectTask != null) {
//            byte resultCode = paramHashMap.get("set_select_result_code")[0];
//            if (resultCode == 0x00) {
//                preSetSelectTask.tryNextActionWithSuccess();
//            } else {
//                preSetSelectTask.tryNextActionWithFail();
//            }
//        }

    }
}