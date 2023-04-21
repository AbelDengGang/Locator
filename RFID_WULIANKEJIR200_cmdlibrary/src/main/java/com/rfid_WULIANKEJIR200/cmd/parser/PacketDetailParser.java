package com.rfid_WULIANKEJIR200.cmd.parser;

import com.rfid_WULIANKEJIR200.cmd.Commands;
import com.rfid_WULIANKEJIR200.cmd.ConstCode;

import java.util.Arrays;

/**
 * 响应包解析器
 * <p>
 * Created by Administrator on 2018/4/23.
 */

public class PacketDetailParser {

    public ParamHashMap parsePacket(byte[] packet) {
        if (packet == null || packet.length < 7) {
            throw new IllegalArgumentException("packet error");
        }

        if (packet[0] != ConstCode.FRAME_BEGIN
                || packet[packet.length - 1] != ConstCode.FRAME_END
                || (packet[1] != ConstCode.FRAME_TYPE_ANS && packet[1] != ConstCode.FRAME_TYPE_INFO)
                || (((packet[3] << 8) & 0xFF00) | (packet[4] & 0xFF)) != packet.length - 7) {
            throw new IllegalArgumentException("packet error");
        }

        byte checkSum = Commands.calculateCheckSum(Arrays.copyOfRange(packet, 1, packet.length - 2));
        if (checkSum != packet[packet.length - 2]) {
            throw new IllegalArgumentException("packet error");
        }

        return parser(packet[2], Arrays.copyOfRange(packet, 5, packet.length - 2));
    }

    private ParamHashMap parser(byte command, byte[] params) {
        ParamHashMap result = new ParamHashMap(command);

        switch (command) {
            case (byte) 0xFF:
                parseError(result, params);
                break;
            case ConstCode.FRAME_CMD_MODULE_INFO:
                parseModuleInfo(result, params);
                break;
            case ConstCode.FRAME_CMD_READ_SINGLE:
            case ConstCode.FRAME_CMD_READ_MULTI:
                parseTagInfo(result, params);
                break;
            case ConstCode.FRAME_CMD_STOP_READ:
                result.put("stop_read_result_code", new byte[]{params[0]});
                break;
            case ConstCode.FRAME_CMD_SET_REGION:
                result.put("set_region_result_code", new byte[]{params[0]});
                break;
            case ConstCode.FRAME_CMD_SET_PA_POWER:
                result.put("set_pow_result_code", new byte[]{params[0]});
                break;
            case ConstCode.FRAME_CMD_GET_PA_POWER:
                result.put("pow", params);
                break;
            case ConstCode.FRAME_CMD_SET_SELECT_MODEL:
                result.put("set_select_mode_result_code", new byte[]{params[0]});
                break;
            case ConstCode.FRAME_CMD_SET_SELECT:
                result.put("set_select_result_code", new byte[]{params[0]});
                break;
            case ConstCode.FRAME_CMD_READ_DATA:
                parseReadTagData(result, params);
                break;
            case ConstCode.FRAME_CMD_WRITE_DATA:
                parseWriteTagData(result, params);
                break;
            case ConstCode.FRAME_CMD_SET_CHANNEL:
                result.put("set_channel_result_code", new byte[]{params[0]});
                break;
            case ConstCode.FRAME_CMD_GET_CHANNEL:
                result.put("channel_index", new byte[]{params[0]});
                break;
            case ConstCode.FRAME_CMD_LOCK:
                parsePCAndEPC(result, params);
                result.put("kill_result_code", new byte[]{params[params.length - 1]});
                break;
            case ConstCode.FRAME_CMD_KILL:
                parsePCAndEPC(result, params);
                result.put("lock_result_code", new byte[]{params[params.length - 1]});
                break;
            case ConstCode.FRAME_CMD_SET_QUERY:
                result.put("set_q_result_code", new byte[]{params[0]});
                break;
            case ConstCode.FRAME_CMD_GET_QUERY:
                result.put("para", new byte[]{params[0], params[1]});
                break;
            case ConstCode.FRAME_CMD_SET_FHSS:
                result.put("set_fhss_result_code", new byte[]{params[0]});
                break;
        }

        return result;
    }

    /**
     * 响应帧中所有的错误情况统一解析处理
     */
    private void parseError(ParamHashMap result, byte[] params) {
        result.put("err_code", new byte[]{params[0]});
        if (params.length != 1) {
            int ul = params[1] & 0xFF;
            result.put("PC", Arrays.copyOfRange(params, 2, 4));
            result.put("EPC", Arrays.copyOfRange(params, 4, 4 + ul - 2));
        }
    }

    /**
     * 解析模块信息响应帧
     */
    private void parseModuleInfo(ParamHashMap result, byte[] params) {
        if (params[0] == 0x00) { // 固件版本
            result.put("firmware_version", Arrays.copyOfRange(params, 1, params.length));
        } else if (params[0] == 0x01) { // 软件版本
            result.put("software_version", Arrays.copyOfRange(params, 1, params.length));
        } else if (params[0] == 0x02) {
            result.put("manufacturers_info", Arrays.copyOfRange(params, 1, params.length));
        }
    }

    /**
     * 解析标签信息响应帧
     */
    private void parseTagInfo(ParamHashMap result, byte[] params) {
        result.put("RSSI", Arrays.copyOfRange(params, 0, 1));
        result.put("PC", Arrays.copyOfRange(params, 1, 3));
        result.put("EPC", Arrays.copyOfRange(params, 3, 15));
        result.put("CRC", Arrays.copyOfRange(params, 15, 17));
        result.put("ANT", Arrays.copyOfRange(params, 17, 18));
    }

    /**
     * 解析读取标签响应帧
     */
    private void parseReadTagData(ParamHashMap result, byte[] params) {
        int ul = params[0] & 0xFF;
        result.put("PC", Arrays.copyOfRange(params, 1, 3));
        result.put("EPC", Arrays.copyOfRange(params, 3, 3 + ul - 2));
        result.put("DATA", Arrays.copyOfRange(params, ul + 1, params.length));
    }

    /**
     * 解析写入标签响应帧
     */
    private void parseWriteTagData(ParamHashMap result, byte[] params) {
        int ul = params[0] & 0xFF;
        result.put("PC", Arrays.copyOfRange(params, 1, 3));
        result.put("EPC", Arrays.copyOfRange(params, 3, 3 + ul - 2));
        result.put("write_data_result_code", new byte[]{params[params.length - 1]});
    }

    private void parsePCAndEPC(ParamHashMap result, byte[] params) {
        int ul = params[0] & 0xFF;
        result.put("PC", Arrays.copyOfRange(params, 1, 3));
        result.put("EPC", Arrays.copyOfRange(params, 3, 3 + ul - 2));
    }
}
