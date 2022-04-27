package com.lexsoft.config;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.HashMap;
import java.util.Map;

/**
 * 概率分布节点配置
 * @author 牧心牛(QQ:2480102119)
 */
public class RatioNode {
    private Map<String,Float> items;
    public RatioNode(Node node){
        this.items = new HashMap<>();
        NodeList nodeList = node.getChildNodes();
        for(int i=0;i<nodeList.getLength();i++){
            Node item = nodeList.item(i);
            String name = item.getNodeName();
            if(Tag.item.name().equalsIgnoreCase(name)){
                float ratio = 1;
                NamedNodeMap nnp = item.getAttributes();
                if(nnp.getNamedItem(Tag.ratio.name())!=null){
                    ratio = Float.parseFloat(nnp.getNamedItem(Tag.ratio.name()).getNodeValue());
                }
                this.items.put(item.getTextContent(),ratio);
            }
        }
    }

    public Map<String,Float> getItems() {
        return items;
    }

    public String toString(){
        return "Ratio{"+items+"}";
    }

}
