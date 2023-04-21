package com.dr.location;

import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.dr.libloc.locator.Locator;
import com.dr.libloc.locator.LocatorMgr;
import com.dr.libloc.mapUtil.DRMap;
import com.dr.libloc.sensor.DRSensorMgr;
import com.dr.libloc.util.CommandQueue;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Method;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    static public String TAG="ExampleInstrumentedTest";
    class CommandQueueTest{
        int id ;
        public int getID(){
            System.out.println(id);
            return id;
        }
        private int _getID(){
            System.out.println(id);
            return id;
        }
        public void setID(Object... args) {
            id = (Integer)args[0];
        }
    }

    //@Test
    public void commqueue_test() {
        Log.e(TAG,"commqueue_test start");

        CommandQueue queue = new CommandQueue();
        queue.start();

        CommandQueueTest q1 = new CommandQueueTest();
        q1.id = 1;
        queue.addCmd(q1,"getID",100);
        q1.id = 2;
        queue.addCmd(q1,"getID",100);
        q1.setID(3);
        assertEquals(3, q1.id);
        q1.setID(new Integer[]{4});
        assertEquals(4, q1.id);
        queue.addCmd(q1,"setID",new Integer[]{5},100);

        Log.e(TAG,"Sleep 500");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            System.out.println(" interrupted.");
        }
        Log.e(TAG,"Sleep finish");
        assertEquals(5, q1.id);
        queue.interrupt();
    }

    //@Test
    public void reflect_test(){
        System.out.println("reflect_test");
        // here's a test for reflect
        CommandQueueTest q1 = new CommandQueueTest();
        q1.id = 2;
        Class clz = q1.getClass();
        try {
            Method m = clz.getMethod("getID");
            assertEquals(2, m.invoke(q1));
        }catch (Exception e){
            System.out.println(e.toString());

        }
    }

    @Test
    public void Locator_RFID_RegionCenter_test(){
        DRMap drMap = new DRMap();
        LocatorMgr.createLocator_RFID();
        Locator locator = LocatorMgr.getLocator();
        locator.start();

    }
    //@Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.dr.location", appContext.getPackageName());
    }
}