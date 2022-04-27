package com.lexsoft.job;

/**
 * 加载数据方式
 * @author 牧心牛(QQ:2480102119)
 */
public enum Method {
    insert, //批量插入
    values, //mysql insert多值
    load,   //mysql load data
    copy,   //postgres及gauss copy
    bulk,   //sqlserver bulkcopy
    file,   //输出到文件
    add_as_needed, //以后扩展
}
