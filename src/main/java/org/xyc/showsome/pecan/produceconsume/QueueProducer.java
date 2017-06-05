package org.xyc.showsome.pecan.produceconsume;

import java.util.concurrent.BlockingQueue;

/**
 * Created by bugu on 2016/1/25.
 */
public class QueueProducer implements Runnable {

    BlockingQueue<String> queue;

    public QueueProducer(BlockingQueue queue) {
        this.queue = queue;
    }

    public void run() {
        try {
            System.out.println("Produce " + Thread.currentThread().getName());
            queue.put(Thread.currentThread().getName() + " Consumer");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
