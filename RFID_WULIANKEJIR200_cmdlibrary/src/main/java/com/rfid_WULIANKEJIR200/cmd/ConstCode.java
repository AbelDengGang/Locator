package com.rfid_WULIANKEJIR200.cmd;

/**
 * Created by Administrator on 2018/4/20.
 */

public class ConstCode{

    /* 固定标志字节 */

    /**
     * 单包数据包开始字节
     */
    public static final byte FRAME_BEGIN = (byte) 0xBB;

    /**
     * 单包数据包结束帧字节
     */
    public static final byte FRAME_END = 0x7E;

    /* 命令帧 */

    /**
     * 命令帧字节
     */
    public static final byte FRAME_TYPE_CMD = 0x00;

    /**
     * 响应帧字节
     */
    public static final byte FRAME_TYPE_ANS = 0x01;

    /**
     * 通知帧字节
     */
    public static final byte FRAME_TYPE_INFO = 0x02;

    /* 指令代码 */

    /**
     * 模块信息指令代码
     */
    public static final byte FRAME_CMD_MODULE_INFO = 0x03;

    /**
     * 单独EPC指令代码
     */
    public static final byte FRAME_CMD_READ_SINGLE = 0x22;

    /**
     * 多读EPC指令代码
     */
    public static final byte FRAME_CMD_READ_MULTI = 0x27;

    /**
     * 停止多次轮询操作的指令代码
     */
    public static final byte FRAME_CMD_STOP_READ = 0x28;

    /**
     * 设置读卡器工作区的指令代码
     */
    public static final byte FRAME_CMD_SET_REGION = 0x07;

    /**
     * 获取读卡器发射功率的指令代码
     */
    public static final byte FRAME_CMD_GET_PA_POWER = (byte) 0xB7;

    /**
     * 设置读卡器发射功率的指令代码
     */
    public static final byte FRAME_CMD_SET_PA_POWER = (byte) 0xB6;

    /**
     * 设置Select参数的指令代码
     */
    public static final byte FRAME_CMD_SET_SELECT = 0x0C;

    /**
     * Kill标签指令代码
     */
    public static final byte FRAME_CMD_KILL = 0x65;

    /**
     * Lock标签指令代码
     */
    public static final byte FRAME_CMD_LOCK = (byte) 0x82;

    /**
     * 读取标签数据指令代码
     */
    public static final byte FRAME_CMD_READ_DATA = 0x39;

    /**
     * 写标签数据指令代码
     */
    public static final byte FRAME_CMD_WRITE_DATA = 0x49;

    /**
     * 设置工作信道指令代码
     */
    public static final byte FRAME_CMD_SET_CHANNEL = (byte) 0xAB;

    /**
     * 获取工作信道指令代码
     */
    public static final byte FRAME_CMD_GET_CHANNEL = (byte) 0xAA;

    /**
     * 获取固件中Query指令代码
     */
    public static final byte FRAME_CMD_GET_QUERY = 0x0D;

    /**
     * 设置固件Query指令代码
     */
    public static final byte FRAME_CMD_SET_QUERY = 0x0E;

    /**
     * 设置自动跳频指令代码
     */
    public static final byte FRAME_CMD_SET_FHSS = (byte) 0xAD;

    /**
     * 设置Select模式指令代码
     */
    public static final byte FRAME_CMD_SET_SELECT_MODEL = 0x12;

    /* 指令参数 */

    /* ----- 模块信息 ----- */
    /**
     * 固件版本指令参数
     */
    public static final byte FRAME_PARAM_FIRMWARE_VERSION = 0x00;

    /**
     * 软件版本指令参数
     */
    public static final byte FRAME_PARAM_SOFTWARE_VERSION = 0x01;

    /**
     * 制造商信息指令参数
     */
    public static final byte FRAME_PARAM_MANUFACTURERS_INFO = 0x02;

    /* ----- 其他 ----- */

    /**
     * 保留位指令参数
     */
    public static final byte FRAME_PARAM_RESERVED = 0x22;

    /* ----- 区域代码参数 ----- */

    /**
     * 中国900MHz地区代码指令参数
     */
    public static final byte FRAME_PARAM_REGION_CHINA_900_MHZ = 0x01;

    /**
     * 中国800MHz地区代码指令参数
     */
    public static final byte FRAME_PARAM_REGION_CHINA_800_MHZ = 0x04;

    /**
     * 美国地区代码指令参数
     */
    public static final byte FRAME_PARAM_REGION_USA = 0x02;

    /**
     * 欧洲地区代码指令参数
     */
    public static final byte FRAME_PARAM_REGION_EUROPE = 0x03;

    /**
     * 韩国地区代码指令参数
     */
    public static final byte FRAME_PARAM_REGION_KOREA = 0x06;

    /**
     * 标签RFU数据存储区指令参数
     */
    public static final byte FRAME_PARAM_MEM_BANK_RFU = 0x00;

    /**
     * 标签EPC数据存储区指令参数
     */
    public static final byte FRAME_PARAM_MEM_BANK_EPC = 0x01;

    /**
     * 标签TID数据存储区指令参数
     */
    public static final byte FRAME_PARAM_MEM_BANK_TID = 0x02;

    /**
     * 标签USER数据存储区指令参数
     */
    public static final byte FRAME_PARAM_MEM_BANK_USER = 0x03;

    /**
     * 关闭truncate指令参数
     */
    public static final byte FRAME_PARAM_TRUNCATE_DISABLE = 0x00;

    /**
     * 开启truncate指令参数
     */
    public static final byte FRAME_PARAM_TRUNCATE_ENABLE = (byte) 0x80;
}
