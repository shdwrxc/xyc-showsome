package org.xyc.showsome.lianxi;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by CCC on 2016/5/24.
 */
public class QueueStatus {

    private static final ThreadPoolExecutor taskPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);

    private static final List<String> finishedList = new CopyOnWriteArrayList<String>();

    private static final AtomicInteger serial = new AtomicInteger();

    public void info1() {
        System.out.println(taskPool.getQueue().size());
        taskPool.submit(new SampleWorker());
        taskPool.submit(new SampleWorker());
        taskPool.submit(new SampleWorker());
        taskPool.submit(new SampleWorker());
        taskPool.submit(new SampleWorker());
        System.out.println(taskPool.getQueue().size());
    }

    public void info2() {
        for (int i = 0; i < 10 ; i++) {
            System.out.println(i);
            taskPool.submit(new Callable<Object>() {
                public Object call() {
                    try {
                        int i = 1 / 0;
                    } catch (Exception e) {

                    } finally {
                        finishedList.add("1");
                    }

                    return null;
                }
            });
            System.out.println(i + "end");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("end");
    }

    public static void info3() {
        serial.set(Integer.MAX_VALUE - 1);
        System.out.println(serial.getAndIncrement());
        System.out.println(serial.getAndIncrement());
        System.out.println(serial.getAndIncrement());
        System.out.println(serial.getAndIncrement());
        System.out.println(serial.getAndIncrement());
    }

    public static void main(String[] args) {
        QueueStatus queueStatus = new QueueStatus();
        queueStatus.info1();
    }

    private class SampleWorker implements Callable {
        public Object call() throws Exception {
            Thread.sleep(2000);
            return null;
        }
    }
}
