package com.dr.libloc.mapUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class LocatorRecoderReadXML {

    private String XMLPath;
    private FileInputStream fis = null;
    private Reader reader = null;
    private BufferedReader bufferedReader = null;

    public LocatorRecoderReadXML(String XMLPath) {
        this.XMLPath = XMLPath;
    }

    public void closeRecode(){
        if (reader != null) {
            try {
                reader.close();
                reader = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (fis != null) {
            try {
                fis.close();
                fis = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (bufferedReader != null) {
            try {
                bufferedReader.close();
                bufferedReader = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String readRecodeLine(){
        String temp = null;

        if(bufferedReader != null){
            try {
                if((temp = bufferedReader.readLine()) !=null ){
                    if(temp.equals("<?xml version=\"1.0\" encoding=\"utf-8\"?>")){
                        temp = "true";
                    }else if(temp.equals("<Tracks>")){
                        temp = "true";
                    }else if(temp.equals("</Tracks>")){
                        temp = null;
                        closeRecode();
                    }
                }else {
                    closeRecode();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return temp;
    }

    public void loadRecode(){
        File file = new File(XMLPath);
        if(!file.exists()){
            return;
        }
        try {
            fis = new FileInputStream(file);
            //字符流
            reader = new InputStreamReader(fis);
            //缓冲流
            bufferedReader = new BufferedReader(reader);
//            StringBuilder result = new StringBuilder();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
