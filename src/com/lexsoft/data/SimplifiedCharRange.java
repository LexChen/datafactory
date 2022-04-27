package com.lexsoft.data;

import java.util.Random;

/**
 * 生成随机简体中文字符串
 * @author 牧心牛(QQ:2480102119)
 */
public class SimplifiedCharRange implements CharRange {
    private Random random;
    private String fixed = "陈";
    int hBase = 0xb0;
    int hLength = 0xf7-hBase;
    int lBase = 0xa1;
    int lLength = 0xfe-lBase;
    public SimplifiedCharRange(){
        this.random = new Random();
    }
    public String fixedString(){
        return this.fixed;
    }
    public char randomChar(){
        byte[] buffer = new byte[2];
        buffer[0] = (byte)(hBase + random.nextInt(hLength));
        buffer[1] = (byte)(lBase + random.nextInt(lLength));
        try{
            return (new String(buffer,"gb2312")).toCharArray()[0];
        }catch(Exception e){
            return '陈';
        }
    }
    public String randomString(int length){
        length = length*2;
        byte[] buffer = new byte[length];
        for(int i=0;i<length;i+=2){
            buffer[i] = (byte)(hBase + random.nextInt(hLength));
            buffer[i+1] = (byte)(lBase + random.nextInt(lLength));
        }
        try{
            return new String(buffer,"gb2312");
        }catch(Exception e){
            return "陈";
        }
    }
}
