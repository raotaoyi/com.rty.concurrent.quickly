package com.rty.concurrent.quick.pool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程之间的协助，展示wait();和notifyAll();的用法，
 */
public class DBPoolTest {
    static DBPool pool = new DBPool(10);
    // 控制器，控制main线程等待所有的Worker结束后才能继续执行
    static CountDownLatch end;

    public static void main(String[] args) throws Exception{
        // 线程数量
        int threadCount=50;
        end=new CountDownLatch(threadCount);
        int count=20;//每个线程的操作次数
        AtomicInteger got=new AtomicInteger();//计数器，统计可以拿到连接的线程
        AtomicInteger notGot=new AtomicInteger();//计数器，统计未拿到连接的线程
        for(int i=0;i<threadCount;i++){
            Thread thread=new Thread(new Worker(count,got,notGot));
            thread.start();
        }
        end.await();//main线程在此处等待
        System.out.println("总共尝试了:"+(threadCount*count));
        System.out.println("拿到连接的次数:"+got);
        System.out.println("没有连接的次数:"+notGot);
    }
    static class Worker implements Runnable{
        int count;
        AtomicInteger got;
        AtomicInteger noGot;

        public Worker(int count, AtomicInteger got, AtomicInteger notGot) {
            this.count=count;
            this.got=got;
            this.noGot=notGot;
        }

        public void run() {
            while(count>0){
                try {
                    //从线程池中获取连接，如果1000ms内无法获取到，将会返回null
                    Connection  connection=pool.fetchConnection(1000);
                    if(connection!=null){
                        try {
/*                            connection.createStatement();
                            PreparedStatement preparedStatement=connection.prepareStatement("");
                            connection.commit();*/
                        } finally {
                            pool.releaseConnection(connection);
                            got.incrementAndGet();
                        }
                    }else{
                        noGot.incrementAndGet();
                        System.out.println(Thread.currentThread().getName()+" 等待超时");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    count--;
                }
            }
            end.countDown();

        }
    }

}
