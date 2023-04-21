package com.dr.libloc.drawUtil;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import com.dr.libloc.attribute.MapItem;

public class MapItemDrawer_chair extends MapItem {
    float bx,by,ex,ey;

    public void setDrawAttribute(){
        setWidth(Float.parseFloat(getAttribute().getAttribute("width")));
        setHeigth(Float.parseFloat(getAttribute().getAttribute("height")));
        setLength(Float.parseFloat(getAttribute().getAttribute("length")));
        setDrawColor("#993300");

        bx = (getPositionPost().getCX() + getBasePoint().getCX());
        by = (getPositionPost().getCY() + getBasePoint().getCY());
        ex = bx + getCLength();
        ey = by + getCWidth();
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        setDrawAttribute();

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor(getDrawColor()));
        paint.setAlpha(200);
        Path path = new Path();
        canvas.rotate(getPositionPost().getXangle(), bx, by);
        path.moveTo(bx,by);
        path.lineTo(bx,ey);
        path.lineTo(ex,ey);
        path.lineTo(ex,by);
        path.lineTo(bx,by);
        canvas.drawPath(path, paint);
        canvas.rotate((360f - getPositionPost().getXangle()), bx, by);

    }
}
