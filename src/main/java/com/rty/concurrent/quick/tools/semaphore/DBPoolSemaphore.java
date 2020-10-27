package com.rty.concurrent.quick.tools.semaphore;

import java.sql.Connection;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;

public class DBPoolSemaphore {
    private final static int POOL_SIZE=10;
    /*存放数据库连接的容器*/
    private static LinkedList<Connection> pool = new LinkedList<>();
    /*两个指示器，分别表示池子还有可用连接和已用连接*/
    private final Semaphore useful,useless;
    static{
        for(int i=0;i<POOL_SIZE;i++){
            pool.addLast(SqlConnectImpl.fetchConnection());
        }
    }

    public DBPoolSemaphore() {
        this.useful=new Semaphore(10);
        this.useless=new Semaphore(0);
    }

    public void returnConnect(Connection connection) throws InterruptedException {
        if(connection!=null){
            System.out.println("当前有"+useful.getQueueLength()+"个线程等待数据库的连接！！"
                    +"可用连接数："+useful.availablePermits());
            useless.acquire();
            synchronized (pool){
                pool.addLast(connection);
            }
            useful.release();
        }
    }

    public Connection takeConnect() throws InterruptedException {
        useful.acquire();
        Connection connection;
        synchronized (pool) {
            connection = pool.removeFirst();
        }
        useless.release();
        return connection;
    }
}
