package com.lexsoft.data;

import java.math.BigDecimal;
import java.util.Random;

/**
 * 生成随机BigDecimal，对应数据库中Numeric或Decimal、Money等
 * @author 牧心牛(QQ:2480102119)
 */
public class DecimalRandom implements DataGenerator<BigDecimal> {
    private BigDecimal start;
    private BigDecimal end;
    private Random random;
    public DecimalRandom(BigDecimal start, BigDecimal end){
        this.start = start;
        this.end = end.subtract(start);
        this.random = new Random();
    }
    @Override
    public BigDecimal generate() {
        return start.add(end.multiply(BigDecimal.valueOf(random.nextFloat())));
    }

    public String toString(){
        return getClass().getSimpleName()+"{min="+start+",max="+end.add(start)+"}";
    }
}
