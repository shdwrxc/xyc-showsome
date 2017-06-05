package org.xyc.showsome.pecan.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ExecutePool {

    public static void main(String[] args) {
        ExecutorService rs = Executors.newFixedThreadPool(10);
        rs.shutdown();
        try {
            rs.awaitTermination(1000, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ThreadPoolExecutor tpe =((ThreadPoolExecutor)rs);
        tpe.setCorePoolSize(10);
        Thread t = new Thread();
        t.setUncaughtExceptionHandler(null);
        ExecutorService rs1 = Executors.unconfigurableExecutorService(tpe);
    }
}
