package com.lexsoft.data;

import java.util.Random;

/**
 * 生成随机Double
 * @author 牧心牛(QQ:2480102119)
 */
public class DoubleRandom implements DataGenerator<Double> {
    private double start;
    private double end;
    private Random random;
    public DoubleRandom(double start, double end){
        this.start = start;
        this.end = end - start;
        this.random = new Random();
    }
    @Override
    public Double generate() {
        return start+random.nextFloat()*end;
    }

    public String toString(){
        return getClass().getSimpleName()+"{min="+start+",max="+(start+end)+"}";
    }
}
