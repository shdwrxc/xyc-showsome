package org.xyc.showsome.corn;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.xyc.showsome.pecan.produceconsume.QueueConsumer;
import org.xyc.showsome.pecan.produceconsume.QueueProducer;
import org.xyc.showsome.util.ExpiringMap;

/**
 * created by wks on date: 2019/5/10
 */
public class ExpiringCacheRun {

    private Random random = new Random();

    private String generateString(int n) {
        String val = "";
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            String str = random.nextInt(2) % 2 == 0 ? "num" : "char";
            if ("char".equalsIgnoreCase(str)) { // 产生字母
                int nextInt = random.nextInt(2) % 2 == 0 ? 65 : 97;
                // System.out.println(nextInt + "!!!!"); 1,0,1,1,1,0,0
                val += (char) (nextInt + random.nextInt(26));
            } else if ("num".equalsIgnoreCase(str)) { // 产生数字
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }

    private void runNow() {
        Cache<String, Integer> cache = CacheBuilder.newBuilder().expireAfterWrite(60, TimeUnit.SECONDS).build();

        BlockingQueue<String> queue = new ArrayBlockingQueue<String>(1000);
        QueueProducer producer = new QueueProducer(queue, cache);
        QueueConsumer consumer = new QueueConsumer(queue, cache);

        for (int i = 0; i < 100; i++) {
            new Thread(producer).start();
            new Thread(consumer).start();
        }
    }

    public static void main(String[] args) {
        ExpiringCacheRun run = new ExpiringCacheRun();
        run.runNow();
    }

    private class QueueConsumer implements Runnable {

        BlockingQueue<String> queue;
        Cache<String, Integer> cache;

        public QueueConsumer(BlockingQueue<String> queue, Cache<String, Integer> cache) {
            this.queue = queue;
            this.cache = cache;
        }

        public void run() {
            try {
                while (true) {
                    String str = queue.take();
                    System.out.println("----------" + cache.getIfPresent(str));
//                    Thread.sleep(1500);
//                    System.out.println("1111111111" + cache.getIfPresent(str));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class QueueProducer implements Runnable {

        BlockingQueue<String> queue;
        Cache<String, Integer> cache;

        public QueueProducer(BlockingQueue queue, Cache<String, Integer> cache) {
            this.queue = queue;
            this.cache = cache;
        }

        public void run() {
            try {
                while (true) {
                    String str = generateString(100);
                    cache.put(str, random.nextInt(100));
                    queue.put(str);
                    System.out.println("++++++++++" + str);
                    Thread.sleep(10);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
