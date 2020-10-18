package com.rty.concurrent.quick.pool;

import java.sql.Connection;
import java.util.LinkedList;

/**
 * 模拟连接池(连接池的实现)  wait和notifyAll的使用
 *
 * @author rty
 * @since 2020-10-18
 */
public class DBPool {
    /*容器，存放连接*/
    private static LinkedList<Connection> pool = new LinkedList<Connection>();

    /*限制了池的大小=20*/
    public DBPool(int initialSize) {
        if (initialSize > 0) {
            for (int i = 0; i < initialSize; i++) {
                pool.addLast(SqlConnectImpl.fetchConnection());
            }
        }
    }

    /*释放连接，通知其他的等待连接的线程*/
    public void releaseConnection(Connection connection) {
        if (connection != null) {
            synchronized (pool) {
                pool.addLast(connection);
                pool.notifyAll();
            }
        }
    }

    /*获取*/
    // 在mills内无法获取到连接，将会返回null
    public Connection fetchConnection(long mills) throws InterruptedException {
        synchronized (pool) {
            //永不超時
            if (mills < 0) {
                while (pool.isEmpty()) {
                    pool.wait();
                }
                return pool.removeFirst();
            } else {
                /*超時限制*/
                long future = System.currentTimeMillis() + mills;
                /*等待时长*/
                long remaining = mills;
                while (pool.isEmpty() && remaining > 0) {
                    pool.wait(remaining);
                    remaining = future - System.currentTimeMillis();
                }
                Connection connection = null;
                if (!pool.isEmpty()) {
                    connection = pool.removeFirst();
                }
                return connection;
            }
        }
    }
}
