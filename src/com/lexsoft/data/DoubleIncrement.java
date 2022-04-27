package com.lexsoft.data;

/**
 * 生成递增Double
 * @author 牧心牛(QQ:2480102119)
 */
public class DoubleIncrement implements DataGenerator<Double> {
    private double start;
    private double end;
    private double step;
    private double current;
    public DoubleIncrement(double start, double end, double step){
        this.start = start;
        this.end = end;
        this.step = step;
        this.current = this.start;
    }
    @Override
    public Double generate() {
        double value = current;
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
