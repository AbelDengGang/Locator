
package com.dr.libloc;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.dr.libloc.attribute.BasePoint;
import com.dr.libloc.attribute.CanvasArguments;
import com.dr.libloc.attribute.MapItem;
import com.dr.libloc.attribute.RefreshStatus;
import com.dr.libloc.drawUtil.MapItemDrawer_gridding;
import com.dr.libloc.drawUtil.MapItemDrawer_roads;
import com.dr.libloc.mapUtil.DRMap;
import com.dr.libloc.sensor.SensorData_RFID;

import org.xml.sax.SAXException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.ParserConfigurationException;
public class MapViewer extends SurfaceView implements Runnable, SurfaceHolder.Callback {
    private String tag = MapViewer.class.toString();
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder sfh;
    //声明一条线程
    private Thread th;
    // 线程消亡标识
    private boolean thread_flag;
    // 绘画标识
    private boolean draw_flag;
    //枚举类,控制刷新
    private RefreshStatus status;
    // 画布的伸缩与平移
    private CanvasArguments carg;
    //原始地图
    private DRMap drMap;
    //    private List<MapItem> mapItemList;
    // 绘画移动物体
    private MapItem item = null;
    // 绘画路
    private MapItem roadItem;
    // 标签
    private SensorData_RFID sensorData_rfid = null;
    private float win_width;
    private float win_height;
    private BasePoint basePoint;

    private MapItemDrawer_gridding itemDrawer_gridding;


