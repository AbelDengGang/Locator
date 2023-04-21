package com.dr.libloc;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.dr.libloc.attribute.AccelerationSensor;
import com.dr.libloc.attribute.RefreshStatus;
import com.dr.libloc.drawUtil.MapItemDrawer_UMB;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class UwbViewer extends SurfaceView implements Runnable, SurfaceHolder.Callback {

    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder sfh;
    //声明一条线程
    private Thread th;
    // 线程消亡标识
    private boolean thread_flag;
    // 绘画标识
    private boolean draw_flag;
    private MapItemDrawer_UMB mapItemDrawer_umb;
    private Queue<MapItemDrawer_UMB> queue;

    private float win_width;
    private float win_height;
    private boolean isLeft = true;

    public UwbViewer(Context context) {
        super(context);
        sfh = this.getHolder();
        //为SurfaceView添加状态监听
        sfh.addCallback(this);
        queue = new ConcurrentLinkedQueue<MapItemDrawer_UMB>();
        mapItemDrawer_umb = new MapItemDrawer_UMB();
        paint = new Paint();
//        paint.setColor(Color.parseColor("#FFFFFF"));
        paint.setStyle(Paint.Style.STROKE);
        paint.setDither(true);
        paint.setAntiAlias(true);
        setFocusable(true);

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        win_height = getHeight();
        win_width = getWidth();
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

    }

    @Override
    public void run() {
        while (thread_flag){
            if(draw_flag){
                refresh_UWB();
                draw_flag = false;
            }
        }
    }

    public void refresh_UWB(){
        canvas = sfh.lockCanvas();
        canvas.drawColor(Color.WHITE);
        mapItemDrawer_umb.draw(canvas, paint);
        traverseQueue();
        sfh.unlockCanvasAndPost(canvas);
    }

    public void traverseQueue(){
        if(!queue.isEmpty()){
            for(MapItemDrawer_UMB umb : queue){
                umb.setLeft(isLeft);
                umb.setShow(false);
                umb.draw(canvas, paint);
            }
        }
    }

    public MapItemDrawer_UMB getMapItemDrawer_umb() {
        return mapItemDrawer_umb;
    }

    public void setMapItemDrawer_umb(MapItemDrawer_UMB mapItemDrawer_umb) {
        this.mapItemDrawer_umb = mapItemDrawer_umb;
    }

    public void setAccelerationSensor(AccelerationSensor alts){
        mapItemDrawer_umb.setAccelerationSensor(alts);
        mapItemDrawer_umb.setLeft(isLeft);
        MapItemDrawer_UMB mid = new MapItemDrawer_UMB();
        mid.setAccelerationSensor(alts);
        if(queue.size() >= 10){
            queue.poll();
        }
        queue.offer(mid);
        draw_flag = true;
    }

    public boolean isThread_flag() {
        return thread_flag;
    }

    public void setThread_flag(boolean thread_flag) {
        this.thread_flag = thread_flag;
    }

    public boolean isLeft() {
        return isLeft;
    }

    public void setLeft(boolean left) {
        isLeft = left;
    }

    public boolean isDraw_flag() {
        return draw_flag;
    }

    public void setDraw_flag(boolean draw_flag) {
        this.draw_flag = draw_flag;
    }
}
