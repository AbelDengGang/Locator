package com.dr.libloc.locator;

import com.dr.libloc.attribute.DR3DPoint;
import com.dr.libloc.attribute.DRMapCell;
import com.dr.libloc.attribute.DRRoad;
import com.dr.libloc.attribute.PositionPost;
import com.dr.libloc.attribute.DRRoadProjectResult;
import com.dr.libloc.mapUtil.DRMap;
import com.dr.libloc.sensor.DRSensorMgr;
import com.dr.libloc.sensor.SensorUpdateListener;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class Locator implements SensorUpdateListener {

    protected DRSensorMgr drSensorMgr;
    private String type;
    private String DRname;
    protected DRMap DRMap;
    private List<PositionPostUpdateListener> ppuList;

    public Locator() {
        this.ppuList = new ArrayList<>();
        this.drSensorMgr = DRSensorMgr.getMgr();
    }


    public void regPPUpdateListener(PositionPostUpdateListener listener){
        ppuList.add(listener);
    }

    public void rmPPUpdateListener(PositionPostUpdateListener listener){
        ppuList.remove(listener);
    }

    // calculate current's postion and post, and call notify if update
    public abstract void calPostionPost();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public DRMap getDRMap() {
        return DRMap;
    }

    public void setDRMap(DRMap DRMap) {
        this.DRMap = DRMap;
    }

    public DRSensorMgr getDrSensorMgr() {
        return drSensorMgr;
    }

    public void setDrSensorMgr(DRSensorMgr drSensorMgr) {
        this.drSensorMgr = drSensorMgr;
    }

    public String getDRname() {
        return DRname;
    }

    public void setDRname(String DRname) {
        this.DRname = DRname;
    }

    public void notify(PositionPost data){
        for (PositionPostUpdateListener listener:ppuList
        ) {
            listener.onPPUpdate(this,data);
        }
    }

    public abstract void start();
    public abstract void stop();
    public abstract void pause();
    public abstract void resume();
    public void playbackItem(PositionPost data){
        notify(data);
    }

    public PositionPost projectToRoad(PositionPost data){
        DR3DPoint p = new DR3DPoint();
        DR3DPoint pProject = null;
        p.x = data.getX();
        p.y = data.getY();
        p.z = data.getZ();
        pProject = _projectToRoad(p);
        if(pProject != null){
            data.setX(pProject.x);
            data.setY(pProject.y);
            data.setZ(pProject.z);
            float cellSize = DRMapCell.getCellSize();
            float cellStartX = ((int)(pProject.x/cellSize)) * cellSize;
            float cellStartY = ((int)(pProject.y/cellSize)) * cellSize;
            LinkedList<DRMapCell> cells ;
            cells = (LinkedList<DRMapCell>) data.getPositionProbabilityCells();
            if (cells == null) {
                cells = new LinkedList<DRMapCell>();
                data.setPositionProbabilityCells(cells);
            }
            DRMapCell cell  = new DRMapCell();
            cell.setBx(cellStartX);
            cell.setBy(cellStartY);
            cell.setProbability(0.9f);
            cells.add(cell);
        }
        return data;
    }

    private DR3DPoint _projectToRoad(DR3DPoint p){
        DRRoadProjectResult result = null;
        // TODO: 在已有的映射中进行匹配. 如果找不到，就重新计算

        Map<String, DRRoad> roadsMaps = DRMap.getRoads();
        for (DRRoad road:roadsMaps.values()){
            DRRoadProjectResult tmp_result = road.findProjectPoint(p);
            if (tmp_result != null){
                // 在所有的路网中寻找最近的投影点
                if (result == null){
                    result = tmp_result;
                }else{
                    if (result.distance > tmp_result.distance){
                        result = tmp_result;
                    }
                }
            }
        }

        if (result != null){
            // TODO: 把得到的投影点加入映射表，以免下次再计算
            return result.projectP;
        }else{
            return null;
        }
    }
}