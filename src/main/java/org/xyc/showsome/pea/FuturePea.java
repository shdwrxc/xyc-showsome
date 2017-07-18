package org.xyc.showsome.pea;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * created by wks on date: 2017/7/18
 */
public class FuturePea {

    private static final Logger logger = LoggerFactory.getLogger(FuturePea.class);

    private static final ExecutorService es = Executors.newSingleThreadExecutor();

    public static void shadowclaw1() {
        Future<String> future = es.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Thread.sleep(1000 * 5);
                return "call";
            }
        });

        try {
            logger.info("start waiting");
            String str = future.get();
            logger.info("end waiting and get {}", str);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public static void shadowclaw2() {
        Future<String> future = es.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000 * 6);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "run");

        try {
            logger.info("start waiting");
            String str = future.get();
            logger.info("end waiting and get {}", str);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public static void shadowclaw3() {
        Future<?> future = es.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000 * 6);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        try {
            logger.info("start waiting");
            //its null
            Object str = future.get();
            logger.info("end waiting and get {}", str);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        shadowclaw3();
    }
}
