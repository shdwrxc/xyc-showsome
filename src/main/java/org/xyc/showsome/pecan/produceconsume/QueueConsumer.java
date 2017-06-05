package org.xyc.showsome.pecan.produceconsume;

import java.util.concurrent.BlockingQueue;

/**
 * Created by bugu on 2016/1/25.
 */
public class QueueConsumer implements Runnable {

    BlockingQueue<String> queue;

    public QueueConsumer(BlockingQueue<String> queue) {
        this.queue = queue;
    }

    public void run() {
        try {
            System.out.println(queue.take());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
