package com.rfid_WULIANKEJIR200.cmd.parser;

import com.rfid_WULIANKEJIR200.cmd.ConstCode;

import java.util.Arrays;



public class PacketParser implements onDataReceiverListener {

    private OnPacketParseListener listener;
    private byte[] buffer = new byte[4096];

    private boolean frameBeginFlag = false;
    private int frameLength = 0;
    private int frameIndex = -1;

    public PacketParser() {
        this(null);
    }

    public PacketParser(OnPacketParseListener listener) {
        this.listener = listener;
        init();
    }

    public OnPacketParseListener getListener() {
        return listener;
    }

    /**
     * 设置数据包监听接口
     *
     * @param listener
     */
    public void setListener(OnPacketParseListener listener) {
        this.listener = listener;
    }

    /**
     * 消息解析器复位，当重新复用该解析器对象前必须调用此方法
     */
    public void reset() {
        init();
    }

    private void init() {
        frameBeginFlag = false;
        frameLength = 0;
        frameIndex = -1;
        Arrays.fill(buffer, (byte) 0x00);
    }

    @Override
    public void onDataReceived(byte[] data) {
        if (data == null && data.length == 0) {
            return;
        }

        for (int i = 0; i < data.length; i++) {
            if (frameBeginFlag) {
                buffer[++frameIndex] = data[i];

                if (frameIndex == 4) { // 读到帧长度位置时
                    frameLength = (((buffer[3] << 8) & 0xFF00) | (buffer[4] & 0xFF));
                    if (frameLength > 3027) {
                        frameBeginFlag = false;
                        continue;
                    }
                } else if (frameIndex == frameLength + 6) { // 读到接受标志位
                    if (buffer[frameIndex] == ConstCode.FRAME_END) {
                        // 读到结束标志位，开始校检
                        int checksum = 0;
                        int l = 1;
                        while (l < (frameIndex - 1)) {
                            checksum += (buffer[l++] & 0xFF);
                        }
                        checksum %= 256;
                        if (checksum != (buffer[frameIndex - 1] & 0xFF)) { // 校检位不通过
                            frameBeginFlag = false;
                            continue;
                        }
                        frameBeginFlag = false;
                        if (listener != null) { // 校检通过，有回调则调用回调
                            byte[] packet = new byte[frameIndex + 1];
                            System.arraycopy(buffer, 0, packet, 0, frameIndex + 1);
                            listener.onPacketReceived(packet);
                        }
                    } else {
                        frameBeginFlag = false;
                    }
                }
            } else if (data[i] == ConstCode.FRAME_BEGIN && !frameBeginFlag) { // 检测帧开始标志位
                frameBeginFlag = true;
                frameIndex = -1;
                buffer[++frameIndex] = data[i];
            }
        }
    }

    public interface OnPacketParseListener {

        /**
         * 当有完整的数据包的时候触发
         */
        void onPacketReceived(byte[] packet);
    }
}
