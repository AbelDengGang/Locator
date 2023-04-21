package com.dr.libloc.mapUtil;

import com.dr.libloc.attribute.SensorRecorderItem;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class SensorRecoderReadXML {

    private String XMLPath;
    private List<SensorRecorderItem> sensorRecorderItems;
    private FileInputStream fis = null;
    private Reader reader = null;
    private BufferedReader bufferedReader = null;

    public SensorRecoderReadXML(String XMLPath) {
        this.XMLPath = XMLPath;
        sensorRecorderItems = new ArrayList<>();
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
                    }else if(temp.equals("<items>")){
                        temp = "true";
                    }else if(temp.equals("</items>")){
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



    public void load(){
        File xmlFile = new File(XMLPath);
        if(xmlFile.exists()){
            StringBuilder sb = new StringBuilder();
            sb.append(" -- Document --\n");
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = null;
            try {

                documentBuilder = documentBuilderFactory.newDocumentBuilder();
                FileInputStream fileInputStream = new FileInputStream(xmlFile);
                Document document = documentBuilder.parse(fileInputStream);
                Element root = document.getDocumentElement();
                NodeList nodes = root.getElementsByTagName("node");
                for(int i = 0; i < nodes.getLength(); i++){
                    Element node = (Element)nodes.item(i);
                    SensorRecorderItem item = new SensorRecorderItem();
                    item.setAntChan(Integer.parseInt(node.getAttribute("antChan")));
                    item.setData(node.getAttribute("data"));
                    item.setID(node.getAttribute("ID"));
                    item.setName(node.getAttribute("name"));
                    item.setRSSI(node.getAttribute("RSSI"));
                    item.setType(node.getAttribute("type"));
                    long timeInMS = Long.parseLong(node.getAttribute("timeInMS"));
                    if(i < (nodes.getLength() - 1)){
                        Element element = (Element)nodes.item(i+1);
                        int waitTime = (int)(Long.parseLong(element.getAttribute("timeInMS")) - Long.parseLong(node.getAttribute("timeInMS")));
                        item.setTimeInMS(waitTime);
                    }
                    sensorRecorderItems.add(item);
                }
            } catch (ParserConfigurationException | FileNotFoundException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

    public List<SensorRecorderItem> getSensorRecorderItems() {
        return sensorRecorderItems;
    }

    public void setSensorRecorderItems(List<SensorRecorderItem> sensorRecorderItems) {
        this.sensorRecorderItems = sensorRecorderItems;
    }
}
