package com.lexsoft.utils;

import com.lexsoft.job.Method;
import com.lexsoft.config.Parameter;
import com.lexsoft.job.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 多线程调度
 * @author 牧心牛(QQ:2480102119)
 */
public class Runner implements Job.JobListener {
    private int coresize;
    private CountDownLatch tasks;
    private ExecutorService executor;
    public Runner(int coresize,int tasks){
        this.coresize = coresize;
        this.tasks = new CountDownLatch(tasks);
    }
    public void start(){
        this.executor = Executors.newFixedThreadPool(this.coresize);
    }

    public void submit(RunnableTask runnableTask){
        Job job = null;
        boolean debug = "true".equalsIgnoreCase(runnableTask.getConfig().getProperty(Parameter.debug.name(),"false"));
        if(Method.insert.name().equalsIgnoreCase(runnableTask.getMethod())){
            job = new InsertJob(runnableTask);
        }else if(Method.values.name().equalsIgnoreCase(runnableTask.getMethod())){
            job = new ValuesJob(runnableTask);
        }else if(Method.load.name().equalsIgnoreCase(runnableTask.getMethod())){
            job = new LoadJob(runnableTask);
        }else if(Method.file.name().equalsIgnoreCase(runnableTask.getMethod())){
            job = new FileJob(runnableTask);
        }else if(Method.copy.name().equalsIgnoreCase(runnableTask.getMethod())){
            job = new CopyJob(runnableTask);
        }else if(Method.bulk.name().equalsIgnoreCase(runnableTask.getMethod())){
            job = new BulkJob(runnableTask);
        }else if(Method.add_as_needed.name().equalsIgnoreCase(runnableTask.getMethod())){
            //when needed
        }
        if(debug){
            System.out.println("submit : "+ runnableTask);
        }else{
            System.out.println("submit : "+runnableTask.getId() + " "+job.getClass().getSimpleName());
        }
        job.setJobListener(this);
        executor.submit(job);
    }

    public void await(){
        try{
            tasks.await();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void stop(){
        if(executor!=null){
            executor.shutdown();
        }
    }

    @Override
    public void notifyDone() {
        tasks.countDown();
    }
}
