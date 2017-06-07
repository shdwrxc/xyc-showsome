package org.xyc.showsome.pecan.thread;

/**
 * 线程测试
 */
public class ThreadA extends Thread {

    int count = 0;

    public void run() {
        System.out.println(getName() + "将要运行...");
        while (!this.isInterrupted()) {
            System.out.println(getName() + "运行中" + count++);
            try {
                Thread.sleep(40000);
            } catch (InterruptedException e) {
                System.out.println(getName() + "从阻塞中退出...");
                System.out.println("ex before this.isInterrupted()=" + this.isInterrupted());
                Thread.currentThread().interrupt();
                System.out.println("ex after this.isInterrupted()=" + this.isInterrupted());
            }
        }
        System.out.println(getName() + "已经终止!");
    }

    public static void main(String[] args) {
        try {
            ThreadA ta = new ThreadA();
            ta.setName("ThreadA");
            ta.start();
            Thread.sleep(2000);
            System.out.println(ta.getName() + "正在被中断...");
            System.out.println("before ta.isInterrupted()=" + ta.isInterrupted());
            ta.interrupt();
            System.out.println("after ta.isInterrupted()=" + ta.isInterrupted());
//            Thread.sleep(2000);
//            ta.interrupt();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
