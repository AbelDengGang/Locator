package com.dr.libloc.toast;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;


public class ToastText {

    public static void toastChooseFile(Context context){
        Toast.makeText(context, "请先选择文件",  Toast.LENGTH_SHORT).show();
    }

    public static void toastEndRecorder(Context context){
        Toast.makeText(context, "请先结束文件记录",  Toast.LENGTH_SHORT).show();
    }

    public static void toastEndPlayback(Context context){
        Looper.prepare();
        Toast.makeText(context, "回放结束", Toast.LENGTH_LONG).show();
        Looper.loop();
    }

    public static void toastSaveXMLToPath(Context context, String path){
        Toast.makeText(context, "保存成功，路径为：" + path, Toast.LENGTH_SHORT).show();
    }

    public static void toastSaveXMLError(Context context){
        Toast.makeText(context, "保存文件错误", Toast.LENGTH_SHORT).show();
    }

    public static void toastAddXmlDataError(Context context){
        Toast.makeText(context, "添加数据错误", Toast.LENGTH_SHORT).show();
    }

    public static void toastCompass(Context context){
        Toast.makeText(context, "当前设备不支持指南针，请检查是否存在加速度和磁场传感器", Toast.LENGTH_SHORT).show();
    }
}
