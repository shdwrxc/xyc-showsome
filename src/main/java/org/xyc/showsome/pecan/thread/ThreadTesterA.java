package org.xyc.showsome.pecan.thread;

public class ThreadTesterA implements Runnable {

    private int counter;

    public void run() {
        while (counter <= 10) {
            System.out.print("Counter = " + counter + " ");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            counter++;
        }
        System.out.println();
    }
}
