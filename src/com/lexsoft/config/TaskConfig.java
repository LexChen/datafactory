package com.lexsoft.config;

import java.util.List;
import java.util.Properties;

/**
 * 任务配置
 * @author 牧心牛(QQ:2480102119)
 */
public class TaskConfig {
    private String  id;
    private String  method = "insert";
    private boolean execute = false;
    private String  connection = "";
    private List<String> tables;
    private Properties config;
    public TaskConfig(String id){
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public boolean isExecute() {
        return execute;
    }

    public void setExecute(boolean execute) {
        this.execute = execute;
    }

    public String getConnection() {
        return connection;
    }

    public void setConnection(String connection) {
        this.connection = connection;
    }

    public List<String> getTables() {
        return tables;
    }

    public void setTables(List<String> tables) {
        this.tables = tables;
    }

    public Properties getConfig() {
        return config;
    }

    public void setConfig(Properties config) {
        this.config = config;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof TaskConfig){
            return id.equalsIgnoreCase(((TaskConfig)o).id);
        }
        return false;
    }

    @Override
    public int hashCode(){
        return id.hashCode();
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName())
                .append("{id=").append(id)
                .append(",method=").append(method)
                .append(",execute=").append(execute)
                .append(",connection=").append(connection);
        if(tables!=null){
            sb.append(",tables=").append(tables);
        }
        if(config!=null){
            sb.append(",config=").append(config);
        }
        sb.append("}");
        return sb.toString();
    }
}
