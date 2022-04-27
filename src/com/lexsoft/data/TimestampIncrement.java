package com.lexsoft.data;

import com.lexsoft.utils.Tools;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * 生成递增时间戳
 * 与date和time递增类似,step中可以指定1d2h3m4s555 的格式,含义不再赘述
 * @author 牧心牛(QQ:2480102119)
 */
public class TimestampIncrement implements DataGenerator<String> {
    private long start;
    private long end;
    private long step;
    private long current;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
    public TimestampIncrement(String start, String end, String step){
        this.start = Timestamp.valueOf(start).getTime();
        this.end = Timestamp.valueOf(end).getTime();
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
