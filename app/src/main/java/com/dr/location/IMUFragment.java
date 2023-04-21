package com.dr.location;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dr.libloc.sensor.DRSensor;
import com.dr.libloc.sensor.DRSensorMgr;
import com.dr.libloc.sensor.DRSensor_ACCELEROMETER;
import com.dr.libloc.sensor.SensorData;
import com.dr.libloc.sensor.SensorUpdateListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IMUFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IMUFragment extends Fragment implements View.OnClickListener , SensorUpdateListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView tv_print;
    private DRSensor_ACCELEROMETER sensor;

    public IMUFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment IMUFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IMUFragment newInstance(String param1, String param2) {
        IMUFragment fragment = new IMUFragment();
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
        View view =  inflater.inflate(R.layout.fragment_imu, container, false);
        tv_print = view.findViewById(R.id.tv_print);
        tv_print.setMovementMethod(ScrollingMovementMethod.getInstance());

        autoSetOnClickListener(view,R.id.btn_clear);
        autoSetOnClickListener(view,R.id.btn_calib);


        sensor = DRSensorMgr.getMgr().getACCSensor();
        sensor.regListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_setup: // 设置串口
                break;
            case R.id.btn_clear: // 清除控制台信息
                tv_print.setText("");
                tv_print.scrollTo(0, 0);
                break;
            case R.id.btn_calib:
                sensor.calibrateInit();
                break;
        }
    }

    @Override
    public void onSensorUpdate(DRSensor sender, SensorData data) {
        // TODO check data type
        appendPrintPanel(data.toString());
    }
}