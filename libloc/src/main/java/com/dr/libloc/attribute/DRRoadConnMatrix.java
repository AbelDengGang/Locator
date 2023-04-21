package com.dr.libloc.attribute;

import java.util.List;

public class DRRoadConnMatrix extends DRRoadConn{
    @Override
    public void addVertext(DRRoadVertex vertex) {

    }

    @Override
    public void removeVertext(DRRoadVertex vertex) {

    }

    @Override
    public List<DRRoadArc> findRoutine(DRRoadVertex vertexSrc, DRRoadVertex vertexDst) {
        return null;
    }

    @Override
    public void addArc(DRRoadArc arc){

    }

    @Override
    public void removeArc(DRRoadArc arc){

    }

    @Override
    public DRRoadVertex createClosestVertex(float x, float y, float z) // 创建一个距离输入坐标最近的点，但是不加入地图连接{
    {
        DRRoadVertex vertex = new DRRoadVertex();
        return vertex;
    }

}
