package org.xyc.showsome.pecan.sort;

/**
 * Created by bugu on 2016/1/25.
 *
 * 冒泡排序
 */
public class MyMaoPaoSort {

    public static void main(String[] args) {
        int[] a = {1, 3, 76, 21, 23, 54, 2};
//        int[] a = {57, 68, 59, 52};

        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a.length - i - 1; j++) {
                if (a[j] > a[j+1]) {
                    int temp = a[j+1];
                    a[j+1] = a[j];
                    a[j] = temp;
                }
            }
        }

        for (int i : a) {
            System.out.println(i);
        }
    }
}
