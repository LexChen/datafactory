package com.lexsoft.job;

import com.lexsoft.config.Column;
import com.lexsoft.utils.Statistic;
import com.lexsoft.utils.Tools;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;

/**
 * mysql load data模式
 * @author 牧心牛(QQ:2480102119)
 */
public class LoadJob extends Job {
    private static byte[] NULL = "\\N".getBytes();
    private static byte DELIM = ',';
    private static byte CRLF = '\n';

    public LoadJob(RunnableTask runnableTask){
        super(runnableTask);
    }

    public void runJob() throws Exception{
        debug("start initialize connection");
        Connection conn = getConnection(runnableTask);
        Statement stmt = conn.createStatement();
        debug("end initialize connection");
        prepareTable(stmt);
        stmt.close();
        debug("preparation completed");
        StringBuilder ctrl = new StringBuilder();
        StringBuilder set = new StringBuilder();
        ctrl.append("load data local infile 'data.csv' ignore into table ").append(table.getName())
                .append(" character set 'utf8mb4' fields terminated by ',' lines terminated by '\\n' ")
                .append(" (");
        List<Column> columns = table.getColumns();
        int  columnCount = table.getColumns().size();
        int[] types = new int[columnCount];
        for(int i=0;i<columnCount;i++){
            Column column = columns.get(i);
            types[i] = column.getColumnType().getJdbcType();
            if(column.getColumnType().getJdbcType()==Types.BLOB){
                ctrl.append("@").append(column.getName()).append(",");
                set.append(column.getName()).append("=unhex(@").append(column.getName()).append("),");
            }else{
                ctrl.append(column.getName()).append(",");
            }
        }
        ctrl.deleteCharAt(ctrl.length()-1);
        ctrl.append(") ");
        if(set.length()>0){
            ctrl.append("set ").append(set,0,set.length()-1);
        }
        debug("load : "+ctrl);
        com.mysql.cj.jdbc.ClientPreparedStatement pstmt = conn.prepareStatement(ctrl.toString())
                .unwrap(com.mysql.cj.jdbc.ClientPreparedStatement.class);
        long generated = 0;
        int  trickle = 0;
        long start = System.currentTimeMillis();
        int  last = columnCount-1;
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        while(generated<count){
            List<String> data = table.generate();
            generated++;
            trickle++;
            for(int i=0;i<columnCount;i++){
                String choosen = data.get(i);
                if(choosen==null){
                    bao.write(NULL);
                }else if(types[i] == Types.CHAR||types[i]==Types.VARCHAR){
                    bao.write(choosen.replaceAll("[\\n\\r,]","").getBytes());
                }else{
                    bao.write(choosen.getBytes());
                }
                if(i!=last){
                    bao.write(DELIM);
                }
            }
            bao.write(CRLF);
            if(trickle>=batch){
                trickle = 0;
                byte[] bytes = bao.toByteArray();
                pstmt.setLocalInfileInputStream(new ByteArrayInputStream(bytes,0,bytes.length-1));
                pstmt.execute();
                Statistic.update(table.getName(),generated,System.currentTimeMillis()-start);
                bao = new ByteArrayOutputStream();
            }
        }
        if(trickle>0){
            byte[] bytes = bao.toByteArray();
            pstmt.setLocalInfileInputStream(new ByteArrayInputStream(bytes,0,bytes.length-1));
            pstmt.execute();
            Statistic.update(table.getName(),generated,System.currentTimeMillis()-start);
        }
        pstmt.close();
        stmt = conn.createStatement();
        for(String sql : runnableTask.getPostSQLs()){
            println("exec post : "+sql);
            Tools.executeSilently(stmt,sql);
        }
        stmt.close();
        conn.close();
        debug("load job complete");
    }
}
