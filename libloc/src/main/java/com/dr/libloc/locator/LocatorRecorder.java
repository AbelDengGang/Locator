package com.dr.libloc.locator;

import android.content.Context;
import android.util.Log;

import com.dr.libloc.attribute.PositionPost;
import com.dr.libloc.interfaces.RecorderInterface;
import com.dr.libloc.mapUtil.LocatorRecoderReadXML;
import com.dr.libloc.mapUtil.LocatorRecoderSaveXML;
import com.dr.libloc.toast.ToastText;

public class LocatorRecorder implements PositionPostUpdateListener, RecorderInterface {

    private String TAG = LocatorRecorder.class.toString();
    private Context context;
    private Locator locator;
    private LocatorRecoderReadXML locatorRecoderReadXML;
    private LocatorRecoderSaveXML locatorRecoderSaveXML;
    private String xmlPath;
    private String readXmlPath;
    private String timeInMs = null;
    private String type = null;
    private int waitTime = 0;

    public LocatorRecorder(Context context, String xmlPath) {
        locator = LocatorMgr.getLocator();
        this.context = context;
        this.xmlPath = xmlPath;
        this.locatorRecoderSaveXML = new LocatorRecoderSaveXML(context, xmlPath);
    }


    @Override
    public void onPPUpdate(Locator locator, PositionPost positionPost) {
        locatorRecoderSaveXML.addRecorder(positionPost.toXML());
    }

    @Override
    public void start() {
        locator.regPPUpdateListener(this);
    }

    @Override
    public void stop() {
        locator.rmPPUpdateListener(this);
    }

    private void stringXmlParser(String attrite){
        String[] split = attrite.split(" ");
        for (String str: split){
            String[] spl = str.split("=");
            if(spl[0].equals("Obj")){
                type = spl[1].split("\"")[1];
            }else if(spl[0].equals("Time")){
                if(timeInMs != null){
                    waitTime =(int) (Long.parseLong(spl[1].split("\"")[1]) - Long.parseLong(timeInMs));
                }
                timeInMs = spl[1].split("\"")[1];
            }
        }
    }

    @Override
    public void playBack() {
        locator.pause();
        stop();
        String temp = null;
        while ((temp = locatorRecoderReadXML.readRecodeLine())!=null) {
            if(!temp.equals("true")){
                stringXmlParser(temp);
                try {
                    System.out.println(waitTime);
                    if(waitTime > 0){
                        Thread.sleep(waitTime);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(type != null){
                    switch (type){
                        case "Car":
                            PositionPost positionPost = new PositionPost();
                            positionPost.fromXML(temp);
                            locator.playbackItem(positionPost);
                            break;
                    }
                }
            }
        }
        timeInMs = null;
        waitTime = 0;
        locator.resume();
        start();
    }

    @Override
    public void load() {
        try {
            locatorRecoderReadXML = new LocatorRecoderReadXML(readXmlPath);
//            sensorRecoderReadXML.load();
            locatorRecoderReadXML.loadRecode();
        }catch (Exception e){
            Log.e(TAG, "Failed to read file :", e);
        }
    }

    @Override
    public void store() {
        locatorRecoderSaveXML.saveRecorder();
    }

    public void initRecorderFile(){
        locatorRecoderSaveXML.initRecorderFile();
    }

    public String getReadXmlPath() {
        return readXmlPath;
    }

    public void setReadXmlPath(String readXmlPath) {
        this.readXmlPath = readXmlPath;
    }

    public String getXmlPath() {
        return xmlPath;
    }

    public void setXmlPath(String xmlPath) {
        this.xmlPath = xmlPath;
        locatorRecoderSaveXML.setXMLPath(xmlPath);
    }
}
