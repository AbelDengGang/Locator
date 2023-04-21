package com.dr.libloc.sensor;

import com.dr.libloc.attribute.SensorRecorderItem;
import com.dr.libloc.comm.COMM;
import com.rfid_WULIANKEJIR200.cmd.ConstCode;
import com.rfid_WULIANKEJIR200.cmd.parser.ByteArrayUtils;
import com.rfid_WULIANKEJIR200.cmd.parser.ParamHashMap;

public class SensorData_RFID extends SensorData{
    public ParamHashMap paramHashMap; // use paramHashMap("typeString") to get data
    public String typeString;
    private String ID;
    private byte antChan;
    private byte RSSI;
    private int cmdType;

    public String toXML(){
        return "<item type=\"RFID\" " + "ID=\"" + ID + "\" antChan=\"" + antChan + "\" RSSI=\"" + RSSI + "\" timeInMS=\"" + timeInMS + "\" />";
    };

    public void fromXML(String attrite){
        String[] split = attrite.split(" ");
        for (String str: split){
            String[] spl = str.split("=");
            if(spl[0].equals("type")){
//                type = spl[1].split("\"")[1];
            }else if(spl[0].equals("ID")){
                ID = spl[1].split("\"")[1];
            }else if (spl[0].equals("antChan")){
                antChan = Byte.parseByte(spl[1].split("\"")[1]);
            }else if(spl[0].equals("RSSI")){
                RSSI = Byte.parseByte(spl[1].split("\"")[1]);
            }
        }
        cmdType = ConstCode.FRAME_CMD_READ_MULTI;
    };

    public SensorData_RFID() {
        super("SensorData_RFID", "SensorData_RFID");
    }

    public SensorData_RFID(String type, String name, String ID, byte antChan, byte RSSI) {
        super(type, name);
        this.ID = ID;
        this.antChan = antChan;
        this.RSSI = RSSI;
        this.cmdType = ConstCode.FRAME_CMD_READ_MULTI;
    }
    public SensorData_RFID(ParamHashMap paramHashMap){
        super("SensorData_RFID", "SensorData_RFID");
        this.paramHashMap = paramHashMap;
        cmdType = paramHashMap.getCommandType();
        timeInMS = System.currentTimeMillis();

        if (cmdType == ConstCode.FRAME_CMD_READ_SINGLE ||
                cmdType == ConstCode.FRAME_CMD_READ_MULTI){
            ID = ByteArrayUtils.toHexString(paramHashMap.get("EPC"),false);
            antChan = paramHashMap.get("ANT")[0];
            RSSI = paramHashMap.get("RSSI")[0];
        }
    }
    public int getCommandType(){
        return cmdType;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public byte getAntChan() {
        return antChan;
    }

    public void setAntChan(byte antChan) {
        this.antChan = antChan;
    }

    public byte getRSSI() {
        return RSSI;
    }

    public void setRSSI(byte RSSI) {
        this.RSSI = RSSI;
    }

    public String toString(){
        if (cmdType == ConstCode.FRAME_CMD_READ_SINGLE ||
                cmdType == ConstCode.FRAME_CMD_READ_MULTI) {
            String strEPC = "EPC:" + ID;
            String strAnt = "ANT:" + Integer.toHexString(antChan & 0xFF);
            String strRSSI = "RSSI:" + Integer.toHexString(RSSI & 0xFF);
            String strTime = "[" + Long.toString(timeInMS) + "]";
            return strTime + "," + strEPC + "," + strAnt + "," + strRSSI;
        }else if (paramHashMap != null){
            return paramHashMap.toString();
        }else{
            return "Unknow data type: " + super.toString();
        }
    }
}
