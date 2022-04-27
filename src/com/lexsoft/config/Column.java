package com.lexsoft.config;

import com.lexsoft.data.DataGenerator;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Random;

/**
 * 字段设置
 * @author 牧心牛(QQ:2480102119)
 */
public abstract class Column {
    private ColumnType columnType;
    private String name;
    private String dbType;
    private float  nullability = -1;
    private Random nullRandom = new Random();
    public static Column createColumn(Node node){
        NamedNodeMap nnm = node.getAttributes();
        String name = nnm.getNamedItem(Tag.name.name()).getNodeValue();
        String typeString = nnm.getNamedItem(Tag.type.name()).getNodeValue();
        ColumnType columnType = ColumnType.of(typeString);
        try{
            Class clz = columnType==ColumnType.USERDEFINE?Class.forName(typeString):Class.forName(columnType.getClassName());
            Column column = (Column)clz.getDeclaredConstructor().newInstance();
            column.setName(name);
            column.setColumnType(columnType);
            column.setUpColumn(node);
            if(nnm.getNamedItem(Tag.nullable.name())!=null){
                column.setNullability(Float.parseFloat(nnm.getNamedItem(Tag.nullable.name()).getNodeValue()));
            }
            return column;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void setColumnType(ColumnType columnType) {
        this.columnType = columnType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public ColumnType getColumnType() {
        return columnType;
    }

    public float getNullability() {
        return nullability;
    }

    public void setNullability(float nullability) {
        this.nullability = nullability;
    }

    public String getName() {
        return name;
    }
    public void setUpColumn(Node node){
        NodeList nodeList = node.getChildNodes();
        for(int i=0;i<nodeList.getLength();i++){
            Node item = nodeList.item(i);
            if(item.getNodeType()!= Node.ELEMENT_NODE){
                continue;
            }
            String name = item.getNodeName();
            if(Tag.dbtype.name().equalsIgnoreCase(name)){
                setDbType(item.getTextContent());
            }else if(Tag.fixed.name().equalsIgnoreCase(name)){
                setupFixed(new FixedNode(item));
            }else if(Tag.increment.name().equalsIgnoreCase(name)){
                setupIncrement(new IncrementNode(item));
            }else if(Tag.random.name().equalsIgnoreCase(name)){
                setupRandom(new RandomNode(item));
            }else if(Tag.list.name().equalsIgnoreCase(name)){
                setupList(new ListNode(item));
            }else if(Tag.roulette.name().equalsIgnoreCase(name)){
                setupRoulette(new RouletteNode(item));
            }else if(Tag.ratio.name().equalsIgnoreCase(name)){
                setupRatio(new RatioNode(item));
            }else if(Tag.skip.name().equalsIgnoreCase(name)){
                this.columnType = ColumnType.SKIP;
            }
        }

    }

    protected abstract void setupFixed(FixedNode fixedNode);
    protected abstract void setupIncrement(IncrementNode incrementNode);
    protected abstract void setupRandom(RandomNode randomNode);
    protected abstract void setupList(ListNode listNode);
    protected abstract void setupRoulette(RouletteNode rouletteNode);
    protected abstract void setupRatio(RatioNode ratioNode);

    /**
     * 生成数据
     * @return 数据
     */
    public String generate(){
        return (nullability<=0||nullRandom.nextFloat()>=nullability)? doGenerate() : null;
    }

    /**
     * 生成数据
     * @return 数据
     */
    protected abstract String doGenerate();

    protected abstract DataGenerator getDataGenerator();

    public String toString(){
        return "Column{name="+getName()+",type="+getColumnType()+",dbType="+getDbType()+",null="+getNullability()+",generator="+getDataGenerator()+"}";
    }

}
