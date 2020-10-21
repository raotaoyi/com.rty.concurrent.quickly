package com.rty.concurrent.quick.sync;

/**
 * synchronized(保证线程安全方式一)
 * 资源共享,多线程访问同一变量是，加锁来保证线性安全
 * 加锁的3种方式(静态锁，其实也是对象锁，锁的是对象的Class对象)
 *
 * @author rty
 * @since 2020-10-21
 */
public class SyncTest {
    private long count = 0;
    private Object obj = new Object();//作为一个锁

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    /*用在同步块上*/
    public void incCount() {
        synchronized (obj) {
            count++;
        }
    }

    /*用在方法上*/
    public synchronized void incCount2() {
        count++;
    }

    public void incCount3() {
        synchronized (this) {
            count++;
        }

    }

    private static class Count extends Thread {
        private SyncTest simpleOper;

        public Count(SyncTest simpleOper) {
            this.simpleOper = simpleOper;

        }

        @Override
        public void run() {
            for (int i = 0; i < 1000; i++) {
                simpleOper.incCount();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        SyncTest simpleOper = new SyncTest();
        Count count1 = new Count(simpleOper);
        Count count2 = new Count(simpleOper);
        count1.start();
        count2.start();
        Thread.sleep(1000);
        System.out.println(simpleOper.getCount());
    }


}
