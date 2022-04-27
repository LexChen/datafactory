package com.lexsoft.utils;

import java.sql.Statement;
import java.util.Properties;

/**
 * 公共方法
 * @author 牧心牛(QQ:2480102119)
 */
public class Tools {
    private static byte[]   HEX_HIGH   = new byte[256];
    private static byte[]   HEX_LOW    = new byte[256];
    static{
        for(int i=0;i<=255;i++){
            String str = "00"+ Integer.toHexString(i);
            str = str.substring(str.length()-2);
            int index = 128+(byte)i;
            HEX_HIGH[index]   = (byte)str.charAt(0);
            HEX_LOW[index]    = (byte)str.charAt(1);
        }
    }
    public static String unHexString(String str){
        if(str==null){
            return null;
        }
        byte[] source = str.getBytes();
        byte[] buffer = new byte[source.length>>>1];
        for(int i=0;i<buffer.length;i++){
            int p = i<<1;
            buffer[i] = toByte(source[p],source[p+1]);
        }
        return new String(buffer);
    }

    public static byte[] unHexToBytes(String str){
        byte[] source = str.getBytes();
        byte[] buffer = new byte[source.length>>>1];
        for(int i=0;i<buffer.length;i++){
            int p = i<<1;
            buffer[i] = toByte(source[p],source[p+1]);
        }
        return buffer;
    }


    /**
     * 后面的优先级高
     * @param propertiesList 参数列表
     * @return prop 合并后参数
     */
    public static Properties priorityProperties(Properties ... propertiesList){
        Properties merged = new Properties();
        for(Properties prop : propertiesList){
            merged.putAll(prop);
        }
        return merged;
    }

    private static byte toByte(byte high,byte low){
        int hv  = high - 48;
        hv = (hv&0x0f) + (((hv&0xf0)!=0)?9:0);
        int lv  = low - 48;
        lv = (lv&0x0f) + (((lv&0xf0)!=0)?9:0);
        return (byte)((hv<<4) + lv);
    }

    public static String toHexString(byte[] bytes,int offset,int length){
        if(bytes==null){
            return null;
        }
        int len = offset+length;
        if(len>bytes.length){
            len = bytes.length;
        }
        byte[] target = new byte[(len-offset)*2];
        for (int i=offset,j=0;i<len;i++,j+=2) {
            int pos = 128+bytes[i];
            target[j]= HEX_HIGH[pos];
            target[j+1]= HEX_LOW[pos];
        }
        return new String(target);
    }
    public static long parseTimeStep(String step) {
        if (step == null || step.isEmpty()) {
            return 0;
        }
        long millis = 0;
        int start = 0;
        int pos = 0;
        byte[] bytes = step.toLowerCase().getBytes();
        while (pos < bytes.length) {
            byte b = bytes[pos];
            int factor = 1;
            if (b == 'd') {
                factor = 24 * 60 * 60 * 1000;
            } else if (b == 'h') {
                factor = 60 * 60 * 1000;
            } else if (b == 'm') {
                factor = 60 * 1000;
            } else if (b == 's') {
                factor = 1000;
            }
            if (factor > 1) {
                millis += Integer.parseInt(new String(bytes, start, pos - start)) * factor;
                pos++;
                start = pos;
            } else {
                pos++;
            }
        }
        if (start < bytes.length) {
            millis += Integer.parseInt(new String(bytes, start, bytes.length - start));
        }
        return millis;
    }

    public static void executeSilently(Statement stmt, String sql){
        try{
            stmt.execute(sql);
        }catch (Exception e){
            //ignore
        }
    }
}
