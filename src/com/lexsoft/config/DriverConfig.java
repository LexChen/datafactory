package com.lexsoft.config;

import org.w3c.dom.Node;

/**
 * 数据库驱动类配置
 * @author 牧心牛(QQ:2480102119)
 */
public class DriverConfig {
    public static DriverConfig EMPTY = new DriverConfig(null);
    private String id;
    private String driverClass;
    public DriverConfig(Node node){
        if(node==null){
            this.id="";
            this.driverClass="";
        }else{
            this.id = node.getAttributes().getNamedItem("id").getNodeValue();
            this.driverClass = node.getTextContent().trim();
        }
    }

    public String getId() {
        return id;
    }

    public String getDriverClass() {
        return driverClass;
    }

    public String toString(){
        return "driver{id="+id+",class="+driverClass+"}";
    }

    @Override
    public boolean equals(Object driver){
        if(driver instanceof DriverConfig){
            return id.equalsIgnoreCase(((DriverConfig)driver).getId());
        }
        return false;
    }

    @Override
    public int hashCode(){
        return id.hashCode();
    }
}
