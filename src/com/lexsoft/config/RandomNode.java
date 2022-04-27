package com.lexsoft.config;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 随机节点配置
 * @author 牧心牛(QQ:2480102119)
 */
public class RandomNode {
    private String min = "";
    private String max = "";
    private String prefix = "";
    private String suffix = "";
    private String candidates = "";
    public RandomNode(Node node){
        NodeList nodeList = node.getChildNodes();
        for(int i=0;i<nodeList.getLength();i++){
            Node item = nodeList.item(i);
            String name = item.getNodeName();
            if(Tag.min.name().equalsIgnoreCase(name)){
                this.min = item.getTextContent();
            }else if(Tag.max.name().equalsIgnoreCase(name)){
                this.max = item.getTextContent();
            }else if(Tag.candidates.name().equalsIgnoreCase(name)){
                this.candidates = item.getTextContent();
            }else if(Tag.prefix.name().equalsIgnoreCase(name)){
                this.prefix = item.getTextContent()==null?"":item.getTextContent();
            }else if(Tag.suffix.name().equalsIgnoreCase(name)){
                this.suffix = item.getTextContent()==null?"":item.getTextContent();
            }
        }
    }

    public String getMin() {
        return min;
    }

    public String getMax() {
        return max;
    }

    public String getCandidates() {
        return candidates;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public String toString(){
        return "Random{min="+min+",max="+max+",candidates="+candidates+",prefix="+prefix+",suffix="+suffix+"}";
    }
}
