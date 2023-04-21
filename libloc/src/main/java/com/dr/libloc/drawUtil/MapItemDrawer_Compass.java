package com.dr.libloc.drawUtil;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.dr.libloc.attribute.MapItem;

public class MapItemDrawer_Compass extends MapItem {

    float centre_x, centre_y, centre, nor_x, nor_y, sou_x, sou_y;
    float we_x, we_y, ea_x, ea_y, rect_bx, rect_by;
    float rect_ex, rect_ey;
    float[] triangle;
    float umb_x ,umb_y;


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
                sou_x,sou_y, sou_x - 10, sou_y - 10,
                sou_x,sou_y, sou_x + 10, sou_y - 10,
                we_x ,we_y,  we_x + 10, we_y - 10,
                we_x ,we_y,  we_x + 10, we_y + 10,
                ea_x ,ea_y,  ea_x - 10, ea_y - 10,
                ea_x ,ea_y,  ea_x - 10, ea_y + 10
        };
        umb_x = centre_x;
        umb_y = centre_y;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        setDrawAttribute(canvas);


        paint.setColor(Color.BLACK);
//        paint.setStyle(Paint.Style.STROKE);
        // 绘画正方形外框
//        canvas.drawRect(rect_bx, rect_by, rect_ex, rect_ey, paint);
//        canvas.drawCircle(centre_x, centre_y, centre, paint);
        paint.setStyle(Paint.Style.FILL);
        paint.setWordSpacing(1);
        paint.setAlpha(100);
        canvas.drawCircle(centre_x, centre_y, 1, paint);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawLine(we_x, we_y, ea_x, ea_y, paint);
        canvas.drawLine(nor_x, nor_y, sou_x, sou_y, paint);
        canvas.drawLines(triangle, paint);
        paint.setTextSize(15);
        paint.setAlpha(255);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawText("北", nor_x + 5, nor_y + 20, paint);
        canvas.drawText("南", sou_x + 5, sou_y - 10, paint);
        canvas.drawText("西", we_x + 5, we_y + 15, paint);
        canvas.drawText("东", ea_x - 20, ea_y + 15, paint);
        paint.setColor(Color.RED);
        paint.setFakeBoldText(true);
        paint.setStrokeWidth(3f);
//        paint.setStyle(Paint.Style.STROKE);
        if(getAccelerationSensor() != null){
            canvas.rotate(getAccelerationSensor().getValues()[0], centre_x,centre_y);
            canvas.drawLine(centre_x, centre_y, centre_x, centre_y - 100, paint);
            canvas.rotate((360f - getAccelerationSensor().getValues()[0]), centre_x,centre_y);
        }

    }
}
