package com.lexsoft.data;

import com.lexsoft.utils.Tools;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * 生成递增日期
 *                 <increment>
 *                     <min>2020-01-01</min>
 *                     <max>2020-03-01</max>
 *                     <step>2d</step>
 *                 </increment>
 *  日期从2020-01-1（包含）开始，到2020-03-01（不包含）截止，步长为 2 天
 *  step支持的格式为：nd ,其中 n为整数, 字母d代表天,不可省略
 * @author 牧心牛(QQ:2480102119)
 */
public class DateIncrement implements DataGenerator<String> {
    private long start;
    private long end;
    private long step;
    private long current;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public DateIncrement(String start, String end, String step){
        this.start = Date.valueOf(start).getTime();
        this.end = Date.valueOf(end).getTime();
        this.step = Tools.parseTimeStep(step);
        this.current = this.start;
    }
    @Override
    public String generate() {
        long value = current;
        current += step;
        if(current>=end){
            current = current - end + start;
        }
        return dateFormat.format(value);
    }

    public String toString(){
        return getClass().getSimpleName()+"{start="+ dateFormat.format(start)+",end="+ dateFormat.format(end)+",step="+step+"}";
    }
}
