package com.lexsoft.data;

/**
 * CharRange接口
 * 注意，为了兼容性，一些特殊字符被自动替换为空格 0x20 ，它们是
 *  零字符 0x00 ， 回车 0x0d ，换行 0x0a， 逗号 0x2c ， 反斜线： 0x5c ，单引号 0x27
 * @author 牧心牛(QQ:2480102119)
 */
public interface CharRange {
    String fixedString();
    char randomChar();
    default String randomString(int length) {
        char[] target = new char[length];
        for (int charCount = 0;charCount < length; charCount++) {
            char nextChar = randomChar();
            if(nextChar==0x00||nextChar==0x0d||nextChar==0x0a||nextChar==','||nextChar=='\\'||nextChar==0x27){
                nextChar = 0x20;
            }
            target[charCount] = nextChar;
        }
        byte[] bytes = (new String(target)).getBytes();
        return new String(bytes,0, bytes.length>length?length:bytes.length);
    }
}
