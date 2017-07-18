package org.xyc.showsome.pea;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * created by wks on date: 2017/7/18
 */
public class FutureTaskPea {

    private static final Logger logger = LoggerFactory.getLogger(FuturePea.class);

    private static final ExecutorService es = Executors.newSingleThreadExecutor();

    private static void shadowclaw1() {
        FutureTask<String> futureTask = new FutureTask<String>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Thread.sleep(5000);
                return "hello world";
            }
        });
        Future<?> future = es.submit(futureTask);

//        try {
//            logger.info("start");
//            //can not get, use futureTask.get() instead
//            Object obj = future.get();
//            logger.info("end and get {}, {}", obj, futureTask.get());
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }

        futureTask.cancel(true);

        while (!futureTask.isDone()) {
            logger.info("still doing. cancel ? {}", futureTask.isCancelled());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        String str = null;
        try {
            str = futureTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        logger.info("end. get {}", str);
    }

    public static void shadowclaw2() {
        FutureTask<String> futureTask = new MyFutureTask(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Thread.sleep(5000);
                return "ok";
            }
        });
        es.submit(futureTask);

        while (!futureTask.isDone()) {
            logger.info("still doing. cancel ? {}", futureTask.isCancelled());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        String str = null;
        try {
            str = futureTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        logger.info("end. get {}", str);
    }

    public static void main(String[] args) {
        shadowclaw2();
    }

    private static class MyFutureTask extends FutureTask<String> {

        public MyFutureTask(Callable<String> callable) {
            super(callable);
        }

        public MyFutureTask(Runnable runnable, String result) {
            super(runnable, result);
        }

        @Override
        protected void done() {
            logger.info("hello done!");
        }
    }
}
