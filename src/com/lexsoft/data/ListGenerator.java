package com.lexsoft.data;

import java.util.ArrayList;
import java.util.List;

/**
 * 按顺序返回List中元素,相对于Roulette而言,Roulette是按照随机顺序返回
 * @author 牧心牛(QQ:2480102119)
 */
public class ListGenerator<T> implements DataGenerator<T> {
    private List<T> list;
    private int cursor = 0;
    private int size;
    public ListGenerator(List<T> list){
        this.list = new ArrayList<>(list);
        this.size = list.size();
    }
    @Override
    public T generate() {
        T value = list.get(cursor);
        cursor++;
        if(cursor>=size){
            cursor = 0;
        }
        return value;
    }
    public String toString(){
        return getClass().getSimpleName()+"{"+(list.toString()).replaceAll("[\\[\\]]","")+"}@"+list.get(0).getClass();
    }

}
