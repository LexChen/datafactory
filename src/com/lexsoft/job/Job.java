package com.lexsoft.job;

import com.lexsoft.config.Parameter;
import com.lexsoft.config.Table;
import com.lexsoft.utils.Statistic;
import com.lexsoft.utils.Tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 任务基础类
 * @author 牧心牛(QQ:2480102119)
 */
public abstract class Job extends Thread {
    protected  RunnableTask runnableTask;
    protected  Table table;
    protected  int threads;
    protected  int batch;
    protected  long count;
    protected  boolean drop;
    protected  boolean create;
    protected  boolean truncate;
    protected  String  path;
    protected  long    split_count;
    protected  long    split_size;
    protected  String  recordDelim;
    protected  String  fieldDelim;
    protected  String  nullas;
    protected  boolean debug;
    private    String  whose;

    public Job(RunnableTask runnableTask){
        this.runnableTask = runnableTask;
        Properties config = runnableTask.getConfig();
        threads = Integer.parseInt(config.getProperty(Parameter.threads.name(),Parameter.threads.defaultValue()));
        batch = Integer.parseInt(config.getProperty(Parameter.batch.name(),Parameter.batch.defaultValue()));
        drop = "true".equalsIgnoreCase(config.getProperty(Parameter.drop.name(),Parameter.drop.defaultValue()));
        create = "true".equalsIgnoreCase(config.getProperty(Parameter.create.name(),Parameter.create.defaultValue()));
        debug = "true".equalsIgnoreCase(config.getProperty(Parameter.debug.name(),Parameter.debug.defaultValue()));
        truncate = "true".equalsIgnoreCase(config.getProperty(Parameter.truncate.name(),Parameter.truncate.defaultValue()));
        count = Long.parseLong(config.getProperty(Parameter.count.name(),Parameter.count.defaultValue()));
        Statistic.addTotal(count);
        table = runnableTask.getTable();
        whose = table.getName()+"> ";
        split_count = Long.parseLong(config.getProperty(Parameter.split_count.name(),Parameter.split_count.defaultValue()));
        if(split_count<=0){
            split_count = Long.MAX_VALUE;
        }
        split_size = Long.parseLong(config.getProperty(Parameter.split_size.name(),Parameter.split_size.defaultValue()));
        if(split_size<=0){
            split_size = Long.MAX_VALUE;
        }
        path = config.getProperty(Parameter.path.name(),System.getProperty("user.dir")).replace("\\","/");
        if(path.endsWith("/")){
            path = path.substring(0,path.length()-1);
        }
        fieldDelim = config.getProperty(Parameter.field_delim.name(),Parameter.field_delim.defaultValue());
        if(fieldDelim.startsWith("0x")){
            fieldDelim = Tools.unHexString(fieldDelim.substring(2));
        }
        recordDelim = config.getProperty(Parameter.record_delim.name(),Parameter.record_delim.defaultValue());
        if(recordDelim.startsWith("0x")){
            recordDelim = Tools.unHexString(recordDelim.substring(2));
        }
        nullas = config.getProperty(Parameter.nullas.name(),Parameter.nullas.defaultValue());
        if(nullas.startsWith("0x")){
            nullas = Tools.unHexString(nullas.substring(2));
        }
    }

    protected void prepareTable(Statement stmt){
        if(drop){
            println("exec : "+table.toDropSQL());
            Tools.executeSilently(stmt,table.toDropSQL());
        }
        if(create){
            println("exec : "+table.toCreateSQL());
            Tools.executeSilently(stmt,table.toCreateSQL());
            for(String sql : table.getPosts()){
                println("exec : "+sql);
                Tools.executeSilently(stmt,sql);
            }
        }
        if(truncate){
            println("exec : "+table.toTruncateSQL());
            Tools.executeSilently(stmt,table.toTruncateSQL());
        }
        for(String sql : runnableTask.getPreSQLs()){
            println("exec pre : "+sql);
            Tools.executeSilently(stmt,sql);
        }
    }

    private static ReentrantLock lock = new ReentrantLock();
    public static Connection getConnection(RunnableTask task) throws ClassNotFoundException,SQLException {
        lock.lock();
        Class.forName(task.getDriverClass());
        Connection conn = DriverManager.getConnection(task.getUrl(),task.getUsername(),task.getPassword());
        lock.unlock();
        return conn;
    }

    public void run(){
        try{
            runJob();
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            if(listener!=null){
                listener.notifyDone();
            }
            clean();
        }
    }

    public void clean(){}

    public abstract void runJob() throws Exception;

    JobListener listener;
    public void setJobListener(JobListener listener){
        this.listener = listener;
    }

    public interface JobListener{
        void notifyDone();
    }

    protected void debug(Object object){
        if(debug){
            System.out.println(whose+object);
        }
    }

    protected void println(Object object){
        System.out.println(whose+object);
    }

}
