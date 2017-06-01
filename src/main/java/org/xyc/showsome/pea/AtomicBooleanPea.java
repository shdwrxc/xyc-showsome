package org.xyc.showsome.pea;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * created by wks on date: 2017/1/5
 */
public class AtomicBooleanPea {

    private static void walk1() {
        AtomicBoolean ab = new AtomicBoolean();
        ab.set(true);
        //先比较，然后赋值
        boolean b = ab.compareAndSet(true, false);
        System.out.println(b);
        System.out.println(ab.get());
    }

    public static void main(String[] args) {
        walk1();
    }
}
