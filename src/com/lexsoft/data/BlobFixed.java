package com.lexsoft.data;

import com.lexsoft.utils.Tools;

/**
 * 生成固定字节序列
 * @author 牧心牛(QQ:2480102119)
 */
public class BlobFixed implements DataGenerator<String> {
    private String value;
    public BlobFixed(String value){
        if(value.startsWith("0x")){
            this.value = value.substring(2);
        }else{
            byte[] bytes = value.getBytes();
            this.value = Tools.toHexString(bytes,0,bytes.length);
        }
    }
    @Override
    public String generate() {
        return value;
    }

    public String toString(){
        return getClass().getSimpleName()+"{value="+value+"}";
    }
}
