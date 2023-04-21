package com.dr.location;

import android.util.Log;

import com.dr.libloc.attribute.DR3DPoint;
import com.dr.libloc.attribute.DRRoadArc;
import com.dr.libloc.attribute.DRRoadVertex;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void arcDistance_isCorrect() {
        DRRoadArc arc = new DRRoadArc();
        DRRoadVertex vertex0 = new DRRoadVertex();
        vertex0.setZ(0);
        vertex0.setX(1);
        vertex0.setY(1);
        DRRoadVertex vertex1 = new DRRoadVertex();
        vertex1.setZ(0);
        vertex1.setX(2);
        vertex1.setY(2);
        arc.setVetext0(vertex0);
        arc.setVetext1(vertex1);
        arc.calLinearParam();
        DR3DPoint p = new DR3DPoint();
        DR3DPoint project;
        float distance;
        boolean bIn;
        p.z = 0;
        p.x = 1;
        p.y = 2;
        distance = arc.calDistence2D(p);
        project = arc.porject2D(p);
        bIn = arc.isPointInArc(project);
        System.out.println(String.format("distance is : %f",distance));
        System.out.println(String.format("project point is : " + project));
        System.out.println(String.format("is in arc : " + bIn));

        p.x = -1;
        distance = arc.calDistence2D(p);
        project = arc.porject2D(p);
        bIn = arc.isPointInArc(project);
        System.out.println(String.format("distance is : %f",distance));
        System.out.println(String.format("project point is : " + project));
        System.out.println(String.format("is in arc : " + bIn));

        arc.setVetext0(vertex1);
        arc.setVetext1(vertex0);

        p.z = 0;
        p.x = 1;
        p.y = 2;
        distance = arc.calDistence2D(p);
        project = arc.porject2D(p);
        bIn = arc.isPointInArc(project);
        System.out.println(String.format("distance is : %f",distance));
        System.out.println(String.format("project point is : " + project));
        System.out.println(String.format("is in arc : " + bIn));

        p.x = -1;
        distance = arc.calDistence2D(p);
        project = arc.porject2D(p);
        bIn = arc.isPointInArc(project);
        System.out.println(String.format("distance is : %f",distance));
        System.out.println(String.format("project point is : " + project));
        System.out.println(String.format("is in arc : " + bIn));


        p.z = 0;
        p.x = 2;
        p.y = 1;
        distance = arc.calDistence2D(p);
        project = arc.porject2D(p);
        bIn = arc.isPointInArc(project);
        System.out.println(String.format("distance is : %f",distance));
        System.out.println(String.format("project point is : " + project));
        System.out.println(String.format("is in arc : " + bIn));

        vertex0.setZ(0);
        vertex0.setX(1);
        vertex0.setY(1);
        vertex1.setZ(0);
        vertex1.setX(1);
        vertex1.setY(2);
        arc.setVetext0(vertex0);
        arc.setVetext1(vertex1);
        arc.calLinearParam();
        p.z = 0;
        p.x = 2;
        p.y = 1;
        distance = arc.calDistence2D(p);
        project = arc.porject2D(p);
        bIn = arc.isPointInArc(project);
        System.out.println(String.format("distance is : %f",distance));
        System.out.println(String.format("project point is : " + project));
        System.out.println(String.format("is in arc : " + bIn));


        p.z = 0;
        p.x = 2;
        p.y = 4;
        distance = arc.calDistence2D(p);
        project = arc.porject2D(p);
        bIn = arc.isPointInArc(project);
        System.out.println(String.format("distance is : %f",distance));
        System.out.println(String.format("project point is : " + project));
        System.out.println(String.format("is in arc : " + bIn));


        vertex0.setZ(0);
        vertex0.setX(1);
        vertex0.setY(1);
        vertex1.setZ(0);
        vertex1.setX(2);
        vertex1.setY(1);
        arc.setVetext0(vertex0);
        arc.setVetext1(vertex1);
        arc.calLinearParam();
        System.out.println(String.format("distance is : %f",distance));
        System.out.println(String.format("project point is : " + project));
        System.out.println(String.format("is in arc : " + bIn));

        p.z = 0;
        p.x = 1.5f;
        p.y = 1;
        distance = arc.calDistence2D(p);
        project = arc.porject2D(p);
        bIn = arc.isPointInArc(project);
        System.out.println(String.format("distance is : %f",distance));
        System.out.println(String.format("project point is : " + project));
        System.out.println(String.format("is in arc : " + bIn));

        assertEquals(4, 2 + 2);
    }

}