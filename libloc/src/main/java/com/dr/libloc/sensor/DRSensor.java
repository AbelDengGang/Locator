package com.dr.libloc.sensor;

import android.app.Activity;

import com.dr.libloc.comm.COMM;
import com.dr.libloc.comm.CommUpdateListener;

import java.util.ArrayList;

public abstract class DRSensor implements CommUpdateListener {
    protected String DRname;
    protected String type;
    protected COMM comm;
    protected Activity activity;
    protected ArrayList<SensorUpdateListener> listenerList;

    public DRSensor(Activity activity, String type, String name) {
        this.activity = activity;
        this.type = type;
        this.DRname = name;
        this.listenerList = new ArrayList<SensorUpdateListener>();
    }

    public String getDRName() {
        return DRname;
    }

    public String getType() {
        return type;
    }

    public boolean regListener(SensorUpdateListener listener){
        if(this.listenerList.contains(listener)){
            return true;
        }else{
            this.listenerList.add(listener);
            return true;
        }
    }
    protected void notify(SensorData data){
        for (SensorUpdateListener listener:listenerList
        ) {
            listener.onSensorUpdate(this,data);
        }
    }

    public void playbackItem(SensorData data){
        notify(data);
    }

    public void rmListener(SensorUpdateListener listener){
        this.listenerList.remove(listener);
    }

    public void setComm(COMM comm){
        this.comm = comm;
    }

    public abstract void start();
    public abstract void stop();
    public abstract void pause();
    public abstract void resume();
}
