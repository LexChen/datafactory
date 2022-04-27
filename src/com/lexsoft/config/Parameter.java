package com.lexsoft.config;

/**
 * options可配置参数
 * @author 牧心牛(QQ:2480102119)
 */
public enum Parameter {
    drop("true"),           //是否在任务开始时drop指定表, if drop the table at start time (if exists)
    create("true"),         //是否在任务开始时create指定表, if create the table at start time (if not exists)
    truncate("true"),       //是否在任务开始时truncate指定表, if truncate the table at start time
    threads("1"),        //指定并发线程数(每个表一个线程) , max parallel threads (one thread for each table)
    batch("2000"),          //批量提交记录数, batch size for inserts
    count("10000"),          //指定要制造的记录行数, record counts to be produced
    path(""),           //指定文件输出目录,缺省当前目录, output path, default current directory
    autoshow("true"),       //是否自动显示执行速度和进度,缺省true , auto print the progress and speed ,缺省 true
    interval("30000"),      //自动显示进度时间间隔,单位毫秒,缺省30秒
    split_count("0"),    //输出文件时,多少条记录分隔为一个文件,缺省不分隔, record counts for splitted output files , default Long.MAX
    split_size("0"),     //输出文件时,多少字符分隔为一个文件,缺省不分隔, character size for splitted output files, default Long.MAX
    field_delim(","),    //输出文件时的字段分隔符,缺省逗号, field delimiter for output file , default comma
    record_delim("\n"),   //输出文件时的记录分隔符,缺省换行, record delimiter for output file , default cr
    nullas("\\N"),         //输出文件时null值记录为何,缺省 \N, how to represent null for output file ,default \N
    debug("false");          //是否显示调试信息
    String defaultValue;
    Parameter(String defaultValue){
        this.defaultValue = defaultValue;
    }
    public String defaultValue(){
        return this.defaultValue;
    }
}
