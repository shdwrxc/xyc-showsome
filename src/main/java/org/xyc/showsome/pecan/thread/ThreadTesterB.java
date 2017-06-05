package org.xyc.showsome.pecan.thread;

public class ThreadTesterB implements Runnable {

    private int i;

    public void run() {
        while (i <= 10) {
            System.out.print("i = " + i + " ");
            i++;
        }
        System.out.println();
    }
}
