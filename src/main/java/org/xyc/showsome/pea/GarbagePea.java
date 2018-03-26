package org.xyc.showsome.pea;

/**
 * created by wks on date: 2018/2/6
 *
 * jvm args;
 * -Xms20M -Xmx20M -verbose:gc -XX:+PrintGCDetails -XX:+PrintGCDateStamps
 */
public class GarbagePea {

    private class NewObject {

        private byte[] bytes = new byte[2 * 1024 * 1024];   //2m

    }

    private void generate() {
        new NewObject();
    }

    public static void main(String[] args) {
        GarbagePea garbagePea = new GarbagePea();
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            garbagePea.generate();
        }
    }
}
