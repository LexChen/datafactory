package com.lexsoft.data;

import java.util.UUID;

/**
 * 生成UUID
 * @author 牧心牛(QQ:2480102119)
 */
public class UUIDCharRange implements CharRange {
    private String fixed;
    public UUIDCharRange(){
        this.fixed = UUID.randomUUID().toString();
    }
    public String fixedString(){
        return this.fixed;
    }

    @Override
    public String randomString(int length){
        return UUID.randomUUID().toString();
    }

    public char randomChar(){
        return ' ';
    }
}
