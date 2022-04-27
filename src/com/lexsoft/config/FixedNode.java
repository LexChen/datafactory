package com.lexsoft.config;

import org.w3c.dom.Node;

/**
 * 固定值节点配置
 * @author 牧心牛(QQ:2480102119)
 */
public class FixedNode {
    private String value;
    public FixedNode(Node node){
        this.value = node.getTextContent()==null?"":node.getTextContent();
    }

    public String getValue() {
        return value;
    }

    public String toString(){
        return "Fixed{"+value+"}";
    }
}
