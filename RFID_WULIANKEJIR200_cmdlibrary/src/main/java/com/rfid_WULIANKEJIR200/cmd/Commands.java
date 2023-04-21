package com.rfid_WULIANKEJIR200.cmd;

/**
 * Created by Administrator on 2018/4/20.
 */

public class Commands {

    /**
     * 构建一个完整的数据帧
     *
     * @param data 内容区(一个完整帧去除掉开始标志位、结束标志位和校验位)
     * @return 完整数据帧
     */
    public static byte[] buildFrame(byte... data) {
        checkBytes(data);

        byte[] result = new byte[data.length + 3];
        result[0] = ConstCode.FRAME_BEGIN;
        result[result.length - 2] = calculateCheckSum(data);
        result[result.length - 1] = ConstCode.FRAME_END;
        System.arraycopy(data, 0, result, 1, data.length);
        return result;
    }

    /**
     * 构建一个完整的数据帧
     *
     * @param fType   帧类型
     * @param cmdCode 指令代码
     * @param data    指定参数
     * @return 完整数据帧
     */
    public static byte[] buildFrame(byte fType, byte cmdCode, byte... data) {
        checkBytes(fType, cmdCode);
        checkBytes(data);

        byte[] temp = new byte[data.length + 4];
        temp[0] = fType;
        temp[1] = cmdCode;
        temp[2] = (byte) ((data.length >> 8) & 0xFF);
        temp[3] = (byte) (data.length & 0xFF);
        System.arraycopy(data, 0, temp, 4, data.length);
        return buildFrame(temp);
    }

    /**
     * 构建一个帧类型为命令帧的完整数据帧
     *
     * @param cmdCode
     * @param data
     * @return 命令帧
     */
    public static byte[] buildCMDFrame(byte cmdCode, byte... data) {
        return buildFrame(ConstCode.FRAME_TYPE_CMD, cmdCode, data);
    }

    /**
     * 构建一个用于获取固件版本的命令帧
     *
     * @return 命令帧
     */
    public static byte[] buildGetFirmwareVersionFrame() {
        return buildCMDFrame(ConstCode.FRAME_CMD_MODULE_INFO, ConstCode.FRAME_PARAM_FIRMWARE_VERSION);
    }

    /**
     * 构建一个用于获取软件版本的命令帧
     *
     * @return 命令帧
     */
    public static byte[] buildGetSoftwareVersionFrame() {
        return buildCMDFrame(ConstCode.FRAME_CMD_MODULE_INFO, ConstCode.FRAME_PARAM_SOFTWARE_VERSION);
    }

    /**
     * 构建一个用于获取制造商信息的命令帧
     *
     * @return 命令帧
     */
    public static byte[] buildGetManufacturersInfoFrame() {
        return buildCMDFrame(ConstCode.FRAME_CMD_MODULE_INFO, ConstCode.FRAME_PARAM_MANUFACTURERS_INFO);
    }

    /**
     * 构建一个单读EPC的命令帧
     *
     * @return 命令帧
     */
    public static byte[] buildReadSingleFrame() {
        return buildCMDFrame(ConstCode.FRAME_CMD_READ_SINGLE);
    }

    /**
     * 构建一个多读EPC的命令帧
     *
     * @param loopNum 轮询次数
     * @return 命令帧
     */
    public static byte[] buildReadMultiFrame(int loopNum) {
        if (loopNum <= 0 || loopNum > 65535) return new byte[0];
        return buildCMDFrame(
                ConstCode.FRAME_CMD_READ_MULTI,
                ConstCode.FRAME_PARAM_RESERVED,
                (byte) ((loopNum >> 8) & 0xFF),
                (byte) (loopNum & 0xFF));
    }

    /**
     * 构建一个立即停止多次轮询操作的命令帧
     *
     * @return 命令帧
     */
    public static byte[] buildStopReadFrame() {
        return buildCMDFrame(ConstCode.FRAME_CMD_STOP_READ);
    }

    /**
     * 构建一个设置读卡器工作区的命令帧
     *
     * @param regionCode 地区代码，目前支持：中国900MHz -> 0x01；中国800MHz -> 0x04；美国 -> 0x02；欧洲 -> 0x03；韩国 -> 0x06
     * @return 命令帧
     */
    public static byte[] buildSetRegionFrame(byte regionCode) {
        return buildCMDFrame(ConstCode.FRAME_CMD_SET_REGION, regionCode);
    }

