package com.rfid_WULIANKEJIR200.cmd.parser;

public interface onDataReceiverListener {
    /**
     * 当串口接收到消息时触发
     * @param data 接受到的字节数组
     */
    void onDataReceived(byte[] data);
}
