package com.dr.libloc.locator;

import com.dr.libloc.attribute.DRMapCell;
import com.dr.libloc.attribute.PositionPost;
import com.dr.libloc.sensor.SensorData_ACCELEROMETER;

import java.util.LinkedList;

public class Locate_RFID_RegionCenter extends Locate_Impl{
    private Locator locator;
    public Locate_RFID_RegionCenter(Locator locator) {
        this.locator = locator;
        rfInfos = new LinkedList<RFID_Info>();
        waitCount = 0;
        windowSize = 30;
        calculateInterval = 15;
    }

    private LinkedList<RFID_Info> rfInfos;
    private int windowSize;         // 使用多少数据进行计算
    private int calculateInterval;  // 计算的间隔
    private int waitCount;          // 当前等待的计数

    public int getWindowSize() {
        return windowSize;
    }

    public void setWindowSize(int windowSize) {
        this.windowSize = windowSize;
    }

    public int getCalculateInterval() {
        return calculateInterval;
    }

    public void setCalculateInterval(int calculateInterval) {
        this.calculateInterval = calculateInterval;
    }

    // 计算区域中心
    //   -x-------------
    //   ----x-----------
    //   -------O--------x
    //   x----------------
    //   -------------x---
    private void calPosotion(){
        boolean bInited = false;
        float minX = 0.0f;
        float maxX = 0.0f;
        float minY = 0.0f;
        float maxY = 0.0f;
        if (rfInfos == null) return;
        if (rfInfos.isEmpty()) return;
        for(RFID_Info rfidInfo:rfInfos){
            float itemX = rfidInfo.mapItem.getPositionPost().getCX();
            float itemY = rfidInfo.mapItem.getPositionPost().getCY();
            if (!bInited){
                minX = itemX;
                maxX = itemX;
                minY = itemY;
                maxY = itemY;
                bInited = true;
                continue;
            }

            if ( itemX < minX ) minX = itemX;
            if ( itemY < minY ) minY = itemY;
            if ( itemX > maxX ) maxX = itemX;
            if ( itemY > maxY ) maxY = itemY;
        }
        float centerX;
        float centerY;
        centerX = (minX + maxX)/2;
        centerY = (minY + maxY)/2;
        PositionPost data = new PositionPost();
        data.setX(centerX);
        data.setY(centerY);
        data.setZ(0.0f);
        data.setXangle(0.0f);
        data.setYangle(0.0f);
        data.setZangle(0.0f);
        float cellSize = DRMapCell.getCellSize();
        int cellStartX =(int) (minX / cellSize);
        int cellEndX =(int) (maxX / cellSize);
        int cellStartY =(int) (minY / cellSize);
        int cellEndY =(int) (maxY / cellSize);
        LinkedList<DRMapCell> cells = new LinkedList<DRMapCell>();
        int xIndex,yIndex;
        for( xIndex = cellStartX ; xIndex < cellEndX ; xIndex ++ ){
            for ( yIndex = cellStartY; yIndex < cellEndY; yIndex ++){
                DRMapCell cell  = new DRMapCell();
                cell.setBx(xIndex*cellSize);
                cell.setBy(yIndex*cellSize);
                cell.setProbability(0.2f);
                cells.add(cell);
            }
        }
        data.setPositionProbabilityCells(cells);

        locator.notify(data);
    }


    @Override
    public void addRFIDInfo(RFID_Info rfidInfo){
        rfInfos.add(rfidInfo);
        waitCount ++;
        if (rfInfos.size() >= windowSize){
            if (waitCount >= calculateInterval){
                // calculat Position
                calPosotion();
                // remove decrapted data
                int i;
                for (i=0;i<calculateInterval;i++){
                    rfInfos.remove();
                }
                waitCount = 0;
            }
        }
    }


    @Override
    public void addACCInfo(SensorData_ACCELEROMETER data) {

    }
}
