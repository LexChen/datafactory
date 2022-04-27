package com.lexsoft.data;

import java.util.Random;

/**
 * 返回随机整数
 * @author 牧心牛(QQ:2480102119)
 */
public class IntRandom implements DataGenerator<Integer> {
    private int start;
    private int end;
    private Random random;
    public IntRandom(int start, int end){
        this.start = start;
        this.end = end - start;
        this.random = new Random();
    }
    @Override
    public Integer generate() {
        return start+random.nextInt(end);
    }

    public String toString(){
        return getClass().getSimpleName()+"{min="+start+",max="+(start+end)+"}";
    }
}
