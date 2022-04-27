package com.lexsoft.data;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Random;

/**
 * 生成随机日期，例如 2020-01-01
 * @author 牧心牛(QQ:2480102119)
 */
public class DateRandom implements DataGenerator<String> {
    private long start;
    private long end;
    private Random random;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public DateRandom(String start, String end){
        this.start = Date.valueOf(start).getTime();
        this.end = Date.valueOf(end).getTime() - this.start;
        this.random = new Random();
    }
    @Override
    public String generate() {
        long next = random.nextLong();
        return dateFormat.format(start + (next>=0?next:-next) % end);
    }

    public String toString(){
        return getClass().getSimpleName()+"{min="+dateFormat.format(start)+",max="+dateFormat.format(start+end)+"}";
    }
}
