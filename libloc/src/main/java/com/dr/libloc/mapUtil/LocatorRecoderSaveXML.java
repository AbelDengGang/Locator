package com.dr.libloc.mapUtil;

import android.content.Context;
import android.util.Log;

import com.dr.libloc.toast.ToastText;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class LocatorRecoderSaveXML {
    private String XMLPath;
    private Context context;
    private String TAG = LocatorRecoderSaveXML.class.toString();
    private FileOutputStream fos = null;

    public LocatorRecoderSaveXML(Context context, String XMLPath) {
        this.context = context;
        this.XMLPath = XMLPath;
    }


    public void saveRecorder(){
        try {
            if(fos != null){
                fos.write("</Tracks>".getBytes());
                fos.close();
                fos = null;
            }
        }catch (IOException e){
            Log.e(TAG, "fail save file", e);
        }
        ToastText.toastSaveXMLToPath(context, XMLPath);
    }

    public void addRecorder(String recorder){
        try {
            if(fos != null){
                fos.write(recorder.getBytes());
                fos.write("\n".getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initRecorderFile(){
        File file = new File(XMLPath);
        try {
            fos = new FileOutputStream(file);
            fos.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>".getBytes());
            fos.write("\n".getBytes());
            fos.write("<Tracks>".getBytes());
            fos.write("\n".getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getXMLPath() {
        return XMLPath;
    }

    public void setXMLPath(String XMLPath) {
        this.XMLPath = XMLPath;
    }
}
