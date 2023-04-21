package com.dr.libloc.comm;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.util.Log;

import com.dr.libloc.BuildConfig;
import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.hoho.android.usbserial.util.SerialInputOutputManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executors;


public class COMM_Serial extends COMM implements  SerialInputOutputManager.Listener{
    private UsbPermission usbPermission = UsbPermission.Unknown;
    private static final String INTENT_ACTION_GRANT_USB = BuildConfig.LIBRARY_PACKAGE_NAME + ".GRANT_USB";
    private static final int WRITE_WAIT_MILLIS = 2000;
    private static final int READ_WAIT_MILLIS = 2000;

    private int baudRate;

    private int portNum;
    private int selectedDeviceId; // which device is used, -1 means use the first on in device list
    private UsbSerialPort usbSerialPort;
    private SerialInputOutputManager usbIoManager;
    private boolean connected;
    private BroadcastReceiver broadcastReceiver;
    static String TAG="COMM_Serial";
    class ListItem {
        UsbDevice device;
        int port;
        UsbSerialDriver driver;

        ListItem(UsbDevice device, int port, UsbSerialDriver driver) {
            this.device = device;
            this.port = port;
            this.driver = driver;
        }
    }
    private enum UsbPermission { Unknown, Requested, Granted, Denied };
    private ArrayList<ListItem> listItems = new ArrayList<>();

    public int getSelectedDeviceId() {
        return selectedDeviceId;
    }

    public void setSelectedDeviceId(int selectedDeviceId) {
        this.selectedDeviceId = selectedDeviceId;
    }

    public COMM_Serial(Activity activity) {

        super(activity,"UART", "UART");
        this.baudRate = 115200;
        this.portNum = 0;
        this.connected = false;
        this.selectedDeviceId = -1;
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(INTENT_ACTION_GRANT_USB)) {
                    usbPermission = intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)
                            ? UsbPermission.Granted : UsbPermission.Denied;
                    connect();
                }
            }
        };

        refresh();
    }

    void refresh(){
        Log.d(TAG,"refresh");
        UsbManager usbManager = (UsbManager) this.activity.getSystemService(Context.USB_SERVICE);
        UsbSerialProber usbDefaultProber = UsbSerialProber.getDefaultProber();
        for(UsbDevice device : usbManager.getDeviceList().values()) {
            UsbSerialDriver driver = usbDefaultProber.probeDevice(device);
            if(driver != null) {
                for(int port = 0; port < driver.getPorts().size(); port++) {
                    Log.d(TAG, "add");
                    Log.i(TAG,driver.getClass().getSimpleName());
                    listItems.add(new ListItem(device, port, driver));
                }
            } else {
                listItems.add(new ListItem(device, 0, null));
            }
        }
    }

    public int getPortNum() {
        return portNum;
    }

    public void setPortNum(int portNum) {
        this.portNum = portNum;
    }


    public int getBaudRate() {
        return baudRate;
    }

    public void setBaudRate(int baudRate) {
        this.baudRate = baudRate;
    }


    /*
     * Serial
     */
    @Override
    public void onNewData(byte[] data) {
        Log.d(TAG,"onNewData");
        for (CommUpdateListener listener:listenerList
             ) {
            listener.onNewData(this,data);
        }
    }

    @Override
    public void onRunError(Exception e) {
        Log.d(TAG,"onRunError");
        for (CommUpdateListener listener:listenerList
        ) {
            listener.onCommEvent(this,e,"excption",-1);
        }
        disconnect();
    }

    private boolean connect() {
        Log.i(TAG,"connect");
        UsbDevice device = null;
        UsbManager usbManager =(UsbManager)activity.getSystemService(Context.USB_SERVICE);
        for(UsbDevice v : usbManager.getDeviceList().values()) {
            if (this.selectedDeviceId == -1){
                device = v;
                break;
            }
            if (v.getDeviceId() == this.selectedDeviceId){
                device = v;
                break;
            }
        }
        if(device == null) {
            Log.e(TAG,"connection failed: device not found");
            return false;
        }
        UsbSerialDriver driver = UsbSerialProber.getDefaultProber().probeDevice(device);
        if(driver == null) {
            Log.e(TAG,"connection failed: no driver for device");
            return false;
        }
        if(driver.getPorts().size() < this.portNum) {
            Log.e(TAG,"connection failed: not enough ports at device");
            return false;
        }
        usbSerialPort = driver.getPorts().get(portNum);
        UsbDeviceConnection usbConnection = usbManager.openDevice(driver.getDevice());
        if(usbConnection == null && usbPermission == UsbPermission.Unknown && !usbManager.hasPermission(driver.getDevice())) {
            usbPermission = UsbPermission.Requested;
            PendingIntent usbPermissionIntent = PendingIntent.getBroadcast(activity, 0, new Intent(INTENT_ACTION_GRANT_USB), 0);
            usbManager.requestPermission(driver.getDevice(), usbPermissionIntent);
            return false;
        }
        if(usbConnection == null) {
            if (!usbManager.hasPermission(driver.getDevice()))
                Log.e(TAG,"connection failed: permission denied");
            else
                Log.e(TAG,"connection failed: open failed");
            return false;
        }

        try {
            usbSerialPort.open(usbConnection);
            usbSerialPort.setParameters(baudRate, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
            usbIoManager = new SerialInputOutputManager(usbSerialPort, this);
            Executors.newSingleThreadExecutor().submit(usbIoManager);
            Log.i(TAG,"connected");
            connected = true;
        } catch (Exception e) {
            Log.e(TAG,"connection failed: " + e.getMessage());
            disconnect();
        }
        return true;
    }

    private void disconnect() {
        Log.i(TAG,"disconnect");
        connected = false;
        if(usbIoManager != null)
            usbIoManager.stop();
        usbIoManager = null;
        try {
            usbSerialPort.close();
        } catch (IOException ignored) {}
        usbSerialPort = null;
    }


    @Override
    public boolean open(){
        return connect();
    }

    @Override
    public boolean close(){
        disconnect();
        return true;
    }

    @Override
    public int write(byte[] data){
        if (!connected){
            Log.e(TAG,"Hasn't opened the UART device!");
            return 0;
        }
        try{
            int writeLen = usbSerialPort.write(data, WRITE_WAIT_MILLIS);
            return writeLen;
        }catch(Exception e) {
            Log.e(TAG,e.getMessage());
            return 0;
        }
    }
}
