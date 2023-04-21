package com.dr.libloc.comm;

public interface CommUpdateListener {
    void onNewData(COMM sender,byte[] data);
    void onCommEvent(COMM sender, Exception e,String msg,int code);
}
