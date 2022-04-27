package com.lexsoft.config;

import com.lexsoft.data.*;

/**
 * Time字段设置
 * @author 牧心牛(QQ:2480102119)
 */
public class TimeColumn extends Column {
    DataGenerator<String> generator;
    @Override
    protected void setupFixed(FixedNode fixedNode) {
        this.generator = new FixedGenerator<>(fixedNode.getValue().trim());
    }

    @Override
    protected void setupIncrement(IncrementNode incrementNode) {
        this.generator = new TimeIncrement(incrementNode.getStart(),incrementNode.getEnd(),incrementNode.getStep());
    }

    @Override
    protected void setupRandom(RandomNode randomNode) {
        this.generator = new TimeRandom(randomNode.getMin(),randomNode.getMax());
    }

    @Override
    protected void setupList(ListNode listNode) {
        this.generator = new ListGenerator<>(listNode.getItems());
    }

    @Override
    protected void setupRoulette(RouletteNode rouletteNode) {
        this.generator = new RouletteGenerator<>(rouletteNode.getItems());
    }

    @Override
    protected void setupRatio(RatioNode ratioNode) {
        this.generator = new RatioGenerator(ratioNode.getItems());
    }

    /**
     * 返回的数据为byte对应的16进制字符串
     * @return
     */
    @Override
    public String doGenerate() {
        return generator.generate();
    }

    @Override
    public DataGenerator getDataGenerator(){
        return this.generator;
    }
}
