package com.dr.libloc.drawUtil;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import com.dr.libloc.attribute.DRMapCell;
import com.dr.libloc.attribute.MapItem;

public class MapItemDrawer_self extends MapItem {
    float bx,by,ex,ey,bex,bey;

    public void setDrawAttribute(){
//        setWidth(Float.parseFloat(getAttribute().getAttribute("width")));
//        setHeigth(Float.parseFloat(getAttribute().getAttribute("height")));
//        setLength(Float.parseFloat(getAttribute().getAttribute("length")));
        setWidth(0.50f);
        setHeigth(0.30f);
        setLength(0.30f);
        setDrawColor("#FF0000");
        bx = (getPositionPost().getCX() + getBasePoint().getCX());
        by = (getPositionPost().getCY() + getBasePoint().getCY()) - 2* getCWidth()/3;
        ex = bx + getCLength()/2;
        ey = by + getCWidth();
        bex = ex - getCLength();
        bey = ey;

    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        if(getPositionPost().getPositionProbabilityCells() != null){
            for(DRMapCell mapCell: getPositionPost().getPositionProbabilityCells()){
                int colorValue = 255 - (int)(mapCell.getProbability() * 255);
                paint.setColor(Color.parseColor(("#ffff" + String.format("%02X", colorValue))));
                bx = (mapCell.getCmBx() + getBasePoint().getCX());
                by = (mapCell.getCmBy() + getBasePoint().getCY());
                ex = (bx + DRMapCell.getCmCellSize());
                ey = (by + DRMapCell.getCmCellSize());

                canvas.drawRect(bx,by,ex,ey, paint);
            }
        }
        setDrawAttribute();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor(getDrawColor()));
        paint.setAlpha(255);
        Path path = new Path();
        canvas.rotate(getPositionPost().getXangle(), getPositionPost().getCX() + getBasePoint().getCX(), getPositionPost().getCY() + getBasePoint().getCY());
        path.moveTo(bx,by);
        path.lineTo(ex,ey);
        path.lineTo(bex,bey);
        path.lineTo(bx,by);
        canvas.drawPath(path, paint);

        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(getPositionPost().getCX() + getBasePoint().getCX(), getPositionPost().getCY() + getBasePoint().getCY(),5, paint);
        canvas.rotate((360f - getPositionPost().getXangle()), getPositionPost().getCX() + getBasePoint().getCX(), getPositionPost().getCY() + getBasePoint().getCY());

    }
}
