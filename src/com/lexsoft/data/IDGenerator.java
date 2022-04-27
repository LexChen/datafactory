package com.lexsoft.data;

import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Random;

/**
 * 生成随机身份证号
 *                 <random>
 *                     <min>1980-01-01</min>
 *                     <max>2000-01-01</max>
 *                     <candidates>??id</candidates>
 *                 </random>
 * 随机生成出生日期在1980-01-01至2000-01-01之间的北京市(110000)开头的身份证号
 * <candidates>??id:110101,110102</candidates>
 * 此种模式可以生成指定地区的身份证号，上例中地区为  110101 或110102
 * @author 牧心牛(QQ:2480102119)
 */
public class IDGenerator implements DataGenerator<String> {
    private String[] prefix = {"110000"};
    private Random random;
    private long min;
    private long max;
    private SimpleDateFormat sdf;
    private DecimalFormat df;
    private static int[] coff = {7,9,10,5,8,4,2,1,6,3,7,9,10,5,8,4,2};
    private static String[] mod = {"1","0","X","9","8","7","6","5","4","3","2"};
    public IDGenerator(String min,String max,String type){
        if(min==null||min.isEmpty()){
            min = "1950-01-01";
        }
        this.min = Date.valueOf(min).getTime();
        if(max==null||max.isEmpty()){
            max = "2020-01-01";
        }
        this.max = Date.valueOf(max).getTime() - this.min;
        this.random = new Random();
        int pos = type.indexOf(":");
        if(pos>0){
           prefix = type.substring(pos+1).split(",");
        }
        sdf = new SimpleDateFormat("yyyyMMdd");
        df = new DecimalFormat("000");
    }

    @Override
    public String generate() {
        String district = prefix[random.nextInt(prefix.length)];
        String birthday = sdf.format(min + Math.abs(random.nextLong())%max);
        String sequence = df.format(random.nextInt(1000));
        byte[] bytes = (district+birthday+sequence).getBytes();
        int check = 0;
        for(int i=0;i<bytes.length;i++){
            check += (bytes[i]-'0')*coff[i];
        }
        String checksum = mod[check%11];
        return district+birthday+sequence+checksum;
    }
}
