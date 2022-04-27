package com.lexsoft.data;

/**
 * 返回递增整数
 * @author 牧心牛(QQ:2480102119)
 */
public class IntIncrement implements DataGenerator<Integer> {
    private int start;
    private int end;
    private int step;
    private int current;
    public IntIncrement(int start,int end,int step){
        this.start = start;
        this.end = end;
        this.step = step;
        this.current = this.start;
    }
    @Override
    public Integer generate() {
        int value = current;
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
