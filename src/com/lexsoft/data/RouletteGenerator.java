package com.lexsoft.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 随机在列表中选择元素(即等概率RatioGenerator)
 * @author 牧心牛(QQ:2480102119)
 */
public class RouletteGenerator<T> implements DataGenerator<T> {
    private List<T> list;
    private int size;
    private Random random;
    public RouletteGenerator(List<T> list){
        this.list = new ArrayList<>(list);
        this.size = list.size();
        this.random = new Random();
    }
    @Override
    public T generate() {
        return list.get(random.nextInt(size));
    }

    public String toString(){
        return getClass().getSimpleName()+"{"+(list.toString()).replaceAll("[\\[\\]]","")+"}@"+list.get(0).getClass();
    }

}
