package com.lexsoft.job;

import com.lexsoft.config.Column;
import com.lexsoft.utils.Statistic;
import com.lexsoft.utils.Tools;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;

/**
 * mysql insert 多值模式
 * @author 牧心牛(QQ:2480102119)
 */
public class ValuesJob extends Job {
    public ValuesJob(RunnableTask runnableTask){
        super(runnableTask);
    }

    public void runJob() throws Exception{
        boolean mysql = runnableTask.getDriverClass().contains("mysql");
        debug("start initialize connection");
        Connection conn = getConnection(runnableTask);
        Statement stmt = conn.createStatement();
        debug("end initialize connection");
        prepareTable(stmt);
        debug("preparation completed");
        int  columnCount = table.getColumns().size();
        int[] types = new int[columnCount];
        for(int i=0;i<columnCount;i++){
            Column column = table.getColumns().get(i);
            types[i] = column.getColumnType().getJdbcType();
        }
        long generated = 0;
        int  trickle = 0;
        long start = System.currentTimeMillis();
        String header = table.toInsertSQL();
        StringBuilder sb = new StringBuilder();
        sb.append(header);
        debug(header);
        while(generated<count){
            List<String> data = table.generate();
            generated++;
            trickle++;
            sb.append("(");
            for(int i=0;i<columnCount;i++){
                String choose = data.get(i);
                if(choose==null){
                    sb.append("null,");
                    continue;
                }
                switch (types[i]){
                    case Types.INTEGER:
                    case Types.BIGINT:
                    case Types.FLOAT:
                    case Types.DOUBLE:
                    case Types.DECIMAL:
                        sb.append(choose).append(",");
                        break;
                    case Types.BLOB:
                        if(mysql){
                            sb.append("unhex('").append(choose).append("'),");
                        }else{
                            sb.append(hex(choose)).append(",");
                        }
                        break;
                    case Types.DATE:
                    case Types.TIME:
                    case Types.TIMESTAMP:
                        sb.append("'").append(choose).append("',");
                        break;
                    case Types.CHAR:
                    case Types.VARCHAR:
                    default:
                        sb.append("'").append(choose.replace("'","''")).append("',");
                }
            }
            sb.deleteCharAt(sb.length()-1);
            sb.append("),");
            if(trickle>=batch){
                trickle = 0;
                stmt.execute(sb.substring(0,sb.length()-1));
                Statistic.update(table.getName(),generated,System.currentTimeMillis()-start);
                sb = new StringBuilder();
                sb.append(header);
            }
        }
        if(trickle>0){
            stmt.execute(sb.substring(0,sb.length()-1));
            Statistic.update(table.getName(),generated,System.currentTimeMillis()-start);
        }
        for(String sql : runnableTask.getPostSQLs()){
            println("exec post : "+sql);
            Tools.executeSilently(stmt,sql);
        }
        stmt.close();
        conn.close();
        debug("values job complete");
    }

    private static String hex(String str){
        if(str==null){
            return null;
        }else if(str.isEmpty()){
            return "''";
        }
        byte[] bytes = str.getBytes();
        byte[] to = new byte[bytes.length+4];
        to[0] = to[to.length-1] = '\'';
        to[1] = '\\';
        to[2] = 'x';
        for(int i=0,j=3;i<bytes.length;i+=2,j+=2){
            to[j]=bytes[i];
            to[j+1]=bytes[i+1];
        }
//        System.out.println(new String(to));
//        System.exit(1);
        return new String(to);
    }

}
