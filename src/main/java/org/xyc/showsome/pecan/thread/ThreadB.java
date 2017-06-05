package org.xyc.showsome.pecan.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ThreadB {

    public Integer test(BlockingQueue<Integer> queue) {
        boolean interrupt = false;
        try {
            while (true) {
                try {
                    return queue.take();
                } catch (InterruptedException e) {
                    interrupt = true;
                }
            }
        } finally {
            if (interrupt) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        ThreadB threadB = new ThreadB();
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<Integer>(10);
        threadB.test(queue);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("end");
        queue.notifyAll();
    }
}
