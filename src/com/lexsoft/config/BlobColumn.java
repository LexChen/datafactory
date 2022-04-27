package com.lexsoft.config;

import com.lexsoft.data.*;
import com.lexsoft.utils.Tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Blob字段设置
 * @author 牧心牛(QQ:2480102119)
 */
public class BlobColumn extends Column {
    DataGenerator<String> generator;
    @Override
    protected void setupFixed(FixedNode fixedNode) {
        this.generator = new BlobFixed(fixedNode.getValue().trim());
    }

    @Override
    protected void setupIncrement(IncrementNode incrementNode) {
        this.generator = new BlobFixed("A");
    }

    @Override
    protected void setupRandom(RandomNode randomNode) {
        this.generator = new BlobRandom(Integer.parseInt(randomNode.getMin()),Integer.parseInt(randomNode.getMax()),randomNode.getCandidates());
    }

    @Override
    protected void setupList(ListNode listNode) {
        List<String> translated = new ArrayList<>();
        for(String item : listNode.getItems()){
            if(item.startsWith("0x")){
                translated.add(item.substring(2));
            }else{
                byte[] bytes = item.getBytes();
                translated.add(Tools.toHexString(bytes,0,bytes.length));
            }
        }
        this.generator = new ListGenerator<>(translated);
    }

    @Override
    protected void setupRoulette(RouletteNode rouletteNode) {
        List<String> translated = new ArrayList<>();
        for(String item : rouletteNode.getItems()){
            if(item.startsWith("0x")){
                translated.add(item.substring(2));
            }else{
                byte[] bytes = item.getBytes();
                translated.add(Tools.toHexString(bytes,0,bytes.length));
            }
        }
        this.generator = new RouletteGenerator<>(translated);
    }

    @Override
    protected void setupRatio(RatioNode ratioNode) {
        Map<String,Float> translated = new HashMap<>();
        for(Map.Entry<String,Float> item : ratioNode.getItems().entrySet()){
            if(item.getKey().startsWith("0x")){
                translated.put(item.getKey().substring(2),item.getValue());
            }else{
                byte[] bytes = item.getKey().getBytes();
                translated.put(Tools.toHexString(bytes,0,bytes.length),item.getValue());
            }
        }
        this.generator = new RatioGenerator(translated);
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
