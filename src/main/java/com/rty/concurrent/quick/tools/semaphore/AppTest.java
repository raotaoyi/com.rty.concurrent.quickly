package com.rty.concurrent.quick.tools.semaphore;

import java.sql.Connection;
import java.util.Random;

public class AppTest {
    private static DBPoolSemaphore dbPool = new DBPoolSemaphore();

    private static class BusiThread extends Thread {
        @Override
        public void run() {
            Random random = new Random();//设置每个线程持有的连接的时间不一样
            long start = System.currentTimeMillis();
            try {
                Connection connection = dbPool.takeConnect();
                System.out.println("Thread_" + Thread.currentThread().getId()
                        + "_获取数据库连接耗时【" + (System.currentTimeMillis() - start) + "】ms");
                System.out.println("查询数据完成，归还连接！");
                Thread.sleep(1000 + random.nextInt(100));//模仿业务消耗的时间，线程持有连接
                dbPool.returnConnect(connection);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 50; i++) {
            Thread thread = new BusiThread();
            thread.start();
        }
    }
}
