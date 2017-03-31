package org.xyc.showsome.lianxi;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by CCC on 2016/5/24.
 */
public class ExecuteInfo {

    public static final ExecutorService es = Executors.newFixedThreadPool(10);

    public static void info() {
        ThreadPoolExecutor executor = (ThreadPoolExecutor)es;
        System.out.println(executor.getActiveCount());
        System.out.println(executor.getPoolSize());
    }

    public static void main(String[] args) {
        info();
    }
}
