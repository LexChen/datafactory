package com.lexsoft.utils;

import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 运行统计
 * @author 牧心牛(QQ:2480102119)
 */
public class Statistic {
    static volatile boolean autoshow = true;
    static int     interval = 30000;
    static HashMap<String,Long> records = new HashMap<>();
    static HashMap<String,Long> times = new HashMap<>();
    static AtomicLong totalRecords = new AtomicLong(0);
    static ReentrantLock lock = new ReentrantLock();
    static long startTime = System.currentTimeMillis();
    static long endTime;
    static ScheduledExecutorService scheduled;

    public static void setAutoshow(boolean _autoshow){
        autoshow = _autoshow;
    }

    public static void setShowInterval(int _interval){
        interval = _interval;
    }

    public static void addTotal(long delta){
        totalRecords.addAndGet(delta);
    }

    public static void reset(){
        lock.lock();
        records = new HashMap<>();
        times = new HashMap<>();
        lock.unlock();
    }

    public static void update(String name,long count,long time){
        lock.lock();
        records.put(name,count);
        times.put(name,time);
        lock.unlock();
    }

    public static String formatMillis(long millis){
        DecimalFormat df = new DecimalFormat("#00");
        long seconds = millis/1000;
        millis = millis%1000;
        long minutes = seconds / 60;
        seconds = seconds%60;
        long hour = minutes/60;
        minutes = minutes%60;
        StringBuilder sb = new StringBuilder();
        if(hour>0){
            sb.append(df.format(hour)).append(":");
        }
        if(minutes>0){
            sb.append(df.format(minutes)).append(":");
        }
        boolean hasHourOrMinutes = (hour+minutes)>0;
        sb.append(hasHourOrMinutes?df.format(seconds):seconds);
        if(millis>0){
            sb.append(".").append(millis);
        }
        if(!hasHourOrMinutes){
            sb.append(" s");
        }
        return sb.toString();
    }

    public static long[] getSpeed(){
        long totalCounts = 0;
        lock.lock();
        for(String name : records.keySet()){
            totalCounts += records.get(name);
        }
        lock.unlock();
        long time = (System.currentTimeMillis() - startTime + 1);
        long remainTime = (long)((1.0*totalRecords.get()/totalCounts - 1) * time);
        return new long[]{(long)(1.0*totalCounts/time*1000),remainTime};
    }

    public static long getFinalSpeed(){
        long totalCounts = 0;
        lock.lock();
        for(String name : records.keySet()){
            totalCounts += records.get(name);
        }
        long time = (endTime - startTime + 1);
        lock.unlock();
        return (long)(1.0*totalCounts/time*1000);
    }

    public static void listDetailSpeed(PrintStream out){
        DecimalFormat df = new DecimalFormat("#,###");
        System.out.println("Table                Records                         Speed");
        System.out.println("-------------------- -------------------- ----------------");
        for(String name : records.keySet()){
            out.print((name+"                      ").substring(0,20));
            out.print(" ");
            String count = df.format(records.get(name))+"                         ";
            out.print(count.substring(0,21));
            String speed = "           "+df.format((int)(1.0*records.get(name)/(times.getOrDefault(name,0L)+1)*1000));
            out.print(speed.substring(speed.length()-9));
            out.println(" recs/s");
        }
    }

    public static void start(){
        startTime = System.currentTimeMillis();
        if(autoshow){
            scheduled = new ScheduledThreadPoolExecutor(1);
            scheduled.scheduleAtFixedRate(() -> {
                long[] speed = getSpeed();
                String speedStr = "       "+new DecimalFormat("#,###").format(speed[0]);
                System.out.println("current speed : "+speedStr.substring(speedStr.length()-8)+" recs/s , remain time : " + formatMillis(speed[1]));
            },interval,interval,TimeUnit.MILLISECONDS);
        }
    }

    public static void end(){
        endTime = System.currentTimeMillis();
        autoshow = false;
        if(scheduled!=null){
            scheduled.shutdown();
        }
    }
}
