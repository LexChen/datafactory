package com.lexsoft.config;

import com.lexsoft.data.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Int字段设置
 * @author 牧心牛(QQ:2480102119)
 */
public class IntColumn extends Column {
    DataGenerator<Integer> generator;

    @Override
    protected void setupFixed(FixedNode fixedNode) {
        this.generator = new FixedGenerator<>(Integer.parseInt(fixedNode.getValue().trim()));
    }

    @Override
    protected void setupIncrement(IncrementNode incrementNode) {
        this.generator = new IntIncrement(
                incrementNode.getStart().isEmpty()?0:Integer.parseInt(incrementNode.getStart().trim()),
                incrementNode.getEnd().isEmpty()?Integer.MAX_VALUE:Integer.parseInt(incrementNode.getEnd().trim()),
                incrementNode.getStep().isEmpty()?1:Integer.parseInt(incrementNode.getStep().trim())
        );
    }

    @Override
    protected void setupRandom(RandomNode randomNode) {
        this.generator = new IntRandom(
                randomNode.getMin().isEmpty()?0:Integer.parseInt(randomNode.getMin().trim()),
                randomNode.getMax().isEmpty()?Integer.MAX_VALUE:Integer.parseInt(randomNode.getMax().trim())
        );
    }

    @Override
    protected void setupList(ListNode listNode) {
        List<Integer> list = new ArrayList<>();
        for(String item : listNode.getItems()){
            list.add(Integer.parseInt(item.trim()));
        }
        this.generator = new ListGenerator<>(list);
    }

    @Override
    protected void setupRoulette(RouletteNode rouletteNode) {
        List<Integer> list = new ArrayList<>();
        for(String item : rouletteNode.getItems()){
            list.add(Integer.parseInt(item.trim()));
        }
        this.generator = new RouletteGenerator<>(list);

    }

    @Override
    protected void setupRatio(RatioNode ratioNode) {
        Map<Integer,Float> items = new HashMap<>();
        for(Map.Entry<String,Float> entry : ratioNode.getItems().entrySet()){
            items.put(Integer.parseInt(entry.getKey()),entry.getValue());
        }
        this.generator = new RatioGenerator(items);
    }

    @Override
    public String doGenerate() {
        return String.valueOf(generator.generate());
    }

    @Override
    public DataGenerator getDataGenerator(){
        return this.generator;
    }
}
