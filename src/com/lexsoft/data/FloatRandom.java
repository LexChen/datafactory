package com.lexsoft.data;

import java.util.Random;

/**
 * 生成随机Float
 * @author 牧心牛(QQ:2480102119)
 */
public class FloatRandom implements DataGenerator<Float> {
    private float start;
    private float end;
    private Random random;
    public FloatRandom(float start, float end){
        this.start = start;
        this.end = end - start;
        this.random = new Random();
    }
    @Override
    public Float generate() {
        return start+random.nextFloat()*end;
    }

    public String toString(){
        return getClass().getSimpleName()+"{min="+start+",max="+(start+end)+"}";
    }
}