    public MapViewer(Context context) {
        super(context);
        sfh = this.getHolder();
        //为SurfaceView添加状态监听
        sfh.addCallback(this);
        paint = new Paint();
//        paint.setColor(Color.parseColor("#FFFFFF"));
        paint.setStyle(Paint.Style.STROKE);
        paint.setDither(true);
        paint.setAntiAlias(true);
        setFocusable(true);
        carg = new CanvasArguments();
        drMap = new DRMap();
        roadItem = new MapItemDrawer_roads();
        basePoint = new BasePoint();
//        basePoint.setScaleX(scaleX);
//        basePoint.setScaleY(scaleY);
        basePoint.setX(0.05f);
        basePoint.setY(0.05f);
        try {
            InputStream inputStream = getContext().getAssets().open("3DModel.xml");
            drMap.load(inputStream);
            roadItem.setDrRoadMap(drMap.getRoads());
            roadItem.setBasePoint(basePoint);
//            mapItemList = map.getMapItemList();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        float scaleX,scaleY,x,y,z;
        win_height = getHeight();
        win_width = getWidth();
        scaleX = (win_width - 20)/ drMap.getMapItem().get("0").getCWidth();
        scaleY = (win_height - 20)/ drMap.getMapItem().get("0").getCLength();
        this.carg.setScale(scaleX > scaleY ? scaleY : scaleX);
        //网格初始化
        itemDrawer_gridding = new MapItemDrawer_gridding();
        itemDrawer_gridding.setBasePoint(basePoint);
        // 设置地图初始化状态
        status = RefreshStatus.refresh_map;
        thread_flag=true;
        draw_flag = true;
        th=new Thread(this);
        th.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        thread_flag=false;
    }

    @Override
    public void run() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Collection<MapItem> values = drMap.getMapItem().values();
                Set<String> strings = drMap.getMapItem().keySet();
                for(String s : strings){
                    MapItem m = drMap.getMapItem().get(s);
                    if(m != null){
                        m.setHighlight(false);
                        drMap.getMapItem().replace(s, m);
                    }
                }
//                for(int i = 1; i < drMap.getMapItem().keySet().size(); i++){
//                    drMap.getMapItemList().get(i).setHighlight(false);
//                }
                status = RefreshStatus.refresh_map;
                draw_flag = true;
            }
        };
        new Timer().schedule(task, 0, 1000);
        while (thread_flag){
            if(draw_flag){
                switch (status){
                    case refresh_map:
                        refresh_map();
                        break;
                    case refresh_stop:
                        break;
                    case refresh_self:
                        refresh_self();
                        break;
                    case refresh_RFID:
                        refresh_RFID();
                        break;
                }
                draw_flag = false;
            }
        }
    }

    public void refresh_map(){
        canvas = sfh.lockCanvas();
        canvas.drawRGB(255, 255, 255);
        canvas.translate(this.carg.getFx(), this.carg.getFy());
        canvas.scale(this.carg.getScale(),this.carg.getScale(), this.carg.getScx(), this.carg.getScy());
        itemDrawer_gridding.griddingData(drMap.getMapItem().get("0").getCLength(), drMap.getMapItem().get("0").getCWidth());
        itemDrawer_gridding.draw(canvas,paint);
        if(roadItem != null){
            roadItem.draw(canvas,paint);
        }
        if(sensorData_rfid != null){
            MapItem m = drMap.getMapItem().get(sensorData_rfid.getID());
            if(m != null){
                m.setAntChan(sensorData_rfid.getAntChan() + "");
                m.setHighlight(true);
                drMap.getMapItem().replace(sensorData_rfid.getID(), m);
                sensorData_rfid = null;
            }
        }
        Collection<MapItem> values = drMap.getMapItem().values();
        for(MapItem mi : values){
            mi.setBasePoint(basePoint);
            mi.draw(canvas,paint);
        }
//        for(int i = 1; i < drMap.getMapItemList().size(); i++){
//            if(sensorData_rfid != null){
//                if(sensorData_rfid.getID().equals(drMap.getMapItemList().get(i).getName())){
//                    drMap.getMapItemList().get(i).setAntChan(sensorData_rfid.getAntChan() + "");
//                    drMap.getMapItemList().get(i).setHighlight(true);
//                    sensorData_rfid = null;
//                }
//            }
//            drMap.getMapItemList().get(i).setBasePoint(basePoint);
//            drMap.getMapItemList().get(i).draw(canvas, paint);
//        }
        if(item != null){
            item.draw(canvas, paint);
        }
        sfh.unlockCanvasAndPost(canvas);//解锁画布，提交画好的图像
    }

    public void refresh_self(){
        if(item != null){
            float left = (item.getPositionPost().getCX() + item.getBasePoint().getCX());
            float top = (item.getPositionPost().getCY() + item.getBasePoint().getCY());
            float right = left + item.getCLength();
            float bottom = top + item.getCWidth();
            canvas = sfh.lockCanvas(new Rect((int)left,(int)top,(int)right,(int)bottom));
            item.draw(canvas, paint);
            sfh.unlockCanvasAndPost(canvas);
        }
    }

    public void refresh_RFID(){
        if(sensorData_rfid != null){
            MapItem m = drMap.getMapItem().get(sensorData_rfid.getID());
            if(m != null){
                m.setAntChan(sensorData_rfid.getAntChan() + "");
                m.setHighlight(true);
                drMap.getMapItem().replace(sensorData_rfid.getID(), m);
                sensorData_rfid = null;
                float radius = Float.parseFloat(m.getAttribute().getAttribute("radius"))*m.getBasePoint().getScaleX();
                float left = m.getPositionPost().getCX() + m.getBasePoint().getCX() - radius;
                float top = m.getPositionPost().getCY() + m.getBasePoint().getCY() - radius;
                float right = left + radius*2;
                float bottom = top + radius*2;
                canvas = sfh.lockCanvas(new Rect((int)left,(int)top,(int)right,(int)bottom));
                m.draw(canvas, paint);
                sfh.unlockCanvasAndPost(canvas);
            }
        }

//        if(sensorData_rfid != null){
//            for(int i = 1; i < drMap.getMapItemList().size(); i++){
//                if(sensorData_rfid != null){
//                    if(sensorData_rfid.getID().equals(drMap.getMapItemList().get(i).getName())){
//                        drMap.getMapItemList().get(i).setBasePoint(basePoint);
//                        drMap.getMapItemList().get(i).setHighlight(true);
//                        drMap.getMapItemList().get(i).setAntChan(sensorData_rfid.getAntChan() + "");
//                        sensorData_rfid = null;
//                        float radius = Float.parseFloat(drMap.getMapItemList().get(i).getAttribute().getAttribute("radius"))*drMap.getMapItemList().get(i).getBasePoint().getScaleX();
//                        float left = drMap.getMapItemList().get(i).getPositionPost().getX() + drMap.getMapItemList().get(i).getBasePoint().getX() - radius;
//                        float top = drMap.getMapItemList().get(i).getPositionPost().getY() + drMap.getMapItemList().get(i).getBasePoint().getY() - radius;
//                        float right = left + radius*2;
//                        float bottom = top + radius*2;
//                        canvas = sfh.lockCanvas(new Rect((int)left,(int)top,(int)right,(int)bottom));
//                        drMap.getMapItemList().get(i).draw(canvas, paint);
//                        sfh.unlockCanvasAndPost(canvas);
//                    }
//                }
//            }
//        }
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    public SurfaceHolder getSfh() {
        return sfh;
    }

    public void setSfh(SurfaceHolder sfh) {
        this.sfh = sfh;
    }

    public boolean isThread_flag() {
        return thread_flag;
    }

    public void setThread_flag(boolean thread_flag) {
        this.thread_flag = thread_flag;
    }

    public boolean isDraw_flag() {
        return draw_flag;
    }

    public void setDraw_flag(RefreshStatus status) {
        this.status = status;
        this.draw_flag = true;
    }

    public CanvasArguments getCarg() {
        return carg;
    }

    public void setCarg(CanvasArguments carg) {
        this.carg = carg;
    }

    public void setScale(float scale, float scx, float scy, RefreshStatus status) {
        this.carg.setScale(scale);
        this.carg.setScx(scx);
        this.carg.setScy(scy);
        if(scale > 1){
            basePoint.setScaleY(1f/1.1f);
            basePoint.setScaleX(1f/1.1f);
        }else {
            basePoint.setScaleX(1.1f);
            basePoint.setScaleY(1.1f);
        }
        setDraw_flag(status);
    }

    public void setFxy(float fx, float fy, RefreshStatus status){
        this.carg.setFx(fx);
        this.carg.setFy(fy);
        setDraw_flag(status);
    }

    public DRMap getDRMap() {
        return drMap;
    }

    public void setDRMap(DRMap drMap) {
        this.drMap = drMap;
    }

    public SensorData_RFID getSensorData_rfid() {
        return sensorData_rfid;
    }

    public void setSensorData_rfid(SensorData_RFID sensorData_rfid, RefreshStatus status) {
        this.sensorData_rfid = sensorData_rfid;
        setDraw_flag(status);
    }

    public MapItem getItem() {
        return item;
    }

    public void setItem(MapItem item, RefreshStatus status) {
        this.item = item;
        this.item.setBasePoint(basePoint);
        setDraw_flag(status);
    }

    public RefreshStatus getStatus() {
        return status;
    }

    public void setStatus(RefreshStatus status) {
        this.status = status;
    }

}