    /**
     * 构建一个获取读卡器发射功率的命令帧
     *
     * @return 命令帧
     */
    public static byte[] buildGetPaPowerFrame() {
        return buildCMDFrame(ConstCode.FRAME_CMD_GET_PA_POWER);
    }

    /**
     * 构建一个设置读卡器发射功率的命令帧
     *
     * @param powerdBm 发射功率
     * @return 命令帧
     */
    public static byte[] buildSetPaPowerFrame(int powerdBm) {
        if (powerdBm < 0) return new byte[0];
        return buildCMDFrame(
                ConstCode.FRAME_CMD_SET_PA_POWER,
                (byte) ((powerdBm >> 8) & 0xFF),
                (byte) (powerdBm & 0xFF));
    }

    /**
     * 构建一个设置Select参数的命令帧
     *
     * @param target
     * @param action
     * @param memBank   标签数据存储区
     * @param pointer
     * @param truncated 是否truncated
     * @param mask      mask内容
     * @return 命令帧
     */
    public static byte[] buildSetSelectFrame(byte target, byte action, byte memBank, long pointer, boolean truncated, byte[] mask) {
        byte selParam = (byte) ((target << 5 | ((action & 0x07) << 2) | (memBank & 0x03)) & 0xFF);
        byte lenByte = (byte) ((mask.length * 8) & 0xFF);

        byte[] data = new byte[7 + mask.length];
        data[0] = selParam;
        data[1] = (byte) ((pointer >> 24) & 0xFF);
        data[2] = (byte) ((pointer >> 16) & 0xFF);
        data[3] = (byte) ((pointer >> 8) & 0xFF);
        data[4] = (byte) (pointer & 0xFF);
        data[5] = lenByte;
        data[6] = truncated ? ConstCode.FRAME_PARAM_TRUNCATE_ENABLE : ConstCode.FRAME_PARAM_TRUNCATE_DISABLE;
        System.arraycopy(mask, 0, data, 7, mask.length);

        return buildCMDFrame(ConstCode.FRAME_CMD_SET_SELECT, data);
    }

    /**
     * 构建一个灭活标签的命令帧
     *
     * @param password kill密码
     * @return 命令帧
     */
    public static byte[] buildKillFrame(byte[] password) {
        return buildCMDFrame(ConstCode.FRAME_CMD_KILL, password);
    }

    /**
     * 构建一个锁定或解锁标签的命令帧
     *
     * @param accessPassword access密码
     * @param ld             Lock操作数，详细含义请参见EPC Gen2协议1.2.0版6.3.2.11.3.5节
     * @return 命令帧
     */
    public static byte[] buildLockFrame(byte[] accessPassword, int ld) {
        int acccessPwdLength = accessPassword.length;
        byte[] dataBytes = new byte[acccessPwdLength + 3];
        System.arraycopy(accessPassword, 0, dataBytes, 0, acccessPwdLength);
        dataBytes[acccessPwdLength++] = (byte) ((ld >> 16) & 0xFF);
        dataBytes[acccessPwdLength++] = (byte) ((ld >> 8) & 0xFF);
        dataBytes[acccessPwdLength++] = (byte) (ld & 0xFF);
        return buildCMDFrame(ConstCode.FRAME_CMD_LOCK, dataBytes);
    }

    /**
     * 构建一个读取标签数据的命令帧
     *
     * @param accessPassword access密码
     * @param memBank        标签数据区
     * @param sa             读标签数据区地址偏移
     * @param dl             读标签数据区地址长度
     * @return 命令帧
     */
    public static byte[] buildReadDataFrame(byte[] accessPassword, byte memBank, int sa, int dl) {
        int accessPwdL = accessPassword.length;
        byte[] dataBytes = new byte[accessPwdL + 5];
        System.arraycopy(accessPassword, 0, dataBytes, 0, accessPwdL);
        dataBytes[accessPwdL++] = memBank;
        dataBytes[accessPwdL++] = (byte) ((sa >> 8) & 0xFF);
        dataBytes[accessPwdL++] = (byte) (sa & 0xFF);
        dataBytes[accessPwdL++] = (byte) ((dl >> 8) & 0xFF);
        dataBytes[accessPwdL++] = (byte) (dl & 0xFF);
        return buildCMDFrame(ConstCode.FRAME_CMD_READ_DATA, dataBytes);
    }

