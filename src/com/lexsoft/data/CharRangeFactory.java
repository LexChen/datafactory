package com.lexsoft.data;

import com.lexsoft.utils.Tools;

/**
 * CharRange工厂
 * 以??开头的为特定字符集合,否则为普通字符集合
 * @author 牧心牛(QQ:2480102119)
 */
public class CharRangeFactory {

    public static CharRange build(String type){
        if(type==null||type.isEmpty()||type.equalsIgnoreCase("??ascii")){//仅ASCII字符集范围中可显字符
            return new CharsetCharRange(0x20,0x7e);
        }if(type.equalsIgnoreCase("??latin1")){//仅Latin1字符集范围
            return new CharsetCharRange(0x00,0xff);
        }else if(type.startsWith("0x")){//以16进制字节指定的字符范围
            return new CandicatesCharRange(Tools.unHexString(type.substring(2)).toCharArray());
        }else if(type.equalsIgnoreCase("??chn")){//仅简体中文(部分)
            return new SimplifiedCharRange();
        }else if(type.equalsIgnoreCase("??gbk")){//仅GBK汉字(部分)
            return new CharsetCharRange(0x4e00,0x9fa5);
        }else if(type.equalsIgnoreCase("??kor")){//仅韩文(部分)
            return new CharsetCharRange(0xac00,0xd7a3);
        }else if(type.equalsIgnoreCase("??jpn")){//仅日文(部分)
            return new CharsetCharRange(0x30a0,0x30ff);
        }else if(type.equalsIgnoreCase("??upper")){//仅大写字母
            return new CandicatesCharRange("ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray());
        }else if(type.equalsIgnoreCase("??lower")){//仅小写字母
            return new CandicatesCharRange("abcdefghijklmnopqrstuvwxyz".toCharArray());
        }else if(type.equalsIgnoreCase("??alpha")){//仅大小写字母
            return new CandicatesCharRange("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray());
        }else if(type.equalsIgnoreCase("??number")){//仅数字
            return new CandicatesCharRange("0123456789".toCharArray());
        }else if(type.equalsIgnoreCase("??word")){ //仅大小写字母和数字
            return new CandicatesCharRange("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray());
        }else if(type.equalsIgnoreCase("??uuid")){ //UUID
            return new UUIDCharRange();
        }else if(type.startsWith("??mobile")){ //手机号
            return new MobileCharRange(type);
        }
        return new CandicatesCharRange(type.toCharArray()); //普通字符串指定的char范围
    }
}
