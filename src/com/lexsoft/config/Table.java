package com.lexsoft.config;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 表配置
 * @author 牧心牛(QQ:2480102119)
 */
public class Table {
    private String name;
    private List<Column> columns;
    private List<Column> skipColumns;
    private List<String> keys;
    private List<String> posts;
    private Properties config;
    public Table(Node node){
        this.name = node.getAttributes().getNamedItem("name").getNodeValue();
        this.columns = new ArrayList<>();
        this.skipColumns = new ArrayList<>();
        this.config = new Properties();
        this.keys = new ArrayList<>();
        this.posts = new ArrayList<>();
        NodeList columnNodeList = node.getChildNodes();
        for(int i=0;i<columnNodeList.getLength();i++){
            Node columnNode = columnNodeList.item(i);
            if(columnNode.getNodeType()==Node.ELEMENT_NODE){
                String name = columnNode.getNodeName();
                if(name.equalsIgnoreCase(Tag.options.name())){
                    Config.loadOptions(columnNode.getChildNodes(),config);
                }else if(name.equalsIgnoreCase(Tag.keys.name())){
                    NodeList keyNodes = columnNode.getChildNodes();
                    for(int k=0;k<keyNodes.getLength();k++){
                        Node keyNode = keyNodes.item(k);
                        if(keyNode.getNodeName().equalsIgnoreCase(Tag.key.name())){
                            String value = keyNode.getTextContent().trim();
                            if(!value.isEmpty()){
                                keys.add(value);
                            }
                        }
                    }
                }else if(name.equalsIgnoreCase(Tag.posts.name())){
                    NodeList postNodes = columnNode.getChildNodes();
                    for(int k=0;k<postNodes.getLength();k++){
                        Node postNode = postNodes.item(k);
                        if(postNode.getNodeName().equalsIgnoreCase(Tag.post.name())){
                            String value = postNode.getTextContent().trim();
                            if(!value.isEmpty()){
                                posts.add(value);
                            }
                        }
                    }
                }else{
                    Column col = Column.createColumn(columnNode);
                    this.columns.add(col);
                    if(col.getColumnType()!=ColumnType.SKIP){
                        this.skipColumns.add(col);
                    }
                }
            }
        }
    }

    public String getName(){
        return name;
    }

    public List<Column> getColumns(){
        return skipColumns;
    }

    public List<String> generate(){
        List<String> datas = new ArrayList<>();
        for(Column column : skipColumns){
            datas.add(column.generate());
        }
        return datas;
    }

    public boolean equals(Object o){
        if(o instanceof Table){
            Table another = (Table)o;
            return name.equalsIgnoreCase(another.name);
        }
        return false;
    }

    public Properties getConfig(){
        return this.config;
    }

    public List<String> getKeys(){
        return keys;
    }

    public String toDropSQL(){
        return "drop table "+name;
    }

    public String toTruncateSQL(){
        return "truncate table "+name;
    }

    public String toCreateSQL(){
        StringBuilder sb = new StringBuilder();
        sb.append("create table ").append(name).append(" (");
        for(Column column : columns){
            sb.append(column.getName()).append(" ").append(column.getDbType()).append(",");
        }
        for(String key : keys){
            sb.append(key).append(",");
        }
        sb.deleteCharAt(sb.length()-1);
        sb.append(")");
        return sb.toString();
    }

    public String toPreparedSQL(){
        StringBuilder sb = new StringBuilder();
        sb.append("insert into ").append(name).append("(");
        StringBuilder sv = new StringBuilder();
        sv.append("(");
        for(Column column : skipColumns){
            sb.append(column.getName()).append(",");
            sv.append("?,");
        }
        sv.deleteCharAt(sv.length()-1);
        sv.append(")");
        sb.deleteCharAt(sb.length()-1);
        sb.append(") values ").append(sv);
        return sb.toString();
    }

    public String toInsertSQL(){
        StringBuilder sb = new StringBuilder();
        sb.append("insert into ").append(name).append("(");
        for(Column column : skipColumns){
            sb.append(column.getName()).append(",");
        }
        sb.deleteCharAt(sb.length()-1);
        sb.append(") values ");
        return sb.toString();
    }

    public List<String> getPosts(){
        return this.posts;
    }


    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Table=").append(name).append("{");
        for(Column column : columns){
            sb.append("\n").append(column);
        }
        sb.append("\n}");
        return sb.toString();
    }
}
