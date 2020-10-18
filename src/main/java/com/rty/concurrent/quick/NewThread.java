package com.rty.concurrent.quick;

/**
 * 创建Thread的两种方式
 *
 * @author rty
 * @since 2020-10-17
 */
public class NewThread {
    /**
     * 方式一 继承Thread
     */
    private static class UseThread extends Thread{
        @Override
        public void run() {
            super.run();
            //do my work
            System.out.println("hello UseThread");
        }
    }
    /**
     * 方式二 实现Runnable接口
     */
    private static class UseRunnable implements  Runnable{

        public void run() {
            //do my work
            System.out.println("hello UseRunnable");
        }
    }

    public static void main(String[] args) {
        UseThread useThread=new UseThread();
        useThread.start();
        UseRunnable useRunnable=new UseRunnable();
        new Thread(useRunnable).start();
    }
}
