package com.lexsoft.data;

/**
 * 始终返回固定值
 * @author 牧心牛(QQ:2480102119)
 */
public class FixedGenerator<T> implements DataGenerator<T> {
    private T value;
    public FixedGenerator(T value){
        this.value = value;
    }
    @Override
    public T generate() {
        return value;
    }

    public String toString(){
        return getClass().getSimpleName()+"{value="+value+",type="+value.getClass()+"}";
    }
}
