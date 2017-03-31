package org.xyc.showsome.lianxi;

/**
 * Created by CCC on 2016/5/25.
 */
public class PlusSequence {

    public static void plus1() {
        int i = 0;
        System.out.println(1 + i != 0 ? 1 : 2);
        System.out.println(1 + (i != 0 ? 1 : 2));
    }

    public static void main(String[] args) {
        plus1();
    }
}
