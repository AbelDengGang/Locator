package com.dr.libloc.drawUtil;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.dr.libloc.attribute.DRMapCell;
import com.dr.libloc.attribute.MapItem;

public class MapItemDrawer_gridding extends MapItem {
    private float[] gridding;
    public float interval;


    public void griddingData(float length, float width){
        interval = DRMapCell.getCmCellSize();
        int row = (int)(length / interval);
        int column = (int)(width / interval);

        gridding = new float[((row + column)*4)];
        int col_by=(int)getBasePoint().getCY();
        int col_ey=(int)length + (int)getBasePoint().getCY();
        int col_bx=(int)getBasePoint().getCX();
        int col_ex=(int)getBasePoint().getCX();
        int row_bx=(int)getBasePoint().getCX();
        int row_ex=(int)width + (int)getBasePoint().getCX();;
        int row_by=(int)getBasePoint().getCY();
        int row_ey=(int)getBasePoint().getCY();

        for(int i = 0; i < column; i++){
            gridding[i*4] = col_bx;
            gridding[i*4+1] = col_by;
            gridding[i*4+2] = col_ex;
            gridding[i*4+3] = col_ey;
            col_bx = col_bx + (int)interval;
            col_ex = col_ex + (int)interval;
        }
        for(int i = column; i < (row + column); i++){
            gridding[i*4] = row_bx;
            gridding[i*4+1] = row_by;
            gridding[i*4+2] = row_ex;
            gridding[i*4+3] = row_ey;
            row_by = row_by + (int)interval;
            row_ey = row_ey + (int)interval;
        }
    }


    @Override
    public void draw(Canvas canvas, Paint paint) {
        paint.setARGB(50, 0, 0 ,255);
//        paint.setColor(Color.parseColor("#F00000FF"));
        canvas.drawLines(gridding, paint);
    }
}
