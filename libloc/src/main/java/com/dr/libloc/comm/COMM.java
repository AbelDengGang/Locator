package com.dr.libloc.comm;


import android.app.Activity;
import android.content.Context;

import com.dr.libloc.sensor.SensorUpdateListener;

import java.util.ArrayList;

public abstract class COMM {
    protected String type;
    protected String name;
    protected Activity activity;
    protected ArrayList<CommUpdateListener> listenerList;

    public COMM(Activity activity, String type, String name) {
        this.activity = activity;
        this.type = type;
        this.name = name;
        this.listenerList = new ArrayList<CommUpdateListener>();
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public boolean regListener(CommUpdateListener listener){
        if(this.listenerList.contains(listener)){
            return true;
        }else{
            this.listenerList.add(listener);
            return true;
        }
    }

    public void rmListener(CommUpdateListener listener){
        this.listenerList.remove(listener);
    }
    abstract public boolean open();
    abstract public boolean close();
    abstract public int write(byte[] data);
}
