package com.lexsoft.config;

import com.lexsoft.data.DataGenerator;

/**
 * Skip段设置
 * @author 牧心牛(QQ:2480102119)
 */
public class SkipColumn extends Column {
    @Override
    protected void setupFixed(FixedNode fixedNode) {
    }

    @Override
    protected void setupIncrement(IncrementNode incrementNode) {
    }

    @Override
    protected void setupRandom(RandomNode randomNode) {
    }

    @Override
    protected void setupList(ListNode listNode) {
    }

    @Override
    protected void setupRoulette(RouletteNode rouletteNode) {
    }

    @Override
    protected void setupRatio(RatioNode ratioNode) {
    }

    @Override
    public String doGenerate() {
        return null;
    }

    @Override
    public DataGenerator getDataGenerator(){
        return DataGenerator.NULL;
    }
}
