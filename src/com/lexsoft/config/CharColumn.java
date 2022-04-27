package com.lexsoft.config;

import com.lexsoft.data.*;
import com.lexsoft.utils.Tools;

import java.util.ArrayList;
import java.util.List;

/**
 * Char字段设置
 * @author 牧心牛(QQ:2480102119)
 */
public class CharColumn extends Column {
    DataGenerator<String> generator;
    @Override
    protected void setupFixed(FixedNode fixedNode) {
        this.generator = new FixedGenerator<>(fixedNode.getValue().trim());
    }

    @Override
    protected void setupIncrement(IncrementNode incrementNode) {
        this.generator = new FixedGenerator<>("A");
    }

    @Override
    protected void setupRandom(RandomNode randomNode) {
        if(randomNode.getCandidates().toLowerCase().startsWith("??id")){
            this.generator = new IDGenerator(randomNode.getMin(),randomNode.getMax(),randomNode.getCandidates());
        }else{
            this.generator = new CharRandom(Integer.parseInt(randomNode.getMin()),Integer.parseInt(randomNode.getMax()),randomNode.getCandidates(),randomNode.getPrefix(),randomNode.getSuffix());
        }
    }

    @Override
    protected void setupList(ListNode listNode) {
        List<String> translated = new ArrayList<>();
        for(String item : listNode.getItems()){
            if(item.startsWith("0x")){
                translated.add(Tools.unHexString(item.substring(2)));
            }else{
                translated.add(item);
            }
        }
        this.generator = new ListGenerator<>(translated);
    }

    @Override
    protected void setupRoulette(RouletteNode rouletteNode) {
        List<String> translated = new ArrayList<>();
        for(String item : rouletteNode.getItems()){
            if(item.trim().startsWith("0x")){
                translated.add(Tools.unHexString(item.substring(2)));
            }else{
                translated.add(item);
            }
        }
        this.generator = new RouletteGenerator<>(translated);
    }

    @Override
    protected void setupRatio(RatioNode ratioNode) {
        this.generator = new RatioGenerator(ratioNode.getItems());
    }

    @Override
    public String doGenerate() {
        return generator.generate();
    }

    @Override
    public DataGenerator getDataGenerator(){
        return this.generator;
    }
}
