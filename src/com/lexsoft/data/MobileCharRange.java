package com.lexsoft.data;

import java.text.DecimalFormat;
import java.util.Random;

/**
 * 随机生成手机号
 * <candidates>??mobile</candidates>
 * 随机手机号,前缀为138/185/133/165
 * <candidates>??mobile:133,145</candidates>
 * 此模式指定前缀类型,此例中手机号前缀为 133或145
 * @author 牧心牛(QQ:2480102119)
 */
public class MobileCharRange implements CharRange {
    private String fixed = "13800000000";
    private String[] prefix = {"138","185","133","165"};
    private DecimalFormat df;
    private Random random;
    public MobileCharRange(String type){
        this.df = new DecimalFormat("00000000");
        this.random = new Random();
        int pos = type.indexOf(":");
        if(pos>0){
           prefix = type.substring(pos+1).split(",");
        }
    }
    public String fixedString(){
        return this.fixed;
    }

    @Override
    public String randomString(int length){
        return prefix[random.nextInt(prefix.length)] + df.format(random.nextInt(99999999));
    }

    public char randomChar(){
        return ' ';
    }
}
