package com.dr.libloc.attribute;
import android.util.Log;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.HashMap;

public class DRRoad {

    // 地图上的路径信息
    public String name;
    public HashMap<String,DRRoadVertex> vertexes; // 路径点
    public HashMap<String,DRRoadArc>  road_arcs;  // 路

    DRRoadConn conn;

    private static final String TAG = DRRoad.class.toString();

    public DRRoad() {
        init();
    }


    void init(){
        this.vertexes = new HashMap<String,DRRoadVertex>();
        this.road_arcs = new HashMap<String,DRRoadArc>();
        this.conn = new DRRoadConnMatrix(); // 使用邻接矩阵
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void loadFromDomElement(Element node){
        Log.e(TAG,node.toString());
        NodeList nodeListVertex = node.getElementsByTagName("vertex");
        for (int i = 0;i < nodeListVertex.getLength();i++){
            Element element = (Element) nodeListVertex.item(i);
            String name = element.getAttribute("name");
            DRRoadVertex vertex = new DRRoadVertex();
            vertex.setName(name);
            vertex.setX(Float.parseFloat(element.getAttribute("x")));
            vertex.setY(Float.parseFloat(element.getAttribute("y")));
            vertex.setZ(Float.parseFloat(element.getAttribute("z")));
            Log.i(TAG,vertex.toString());
            this.vertexes.put(name,vertex);
        }


        NodeList nodeListArc = node.getElementsByTagName("arc");
        for (int i = 0;i < nodeListArc.getLength();i++){
            Element element = (Element) nodeListArc.item(i);
            String name = element.getAttribute("name");
            DRRoadArc arc = new DRRoadArc();
            arc.setName(name);
            arc.setVertext0Name(element.getAttribute("vertex0"));
            arc.setVertext1Name(element.getAttribute("vertex1"));
            //Try
            arc.setVetext0(this.vertexes.get(arc.vertext0Name));
            arc.setVetext1(this.vertexes.get(arc.vertext1Name));
            arc.calLinearParam();
            arc.setWidth(Float.parseFloat(element.getAttribute("width")));
            Log.i(TAG,arc.toString());
            this.road_arcs.put(name,arc);
        }

    }

    public HashMap<String, DRRoadVertex> getVertexes() {
        return vertexes;
    }

    public void setVertexes(HashMap<String, DRRoadVertex> vertexes) {
        this.vertexes = vertexes;
    }

    public HashMap<String, DRRoadArc> getRoad_arcs() {
        return road_arcs;
    }

    public void setRoad_arcs(HashMap<String, DRRoadArc> road_arcs) {
        this.road_arcs = road_arcs;
    }

    public DRRoadConn getConn() {
        return conn;
    }

    public void setConn(DRRoadConn conn) {
        this.conn = conn;
    }

    public DRRoadProjectResult findProjectPoint(DR3DPoint realP){
        DRRoadProjectResult result = null;
        // TODO: 在已有的映射中进行匹配. 如果找不到，就重新计算
        if(false){
            return result;

        }else {
           // TODO: 把点投影到路线上，返回距离最近的投影点
            float minDistance = Float.MAX_VALUE;
            float MAX_PROJECT_DISTANCE = Float.MAX_VALUE; // 超出这个距离就不认为是投影了
            //Collection<DRRoadArc> roads = this.road_arcs.values();
            for (DRRoadArc arc: this.road_arcs.values()){
                float tempD = arc.calDistence2D(realP);
                if (tempD > MAX_PROJECT_DISTANCE){
                    continue;
                }
                if (tempD > minDistance){
                    continue;
                }
                // check and replace

                DR3DPoint tempProjectP = arc.porject2D(realP);
                if (arc.isPointInArc(tempProjectP)){
                    // OK replace it
                    if (result == null){
                        result = new DRRoadProjectResult();
                    }
                    minDistance = tempD;
                    result.distance = minDistance;
                    result.projectP = tempProjectP;
                }

            }

           // TODO: 把得到的投影点加入映射表，以免下次再计算
            return result;
        }
    }
}

