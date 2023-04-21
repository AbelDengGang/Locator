package com.dr.libloc.mapUtil;

import com.dr.libloc.attribute.DRRoad;
import com.dr.libloc.attribute.MapItem;
import com.dr.libloc.attribute.PositionPost;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class DRMap {


//    private List<MapItem> mapItemList;

    private Map<String, MapItem> mapItem;
    private Map<String, DRRoad> roads;

//    public List<MapItem> getMapItemList() {
//        return mapItemList;
//    }

//    public void setMapItemList(List<MapItem> mapItemList) {
//        this.mapItemList = mapItemList;
//    }

    public Map<String, DRRoad> getRoads() {
        return roads;
    }

    public void setRoads(Map<String, DRRoad> roads) {
        this.roads = roads;
    }

    public Map<String, MapItem> getMapItem() {
        return mapItem;
    }

    public void setMapItem(Map<String, MapItem> mapItem) {
        this.mapItem = mapItem;
    }

    public DRMap() {
//        this.mapItemList = new ArrayList<>();
        this.mapItem = new HashMap<>();
        this.roads = new HashMap();
    }

//    public void addMapItem(MapItem mapItem){
//        this.mapItemList.add(mapItem);
//    }

    public void addMapItemKV(String name, MapItem mapItem){
        this.mapItem.put(name, mapItem);
    }

    public void setMapItem(Element node) {
        if(!node.getAttribute("type").equals("box")){
            try {
                MapItem mapItem = (MapItem) Class.forName("com.dr.libloc.drawUtil." + node.getAttribute("type")).newInstance();
//                mapItem.setWidth(Float.parseFloat(node.getAttribute("width")));
//                mapItem.setHeigth(Float.parseFloat(node.getAttribute("height")));
//                mapItem.setLength(Float.parseFloat(node.getAttribute("length")));
                PositionPost positionPost = new PositionPost();
                positionPost.setX(Float.parseFloat(node.getAttribute("x")));
                positionPost.setY(Float.parseFloat(node.getAttribute("y")));
                positionPost.setZ(Float.parseFloat(node.getAttribute("z")));
                positionPost.setXangle(Float.parseFloat(node.getAttribute("xangle")));
                positionPost.setYangle(Float.parseFloat(node.getAttribute("yangle")));
                positionPost.setZangle(Float.parseFloat(node.getAttribute("zangle")));
                mapItem.setPositionPost(positionPost);
                mapItem.setType(node.getAttribute("type"));
                mapItem.setName(node.getAttribute("name"));
                mapItem.setAttribute(node);
                addMapItemKV(node.getAttribute("name"), mapItem);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }
    public void load(InputStream inputStream) throws IOException, ParserConfigurationException, SAXException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(inputStream);
        Element root = document.getDocumentElement();

        MapItem mapItem = (MapItem)Class.forName("com.dr.libloc.drawUtil."+root.getAttribute("type")).newInstance();
        mapItem.setWidth(Float.parseFloat(root.getAttribute("width")));
        mapItem.setHeigth(Float.parseFloat(root.getAttribute("height")));
        mapItem.setLength(Float.parseFloat(root.getAttribute("length")));
        mapItem.setName(root.getAttribute("name"));
        mapItem.setType(root.getAttribute("type"));
        addMapItemKV("0", mapItem);
        NodeList nodeList = root.getElementsByTagName("node");
        for (int i = 0; i < nodeList.getLength(); i++){
            Element element = (Element) nodeList.item(i);
            setMapItem(element);
        }
        NodeList nodeListRoad = root.getElementsByTagName("roads");
        for (int i = 0;i < nodeListRoad.getLength();i++){
            Element element = (Element) nodeListRoad.item(i);
            String name = element.getAttribute("name");
            DRRoad road = new DRRoad();
            roads.put(name,road);
            road.loadFromDomElement(element);

        }

    }

    public MapItem getItem(String name){
        if(name != null){
            return mapItem.get(name);
        }
        return null;
    }

}
