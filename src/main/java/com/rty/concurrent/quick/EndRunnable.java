package com.rty.concurrent.quick;

/**
 * interrupt()的用法 (runnable下的用法)
 * 不推荐自己创建的boolean的中断标识符
 */
public class EndRunnable {

    private static class UseRunnable implements Runnable {

        public void run() {
            //do my work
            String threadName = Thread.currentThread().getName();
            System.out.println(threadName + "interrupt flag=" + Thread.currentThread().isInterrupted());
            while (!Thread.currentThread().isInterrupted()) {
                // while (Thread.interrupted()){
                //    while (true){
                System.out.println(threadName + " is running");
                System.out.println(threadName + " inner interrupt flag=" + Thread.currentThread().isInterrupted());

        /*            }

                }*/
                System.out.println(threadName + " interrupt flag=" + Thread.currentThread().isInterrupted());

            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread endThread = new Thread(new UseRunnable(), "endThread");
        endThread.start();
        Thread.sleep(20);
        endThread.interrupt();//中断线程，其实是设置中断标志位
    }
}
