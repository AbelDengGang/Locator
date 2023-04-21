package com.dr.libloc.sensor;

import android.app.Activity;

import com.dr.libloc.comm.COMM;
import com.dr.libloc.comm.COMM_Serial;
import com.dr.libloc.util.CommandQueue;
import com.rfid_WULIANKEJIR200.cmd.Commands;
import com.rfid_WULIANKEJIR200.cmd.ConstCode;
import com.rfid_WULIANKEJIR200.cmd.parser.PacketDetailParser;
import com.rfid_WULIANKEJIR200.cmd.parser.PacketParser;
import com.rfid_WULIANKEJIR200.cmd.parser.ParamHashMap;

public class DRSensor_RFID_WULIANKJ_R200 extends DRSensor implements PacketParser.OnPacketParseListener{
    private PacketDetailParser packetDetailParser = new PacketDetailParser();
    private PacketParser packetParser = new PacketParser(this);
    private CommandQueue cmdQueue;

    public DRSensor_RFID_WULIANKJ_R200(Activity activity, String name) {
        super(activity,"WULIANKJ_R200", name);
        cmdQueue = new CommandQueue();
        cmdQueue.start();
    }

    @Override
    public void onNewData(COMM sender, byte[] data){
        // Feed data to packet parser, packet parser will collect and compose data into a valid package.
        // When a valid packet is recieved, packetParser will call onPacketReceived
        packetParser.onDataReceived(data);
    }

    @Override
    public void onCommEvent(COMM sender, Exception e,String msg,int code){

    }

    public void start(){
        //TODO init sensor
        this.comm = new COMM_Serial(activity);
        this.comm.open();
        resume();
    }
    public  void pause(){
        this.comm.rmListener(this);
    }
    public  void resume(){
        // regist listerner to comm
        packetParser.reset();
        this.comm.regListener(this);
        sendUse2AntCmd();
    }

    public void stop(){
        //remove listener from comm
        pause();
        this.comm.close();
        this.comm = null;
    }
    /**
     * 通过串口发送数据包
     */
    private void sendPacket(byte[] sendPacket) {
        if (comm != null) {
            comm.write(sendPacket);
        }
    }
    private void _sendGetFirmwareVersionCmd(){
        sendPacket(Commands.buildGetFirmwareVersionFrame());
    }
    public void sendGetFirmwareVersionCmd(){
        this.cmdQueue.addCmd(this,"_sendGetFirmwareVersionCmd",100);
    }

    private void _sendUse2AndCmd(){
        byte[] antData = new byte[]{(byte)0xBB,0x00,0x1B,0x00,0x03,0x02,0x10,0x10,0x40,0x7E};
        sendPacket(antData);
    }
    public void sendUse2AntCmd(){
        this.cmdQueue.addCmd(this,"_sendUse2AndCmd",100);
    }

    private void _sendReadMultiFrame(Object...args){
        sendPacket(Commands.buildReadMultiFrame((Integer)args[0]));
    }
    public void sendReadMultiFrame(int loopNum){
        this.cmdQueue.addCmd(this,"_sendReadMultiFrame", new Integer[]{loopNum},10);
    }

    private void _sendGetSoftwareVersionCmd(){
        sendPacket(Commands.buildGetSoftwareVersionFrame());
    }
    public void sendGetSoftwareVersionCmd(){
        this.cmdQueue.addCmd(this,"_sendGetSoftwareVersionCmd", 10);
    }

    private void _sendGetManufacturesInfoCmd(){
        sendPacket(Commands.buildGetManufacturersInfoFrame());
    }
    public void sendGetManufacturesInfoCmd(){
        this.cmdQueue.addCmd(this,"_sendGetManufacturesInfoCmd", 10);
    }

    private void _sendReadSingleCmd(){
        sendPacket(Commands.buildReadSingleFrame());
    }
    public void sendReadSingleCmd(){
        this.cmdQueue.addCmd(this,"_sendReadSingleCmd", 10);
    }

    private void _sendStopReadCmd(){
        sendPacket(Commands.buildStopReadFrame());
    }
    public void sendStopReadCmd(){
        this.cmdQueue.addCmd(this,"_sendStopReadCmd", 0);
    }

    private void _sendSetRegionCmd(Object...args){
        byte regionCode = (byte)args[0];
        sendPacket(Commands.buildSetRegionFrame(regionCode));
    }
    public void sendSetRegionCmd(byte regionCode){
        this.cmdQueue.addCmd(this,"_sendSetRegionCmd", new Byte[]{regionCode},100);
    }

    private void _sendGetPaPowerCmd(){
        sendPacket(Commands.buildGetPaPowerFrame());
    }
    public void sendGetPaPowerCmd(){
        this.cmdQueue.addCmd(this,"_sendGetPaPowerCmd", 100);
    }

    private void _sendSetPaPowerCmd(Object...args){
        sendPacket(Commands.buildSetPaPowerFrame((Integer)args[0]));
    }
    public void sendSetPaPowerCmd(int power){
        this.cmdQueue.addCmd(this,"_sendSetPaPowerCmd", new Integer[]{power},100);
    }

    private void _sendSetChannelCmd(Object...args){
        sendPacket(Commands.buildSetChannelFrame((byte) ((Integer)args[0] & 0xFF)));
    }
    public void sendSetChannelCmd(int channel){
        this.cmdQueue.addCmd(this,"_sendSetChannelCmd", new Integer[]{channel},100);
    }

    private void _sendGetChannelCmd(){
        sendPacket(Commands.buildGetChannelFrame());
    }
    public void sendGetChannelCmd(){
        this.cmdQueue.addCmd(this,"_sendGetChannelCmd", 100);
    }

    private void _sendGetQueryCmd(){
        sendPacket(Commands.buildGetQueryFrame());
    }
    public void sendGetQueryCmd(){
        this.cmdQueue.addCmd(this,"_sendGetQueryCmd", 100);
    }

    private void _sendSetEnFhssCmd(){
        sendPacket(Commands.buildSetFhssFrame(true));
    }
    public void sendSetEnFhssCmd(){
        this.cmdQueue.addCmd(this,"_sendSetEnFhssCmd", 100);
    }

    private void _sendSetCancelFhssCmd(){
        sendPacket(Commands.buildSetFhssFrame(false));
    }
    public void sendSetCancelFhssCmd(){
        this.cmdQueue.addCmd(this,"_sendSetCancelFhssCmd", 100);
    }

    @Override
    public void onPacketReceived(byte[] packet) {
        // receive a valid package.
        ParamHashMap paramHashMap = packetDetailParser.parsePacket(packet);
        SensorData_RFID data = new SensorData_RFID(paramHashMap);
        notify(data);
    }
}
