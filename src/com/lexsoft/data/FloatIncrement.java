package com.lexsoft.data;

/**
 * 生成递增Float
 * @author 牧心牛(QQ:2480102119)
 */
public class FloatIncrement implements DataGenerator<Float> {
    private float start;
    private float end;
    private float step;
    private float current;
    public FloatIncrement(float start, float end, float step){
        this.start = start;
        this.end = end;
        this.step = step;
        this.current = this.start;
    }
    @Override
    public Float generate() {
        float value = current;
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
