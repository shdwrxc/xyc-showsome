package org.xyc.showsome.util;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by wks on 2016/1/26.
 *
 * ashenvale - 一个流程的序列号（多线程不要使用）
 * darkshore - 耗时
 *
 */
public class LogUtils {

    private static final Logger logger = LoggerFactory.getLogger(LogUtils.class);

    private static final String ASHENVALE = "ashenvale - ";

    private static final ExecutorService logThread = Executors.newFixedThreadPool(5);

    private static final AtomicInteger serial = new AtomicInteger();

    private static ThreadLocal<Integer> serialLocal = new ThreadLocal<Integer>(){
        @Override
        protected Integer initialValue() {
            return serial.getAndIncrement();
        }
    };

    /**
     * 使用线程池，每次流程开始前要调用下这个方法
     */
    public static void initialSerialLocal() {
        serialLocal.set(Integer.valueOf(serial.getAndIncrement()));
    }

    public static int getSerial() {
        return serialLocal.get();
    }

    public static long getStartTime() {
        return System.nanoTime();
    }

    private static long getAllTime(long startTime) {
        return (System.nanoTime() - startTime) / 1000000;
    }

    public static void logTime(String message, long startTime, Object... params) {
        logIgnoreTime(message, startTime, 50, params);
    }

    public static void logIgnoreTime(String message, long startTime, int ignore, Object... params) {
        long spend = getAllTime(startTime);
        if (spend < ignore)
            return;
        writeLog(Level.WARN, new StringBuilder().append(ASHENVALE).append(serialLocal.get()).append(", darkshore [").append(spend).append("]. ").append(message).toString(), params);
    }

    public static void debug(String message, Object... params) {
        writeLog(Level.DEBUG, appendMessage(message), params);
    }

    private static String appendMessage(String message) {
        return new StringBuilder().append(ASHENVALE).append(serialLocal.get()).append(". ").append(message).toString();
    }

    private static String appendMessage(int serial, String message) {
        return new StringBuilder().append(ASHENVALE).append(serial).append(". ").append(message).toString();
    }

    public static void debug(int serial, String message, Object... params) {
        writeLog(Level.DEBUG, appendMessage(serial, message), params);
    }

    public static void info(String message, Object... params) {
        writeLog(Level.INFO, appendMessage(message), params);
    }

    public static void info(int serial, String message, Object... params) {
        writeLog(Level.INFO, appendMessage(serial, message), params);
    }

    public static void warn(String message, Object... params) {
        writeLog(Level.WARN, appendMessage(message), params);
    }

    public static void warn(int serial, String message, Object... params) {
        writeLog(Level.WARN, appendMessage(serial, message), params);
    }

    public static void error(String message, Object... params) {
        writeLog(Level.ERROR, appendMessage(message), params);
    }

    public static void error(int serial, String message, Object... params) {
        writeLog(Level.ERROR, appendMessage(serial, message), params);
    }

    private static void writeLog(final Level level, final String message, final Object... params) {
        logThread.submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                if (level == Level.DEBUG)
                    logger.debug(message, params);
                else if (level == Level.INFO)
                    logger.info(message, params);
                else if (level == Level.WARN)
                    logger.warn(message, params);
                else if (level == Level.ERROR)
                    logger.error(message, params);
                return null;
            }
        });
    }

    private enum Level {
        DEBUG,INFO,WARN,ERROR
    }

    public static void main(String[] args) {
//        for (int i = 0; i < 10; i++) {
//            new Thread(){
//                @Override
//                public void run() {
//                    LogUtils.info("hello " + Thread.currentThread().getName());
//                }
//            }.start();
//        }

//        ExecutorService es = Executors.newFixedThreadPool(5);
//        ExecutorService es = Executors.newCachedThreadPool();
//        for (int i = 0; i < 50; i++) {
//            es.execute(new Runnable() {
//                @Override
//                public void run() {
//                    LogUtils.info("hello " + Thread.currentThread().getName());
//                }
//            });
//        }
    }
}
