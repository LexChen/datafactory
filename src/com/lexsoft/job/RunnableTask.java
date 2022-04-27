package com.lexsoft.job;

import com.lexsoft.config.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 运行时参数配置
 * @author 牧心牛(QQ:2480102119)
 */
public class RunnableTask {
    private String id;
    private String method;
    private String driverClass;
    private String url;
    private String username;
    private String password;
    private Properties config;
    private Table table;
    private List<String> preSQLs;
    private List<String> postSQLs;
    public RunnableTask(String id){
        this.id = id;
    }

    public String toString(){
        return getClass().getSimpleName()+"{id="+
                id+",driver="+driverClass+",url="+
                url+",username="+username+",password="+
                password+",config="+config+",table="+
                table+",pre="+preSQLs+",post="+postSQLs+"}";
    }

    public String getId(){
        return this.id;
    }

    public String getDriverClass() {
        return driverClass;
    }

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Properties getConfig() {
        return config;
    }

    public void setConfig(Properties config) {
        this.config = config;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public List<String> getPreSQLs() {
        return preSQLs;
    }

    public void setPreSQLs(List<String> preSQLs) {
        this.preSQLs = (preSQLs==null?new ArrayList<>():preSQLs);
    }

    public List<String> getPostSQLs() {
        return postSQLs;
    }

    public void setPostSQLs(List<String> postSQLs) {
        this.postSQLs = (postSQLs==null?new ArrayList<>():postSQLs);
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
