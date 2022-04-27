package com.lexsoft.data;

/**
 * 生成递增Long
 * @author 牧心牛(QQ:2480102119)
 */
public class LongIncrement implements DataGenerator<Long> {
    private long start;
    private long end;
    private long step;
    private long current;
    public LongIncrement(long start, long end, long step){
        this.start = start;
        this.end = end;
        this.step = step;
        this.current = this.start;
    }
    @Override
    public Long generate() {
        long value = current;
        current += step;
        if(current>=end){
            current = current - end + start;
        }
        return value;
    }
    public String toString(){
        return getClass().getSimpleName()+"{start="+start+",end="+end+",step="+step+"}";
    }
}
