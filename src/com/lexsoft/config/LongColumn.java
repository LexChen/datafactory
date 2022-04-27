package com.lexsoft.config;

import com.lexsoft.data.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Long字段设置
 * @author 牧心牛(QQ:2480102119)
 */
public class LongColumn extends Column {
    DataGenerator<Long> generator;

    @Override
    protected void setupFixed(FixedNode fixedNode) {
        this.generator = new FixedGenerator<>(Long.parseLong(fixedNode.getValue().trim()));
    }

    @Override
    protected void setupIncrement(IncrementNode incrementNode) {
        this.generator = new LongIncrement(
                incrementNode.getStart().isEmpty()?0:Long.parseLong(incrementNode.getStart().trim()),
                incrementNode.getEnd().isEmpty()?Long.MAX_VALUE:Long.parseLong(incrementNode.getEnd().trim()),
                incrementNode.getStep().isEmpty()?1:Long.parseLong(incrementNode.getStep().trim())
        );
    }

    @Override
    protected void setupRandom(RandomNode randomNode) {
        this.generator = new LongRandom(
                randomNode.getMin().isEmpty()?0:Long.parseLong(randomNode.getMin().trim()),
                randomNode.getMax().isEmpty()?Long.MAX_VALUE:Long.parseLong(randomNode.getMax().trim())
        );
    }

    @Override
    protected void setupList(ListNode listNode) {
        List<Long> list = new ArrayList<>();
        for(String item : listNode.getItems()){
            list.add(Long.parseLong(item.trim()));
        }
        this.generator = new ListGenerator<>(list);
    }

    @Override
    protected void setupRoulette(RouletteNode rouletteNode) {
        List<Long> list = new ArrayList<>();
        for(String item : rouletteNode.getItems()){
            list.add(Long.parseLong(item.trim()));
        }
        this.generator = new RouletteGenerator<>(list);

    }

    @Override
    protected void setupRatio(RatioNode ratioNode) {
        Map<Long,Float> items = new HashMap<>();
        for(Map.Entry<String,Float> entry : ratioNode.getItems().entrySet()){
            items.put(Long.parseLong(entry.getKey()),entry.getValue());
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
