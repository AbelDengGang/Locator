package com.dr.libloc.drawUtil;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import com.dr.libloc.attribute.DRRoad;
import com.dr.libloc.attribute.DRRoadArc;
import com.dr.libloc.attribute.DRRoadVertex;
import com.dr.libloc.attribute.MapItem;

import java.util.Collection;
import java.util.HashMap;


public class MapItemDrawer_roads extends MapItem{

    public float b = 20;
    private DRRoadVertex point_v1;
    private DRRoadVertex point_v2;
    float k1;
    float k2;
    private float v1_x1;
    private float v1_y1;
    private float v1_x2;
    private float v1_y2;
    private float v2_x1;
    private float v2_y1;
    private float v2_x2;
    private float v2_y2;

    public void k2IsZero(Path path, Canvas canvas, Paint paint){
        k2 = 0;
        v1_x1 = point_v1.getCX();  // +b
        v1_y1 = b + point_v1.getCY();
        v1_x2 = point_v1.getCX();
        v1_y2 = - b + point_v1.getCY();
        float x =  point_v2.getCX() - point_v1.getCX();
        float y =  point_v2.getCY() - point_v1.getCY();
        float c = (y - (k2 * x));
        v2_x1 = point_v2.getCX();
        v2_y1 = b + point_v2.getCY();
        v2_x2 = point_v2.getCX();
        v2_y2 = point_v2.getCY() - b;
    }

    public void k1IsZero(Path path, Canvas canvas, Paint paint){
        k2 = 0;
        v1_x1 = point_v1.getCX() + b;  // +b
        v1_y1 = point_v1.getCY();
        v1_x2 = point_v1.getCX() - b;
        v1_y2 = point_v1.getCY();
        float x =  point_v2.getCX() - point_v1.getCX();
        float y =  point_v2.getCY() - point_v1.getCY();
        float c = (y - (k2 * x));
        v2_x1 = point_v2.getCX() + b;
        v2_y1 = point_v2.getCY();
        v2_x2 = point_v2.getCX() - b;
        v2_y2 = point_v2.getCY();
    }

    public void k1NotZero(Path path, Canvas canvas, Paint paint){
        k2 = -1/k1;
        // +b
        v1_x1 = (b)/(k2-k1) + point_v1.getCX();
        v1_y1 = ((b)/(k2-k1))*k1 + b + point_v1.getCY();
        v1_x2 = (b)/(k1-k2) + point_v1.getCX();
        v1_y2 = k1*((b)/(k1-k2)) - b + point_v1.getCY();
        float x =  point_v2.getCX() - point_v1.getCX();
        float y =  point_v2.getCY() - point_v1.getCY();
        float c = (y - (k2 * x));
        v2_x1 = (b-c)/(k2-k1) + point_v1.getCX();
        v2_y1 = ((b-c)/(k2-k1)) * k1 + b + point_v1.getCY();
        v2_x2 = (c+b)/(k1-k2) + point_v1.getCX();
        v2_y2 = ((c+b)/(k1-k2)) * k1 - b + point_v1.getCY();
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {

        paint.setAlpha(255);
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.FILL);
        // 获取多个roads
        Collection<DRRoad> drRoads = getDrRoadMap().values();
        Path path = new Path();
        for (DRRoad road: drRoads){
            //获取单个roads的DRRoadArc集合
            Collection<DRRoadArc> drRoadArcs = road.getRoad_arcs().values();
            //获取单个roads的DRRoadVertex集合
            HashMap<String, DRRoadVertex> vertexes = road.getVertexes();
            for (DRRoadArc roadArc: drRoadArcs){
                point_v1 = roadArc.getVetext0();
                point_v2 = roadArc.getVetext1();
                if((point_v1.getCX() - point_v2.getCX()) == 0){
                    k1 = 0;
                    k1IsZero(path,canvas,paint);
                }else {
                    k1 = ((point_v1.getCY() - point_v2.getCY())/(point_v1.getCX() - point_v2.getCX()));
                    if(k1 == 0f){
                        k2IsZero(path,canvas,paint);
                    }else {
                        k1NotZero(path,canvas,paint);
                    }
                }
                path.moveTo(v1_x1 + getBasePoint().getCX(), v1_y1 + getBasePoint().getCY());
                path.lineTo(v1_x2 + getBasePoint().getCX(), v1_y2 + getBasePoint().getCY());
                path.lineTo(v2_x2 + getBasePoint().getCX(), v2_y2 + getBasePoint().getCY());
                path.lineTo(v2_x1 + getBasePoint().getCX(), v2_y1 + getBasePoint().getCY());
                path.moveTo(v1_x1 + getBasePoint().getCX(), v1_y1 + getBasePoint().getCY());
                canvas.drawPath(path,paint);
            }
        }

        for (DRRoad road: drRoads){
            //获取单个roads的DRRoadArc集合
            Collection<DRRoadArc> drRoadArcs = road.getRoad_arcs().values();
            //获取单个roads的DRRoadVertex集合
            HashMap<String, DRRoadVertex> vertexes = road.getVertexes();
            paint.setColor(Color.RED);
            paint.setStyle(Paint.Style.STROKE);
            for (DRRoadArc roadArc: drRoadArcs){
                point_v1 = roadArc.getVetext0();
                point_v2 = roadArc.getVetext1();
                canvas.drawLine(point_v1.getCX()+ getBasePoint().getCX(),point_v1.getCY() + getBasePoint().getCY(),point_v2.getCX()+ getBasePoint().getCX(),point_v2.getCY() + getBasePoint().getCY(),paint);
            }
            for (DRRoadVertex vertex : vertexes.values()) {
                canvas.drawCircle(vertex.getCX()+ getBasePoint().getCX(),vertex.getCY() + getBasePoint().getCY(), 10, paint);
            }
        }
    }
}
