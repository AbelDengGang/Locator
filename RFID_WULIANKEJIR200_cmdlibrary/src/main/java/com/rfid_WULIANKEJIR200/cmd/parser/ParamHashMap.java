package com.rfid_WULIANKEJIR200.cmd.parser;


import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Administrator on 2018/4/23.
 */

public class ParamHashMap extends HashMap<String, byte[]> {

    private byte commandType;

    public ParamHashMap(byte commandType) {
        this.commandType = commandType;
    }

    public byte getCommandType() {
        return commandType;
    }

    public void setCommandType(byte commandType) {
        this.commandType = commandType;
    }

    @Override
    public String toString() {
        Iterator<Entry<String, byte[]>> iterator = entrySet().iterator();
        if (!iterator.hasNext()) {
            return "{}";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (;;) {
            Entry<String, byte[]> entry = iterator.next();
            String key = entry.getKey();
            byte[] val = entry.getValue();
            sb.append(key);
            sb.append("=");
            sb.append("[");
            sb.append(ByteArrayUtils.toHexString(val));
            sb.append("]");
            if (!iterator.hasNext()) {
                return sb.append("}").toString();
            }
            sb.append(",").append(" ");
        }
    }
}
