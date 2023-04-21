package com.dr.libloc.sensor;

public class SensorData_NATIVE_COMPASS extends SensorData {
    private float values[];
    private long timeMs;

    public SensorData_NATIVE_COMPASS() {
        super("COMPASS", "COMPASS");
    }

    public SensorData_NATIVE_COMPASS(float values[], long timeMs) {
        super("COMPASS", "COMPASS");
        this.timeMs = timeMs;
        this.values = values;
    }

    public String toXML(){
        return "<item type=\"COMPASS\" direction=\"" + values[0] + "\" timeInMS=\""
                + timeInMS + "\" />";
    }

    public void fromXML(String xmlItem){
        values = new float[3];
        String[] split = xmlItem.split(" ");
        for (String str: split){
            String[] spl = str.split("=");
            if(spl[0].equals("type")){
//                type = spl[1].split("\"")[1];
            }else if(spl[0].equals("direction")){
                values[0] = Float.parseFloat(spl[1].split("\"")[1]);
            }
        }
    }

    public float[] getValues() {
        return values;
    }

    public void setValues(float[] values) {
        this.values = values;
    }

    public long getTimeMs() {
        return timeMs;
    }

    public void setTimeMs(long timeMs) {
        this.timeMs = timeMs;
    }
}
