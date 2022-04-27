package com.lexsoft.data;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Random;

/**
 * 生成随机时间
 * @author 牧心牛(QQ:2480102119)
 */
public class TimeRandom implements DataGenerator<String> {
    private int start;
    private int end;
    private Random random;
    private DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    public TimeRandom(String start, String end){
        this.start = (int)Time.valueOf(start).getTime();
        this.end = (int)Time.valueOf(end).getTime() - this.start;
        this.random = new Random();
    }
    @Override
    public String generate() {
        return timeFormat.format(start+random.nextInt(end));
    }

    public String toString(){
        return getClass().getSimpleName()+"{min="+timeFormat.format(start)+",max="+timeFormat.format(start+end)+"}";
    }
}
