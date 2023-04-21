package com.dr.libloc.mapUtil;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.dr.libloc.sensor.SensorData;
import com.dr.libloc.sensor.SensorData_RFID;
import com.dr.libloc.toast.ToastText;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;

public class SensorRecoderSaveXML {

    private Document document = null;
    private Element nodes = null;
    private String TAG = SensorRecoderSaveXML.class.toGenericString();
    private String XMLPath;
    private Context context;
    private FileOutputStream fos = null;

    public SensorRecoderSaveXML(Context context, String XMLPath) {
        this.context = context;
        this.XMLPath = XMLPath;
    }

    public void initXMLFile(){
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = null;
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            document = documentBuilder.newDocument();
            document.setXmlStandalone(true);
            document.setXmlVersion("1.0");
            nodes = document.createElement("nodes");
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void saveRecorder(){
        try {
            if(fos != null){
                fos.write("</items>".getBytes());
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
            fos.write("<items>".getBytes());
            fos.write("\n".getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveXMLFile(){
        if(document != null){
            try {
                document.appendChild(nodes);

                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();

                // 转成string
                transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//            transformer.setOutputProperty(OutputKeys.CDATA_SECTION_ELEMENTS, ".xml");

                StringWriter stringWriter = new StringWriter();
                transformer.transform(new DOMSource(document), new StreamResult(stringWriter));

                //xml文件的序列号器  帮助生成一个xml文件
                FileOutputStream fos = new FileOutputStream(new File(XMLPath));
                fos.write(stringWriter.toString().getBytes());
                fos.flush();
                fos.close();

                document = null;
                nodes = null;
                ToastText.toastSaveXMLToPath(context, XMLPath);
            } catch (TransformerConfigurationException e) {
                e.printStackTrace();
                ToastText.toastSaveXMLError(context);
                Log.e(TAG, "Save the error: ", e);
            } catch (TransformerException e) {
                ToastText.toastSaveXMLError(context);
                Log.e(TAG, "Save the error: ", e);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                ToastText.toastSaveXMLError(context);
                Log.e(TAG, "Save the error: ", e);
            } catch (IOException e) {
                e.printStackTrace();
                ToastText.toastSaveXMLError(context);
                Log.e(TAG, "Save the error: ", e);
            }
        }
    }

    public void addElement(SensorData data, long timeInMS){
        SensorData_RFID dataRFID = (SensorData_RFID)  data;
        try {
            if(document != null && nodes != null){
                Element node = document.createElement("node");
                node.setAttribute("ID", dataRFID.getID());
                node.setAttribute("antChan", String.format("%d", dataRFID.getAntChan()));
                node.setAttribute("RSSI", String.format("%d",dataRFID.getRSSI()));
                node.setAttribute("type", dataRFID.getType());
                node.setAttribute("name", dataRFID.getDRName());
                node.setAttribute("timeInMS", timeInMS + "");
                nodes.appendChild(node);
            }
//            Log.e(TAG, "Error adding node, not initialized \n");
        }catch (Exception e){
            Log.e(TAG, "Error adding a node :", e);
            ToastText.toastAddXmlDataError(context);
        }
    }

    public String getXMLPath() {
        return XMLPath;
    }

    public void setXMLPath(String XMLPath) {
        this.XMLPath = XMLPath;
    }

    public boolean getDocument() {
        if(document != null)
            return true;
        else
            return false;
    }

    public FileOutputStream getFos() {
        return fos;
    }

    public void setFos(FileOutputStream fos) {
        this.fos = fos;
    }

    public void setDocument(Document document) {
        this.document = document;
    }
}
