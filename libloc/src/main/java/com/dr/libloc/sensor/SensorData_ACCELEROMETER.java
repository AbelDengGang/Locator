package com.dr.libloc.sensor;

import android.hardware.SensorEvent;

public class SensorData_ACCELEROMETER extends SensorData{
    private float m; // 模长
    private float values[];

    public SensorData_ACCELEROMETER() {
        super("ACCELEROMETER", "ACCELEROMETER");
    }

    public SensorData_ACCELEROMETER(SensorEvent event) {
        super("ACCELEROMETER", "ACCELEROMETER");
        this.timeInMS = eventTimeToUTCMS(event.timestamp);
        this.values = event.values;
        m = (float)Math.sqrt(Math.pow(event.values[0],2) + Math.pow(event.values[1],2) + Math.pow(event.values[2],2));
    }

    // value[0]: Acceleration minus Gx on the x-axis
    // value[1]: Acceleration minus Gy on the y-axis
    // value[2]: Acceleration minus Gz on the z-axis
    public float[] getValue(){
        return values;
    }

    public String toXML(){
        return "<item type=\"ACCELEROMETER\" x=\"" + values[0] + "\" y=\"" + values[1] + "\" z=\"" + values[2] + "\" timeInMS=\""
                + timeInMS + "\" />";
    };

    public void fromXML(String xmlItem){
        values = new float[4];
        String[] split = xmlItem.split(" ");
        for (String str: split){
            String[] spl = str.split("=");
            if(spl[0].equals("type")){
//                type = spl[1].split("\"")[1];
            }else if(spl[0].equals("x")){
                values[0] = Float.parseFloat(spl[1].split("\"")[1]);
            }else if (spl[0].equals("y")){
                values[1] = Float.parseFloat(spl[1].split("\"")[1]);
            }else if(spl[0].equals("z")){
                values[2] = Float.parseFloat(spl[1].split("\"")[1]);
            }
        }
    };


    public String toString(){
        return String.format("ax:%f, ay:%f, az:%f, m=%f\n",
                values[0],values[1],values[2],m);
    }
}
