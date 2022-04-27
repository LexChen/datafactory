package com.lexsoft.job;

import com.lexsoft.config.Column;
import com.lexsoft.utils.Statistic;
import com.lexsoft.utils.Tools;
import org.postgresql.core.Utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;

/**
 * postgres及gauss copy模式
 * @author 牧心牛(QQ:2480102119)
 */
public class CopyJob extends Job {
    private static byte[] NULL = "\\N".getBytes();
    private static byte DELIM = ',';
    private static byte CRLF = '\n';

    public CopyJob(RunnableTask runnableTask){
        super(runnableTask);
    }

    public void runJob() throws Exception{
        boolean gauss = runnableTask.getDriverClass().contains("gauss");
        debug("start initialize connection");
        Connection conn = getConnection(runnableTask);
        Statement stmt = conn.createStatement();
        debug("end initialize connection");
        prepareTable(stmt);
        stmt.close();
        debug("preparation completed");
        StringBuilder ctrl = new StringBuilder();
        StringBuilder set = new StringBuilder();
        ctrl.append("copy ").append(table.getName()).append(" (");
        List<Column> columns = table.getColumns();
        int  columnCount = table.getColumns().size();
        int[] types = new int[columnCount];
        for(int i=0;i<columnCount;i++){
            Column column = columns.get(i);
            types[i] = column.getColumnType().getJdbcType();
            ctrl.append(column.getName()).append(",");
        }
        ctrl.deleteCharAt(ctrl.length()-1);
        ctrl.append(") from stdin with ( format text, delimiter E'\\x")
                .append(Utils.toHexString(new byte[]{DELIM}))
                .append("', encoding utf8")
                .append(gauss?",compatible_illegal_chars on":"")
                .append(")");
        String cmd = ctrl.toString();
        println("copy : "+cmd);
        Copy copy = gauss?new GaussCopy(conn):new PostgresCopy(conn);
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
                }else if(types[i] == Types.BLOB){
                    byte[] bytes = choosen.getBytes();
                    byte[] target = new byte[bytes.length*2];
                    for(int s=0,t=0;s<bytes.length;s+=2,t+=4){
                        target[t] = '\\';
                        target[t+1] = 'x';
                        target[t+2] = bytes[s];
                        target[t+3] = bytes[s+1];
                    }
                    bao.write(target);
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
                copy.copyIn(cmd,new ByteArrayInputStream(bytes,0,bytes.length-1));
                Statistic.update(table.getName(),generated,System.currentTimeMillis()-start);
                bao = new ByteArrayOutputStream();
            }
        }
        if(trickle>0){
            byte[] bytes = bao.toByteArray();
            copy.copyIn(cmd,new ByteArrayInputStream(bytes,0,bytes.length-1));
            Statistic.update(table.getName(),generated,System.currentTimeMillis()-start);
        }
        stmt = conn.createStatement();
        for(String sql : runnableTask.getPostSQLs()){
            println("exec post : "+sql);
            Tools.executeSilently(stmt,sql);
        }
        stmt.close();
        conn.close();
        debug("copy job complete");
    }

    private interface Copy{
        long copyIn(String cmd, InputStream in) throws SQLException,IOException;
    }

    private static class GaussCopy implements Copy{
        com.huawei.gauss200.jdbc.copy.CopyManager copyManager;
        public GaussCopy(Connection conn) throws SQLException {
            copyManager = new com.huawei.gauss200.jdbc.copy.CopyManager(conn.unwrap(com.huawei.gauss200.jdbc.core.BaseConnection.class));
        }

        @Override
        public long copyIn(String cmd, InputStream in) throws SQLException,IOException {
            return copyManager.copyIn(cmd,in);
        }
    }
    private static class PostgresCopy implements Copy{
        org.postgresql.copy.CopyManager copyManager;
        public PostgresCopy(Connection conn) throws SQLException{
            copyManager = new org.postgresql.copy.CopyManager(conn.unwrap(org.postgresql.core.BaseConnection.class));
        }

        @Override
        public long copyIn(String cmd, InputStream in) throws IOException,SQLException {
            return copyManager.copyIn(cmd,in);
        }
    }

}
