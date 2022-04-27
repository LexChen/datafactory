package com.lexsoft.data;

import com.lexsoft.utils.Tools;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * 生成递增时间
 *                 <increment>
 *                     <min>00:00:01</min>
 *                     <max>12:00:01</max>
 *                     <step>30m</step>
 *                 </increment>
 *  日期从00:00:01（包含）开始，到12:00:01（不包含）截止，步长为 30 分钟
 *  step支持的格式为：1h2m3s123 ,其中数字可按需指定,不需要的部分可省略
 *  字母h代表小时,字母m代表分钟,s代表秒,最后的三位数字代表毫秒,上例含义为  1小时2分钟3秒123毫秒
 *  再入: 1h2s 代表1小时2秒
 * @author 牧心牛(QQ:2480102119)
 */
public class TimeIncrement implements DataGenerator<String> {
    private int start;
    private int end;
    private int step;
    private int current;
    private DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    public TimeIncrement(String start, String end, String step){
        this.start = (int) Time.valueOf(start).getTime();
        this.end = (int) Time.valueOf(end).getTime();
        this.step = (int)Tools.parseTimeStep(step);
        this.current = this.start;
    }
    @Override
    public String generate() {
        int value = current;
        current += step;
        if(current>=end){
            current = current - end + start;
        }
        return timeFormat.format(value);
    }

    public String toString(){
        return getClass().getSimpleName()+"{start="+timeFormat.format(start)+",end="+timeFormat.format(end)+",step="+step+"}";
    }
}
