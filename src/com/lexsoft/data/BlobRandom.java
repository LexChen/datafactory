package com.lexsoft.data;

import com.lexsoft.utils.Tools;

import java.util.Random;

/**
 * 生成随机字节序列
 * @author 牧心牛(QQ:2480102119)
 */
public class BlobRandom implements DataGenerator<String> {
    private int min;
    private int max;
    private CharRange charRange;
    private String candidates;
    private Random random;
    public BlobRandom(int min, int max, String candidates){
        this.min = min;
        this.max = max - min;
        this.candidates = candidates;
        this.charRange = CharRangeFactory.build(candidates);
        this.random = new Random();
    }
    @Override
    public String generate() {
        int size = random.nextInt(max)+min;
        if(size==0){
            return "";
        }
        byte[] bytes = charRange.randomString(size).getBytes();
        return Tools.toHexString(bytes,0,size<bytes.length?size:bytes.length);
    }

    public String toString(){
        return getClass().getSimpleName()+"{min="+min+",max="+(max+min)+",candidates="+candidates+"}";
    }
}
