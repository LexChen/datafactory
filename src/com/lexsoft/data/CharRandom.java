package com.lexsoft.data;

import java.util.Random;

/**
 * 生成随机字符串
 * @author 牧心牛(QQ:2480102119)
 */
public class CharRandom implements DataGenerator<String> {
    private int min;
    private int max;
    private int limit;
    private String prefix;
    private int    prefixLength;
    private String suffix;
    private int    suffixLength;
    private CharRange charRange;
    private String candidates;
    private Random random;
    public CharRandom(int min,int max,String candidates,String prefix,String suffix){
        this.min = min;
        this.limit = max-1;
        this.max = max - min;
        this.prefix = prefix;
        this.prefixLength = prefix.length();
        this.suffix = suffix;
        this.suffixLength = suffix.length();
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
        String leading = "";
        if(size>=prefixLength){
            size-=prefixLength;
            leading = prefix;
        }
        String tailing = "";
        if(size>=suffixLength){
            size -= suffixLength;
            tailing = suffix;
        }
        if(size==0){
            return leading+tailing;
        }else{
            return leading+charRange.randomString(size)+tailing;
        }
    }

    public String toString(){
        return getClass().getSimpleName()+"{min="+min+",max="+(max+min)+",candidates="+candidates+",prefix="+prefix+",suffix="+suffix+"}";
    }
}
