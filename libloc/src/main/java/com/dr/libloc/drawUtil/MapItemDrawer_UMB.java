package com.dr.libloc.drawUtil;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.dr.libloc.attribute.MapItem;

public class MapItemDrawer_UMB extends MapItem {
//    float radius, x, y;
    float centre_x, centre_y, centre, nor_x, nor_y, sou_x, sou_y;
    float we_x, we_y, ea_x, ea_y, rect_bx, rect_by;
    float rect_ex, rect_ey;
    float[] triangle;
    float umb_x ,umb_y;
    boolean isShow = true;
    boolean isLeft = true;

    public void setDrawAttribute(Canvas canvas){
        rect_bx = canvas.getWidth() - 210;
        rect_by = 10;
        rect_ex = canvas.getWidth() - 10;
        rect_ey = 210;
        centre_x = canvas.getWidth() - 110;
        centre_y = 110;
        centre = 50;
        nor_x = canvas.getWidth() - 110;
        nor_y = 10;
        sou_x = canvas.getWidth() - 110;
        sou_y = 210;
        we_x = canvas.getWidth() - 210;
        we_y = 110;
        ea_x = canvas.getWidth() - 10;
        ea_y = 110;
        triangle = new float[]{
            nor_x,nor_y, nor_x - 10, nor_y + 10,
            nor_x,nor_y, nor_x + 10, nor_y + 10,
//            sou_x,sou_y, sou_x - 10, sou_y - 10,
//            sou_x,sou_y, sou_x + 10, sou_y - 10,
//            we_x ,we_y,  we_x + 10, we_y - 10,
//            we_x ,we_y,  we_x + 10, we_y + 10,
            ea_x ,ea_y,  ea_x - 10, ea_y - 10,
            ea_x ,ea_y,  ea_x - 10, ea_y + 10
        };
        umb_x = centre_x;
        umb_y = centre_y;
        if(getAccelerationSensor() != null){
/*      固定长度
            if(getAccelerationSensor().getX() != 0f ){
                if(getAccelerationSensor().getY() != 0f){
                    float ratio = Math.abs(getAccelerationSensor().getX()) / Math.abs(getAccelerationSensor().getY());
                    float times = 1;
                    times  = ratio > 1 ? 80f/Math.abs(getAccelerationSensor().getX()) : 80f/Math.abs(getAccelerationSensor().getY());
                    umb_x = getAccelerationSensor().getX() > 0 ? Math.abs(getAccelerationSensor().getX()) * times + centre_x : centre_x - Math.abs(getAccelerationSensor().getX()) * times;
                    umb_y = getAccelerationSensor().getY() > 0 ? Math.abs(getAccelerationSensor().getY()) * times + centre_y : centre_y - Math.abs(getAccelerationSensor().getY()) * times;
                }else {
                    umb_y = centre_y;
                    umb_x = getAccelerationSensor().getX() > 0 ? 80 + centre_x : centre_x - 80;
                }
            }else {
                if(getAccelerationSensor().getY() != 0f){
                    umb_x = centre_x;
                    umb_y = getAccelerationSensor().getY() > 0 ? 80 + centre_y : centre_y - 80;
                }else {
                    umb_y = centre_y;
                    umb_x = centre_x;
                }
            }
*/          if(isLeft){
                umb_y = getAccelerationSensor().getX() > 0 ? Math.abs(getAccelerationSensor().getX()) * 10 + centre_y : centre_y - Math.abs(getAccelerationSensor().getX()) * 10;
                umb_x = getAccelerationSensor().getY() > 0 ? Math.abs(getAccelerationSensor().getY()) * 10 + centre_x : centre_x -
                        Math.abs(getAccelerationSensor().getY()) * 10;
            }else {
                umb_y = getAccelerationSensor().getX() > 0 ? centre_y - Math.abs(getAccelerationSensor().getX()) * 10 : centre_y - Math.abs(getAccelerationSensor().getX()) * 10;
                umb_x = getAccelerationSensor().getY() > 0 ? centre_x - Math.abs(getAccelerationSensor().getY()) * 10 : centre_x + Math.abs(getAccelerationSensor().getY()) * 10;
            }


        }

    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        setDrawAttribute(canvas);
        if(isShow){
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
            // 绘画正方形外框
//        canvas.drawRect(rect_bx, rect_by, rect_ex, rect_ey, paint);
//        canvas.drawCircle(centre_x, centre_y, centre, paint);
            paint.setStyle(Paint.Style.FILL);
            paint.setAlpha(100);
            canvas.drawCircle(centre_x, centre_y, 1, paint);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawLine(we_x, we_y, ea_x, ea_y, paint);
            canvas.drawLine(nor_x, nor_y, sou_x, sou_y, paint);
            canvas.drawLines(triangle, paint);
            paint.setTextSize(15);
            paint.setAlpha(255);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawText("y", nor_x + 5, nor_y + 20, paint);
//        canvas.drawText("南", sou_x + 5, sou_y - 10, paint);
//        canvas.drawText("西", we_x + 5, we_y + 15, paint);
            canvas.drawText("x", ea_x - 20, ea_y + 15, paint);
            paint.setColor(Color.RED);
            paint.setFakeBoldText(true);
            paint.setStrokeWidth(3f);
//        paint.setStyle(Paint.Style.STROKE);
            canvas.drawLine(centre_x, centre_y, umb_x, umb_y, paint);
            if(getAccelerationSensor() != null){
                canvas.drawText(String.format("(%f , %f) m/s", Math.abs(getAccelerationSensor().getY()), Math.abs(getAccelerationSensor().getX())), rect_bx - 100, rect_by + 10, paint);
            }
        }else {
            paint.setColor(Color.parseColor("#FF9999"));
            canvas.drawLine(centre_x, centre_y, umb_x, umb_y, paint);
        }
        paint.setStrokeWidth(1);
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public boolean isLeft() {
        return isLeft;
    }

    public void setLeft(boolean left) {
        isLeft = left;
    }
}
