package com.lexsoft.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 按概率分布在列表中选择元素,例如:
 *                 <ratio>
 *                     <item ratio="1">a1</item>
 *                     <item ratio="2">a2</item>
 *                     <item ratio="3">a3</item>
 *                 </ratio>
 *                 则 a1 的概率为  1/(1+2+3),a2概率为 2/(1+2+3) ,a3概率为 1/(1+2+3)
 *
 * @author 牧心牛(QQ:2480102119)
 */
public class RatioGenerator<T> implements DataGenerator<T> {
    private List<T> dataList;
    private List<Float> ratioList;
    private Random random;
    private int    size;
    public RatioGenerator(Map<T,Float> items){
        dataList = new ArrayList<>();
        ratioList = new ArrayList<>();
        float total = 0;
        for(Map.Entry<T,Float> entry : items.entrySet()){
            dataList.add(entry.getKey());
            ratioList.add(entry.getValue());
            total += entry.getValue();
        }
        this.size = ratioList.size() - 1;
        for(int i=0;i<=size;i++){
            ratioList.set(i,ratioList.get(i)/total);
        }
        this.random = new Random();
    }

    @Override
    public T generate() {
        float scale = random.nextFloat();
        for(int i=0;i<size;i++){
            if(scale<ratioList.get(i)){
                return dataList.get(i);
            }
        }
        return dataList.get(size);
    }

    public String toString(){
        return getClass().getSimpleName()+"{data="+dataList+",ratio="+ratioList+",type="+dataList.get(0).getClass()+"}";
    }
}
