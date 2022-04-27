package com.lexsoft.data;

import java.util.Random;

/**
 * 可选字符集合
 * @author 牧心牛(QQ:2480102119)
 */
public class CandicatesCharRange implements CharRange {
    private char[] candidates;
    private Random random;
    private String fixed;
    public CandicatesCharRange(char[] candidates){
        this.candidates = candidates;
        this.random = new Random();
        this.fixed = new String(candidates);
    }
    public String fixedString(){
        return this.fixed;
    }
    public char randomChar(){
        return candidates[random.nextInt(candidates.length)];
    }
}
