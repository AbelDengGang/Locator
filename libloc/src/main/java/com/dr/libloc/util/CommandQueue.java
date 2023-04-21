package com.dr.libloc.util;

import android.util.Log;

import java.lang.reflect.Method;
import java.time.Instant;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

import javax.security.auth.callback.CallbackHandler;

import static org.junit.Assert.assertEquals;

public class CommandQueue extends Thread{
    class CommandQueueItem{
        private Object obj;
        private Object params;
        private String funName;
        private long delayms;

        public CommandQueueItem(Object obj, String funName,Object[] params, long delayms) {
            this.obj = obj;
            this.funName = funName;
            this.delayms = delayms;
            this.params = params;
        }

        public CommandQueueItem(Object obj, String funName, long delayms) {
            this.obj = obj;
            this.funName = funName;
            this.delayms = delayms;
        }

        public boolean exec(){
            // Exec functions, and delay after exec
            Log.i("CommandQueueItem","exec:"+ funName);

            Class clz = obj.getClass();
            try {
                if (params == null) {
                    Method m = clz.getDeclaredMethod(funName);
                    m.setAccessible(true);// 调用private方法的关键一句话
                    m.invoke(obj);
                } else {
                    Method m = clz.getDeclaredMethod(funName,Object[].class);
                    m.setAccessible(true);// 调用private方法的关键一句话
                    m.invoke(obj,params);
                }
            }catch (Exception e){
                Log.e(TAG,e.toString());
            }
            if (delayms > 0 ) {
                try {
                    Thread.sleep(delayms);
                }catch (InterruptedException e){
                    Log.e("CommandQueueItem",e.toString());
                    Log.e("CommandQueueItem","Interrupted");
                    return false;
                }
            }
            return true;
        }
    };
    private Semaphore semp;
    private static String threadName = "CommandQueue";
    static String TAG = "CommandQueue";

    private ConcurrentLinkedQueue<CommandQueueItem> cmds;

    public CommandQueue() {
        this.cmds = new ConcurrentLinkedQueue<CommandQueueItem>();
        semp = new Semaphore(0);
    }

    public void addCmd(Object obj,String funName,long delayms){
        CommandQueueItem item = new CommandQueueItem(obj,funName,delayms);
        cmds.offer(item);
        semp.release();
    }
    public void addCmd(Object obj,String funName,Object[] params,long delayms){
        CommandQueueItem item = new CommandQueueItem(obj,funName,params,delayms);
        cmds.offer(item);
        semp.release();
    }

    public void run(){
        Log.i(TAG,"run");
        while (true){
            try {
                Log.i(TAG,"try to acquire semp");
                semp.acquire();
                Log.i(TAG,"acquired");
            }catch (InterruptedException e){
                Log.e(TAG,e.toString());
                Log.e(TAG,"Interrupted ,quit thread");
                break;
            }
            // get a new command to exec
            CommandQueueItem item = pollCmd();
            if (item.exec() == false){
                Log.e(TAG,"exec false ,quit thread");
                break;
            }
            item = null;
        }
        Log.i(TAG,"finish");
    }
    private CommandQueueItem pollCmd(){
        return cmds.poll();
    }
}
