package com.lexsoft.data;

import java.util.Random;

/**
 * 随机生成Long
 * @author 牧心牛(QQ:2480102119)
 */
public class LongRandom implements DataGenerator<Long> {
    private long start;
    private long end;
    private Random random;
    public LongRandom(long start, long end){
        this.start = start;
        this.end = end - start;
        this.random = new Random();
    }
    @Override
    public Long generate() {
        long next = random.nextLong();
        return start + (next>=0?next:-next) % end;
    }
    public String toString(){
        return getClass().getSimpleName()+"{min="+start+",max="+(start+end)+"}";
    }
}
