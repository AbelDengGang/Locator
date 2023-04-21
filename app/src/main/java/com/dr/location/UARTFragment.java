package com.dr.location;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.text.SpannableStringBuilder;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dr.libloc.comm.COMM;
import com.dr.libloc.comm.COMM_Serial;
import com.dr.libloc.comm.CommUpdateListener;
import com.dr.libloc.util.HexDump;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UARTFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UARTFragment extends Fragment implements CommUpdateListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    static String TAG = "UARTFragment";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    COMM_Serial comm;
    private Handler mainLooper;
    private TextView logText;

    private void receive(byte[] data) {
        Log.e(TAG,"receive");
        // TODO this function run in MAIN thread
        SpannableStringBuilder spn = new SpannableStringBuilder();
        spn.append("receive " + data.length + " bytes");
        Log.e(TAG,"receive ");
        if(data.length > 0)
            spn.append(HexDump.dumpHexString(data)+"\n");
        logText.append(spn);

    }
    @Override
    public void onNewData(COMM sender, byte[] data){
        //TODO send background envent to main thread
        Log.e(TAG,"onNewData" + data.toString());
        mainLooper.post(() -> {
            receive(data);
        });
    }

    @Override
    public void onCommEvent(COMM sender, Exception e,String msg,int code){
        //TODO send background envent to main thread
        Log.e(TAG,"onCommEvent");
        mainLooper.post(() -> {
            status("connection lost: " + code);
        });

    }
    void status(String str) {
//        Log.e(TAG,str);
        SpannableStringBuilder spn = new SpannableStringBuilder(str+'\n');
        //spn.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorStatusText)), 0, spn.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        logText.append(spn);
    }

    public UARTFragment() {
        // Required empty public constructor
        mainLooper = new Handler(Looper.getMainLooper());
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UARTFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UARTFragment newInstance(String param1, String param2) {
        UARTFragment fragment = new UARTFragment();
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
        View view = inflater.inflate(R.layout.fragment_uart, container, false);
        logText = view.findViewById(R.id.textView_log);                          // TextView performance decreases with number of spans
        logText.setMovementMethod(ScrollingMovementMethod.getInstance());


        return view;

    }

    @Override
    public void onResume() {
        Log.e(TAG,"onResume");
        super.onResume();
        // getActivity() can not run in constructor,must run in onResume or onCreate
        this.comm = new COMM_Serial(getActivity());
        this.comm.regListener(this);
        this.comm.open();
        this.comm.write("Test".getBytes());
    }

    @Override
    public void onPause() {
        Log.e(TAG,"onResume");
        super.onPause();
        if(this.comm != null){
            this.comm.rmListener(this);
            this.comm.close();
            this.comm=null;
        }

    }
}