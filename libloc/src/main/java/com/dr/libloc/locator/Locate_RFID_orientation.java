package com.dr.libloc.locator;

import com.dr.libloc.attribute.DR3DPoint;
import com.dr.libloc.attribute.DRMapCell;
import com.dr.libloc.attribute.PositionPost;
import com.dr.libloc.sensor.SensorData_ACCELEROMETER;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class Locate_RFID_orientation extends Locate_Impl {

    private LinkedList<RFID_Info> rfInfos_1;
    private LinkedList<RFID_Info> rfInfos_2;
    private LinkedList<RFID_Info> rfInfos;
    private LinkedList<SensorData_ACCELEROMETER> accelerometers;
    private Locator locator;
    private int windowSize;         // 使用多少数据进行计算
    private int calculateInterval;  // 计算的间隔
    private int waitCount;          // 当前等待的计数
    private float pre_1_x = -1f;            //前一个1号天线x节点
    private float pre_1_y = -1f;            //前一个1号天线y节点
    private float pre_2_x = -1f;            //前一个2号天线x节点
    private float pre_2_y = -1f;            //前一个2号天线y节点
    private float center_x = 3.50f;
    private float center_y = 4.30f;
    private float center_z = 0f;
    private boolean isFirst = true;
    private float speed_x = 0;
    private float speed_y = 0;
    private float speed_z = 0;

    //方向 1 x正方向, 2 x负方向, 3 y正方向, 4 y负方向
    private int RFID_orientation;

    public Locate_RFID_orientation(Locator locator) {
        this.locator = locator;
        rfInfos_1 = new LinkedList<RFID_Info>();
        rfInfos_2 = new LinkedList<RFID_Info>();
        rfInfos = new LinkedList<RFID_Info>();
        accelerometers = new LinkedList<>();
        waitCount = 0;
        windowSize = 30;
        calculateInterval = 15;

    }

    public void calPosotion(){
        if(isFirst){
            firstPosotion();

        }else {
            rfInfos_1.clear();
            rfInfos_2.clear();
            for (int i=(rfInfos.size() - 15); i < rfInfos.size(); i++){
                if(rfInfos.get(i).mapItem.getAntChan().equals("1")){
                    rfInfos_1.add(rfInfos.get(i));
                }else if (rfInfos.get(i).mapItem.getAntChan().equals("2")){
                    rfInfos_2.add(rfInfos.get(i));
                }
            }
            double min = -1;
            for (RFID_Info rfid_info: rfInfos_2){
                float itemx_2 = rfid_info.mapItem.getPositionPost().getCX();
                float itemy_2 = rfid_info.mapItem.getPositionPost().getCY();
                for (RFID_Info rfid_info1 : rfInfos_1){
                    float itemx_1 = rfid_info1.mapItem.getPositionPost().getCX();
                    float itemy_1 = rfid_info1.mapItem.getPositionPost().getCY();
                    double line = Math.sqrt(((itemx_1-itemx_2)*(itemx_1-itemx_2)+(itemy_1-itemy_2)*(itemy_1-itemy_2)));
                    if(min > 0){
                        if(line < min){
                            min = line;
                            pre_1_x = itemx_1;
                            pre_1_y = itemy_1;
                            pre_2_x = itemx_2;
                            pre_2_y = itemy_2;
                        }
                    }else if(min <= 0){
                        min = line;
                        pre_1_x = itemx_1;
                        pre_1_y = itemy_1;
                        pre_2_x = itemx_2;
                        pre_2_y = itemy_2;
                    }
                    isFirst = true;
                }
            }
            if(isFirst){
                center_y = (pre_2_y + pre_1_y)/2;
                center_x = (pre_2_x + pre_1_x)/2;
                isFirst = false;
                if(Math.abs(pre_1_x - pre_2_x)> Math.abs(pre_1_y - pre_2_y)) {
                    if((pre_1_x - pre_2_x)>0){
                        RFID_orientation = 180; //3
                    }else {
                        RFID_orientation = 0;  //4
                    }
                }else{
                    if((pre_1_y - pre_2_y)>0){
                        RFID_orientation = 270;  //2
                    }else {
                        RFID_orientation = 90;    //1
                    }
                }

            }

            if(rfInfos_2.size() <= 0){
                double min_xy = -1;
                double min_x = -1;
                double min_y = -1;

                Map<String, Integer> map = new HashMap<>();
                for (RFID_Info rfid_info: rfInfos_1){
                    Set<String> strings = map.keySet();
                    for (String s:strings){
                        if(s.equals(rfid_info.mapItem.getName())){
                            map.replace(s,map.get(s)+1);
                        }
                    }
                    if (map.get(rfid_info.mapItem.getName()) == null){
                        map.put(rfid_info.mapItem.getName(),1);
                    }
                }
                Collection<Integer> values = map.values();
                for (int integ : values){
                    if(integ > 1){
                        return;
                    }
                }
                //确定单向运动方向
                for (RFID_Info rfid_info: rfInfos_1){
                    float r_itemx = rfid_info.mapItem.getPositionPost().getCX();
                    float r_itemy = rfid_info.mapItem.getPositionPost().getCY();
                    if(min_x > 0){
                        double x = Math.abs((r_itemx - pre_1_x));
                        if(min_x > x){
                            min_x = x;
                        }
                    }else {
                        min_x = Math.abs((r_itemx - pre_1_x));
                    }
                    if(min_y > 0){
                        double y = Math.abs((r_itemy - pre_1_y));
                        if(min_y > y){
                            min_y = y;
                        }
                    }
                    double line = Math.sqrt(((pre_1_x-r_itemx)*(pre_1_x-r_itemx)+(pre_1_y-r_itemy)*(pre_1_y-r_itemy)));
                    if(min_xy > 0){
                        if(min_xy > line){
                            min_xy = line;
                            pre_1_x = r_itemx;
                            pre_1_y = r_itemy;
                        }
                    }else {
                        min_xy = line;
                        pre_1_x = r_itemx;
                        pre_1_y = r_itemy;
                    }
                }
                if(min_x > min_y){
                    center_y = (pre_1_y + pre_2_y)/2;
                }else {
                    center_x = (pre_1_x + pre_2_x)/2;
                }
            }

            if(rfInfos_1.size() <= 0){
                double min_xy = -1;
                double min_x = -1;
                double min_y = -1;
                Map<String, Integer> map = new HashMap<>();
                for (RFID_Info rfid_info: rfInfos_2){
                    Set<String> strings = map.keySet();
                    for (String s:strings){
                        if(s.equals(rfid_info.mapItem.getName())){
                            map.replace(s,map.get(s)+1);
                        }
                    }
                    if (map.get(rfid_info.mapItem.getName()) == null){
                        map.put(rfid_info.mapItem.getName(),1);
                    }
                }
                Collection<Integer> values = map.values();
                for (int integ : values){
                    if(integ > 1){
                        return;
                    }
                }
                //确定单向运动方向
                for (RFID_Info rfid_info: rfInfos_2){
                    float r_itemx = rfid_info.mapItem.getPositionPost().getCX();
                    float r_itemy = rfid_info.mapItem.getPositionPost().getCY();
                    if(min_x > 0){
                        double x = Math.abs((r_itemx - pre_2_x));
                        if(min_x > x){
                            min_x = x;
                        }
                    }else {
                        min_x = Math.abs((r_itemx - pre_2_x));
                    }
                    if(min_y > 0){
                        double y = Math.abs((r_itemy - pre_2_y));
                        if(min_y > y){
                            min_y = y;
                        }
                    }
                    double line = Math.sqrt(((pre_2_x-r_itemx)*(pre_2_x-r_itemx)+(pre_2_y-r_itemy)*(pre_2_y-r_itemy)));
                    if(min_xy > 0){
                        if(min_xy > line){
                            min_xy = line;
                            pre_2_x = r_itemx;
                            pre_2_y = r_itemy;
                        }
                    }else {
                        min_xy = line;
                        pre_2_x = r_itemx;
                        pre_2_y = r_itemy;
                    }
                }

                if(min_x > min_y){
                    center_y = (pre_1_y + pre_2_y)/2;
                }else {
                    center_x = (pre_1_x + pre_2_x)/2;
                }
            }


            if(!isFirst){
                center_y = (pre_2_y + pre_1_y)/2;
                center_x = (pre_2_x + pre_1_x)/2;
                isFirst = false;
            }



        }
        PositionPost data = new PositionPost();
        data.setX(center_x/100);
        data.setY(center_y/100);
        data.setZ(0);
        data.setXangle(RFID_orientation);
        data.setYangle(0);
        data.setZangle(0);

        // project to road
        data = locator.projectToRoad(data);

        locator.notify(data);
    }

    public void firstPosotion(){
        for (RFID_Info rfid_info:rfInfos){
            if(rfid_info.mapItem.getAntChan().equals("1")){
                rfInfos_1.add(rfid_info);
            }else if (rfid_info.mapItem.getAntChan().equals("2")){
                rfInfos_2.add(rfid_info);
            }
        }
        double min = -1;
        for (RFID_Info rfid_info: rfInfos_2){
            float itemx_2 = rfid_info.mapItem.getPositionPost().getCX();
            float itemy_2 = rfid_info.mapItem.getPositionPost().getCY();
            for (RFID_Info rfid_info1 : rfInfos_1){
                float itemx_1 = rfid_info1.mapItem.getPositionPost().getCX();
                float itemy_1 = rfid_info1.mapItem.getPositionPost().getCY();
                double line = Math.sqrt(((itemx_1-itemx_2)*(itemx_1-itemx_2)+(itemy_1-itemy_2)*(itemy_1-itemy_2)));
                if(min > 0){
                    if(line < min){
                        min = line;
                        pre_1_x = itemx_1;
                        pre_1_y = itemy_1;
                        pre_2_x = itemx_2;
                        pre_2_y = itemy_2;
                    }
                }else if(min <= 0){
                    min = line;
                    pre_1_x = itemx_1;
                    pre_1_y = itemy_1;
                    pre_2_x = itemx_2;
                    pre_2_y = itemy_2;
                }
                isFirst = false;
            }
        }
        if(!isFirst){
            center_y = (pre_2_y + pre_1_y)/2;
            center_x = (pre_2_x + pre_1_x)/2;
        }
    }


    @Override
    public void addRFIDInfo(RFID_Info rfidInfo) {

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

    public void moveDistance(){
        int timeMs = (int)(accelerometers.get(1).getTimeInMS() - accelerometers.get(0).getTimeInMS());
        float x = accelerometers.get(0).getValue()[0];
        float y = accelerometers.get(0).getValue()[1];
        float z = accelerometers.get(0).getValue()[2];
        speed_x = speed_x + x*timeMs*0.001f;
        speed_y = speed_y + y*timeMs*0.001f;
        speed_z = speed_z + z*timeMs*0.001f;
        center_x = center_x + speed_x * timeMs*0.001f;
        center_y = center_y + speed_y * timeMs*0.001f;
        center_z = center_z + speed_z * timeMs*0.001f;
        PositionPost positionPost = new PositionPost();
        positionPost.setZangle(0f);
        positionPost.setYangle(0f);
        positionPost.setXangle(0f);
        positionPost.setZ(center_z);
        positionPost.setY(center_y);
        positionPost.setX(center_x);
//        locator.notify(positionPost);

    }

    @Override
    public void addACCInfo(SensorData_ACCELEROMETER data) {
        if(accelerometers.size() >= 2){
            accelerometers.remove();
            accelerometers.add(data);
            moveDistance();
        }else {
            accelerometers.add(data);
        }
    }

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
}
