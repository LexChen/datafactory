package com.lexsoft.config;

import com.lexsoft.data.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Float字段设置
 * @author 牧心牛(QQ:2480102119)
 */
public class FloatColumn extends Column {
    DataGenerator<Float> generator;

    @Override
    protected void setupFixed(FixedNode fixedNode) {
        this.generator = new FixedGenerator<>(Float.parseFloat(fixedNode.getValue().trim()));
    }

    @Override
    protected void setupIncrement(IncrementNode incrementNode) {
        this.generator = new FloatIncrement(
                incrementNode.getStart().isEmpty()?0:Float.parseFloat(incrementNode.getStart().trim()),
                incrementNode.getEnd().isEmpty()?Float.MAX_VALUE:Float.parseFloat(incrementNode.getEnd().trim()),
                incrementNode.getStep().isEmpty()?1:Float.parseFloat(incrementNode.getStep().trim())
        );
    }

    @Override
    protected void setupRandom(RandomNode randomNode) {
        this.generator = new FloatRandom(
                randomNode.getMin().isEmpty()?0:Float.parseFloat(randomNode.getMin().trim()),
                randomNode.getMax().isEmpty()?Float.MAX_VALUE:Float.parseFloat(randomNode.getMax().trim())
        );
    }

    @Override
    protected void setupList(ListNode listNode) {
        List<Float> list = new ArrayList<>();
        for(String item : listNode.getItems()){
            list.add(Float.parseFloat(item.trim()));
        }
        this.generator = new ListGenerator<>(list);
    }

    @Override
    protected void setupRoulette(RouletteNode rouletteNode) {
        List<Float> list = new ArrayList<>();
        for(String item : rouletteNode.getItems()){
            list.add(Float.parseFloat(item.trim()));
        }
        this.generator = new RouletteGenerator<>(list);

    }

    @Override
    protected void setupRatio(RatioNode ratioNode) {
        Map<Float,Float> items = new HashMap<>();
        for(Map.Entry<String,Float> entry : ratioNode.getItems().entrySet()){
            items.put(Float.parseFloat(entry.getKey()),entry.getValue());
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
