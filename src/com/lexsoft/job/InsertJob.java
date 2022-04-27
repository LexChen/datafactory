package com.lexsoft.job;

import com.lexsoft.config.Column;
import com.lexsoft.utils.Statistic;
import com.lexsoft.utils.Tools;

import java.math.BigDecimal;
import java.sql.*;
import java.util.List;

/**
 * 所有数据库批量插入模式
 * @author 牧心牛(QQ:2480102119)
 */
public class InsertJob extends Job {
    public InsertJob(RunnableTask runnableTask){
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
        debug(table.toPreparedSQL());
        PreparedStatement pstmt = conn.prepareStatement(table.toPreparedSQL());
        int  columnCount = table.getColumns().size();
        int[] types = new int[columnCount];
        for(int i=0;i<columnCount;i++){
            Column column = table.getColumns().get(i);
            types[i] = column.getColumnType().getJdbcType();
        }
        long generated = 0;
        int  trickle = 0;
        long start = System.currentTimeMillis();
        while(generated<count){
            List<String> data = table.generate();
            generated++;
            trickle++;
            for(int i=0;i<columnCount;i++){
                String choose = data.get(i);
                switch (types[i]){
                    case Types.SMALLINT:
                        if(choose ==null){
                            pstmt.setNull(i+1,Types.SMALLINT);
                        }else{
                            pstmt.setShort(i+1,Short.parseShort(choose));
                        }
                        break;
                    case Types.INTEGER:
                        if(choose ==null){
                            pstmt.setNull(i+1,Types.INTEGER);
                        }else{
                            pstmt.setInt(i+1,Integer.parseInt(choose));
                        }
                        break;
                    case Types.BIGINT:
                        if(choose ==null){
                            pstmt.setNull(i+1,Types.FLOAT);
                        }else{
                            pstmt.setLong(i+1,Long.parseLong(choose));
                        }
                        break;
                    case Types.FLOAT:
                        if(choose ==null){
                            pstmt.setNull(i+1,Types.INTEGER);
                        }else{
                            pstmt.setFloat(i+1,Float.parseFloat(choose));
                        }
                        break;
                    case Types.DOUBLE:
                        if(choose ==null){
                            pstmt.setNull(i+1,Types.DOUBLE);
                        }else{
                            pstmt.setDouble(i+1,Double.parseDouble(choose));
                        }
                        break;
                    case Types.DECIMAL:
                        if(choose ==null){
                            pstmt.setNull(i+1,Types.DECIMAL);
                        }else{
                            pstmt.setBigDecimal(i+1,new BigDecimal(choose));
                        }
                        break;
                    case Types.CHAR:
                        if(choose ==null){
                            pstmt.setNull(i+1,Types.CHAR);
                        }else{
                            pstmt.setString(i+1, choose);
                        }
                        break;
                    case Types.VARCHAR:
                        if(choose ==null){
                            pstmt.setNull(i+1,Types.VARCHAR);
                        }else{
                            pstmt.setString(i+1, choose);
                        }
                        break;
                    case Types.BLOB:
                        if(choose ==null){
                            pstmt.setNull(i+1,Types.BLOB);
                        }else{
                            pstmt.setBytes(i+1, Tools.unHexToBytes(choose));
                        }
                        break;
                    case Types.DATE:
                        if(choose ==null){
                            pstmt.setNull(i+1,Types.DATE);
                        }else{
                            pstmt.setDate(i+1,Date.valueOf(choose));
                        }
                        break;
                    case Types.TIME:
                        if(choose ==null){
                            pstmt.setNull(i+1,Types.TIME);
                        }else{
                            pstmt.setTime(i+1,Time.valueOf(choose));
                        }
                        break;
                    case Types.TIMESTAMP:
                        if(choose ==null){
                            pstmt.setNull(i+1,Types.TIMESTAMP);
                        }else{
                            pstmt.setTimestamp(i+1,Timestamp.valueOf(choose));
                        }
                        break;
                    default:
                        if(choose ==null){
                            pstmt.setObject(i+1,null);
                        }else{
                            pstmt.setString(i+1, choose);
                        }
                }
            }
            pstmt.addBatch();
            if(trickle>=batch){
                trickle = 0;
                pstmt.executeBatch();
                Statistic.update(table.getName(),generated,System.currentTimeMillis()-start);
            }
        }
        if(trickle>0){
            pstmt.executeBatch();
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
        debug("insert job complete");
    }
}
