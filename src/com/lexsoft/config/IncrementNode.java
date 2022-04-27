package com.lexsoft.config;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 递增节点配置
 * @author 牧心牛(QQ:2480102119)
 */
public class IncrementNode {
    private String start = "";
    private String end = "";
    private String step = "";
    public IncrementNode(Node node){
        NodeList nodeList = node.getChildNodes();
        for(int i=0;i<nodeList.getLength();i++){
            Node item = nodeList.item(i);
            String name = item.getNodeName();
            if("start".equalsIgnoreCase(name)){
                this.start = item.getTextContent();
            }else if("end".equalsIgnoreCase(name)){
                this.end = item.getTextContent();
            }else if("step".equalsIgnoreCase(name)){
                this.step = item.getTextContent();
            }
        }
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public String getStep() {
        return step;
    }

    public String toString(){
        return "Increment{start="+start+",end="+end+",step="+step+"}";
    }
}
