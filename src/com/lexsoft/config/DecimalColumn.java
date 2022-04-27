package com.lexsoft.config;

import com.lexsoft.data.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Decimal字段设置
 * @author 牧心牛(QQ:2480102119)
 */
public class DecimalColumn extends Column {
    DataGenerator<BigDecimal> generator;

    @Override
    protected void setupFixed(FixedNode fixedNode) {
        this.generator = new FixedGenerator<>(new BigDecimal(fixedNode.getValue().trim()));
    }

    @Override
    protected void setupIncrement(IncrementNode incrementNode) {
        this.generator = new DecimalIncrement(
                incrementNode.getStart().isEmpty()?BigDecimal.ZERO:new BigDecimal(incrementNode.getStart().trim()),
                incrementNode.getEnd().isEmpty()?BigDecimal.valueOf(Long.MAX_VALUE):new BigDecimal(incrementNode.getEnd().trim()),
                incrementNode.getStep().isEmpty()?BigDecimal.ONE:new BigDecimal(incrementNode.getStep().trim())
        );
    }

    @Override
    protected void setupRandom(RandomNode randomNode) {
        this.generator = new DecimalRandom(
                randomNode.getMin().isEmpty()?BigDecimal.ZERO:new BigDecimal(randomNode.getMin().trim()),
                randomNode.getMax().isEmpty()?BigDecimal.valueOf(Long.MAX_VALUE):new BigDecimal(randomNode.getMax().trim())
        );
    }

    @Override
    protected void setupList(ListNode listNode) {
        List<BigDecimal> list = new ArrayList<>();
        for(String item : listNode.getItems()){
            list.add(new BigDecimal(item.trim()));
        }
        this.generator = new ListGenerator<>(list);
    }

    @Override
    protected void setupRoulette(RouletteNode rouletteNode) {
        List<BigDecimal> list = new ArrayList<>();
        for(String item : rouletteNode.getItems()){
            list.add(new BigDecimal(item.trim()));
        }
        this.generator = new RouletteGenerator<>(list);

    }

    @Override
    protected void setupRatio(RatioNode ratioNode) {
        Map<BigDecimal,Float> items = new HashMap<>();
        for(Map.Entry<String,Float> entry : ratioNode.getItems().entrySet()){
            items.put(new BigDecimal(entry.getKey()),entry.getValue());
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
