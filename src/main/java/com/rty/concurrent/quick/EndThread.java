package com.rty.concurrent.quick;

/**
 * Interrupt:通知线程中断(打一个标致位)，线程可以不停止，不理会，线程是协助式，不是抢占式，isInterrupted检查标致位，
 * 静态的isInterrupted:检查中断标志位，并且修改标志位true为false
 *
 * 不推荐自己创建的boolean的中断标识符
 */
public class EndThread  {
    private static class UseThread extends Thread{
        public UseThread(String name){
            super(name);
        }
        @Override
        public void run() {
            super.run();
            //do my work
            String threadName=Thread.currentThread().getName();
            System.out.println(threadName+"interrupt flag="+isInterrupted());
            while(!isInterrupted()){
               // while (Thread.interrupted()){
                //    while (true){
                        System.out.println(threadName+" is running");
                        System.out.println(threadName+" inner interrupt flag="+isInterrupted());

        /*            }

                }*/
                System.out.println(threadName+" interrupt flag="+isInterrupted());

            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread endThread=new UseThread("endThread");
        endThread.start();
        Thread.sleep(20);
        endThread.interrupt();//中断线程，其实是设置中断标志位
    }
}
