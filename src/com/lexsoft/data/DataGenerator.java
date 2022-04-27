package com.lexsoft.data;

/**
 * 数据生成器接口
 * @author 牧心牛(QQ:2480102119)
 */
public interface DataGenerator<T> {
    DataGenerator<String> NULL = () -> null;
    T generate();
}
