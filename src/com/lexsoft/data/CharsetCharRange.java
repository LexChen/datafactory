package com.lexsoft.data;

import java.util.Random;

/**
 * char范围
 * @author 牧心牛(QQ:2480102119)
 */
public class CharsetCharRange implements CharRange {
    private Random random;
    int start;
    int length;
    private String fixed;
    public CharsetCharRange(int start,int end){
        this.start = start;
        this.length = end-start;
        this.fixed = new String(new char[]{(char)start});
        this.random = new Random();
    }
    public String fixedString(){
        return this.fixed;
    }
    public char randomChar(){
        return (char)(start+random.nextInt(length));
    }
}
