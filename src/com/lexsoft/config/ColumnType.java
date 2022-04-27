package com.lexsoft.config;

import java.sql.Types;

/**
 * 字段类型
 * @author 牧心牛(QQ:2480102119)
 */
public enum ColumnType {
    SKIP("",Types.NULL),
    SHORT("com.lexsoft.config.ShortColumn", Types.SMALLINT),
    INT("com.lexsoft.config.IntColumn", Types.INTEGER),
    LONG("com.lexsoft.config.LongColumn",Types.BIGINT),
    FLOAT("com.lexsoft.config.FloatColumn",Types.FLOAT),
    DOUBLE("com.lexsoft.config.DoubleColumn",Types.DOUBLE),
    DECIMAL("com.lexsoft.config.DecimalColumn",Types.DECIMAL),
    CHAR("com.lexsoft.config.CharColumn",Types.CHAR),
    VARCHAR("com.lexsoft.config.VarcharColumn",Types.VARCHAR),
    BLOB("com.lexsoft.config.BlobColumn",Types.BLOB),
    DATE("com.lexsoft.config.DateColumn",Types.DATE),
    TIME("com.lexsoft.config.TimeColumn",Types.TIME),
    TIMESTAMP("com.lexsoft.config.TimestampColumn",Types.TIMESTAMP),
    USERDEFINE("",-9999);
    String className;
    int    jdbcType;

    ColumnType(String className,int jdbcType){
        this.className = className;
        this.jdbcType = jdbcType;
    }

    public String getClassName(){
        return this.className;
    }

    public int getJdbcType(){
        return this.jdbcType;
    }
    public static ColumnType of(String name){
        for(ColumnType type : values()){
            if(type.name().equalsIgnoreCase(name)){
                return type;
            }
        }
        return USERDEFINE;
    }

}
