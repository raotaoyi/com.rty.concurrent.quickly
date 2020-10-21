package com.rty.concurrent.quick.tools;

import java.util.concurrent.CountDownLatch;

/**
 * CountDownLatch的个数不仅仅代表线程的个数，过程中也可以countDown()
 */
public class UseCountDownLatch {
    static CountDownLatch latch = new CountDownLatch(6);

    private static class InitThread implements Runnable {
        public void run() {
            System.out.println("Thread_" + Thread.currentThread().getId() + " ready init work........");
            latch.countDown();
            for (int i = 0; i < 2; i++) {
                System.out.println("Thread+_" + Thread.currentThread().getId() + "......continue do its work");
            }
        }
    }

    private static class BusiThread implements Runnable {
        public void run() {
            try {
                latch.await();
                for (int i = 0; i < 3; i++) {
                    System.out.println("BusiThread_" + Thread.currentThread().getId() + "do business------");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.currentThread().sleep(1000);
                    System.out.println("Thread_" + Thread.currentThread().getId() + " ready init work step 1st");
                    latch.countDown();
                    System.out.println("begin step 2nd");
                    Thread.currentThread().sleep(1000);
                    System.out.println("Thread_" + Thread.currentThread().getId() + " ready init work step 2st");
                    latch.countDown();
                } catch (InterruptedException e) {
                    e.fillInStackTrace();
                }
            }
        }).start();
        new Thread(new BusiThread()).start();
        for (int i = 0; i <= 3; i++) {
            Thread thread = new Thread(new InitThread());
            thread.start();
        }

        latch.await();
        System.out.println("main do ites work .....");
    }

}
