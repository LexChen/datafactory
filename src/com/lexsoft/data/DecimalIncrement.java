package com.lexsoft.data;

import java.math.BigDecimal;

/**
 * 生成递增BigDecimal
 * @author 牧心牛(QQ:2480102119)
 */
public class DecimalIncrement implements DataGenerator<BigDecimal> {
    private BigDecimal start;
    private BigDecimal end;
    private BigDecimal step;
    private BigDecimal current;
    public DecimalIncrement(BigDecimal start, BigDecimal end, BigDecimal step){
        this.start = start;
        this.end = end;
        this.step = step;
        this.current = this.start;
    }
    @Override
    public BigDecimal generate() {
        BigDecimal value = current;
        current = current.add(step);
        if(current.compareTo(end)>0){
            current = current.subtract(end).add(start);
        }
        return value;
    }

    public String toString(){
        return getClass().getSimpleName()+"{start="+start+",end="+end+",step="+step+"}";
    }
}
