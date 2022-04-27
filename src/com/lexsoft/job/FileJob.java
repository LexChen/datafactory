package com.lexsoft.job;

import com.lexsoft.utils.Statistic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.List;

/**
 * 导出为文件模式
 * @author 牧心牛(QQ:2480102119)
 */
public class FileJob extends Job {
    public FileJob(RunnableTask runnableTask){
        super(runnableTask);
    }

    public void runJob() throws Exception{
        long generated = 0;
        long trickle = 0;
        long bytes = 0;
        int no = 0;
        int nullLen = nullas.length();
        DecimalFormat df = new DecimalFormat("0000");
        String base = path + File.separator + table.getName() + "_";
        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(base+df.format(no++))));
        int columnCount = table.getColumns().size()-1;
        long start = System.currentTimeMillis();
        while(generated<count){
            List<String> data = table.generate();
            generated++;
            trickle++;
            for(int i=0;i<columnCount;i++){
                String choose = data.get(i);
                if(choose==null){
                    bytes+= nullLen;
                    pw.print(nullas);
                }else{
                    bytes+=choose.length();
                    pw.print(choose);
                }
                pw.print(fieldDelim);
            }
            pw.print(data.get(columnCount));
            pw.print(recordDelim);
            if(trickle>=split_count || bytes>=split_size){
                Statistic.update(table.getName(),generated,System.currentTimeMillis()-start);
                pw.close();
                pw = new PrintWriter(new BufferedWriter(new FileWriter(base+df.format(no++))));
                trickle = 0;
                bytes = 0;
            }
        }
        Statistic.update(table.getName(),generated,System.currentTimeMillis()-start);
        pw.close();
    }
}
