package org.xyc.showsome.sample.common;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by IntelliJ IDEA.
 * Date: 2017/1/5
 */
public class AtomicSample {

    private static void booleanSample1() {
        AtomicBoolean ab = new AtomicBoolean();
        ab.set(true);
        boolean b = ab.compareAndSet(false, true);
        System.out.println(b);
        System.out.println(ab.get());
    }

    public static void main(String[] args) {
        booleanSample1();
    }
}
