package org.xyc.showsome.pecan.produceconsume;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by bugu on 2016/1/25.
 */
public class QueueMain {

    public static void main(String[] args) {
        BlockingQueue<String> queue = new ArrayBlockingQueue<String>(2);
        QueueProducer producer = new QueueProducer(queue);
        QueueConsumer consumer = new QueueConsumer(queue);

        for (int i = 0; i < 5; i++) {
            new Thread(producer).start();
            new Thread(consumer).start();
        }
    }
}
