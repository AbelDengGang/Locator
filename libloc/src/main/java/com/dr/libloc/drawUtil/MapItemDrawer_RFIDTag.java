package com.dr.libloc.drawUtil;


import com.dr.libloc.attribute.MapItem;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class MapItemDrawer_RFIDTag extends MapItem {
    float radius, x, y;

    public void setDrawAttribute(){
        radius = Float.parseFloat(getAttribute().getAttribute("radius"))*100*getBasePoint().getScaleX();
        setDrawColor("#3366ff");
        x = getPositionPost().getCX() + getBasePoint().getCX();
        y = getPositionPost().getCY() + getBasePoint().getCY();
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        setDrawAttribute();

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor(getDrawColor()));
        canvas.drawCircle(x,y,radius,paint);
        if(getDrawColor().equals("#33ff00")){
            paint.setColor(Color.BLACK);
            paint.setAlpha(255);
            paint.setTextSize(40.0f);
            canvas.drawText(getAntChan(), x, y, paint);
        }
    }
}