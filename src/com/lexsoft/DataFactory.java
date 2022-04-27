package com.lexsoft;

import com.lexsoft.config.Config;
import com.lexsoft.config.Parameter;
import com.lexsoft.job.RunnableTask;
import com.lexsoft.utils.Runner;
import com.lexsoft.utils.Statistic;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Main
 * @author 牧心牛(QQ:2480102119)
 */
public class DataFactory {
    public static void main(String[] args){
        if(args==null||args.length==0){
            System.out.println("Usage 1 : java -cp ... "+DataFactory.class.getName()+" path_of_config_file");
            System.out.println("Usage 2 : java -cp ... "+DataFactory.class.getName()+" new [mysql|oracle|postgresql|gauss|sqlserver|db2|sqlite|*] path_of_config_file_to_be_created");
            System.exit(-1);
        }
        if(args.length>1 && "new".equalsIgnoreCase(args[0])){
            if(args.length>2){
                Config.newConfig(args[1],args[2]);
            }else{
                Config.newConfig("*",args[1]);
            }
            System.exit(0);
        }
        File file = new File(args[0]);
        if(!file.exists()){
            System.out.println("Error : "+file.getAbsolutePath()+" not exists.");
            System.exit(-1);
        }
        if(!file.isFile()){
            System.out.println("Error : "+file.getAbsolutePath()+" is not a file.");
            System.exit(-1);
        }
        if(!file.canRead()){
            System.out.println("Error : "+file.getAbsolutePath()+" can not be read.");
            System.exit(-1);
        }
        Config config = new Config(args[0]);
        List<RunnableTask> runnableTasks = config.getRunnableTasks();
        if(runnableTasks.size()==0){
            System.out.println("Warn : empty task list.");
            System.exit(0);
        }
        System.out.println("Tasks : "+runnableTasks.size());
        Runner runner = new Runner(
                Integer.parseInt(config.getControlConfig().getProperty(Parameter.threads.name(),Parameter.threads.defaultValue())),
                runnableTasks.size());
        runner.start();
        long t = System.currentTimeMillis();
        Statistic.setAutoshow("true".equalsIgnoreCase(config.getControlConfig().getProperty(Parameter.autoshow.name(),Parameter.autoshow.defaultValue())));
        Statistic.setShowInterval(Integer.parseInt(config.getControlConfig().getProperty(Parameter.interval.name(),Parameter.interval.defaultValue())));
        Statistic.start();
        for(RunnableTask runnableTask : runnableTasks){
            runner.submit(runnableTask);
        }
        runner.await();
        runner.stop();
        Statistic.end();
        System.out.println("Total Elapsed : "+ Statistic.formatMillis(System.currentTimeMillis()-t)+
                " , Average Speed = "+new DecimalFormat("#,###").format(Statistic.getFinalSpeed())+" recs/s");
        Statistic.listDetailSpeed(System.out);
    }
}
