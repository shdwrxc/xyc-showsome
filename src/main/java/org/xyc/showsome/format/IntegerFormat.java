package org.xyc.showsome.format;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.concurrent.atomic.AtomicInteger;

public class IntegerFormat {

    private static final AtomicInteger process = new AtomicInteger();

    private static final String STR_FORMAT = "00000";

    private static DecimalFormat df = new DecimalFormat(STR_FORMAT);

    //slower,3500ms
    public static void f1() {
        long l1 = System.currentTimeMillis();
        for (int i = 0; i < 9999999; i++) {
//            System.out.println(String.format("%06d", process.getAndIncrement()));
            String.format("%06d", process.getAndIncrement());
        }
        System.out.println(System.currentTimeMillis() - l1);
    }

    //faster,1300ms
    public static void f2() {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        // 设置最大整数位数
        nf.setMaximumIntegerDigits(5);
        // 设置最小整数位数
        nf.setMinimumIntegerDigits(5);
        long l1 = System.currentTimeMillis();
        for (int i = 0; i < 9999999; i++) {
//            System.out.println(nf.format(i));
            nf.format(i);
        }
        System.out.println(System.currentTimeMillis() - l1);
    }

    public static void f3() {

        long l1 = System.currentTimeMillis();

        for (int i = 0; i < 9999999; i++) {
//            System.out.println(df.format(i));
            DecimalFormat df = new DecimalFormat(STR_FORMAT);
            df.format(i);
        }
        System.out.println(System.currentTimeMillis() - l1);
    }

    public static void main(String[] args) {
//        f1();
        f2();
        f3();
    }
}