    /**
     * 构建一个写入标签数据的命令帧
     *
     * @param accessPassword access密码
     * @param memBank        标签数据区
     * @param sa             标签数据区地址偏移
     * @param dl             标签数据区地址长度
     * @param dt             写入数据
     * @return 命令帧
     */
    public static byte[] buildWriteDataFrame(byte[] accessPassword, byte memBank, int sa, int dl, byte[] dt) {
        int accessPwdL = accessPassword.length;
        int dtL = dt.length;
        byte[] dataBytes = new byte[accessPwdL + dtL + 5];
        System.arraycopy(accessPassword, 0, dataBytes, 0, accessPwdL);
        dataBytes[accessPwdL++] = memBank;
        dataBytes[accessPwdL++] = (byte) ((sa >> 8) & 0xFF);
        dataBytes[accessPwdL++] = (byte) (sa & 0xFF);
        dataBytes[accessPwdL++] = (byte) ((dl >> 8) & 0xFF);
        dataBytes[accessPwdL++] = (byte) (dl & 0xFF);
        System.arraycopy(dt, 0, dataBytes, accessPwdL, dtL);
        return buildCMDFrame(ConstCode.FRAME_CMD_WRITE_DATA, dataBytes);
    }

    /**
     * 构建一个设置工作信道的命令帧
     *
     * @param ch 信道代码
     * @return 命令帧
     */
    public static byte[] buildSetChannelFrame(byte ch) {
        return buildCMDFrame(ConstCode.FRAME_CMD_SET_CHANNEL, ch);
    }

    /**
     * 构建一个获取工作信道的命令帧
     *
     * @return 命令帧
     */
    public static byte[] buildGetChannelFrame() {
        return buildCMDFrame(ConstCode.FRAME_CMD_GET_CHANNEL);
    }

    /**
     * 构建一个获取固件Query值的命令帧
     *
     * @return 命令帧
     */
    public static byte[] buildGetQueryFrame() {
        return buildCMDFrame(ConstCode.FRAME_CMD_GET_QUERY);
    }

    /**
     * 构建一个设置固件Query值的命令帧
     *
     * @param dr
     * @param m
     * @param tREXT
     * @param sel
     * @param session
     * @param targer
     * @param q       Q值
     * @return 命令帧
     */
    public static byte[] buildSetQueryFrame(int dr, int m, int tREXT, int sel, int session, int targer, int q) {
        byte msb = (byte) (dr << 7 | m << 5 | tREXT << 4 | sel << 2 | session);
        byte lsb = (byte) (targer << 7 | q << 3);
        return buildCMDFrame(ConstCode.FRAME_CMD_SET_QUERY, msb, lsb);
    }

    /**
     * 构建一个设置固件为自动跳频模式或者取消自动跳频模式的命令帧
     *
     * @param isAutoFhss true：自动跳频; false:取消自动跳频
     * @return 命令帧
     */
    public static byte[] buildSetFhssFrame(boolean isAutoFhss) {
        return buildCMDFrame(ConstCode.FRAME_CMD_SET_FHSS, isAutoFhss ? (byte) 0xFF : 0x00);
    }

    /**
     * 构建一个设置Select模式的命令帧
     *
     * @param selectModel Select模式
     * @return 命令帧
     */
    public static byte[] buildSetSelectModelFrame(byte selectModel) {
        return buildCMDFrame(ConstCode.FRAME_CMD_SET_SELECT_MODEL, selectModel);
    }

    /**
     * 计算校验位
     *
     * @param data 需校验的数组
     * @return 校验值
     */
    public static byte calculateCheckSum(byte[] data) {
        checkBytes(data);

        int checksum = 0;
        for (byte item : data) {
            checksum += (item & 0xFF);
        }
        checksum %= 256;
        return (byte) checksum;
    }

    private static void checkBytes(byte... data) {
        if (data == null) {
            throw new NullPointerException();
        }
    }
}
