package com.lexsoft.job;

import com.lexsoft.config.Column;
import com.lexsoft.utils.Statistic;
import com.lexsoft.utils.Tools;
import com.microsoft.sqlserver.jdbc.ISQLServerBulkData;
import com.microsoft.sqlserver.jdbc.SQLServerBulkCopy;
import com.microsoft.sqlserver.jdbc.SQLServerBulkCopyOptions;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.sql.Date;
import java.util.*;

/**
 * Sql Server bulk copy 模式
 * @author 牧心牛(QQ:2480102119)
 */
public class BulkJob extends Job {
    public BulkJob(RunnableTask runnableTask){
        super(runnableTask);
    }

    public void runJob() throws Exception{
        debug("start initialize connection");
        Connection conn = getConnection(runnableTask);
        Statement stmt = conn.createStatement();
        debug("end initialize connection");
        prepareTable(stmt);
        debug("preparation completed");
        SQLServerBulkCopy bulkCopy = new SQLServerBulkCopy(conn);
        bulkCopy.setDestinationTableName(table.getName());
        SQLServerBulkCopyOptions options = new SQLServerBulkCopyOptions();
        options.setBatchSize(batch);
        bulkCopy.setBulkCopyOptions(options);
        int  columnCount = table.getColumns().size();
        StringBuilder sb = new StringBuilder();
        sb.append("select ");
        for(int i=0;i<columnCount;i++){
            Column column = table.getColumns().get(i);
            sb.append(column.getName()).append(",");
            bulkCopy.addColumnMapping(i+1,i+1);
        }
        sb.deleteCharAt(sb.length()-1);
        sb.append(" from ").append(table.getName()).append(" where 1=2");
        ResultSet rs = stmt.executeQuery(sb.toString());
        ListBulkRecord records = new ListBulkRecord(rs.getMetaData());
        rs.close();
        stmt.close();
        long generated = 0;
        int  trickle = 0;
        long start = System.currentTimeMillis();
        while(generated<count){
            generated++;
            trickle++;
            records.addRowData(table.generate().toArray(new String[0]));
            if(trickle>=batch){
                bulkCopy.writeToServer(records);
                records.clear();
                trickle = 0;
                Statistic.update(table.getName(),generated,System.currentTimeMillis()-start);
            }
        }
        if(trickle>0){
            bulkCopy.getBulkCopyOptions().setBatchSize(trickle);
            bulkCopy.writeToServer(records);
            Statistic.update(table.getName(),generated,System.currentTimeMillis()-start);
        }
        stmt = conn.createStatement();
        for(String sql : runnableTask.getPostSQLs()){
            println("exec post : "+sql);
            Tools.executeSilently(stmt,sql);
        }
        stmt.close();
        conn.close();
        debug("insert job complete");
    }

    private static class ListBulkRecord implements ISQLServerBulkData {
        private int columnCount;
        private Map<Integer,ColumnMeta> columnMetaList;
        private List<Object[]> rowDataList;
        private int cursor;

        private static class ColumnMeta{
            String name;
            int    type;
            int    precision;
            int    scale;
        }

        public ListBulkRecord(ResultSetMetaData rsm) throws SQLException{
            columnCount = rsm.getColumnCount();
            columnMetaList = new LinkedHashMap<>();
            for(int i=1;i<=columnCount;i++){
                ColumnMeta columnMeta = new ColumnMeta();
                columnMeta.name = rsm.getColumnName(i);
                columnMeta.type = rsm.getColumnType(i);
                columnMeta.precision = rsm.getPrecision(i);
                columnMeta.scale = rsm.getScale(i);
                columnMetaList.put(i,columnMeta);
            }
            this.cursor = -1;
            this.rowDataList = new ArrayList<>();
        }

        public void clear(){
            this.cursor = -1;
            this.rowDataList = new ArrayList<>();
        }

        public void addRowData(String[] data){
            Object[] properData = new Object[columnCount];
            for(int i=0;i<data.length;i++){
                if(data[i]==null){
                    properData[i] = null;
                    continue;
                }
                ColumnMeta cm = columnMetaList.get(i+1);
                BigDecimal bd;
                switch(cm.type) {
                    case -148:
                    case -146:
                    case Types.NUMERIC:
                    case Types.DECIMAL:
                        bd = new BigDecimal(data[i].trim());
                        properData[i] = bd.setScale(cm.scale, RoundingMode.HALF_UP);
                        break;
                    case Types.LONGNVARCHAR:
                    case Types.NCHAR:
                    case Types.NVARCHAR:
                    case Types.LONGVARCHAR:
                    case Types.CHAR:
                    case Types.VARCHAR:
                    case Types.CLOB:
                    default:
                        properData[i] = data[i];
                        break;
                    case Types.BIT:
                        try {
                            properData[i] = 0.0D == Double.parseDouble(data[i]) ? Boolean.FALSE : Boolean.TRUE;
                        } catch (NumberFormatException var10) {
                            properData[i] = Boolean.parseBoolean(data[i]);
                        }
                        break;
                    case Types.TINYINT:
                    case Types.SMALLINT:
                        properData[i] = Short.parseShort(data[i]);
                        break;
                    case Types.INTEGER:
                        properData[i] = Integer.parseInt(data[i]);
                        break;
                    case Types.BIGINT:
                        properData[i] = Long.parseLong(data[i]);
                        break;
                    case Types.LONGVARBINARY:
                    case Types.VARBINARY:
                    case Types.BINARY:
                    case Types.BLOB:
                        properData[i] = Tools.unHexToBytes(data[i]);
                        break;
                    case Types.NULL:
                        properData[i] = null;
                        break;
                    case Types.REAL:
                        properData[i] = Float.parseFloat(data[i]);
                        break;
                    case Types.DOUBLE:
                        properData[i] = Double.parseDouble(data[i]);
                        break;
                    case Types.DATE:
                        properData[i] = Date.valueOf(data[i]);
                        break;
                    case Types.TIME_WITH_TIMEZONE:
                        properData[i] = Time.valueOf(data[i]);
                        break;
                    case Types.TIMESTAMP_WITH_TIMEZONE:
                        properData[i] = Timestamp.valueOf(data[i]);
                }
            }
            this.rowDataList.add(properData);
        }

        @Override
        public Set<Integer> getColumnOrdinals() {
            return columnMetaList.keySet();
        }

        @Override
        public String getColumnName(int i) {
            return columnMetaList.get(i).name;
        }

        @Override
        public int getColumnType(int i) {
            return columnMetaList.get(i).type;
        }

        @Override
        public int getPrecision(int i) {
            return columnMetaList.get(i).precision;
        }

        @Override
        public int getScale(int i) {
            return columnMetaList.get(i).scale;
        }

        @Override
        public Object[] getRowData() throws SQLException {
            return rowDataList.get(cursor);
        }

        @Override
        public boolean next() throws SQLException {
            cursor++;
            return cursor<rowDataList.size();
        }
    }
}
