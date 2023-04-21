package com.dr.libloc.attribute;

import java.util.HashMap;
import java.util.List;

abstract public class DRRoadConn {

    public abstract void addVertext(DRRoadVertex vertex);
    public abstract void removeVertext(DRRoadVertex vertex);
    public abstract void addArc(DRRoadArc arc);
    public abstract void removeArc(DRRoadArc arc);
    public abstract List<DRRoadArc> findRoutine(DRRoadVertex vertexSrc, DRRoadVertex vertexDst); // 在返回从源点到目标点的路径
    public abstract DRRoadVertex createClosestVertex(float x, float y, float z); // 创建一个距离输入坐标最近的点，但是不加入地图连接
}
