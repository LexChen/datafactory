package com.lexsoft.config;

import com.lexsoft.job.Method;
import com.lexsoft.job.RunnableTask;
import com.lexsoft.utils.Tools;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.*;

/**
 * 配置文件解析
 * @author 牧心牛(QQ:2480102119)
 */
public class Config {
    private Properties globalConfig = new Properties();
    private List<Table>     tables;
    private List<DriverConfig> driverConfigs;
    private List<ConnectionConfig> connectionConfigs;
    private List<TaskConfig> taskConfigs;

    /**
     * automatically load configs
     */
    public Config(){
        File file = new File("config.xml");
        File deep = new File("resources/config.xml");
        try{
            if(file.exists()){
                loadConfig(new FileInputStream(file));
            }else if(deep.exists()) {
                loadConfig(new FileInputStream(deep));
            }else{
                loadConfig(getClass().getResourceAsStream("resources/config.xml"));
            }
        }catch (Exception e){
            throw new RuntimeException("can not load config :"+e.getMessage(),e);
        }
    }

    public Config(String path){
        try{
            loadConfig(new FileInputStream(new File(path)));
        }catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException("can not load config :"+e.getMessage(),e);
        }
    }

    /**
     * load config file
     * @param is inputstream
     * @throws ParserConfigurationException if any
     * @throws IOException  if any
     * @throws SAXException  if any
     */
    private void loadConfig(InputStream is) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        factory.setNamespaceAware(false);
        factory.setIgnoringComments(true);
        factory.setCoalescing(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(is);
        NodeList nodeList = document.getDocumentElement().getChildNodes();
        for(int i=0;i<nodeList.getLength();i++){
            Node config = nodeList.item(i);
            String name = config.getNodeName();
            if(Tag.drivers.name().equalsIgnoreCase(name)){
                loadDrivers(config.getChildNodes());
            }else if(Tag.tables.name().equalsIgnoreCase(name)){
                loadTables(config.getChildNodes());
            }else if(Tag.options.name().equalsIgnoreCase(name)){
                loadOptions(config.getChildNodes(),globalConfig);
            }else if(Tag.connections.name().equalsIgnoreCase(name)){
                loadConnections(config.getChildNodes());
            }else if(Tag.tasks.name().equalsIgnoreCase(name)){
                loadTask(config.getChildNodes());
            }
        }
    }

    private void loadDrivers(NodeList driverNodes){
        this.driverConfigs = new ArrayList<>();
        for(int i = 0; i< driverNodes.getLength(); i++){
            Node driverNode = driverNodes.item(i);
            if(driverNode.getNodeType()== Node.ELEMENT_NODE){
                this.driverConfigs.add(new DriverConfig(driverNode));
            }
        }
    }

    private void loadConnections(NodeList connectionNodes){
        this.connectionConfigs = new ArrayList<>();
        for(int i = 0; i< connectionNodes.getLength(); i++){
            Node connNode = connectionNodes.item(i);
            if(connNode.getNodeType()== Node.ELEMENT_NODE){
                NodeList configNodes = connNode.getChildNodes();
                NamedNodeMap nnm = connNode.getAttributes();
                ConnectionConfig connectionConfig = new ConnectionConfig(nnm.getNamedItem("id").getNodeValue());
                for(int j=0;j<configNodes.getLength();j++){
                    Node config = configNodes.item(j);
                    if(config.getNodeType() == Node.ELEMENT_NODE){
                        String name = config.getNodeName();
                        if(Tag.driver.name().equalsIgnoreCase(name)){
                            connectionConfig.setDriver(config.getTextContent().trim());
                        }else if(Tag.url.name().equalsIgnoreCase(name)){
                            connectionConfig.setUrl(config.getTextContent().trim());
                        }else if(Tag.username.name().equalsIgnoreCase(name)){
                            connectionConfig.setUsername(config.getTextContent().trim());
                        }else if(Tag.password.name().equalsIgnoreCase(name)){
                            connectionConfig.setPassword(config.getTextContent().trim());
                        }else if(Tag.pres.name().equalsIgnoreCase(name)){
                            connectionConfig.setPreList(loadNameList(Tag.pre.name(),config.getChildNodes()));
                        }else if(Tag.posts.name().equalsIgnoreCase(name)){
                            connectionConfig.setPostList(loadNameList(Tag.post.name(),config.getChildNodes()));
                        }
                    }
                }
                connectionConfigs.add(connectionConfig);
            }
        }
    }

    private void loadTask(NodeList taskNodes){
        this.taskConfigs = new ArrayList<>();
        for(int i = 0; i< taskNodes.getLength(); i++){
            Node taskNode = taskNodes.item(i);
            if(taskNode.getNodeType()== Node.ELEMENT_NODE && Tag.task.name().equalsIgnoreCase(taskNode.getNodeName())){
                NamedNodeMap nnp = taskNode.getAttributes();
                TaskConfig taskConfig = new TaskConfig(nnp.getNamedItem(Tag.id.name()).getNodeValue());
                Node node = nnp.getNamedItem(Tag.exec.name());
                if(node!=null){
                    taskConfig.setExecute("true".equalsIgnoreCase(node.getNodeValue().trim()));
                }
                node = nnp.getNamedItem(Tag.connection.name());
                if(node!=null){
                    taskConfig.setConnection(node.getNodeValue().trim());
                }
                node = nnp.getNamedItem(Tag.method.name());
                if(node!=null){
                    taskConfig.setMethod(node.getNodeValue().trim());
                }
                NodeList taskConfigList = taskNode.getChildNodes();
                for(int j=0;j<taskConfigList.getLength();j++){
                    Node configNode = taskConfigList.item(j);
                    String name = configNode.getNodeName();
                    if(Tag.tables.name().equalsIgnoreCase(name)){
                        taskConfig.setTables(loadNameList(Tag.table.name(),configNode.getChildNodes()));
                    }else if(Tag.options.name().equalsIgnoreCase(name)){
                        Properties prop = new Properties();
                        loadOptions(configNode.getChildNodes(),prop);
                        taskConfig.setConfig(prop);
                    }
                }
                taskConfigs.add(taskConfig);
            }
        }
    }

    private static List<String> loadNameList(String name,NodeList nodeList){
        ArrayList<String> values = new ArrayList<>();
        for(int i=0;i<nodeList.getLength();i++){
            Node node = nodeList.item(i);
            if(name.equalsIgnoreCase(node.getNodeName())){
                String text = node.getTextContent().trim();
                if(!text.isEmpty()){
                    values.add(text);
                }
            }
        }
        return values;
    }

    private void loadTables(NodeList tableNodes){
        this.tables = new ArrayList<>();
        for(int i=0;i<tableNodes.getLength();i++){
            Node tableNode = tableNodes.item(i);
            if(tableNode.getNodeType()==Node.ELEMENT_NODE){
                this.tables.add(new Table(tableNode));
            }
        }
    }

    public static void loadOptions(NodeList optionList,Properties config){
        for(int i=0;i<optionList.getLength();i++){
            Node optionNode = optionList.item(i);
            if(optionNode.getNodeType()==Node.ELEMENT_NODE){
                NamedNodeMap nnp = optionNode.getAttributes();
                Node value = nnp.getNamedItem(Tag.value.name());
                String textValue = (value==null?optionNode.getTextContent():value.getNodeValue());
                if(textValue.startsWith("0x")){
                    config.put(optionNode.getNodeName(),Tools.unHexString(textValue.substring(2)));
                }else{
                    config.put(optionNode.getNodeName(),textValue);
                }
            }
        }
    }

    public List<Table> getTables(){
        return tables;
    }

    public List<DriverConfig> getDriverConfig(){
        return driverConfigs;
    }

    public Properties getControlConfig(){
        return globalConfig;
    }

    public List<ConnectionConfig> getConnectionConfig(){
        return connectionConfigs;
    }

    public List<TaskConfig> getTaskConfig(){
        return taskConfigs;
    }

    public List<RunnableTask> getRunnableTasks(){
        List<RunnableTask> runnableTasks = new ArrayList<>();
        for(TaskConfig taskConfig : taskConfigs){
            if(!taskConfig.isExecute()){
                continue;
            }
            ConnectionConfig connectionConfig = ConnectionConfig.EMPTY;
            DriverConfig driverConfig = DriverConfig.EMPTY;
            if(!Method.file.name().equalsIgnoreCase(taskConfig.getMethod())){
                if(taskConfig.getTables()==null || taskConfig.getTables().size()==0){
                    continue;
                }
                if(taskConfig.getConnection()==null || taskConfig.getConnection().isEmpty()){
                    continue;
                }
                String connId = taskConfig.getConnection();
                for(ConnectionConfig conn : connectionConfigs){
                    if(conn.getId().equalsIgnoreCase(connId)){
                        connectionConfig = conn;
                        break;
                    }
                }
                if(connectionConfig==null){
                    continue;
                }
                String driverId = connectionConfig.getDriver();
                for(DriverConfig driver : driverConfigs){
                    if(driver.getId().equalsIgnoreCase(driverId)){
                        driverConfig = driver;
                        break;
                    }
                }
                if(driverConfig==null){
                    continue;
                }
            }
            for(String table : taskConfig.getTables()){
                Table tableConfig = null;
                for(Table tbl : tables){
                    if(tbl.getName().equalsIgnoreCase(table)){
                        tableConfig = tbl;
                    }
                }
                if(tableConfig==null){
                    continue;
                }
                RunnableTask runnableTask = new RunnableTask(taskConfig.getId());
                runnableTask.setMethod(taskConfig.getMethod());
                runnableTask.setDriverClass(driverConfig.getDriverClass());
                runnableTask.setUrl(connectionConfig.getUrl());
                runnableTask.setUsername(connectionConfig.getUsername());
                runnableTask.setPassword(connectionConfig.getPassword());
                runnableTask.setTable(tableConfig);
                runnableTask.setConfig(Tools.priorityProperties(globalConfig, tableConfig.getConfig(), taskConfig.getConfig()));
                runnableTask.setPreSQLs(connectionConfig.getPreList());
                runnableTask.setPostSQLs(connectionConfig.getPostList());
                runnableTasks.add(runnableTask);
            }
        }
        return runnableTasks;
    }

    public static void newConfig(String db,String path){
        Map<String,String> drivers = new HashMap<>();
        Map<String,String> urls = new HashMap<>();
        drivers.put("oracle","oracle.jdbc.driver.OracleDriver");
        urls.put("oracle","jdbc:oracle:thin:@[host]:[port]:[sid]");
        drivers.put("mysql","com.mysql.cj.jdbc.Driver");
        urls.put("mysql","jdbc:mysql://[host]:[port]/[database]?useUnicode=true&characterEncoding=utf8&&rewriteBatchedStatements=true");
        drivers.put("postgresql","org.postgresql.Driver");
        urls.put("postgresql","jdbc:postgresql://[host]:[port]/[database]");
        drivers.put("gauss","com.huawei.gauss200.jdbc.Driver");
        urls.put("gauss","jdbc:gaussdb://[host]:[port]/[database]");
        drivers.put("sqlserver","com.microsoft.sqlserver.jdbc.SQLServerDriver");
        urls.put("sqlserver","jdbc:sqlserver://[host]:[port];databaseName=[database]");
        drivers.put("db2","com.ibm.db2.jcc.DB2Driver");
        urls.put("db2","jdbc:db2://[host]:[port]/[database]");
        drivers.put("sqlite","org.sqlite.JDBC");
        urls.put("sqlite","jdbc:sqlite:[path]");

        try(PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(path)))){
            pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            pw.println("<config>");
            pw.println("\t<drivers>");
            for(Map.Entry<String,String> entry : drivers.entrySet()){
                if(db.isEmpty()||db.equalsIgnoreCase("*")||db.equalsIgnoreCase(entry.getKey())){
                    pw.println("\t\t<driver id=\""+entry.getKey()+"\">"+entry.getValue()+"</driver>");
                }
            }
            pw.println("\t</drivers>");
            pw.println("\t<connections>");
            for(Map.Entry<String,String> entry : urls.entrySet()){
                if(db.isEmpty()||db.equalsIgnoreCase("*")||db.equalsIgnoreCase(entry.getKey())){
                    pw.println("\t\t<connection id=\"c_"+entry.getKey()+"\">");
                    pw.println("\t\t\t<driver>"+entry.getKey()+"</driver>");
                    pw.println("\t\t\t<url><![CDATA["+entry.getValue()+"]]></url>");
                    pw.println("\t\t\t<username></username>");
                    pw.println("\t\t\t<password></password>");
                    pw.println("\t\t\t<pres>");
                    pw.println("\t\t\t</pres>");
                    pw.println("\t\t\t<posts>");
                    if("oracle".equalsIgnoreCase(entry.getKey())){
                        pw.println("\t\t\t\t<pre>ALTER SESSION SET NLS_DATE_FORMAT='YYYY-MM-DD HH24:MI:SS'</pre>");
                        pw.println("\t\t\t\t<pre>ALTER SESSION SET NLS_TIMESTAMP_FORMAT='YYYY-MM-DD HH24:MI:SSXFF'</pre>");
                        pw.println("\t\t\t\t<pre>ALTER SESSION SET NLS_TIMESTAMP_TZ_FORMAT='YYYY-MM-DD HH24:MI:SSXFF TZH:TZM'</pre>");
                    }
                    pw.println("\t\t\t</posts>");
                    pw.println("\t\t</connection>");
                }
            }
            pw.println("\t</connections>");
            pw.println("\t<options>");
            for(Parameter param : Parameter.values()){
                pw.println("\t\t<"+param.name()+" value=\""+param.defaultValue().replace("\n","0x0a").replace("\r","0x0d")+"\"/>");
            }
            pw.println("\t</options>");
            pw.println("\t<tasks>");
            for(Map.Entry<String,String> entry : drivers.entrySet()){
                if(db.isEmpty()||db.equalsIgnoreCase("*")||db.equalsIgnoreCase(entry.getKey())){
                    pw.println("\t\t<task id=\"t_"+entry.getKey()+"\" exec=\"true\" connection=\"c_"+entry.getKey()+"\" method=\"insert\">");
                    pw.println("\t\t\t<tables>");
                    pw.println("\t\t\t\t<table>test_"+entry.getKey()+"</table>");
                    pw.println("\t\t\t</tables>");
                    pw.println("\t\t\t<options>");
                    pw.println("\t\t\t</options>");
                    pw.println("\t\t</task>");
                }
            }
            pw.println("\t</tasks>");
            pw.println("\t<tables>");
            pw.println("<!-- possible column data generator examples:");
            pw.println("<fixed>fixed_value</fixed>\n");
            pw.println("<increment>");
            pw.println("\t<start>1</start>");
            pw.println("\t<end>100</end>");
            pw.println("\t<step>1</step>");
            pw.println("</increment>\n");
            pw.println("<random>");
            pw.println("\t<min>1</min>");
            pw.println("\t<max>10</max>");
            pw.println("\t<candidates>??latin1</candidates>");
            pw.println("</random>\n");
            pw.println("possible candidates format :");
            pw.println("plain text ,such as abcd1234");
            pw.println("hex string ,such as 0x31325137a2");
            pw.println("special collections: ??id ??uuid ??mobile ??latin1 ??chn ??gbk ??kor ??jpn ??upper ??lower ??alpha ??number ??word\n");
            pw.println("<list>");
            pw.println("\t<item>1</item>");
            pw.println("\t<item>2</item>");
            pw.println("\t<item>3</item>");
            pw.println("</list>\n");
            pw.println("<roulette>");
            pw.println("\t<item>1</item>");
            pw.println("\t<item>2</item>");
            pw.println("\t<item>3</item>");
            pw.println("</roulette>\n");
            pw.println("<ratio>");
            pw.println("\t<item ratio=\"10\">1</item>");
            pw.println("\t<item ratio=\"20\">2</item>");
            pw.println("\t<item ratio=\"10\">3</item>");
            pw.println("</ratio>");
            pw.println("-->");
            for(Map.Entry<String,String> entry : drivers.entrySet()){
                if(db.isEmpty()||db.equalsIgnoreCase("*")||db.equalsIgnoreCase(entry.getKey())){
                    pw.println("\t\t<table name=\"test_"+entry.getKey()+"\">");
                    pw.println("\t\t\t<options>");
                    pw.println("\t\t\t</options>");
                    int no = 1;
                    for(ColumnType columnType : ColumnType.values()){
                        if(columnType==ColumnType.USERDEFINE){
                            continue;
                        }
                        pw.println("\t\t\t<column name=\"col"+no+"\" type=\""+columnType.name().toLowerCase()+"\" nullable=\"0\">");
                        pw.println("\t\t\t\t<dbtype></dbtype>");
                        pw.println("\t\t\t\t<!-- set up here -->");
                        pw.println("\t\t\t</column>");
                        no++;
                    }
                    pw.println("\t\t<keys>");
                    pw.println("\t\t</keys>");
                    pw.println("\t\t<posts>");
                    pw.println("\t\t</posts>");
                    pw.println("\t\t</table>");
                }
            }
            pw.println("\t</tables>");
            pw.println("</config>");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
