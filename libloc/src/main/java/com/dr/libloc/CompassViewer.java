package com.dr.libloc;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.dr.libloc.attribute.AccelerationSensor;
import com.dr.libloc.attribute.MapItem;
import com.dr.libloc.drawUtil.MapItemDrawer_Compass;
import com.dr.libloc.sensor.SensorData_NATIVE_COMPASS;

public class CompassViewer extends SurfaceView implements Runnable, SurfaceHolder.Callback{

    private SensorData_NATIVE_COMPASS native_compass;
    private MapItem compassItem;

    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder sfh;
    //声明一条线程
    private Thread th;
    // 线程消亡标识
    private boolean thread_flag;
    // 绘画标识
    private boolean draw_flag;



    public CompassViewer(Context context) {
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
        compassItem = new MapItemDrawer_Compass();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
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
                refresh_Compass();
                draw_flag = false;
            }
        }
    }

    public void refresh_Compass(){
        if(compassItem != null){
            canvas = sfh.lockCanvas();
            canvas.drawColor(Color.WHITE);
            compassItem.draw(canvas, paint);
            sfh.unlockCanvasAndPost(canvas);
        }
    }

    public SensorData_NATIVE_COMPASS getNative_compass() {
        return native_compass;
    }

    public void setNative_compass(SensorData_NATIVE_COMPASS native_compass) {
        this.native_compass = native_compass;
        AccelerationSensor accelerationSensor = new AccelerationSensor();
        accelerationSensor.setValues(native_compass.getValues());
        this.compassItem.setAccelerationSensor(accelerationSensor);
        draw_flag = true;
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

    public void setDraw_flag(boolean draw_flag) {
        this.draw_flag = draw_flag;
    }
}
