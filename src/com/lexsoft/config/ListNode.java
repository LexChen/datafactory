package com.lexsoft.config;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * 列表节点配置
 * @author 牧心牛(QQ:2480102119)
 */
public class ListNode {
    private List<String> items;
    public ListNode(Node node){
        this.items = new ArrayList<>();
        NodeList nodeList = node.getChildNodes();
        for(int i=0;i<nodeList.getLength();i++){
            Node item = nodeList.item(i);
            String name = item.getNodeName();
            if(Tag.item.name().equalsIgnoreCase(name)){
                this.items.add(item.getTextContent());
            }
        }
    }

    public List<String> getItems() {
        return items;
    }

    public String toString(){
        return "List{"+items+"}";
    }
}
