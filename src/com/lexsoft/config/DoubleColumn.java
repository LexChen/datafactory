package com.lexsoft.config;

import com.lexsoft.data.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Double字段设置
 * @author 牧心牛(QQ:2480102119)
 */
public class DoubleColumn extends Column {
    DataGenerator<Double> generator;

    @Override
    protected void setupFixed(FixedNode fixedNode) {
        this.generator = new FixedGenerator<>(Double.parseDouble(fixedNode.getValue().trim()));
    }

    @Override
    protected void setupIncrement(IncrementNode incrementNode) {
        this.generator = new DoubleIncrement(
                incrementNode.getStart().isEmpty()?0:Double.parseDouble(incrementNode.getStart().trim()),
                incrementNode.getEnd().isEmpty()?Double.MAX_VALUE:Double.parseDouble(incrementNode.getEnd().trim()),
                incrementNode.getStep().isEmpty()?1:Double.parseDouble(incrementNode.getStep().trim())
        );
    }

    @Override
    protected void setupRandom(RandomNode randomNode) {
        this.generator = new DoubleRandom(
                randomNode.getMin().isEmpty()?0:Double.parseDouble(randomNode.getMin().trim()),
                randomNode.getMax().isEmpty()?Double.MAX_VALUE:Double.parseDouble(randomNode.getMax().trim())
        );
    }

    @Override
    protected void setupList(ListNode listNode) {
        List<Double> list = new ArrayList<>();
        for(String item : listNode.getItems()){
            list.add(Double.parseDouble(item.trim()));
        }
        this.generator = new ListGenerator<>(list);
    }

    @Override
    protected void setupRoulette(RouletteNode rouletteNode) {
        List<Double> list = new ArrayList<>();
        for(String item : rouletteNode.getItems()){
            list.add(Double.parseDouble(item.trim()));
        }
        this.generator = new RouletteGenerator<>(list);

    }

    @Override
    protected void setupRatio(RatioNode ratioNode) {
        Map<Double,Float> items = new HashMap<>();
        for(Map.Entry<String,Float> entry : ratioNode.getItems().entrySet()){
            items.put(Double.parseDouble(entry.getKey()),entry.getValue());
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
