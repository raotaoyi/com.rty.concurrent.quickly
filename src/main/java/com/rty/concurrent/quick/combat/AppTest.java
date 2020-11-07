package com.rty.concurrent.quick.combat;

import java.util.List;
import java.util.Random;

public class AppTest {
    private static final String JOB_NAME="计算数值";
    private final static int JOB_LENGTH=1000;
    //查询任务进程的线程
    private static class QueryResult implements Runnable{
        private PendingJobPool pool;

        public QueryResult(PendingJobPool pool){
            this.pool=pool;
        }
        @Override
        public void run() {
            int i=0;
            while(i<350){
                List<TaskResult<String>> taskDetail=pool.getTaskDetail(JOB_NAME);
                if(!taskDetail.isEmpty()){
                    System.out.println(pool.getTaskProgess( JOB_NAME));
                    System.out.println(taskDetail);
                }
                try {
                    Thread.sleep(100);
                    i++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        MyTask myTask=new MyTask();
        PendingJobPool pool=PendingJobPool.getInstance();
        pool.registerJob(JOB_NAME,JOB_LENGTH,myTask,5);
        Random r=new Random();
        for(int i=0;i<JOB_LENGTH;i++){
            pool.putTask(JOB_NAME,r.nextInt(1000));
        }
        Thread t=new Thread(new QueryResult(pool));
        t.start();
    }
}
