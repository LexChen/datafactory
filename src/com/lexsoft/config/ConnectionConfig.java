package com.lexsoft.config;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库连接配置
 * @author 牧心牛(QQ:2480102119)
 */
public class ConnectionConfig {
    public static ConnectionConfig EMPTY = new ConnectionConfig("");
    private String id;
    private String driver;
    private String url;
    private String username;
    private String password;
    private List<String> preList;
    private List<String> postList;
    public ConnectionConfig(String id){
        if(id==null||id.isEmpty()){
            this.id = "";
            this.driver = "";
            this.url = "";
            this.username = "";
            this.password = "";
            this.preList = new ArrayList<>();
            this.postList = new ArrayList<>();
        }else{
            this.id = id;
        }
    }

    public String getId() {
        return id;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
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

    public List<String> getPreList() {
        return preList;
    }

    public void setPreList(List<String> preList) {
        this.preList = preList;
    }

    public List<String> getPostList() {
        return postList;
    }

    public void setPostList(List<String> postList) {
        this.postList = postList;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName())
                .append("{id=").append(id)
                .append(",driver=").append(driver)
                .append(",url=").append(url)
                .append(",username=").append(username)
                .append(",password=").append(password);
        if(preList!=null && preList.size()>0){
            sb.append(",pre=").append(preList);
        }
        if(postList!=null && postList.size()>0){
            sb.append(",post=").append(postList);
        }
        sb.append("}");
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ConnectionConfig){
            return id.equalsIgnoreCase(((ConnectionConfig)obj).id);
        }
        return false;
    }

    @Override
    public int hashCode(){
        return id.hashCode();
    }
}
