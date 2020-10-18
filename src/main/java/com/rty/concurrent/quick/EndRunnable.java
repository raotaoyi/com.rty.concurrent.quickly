package com.rty.concurrent.quick;

public class EndRunnable  {

    private static class UseRunnable implements  Runnable{

        public void run() {
            //do my work
            String threadName=Thread.currentThread().getName();
            System.out.println(threadName+"interrupt flag="+Thread.currentThread().isInterrupted());
            while(!Thread.currentThread().isInterrupted()){
                // while (Thread.interrupted()){
                //    while (true){
                System.out.println(threadName+" is running");
                System.out.println(threadName+" inner interrupt flag="+Thread.currentThread().isInterrupted());

        /*            }

                }*/
                System.out.println(threadName+" interrupt flag="+Thread.currentThread().isInterrupted());

            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread endThread =new Thread(new UseRunnable(),"endThread");
        endThread.start();
        Thread.sleep(20);
        endThread.interrupt();//中断线程，其实是设置中断标志位
    }
}
