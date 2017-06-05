package org.xyc.showsome.pecan.thread;

public class ThreadTester {

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(new ThreadTesterA());
        Thread t2 = new Thread(new ThreadTesterB());
        t1.start();
        t1.join(2500); // wait t1 to be finished
        t1.interrupt();
        t2.start();
        t2.join(); // in this program, this may be removed
    }
}
