package org.xyc.showsome.pecan.sort;

/**
 * Created by bugu on 2016/1/25.
 *
 * 二分排序
 */
public class MyErFenSort {

    public static void main(String[] args) {
        int[] a = {12, 34, 4, 78, 54, 89, 16};

        for (int i = 0; i < a.length; i++) {
            int temp = a[i];
            int left = 0;
            int right = i - 1;
            int mid = 0;
            while (left <= right) {
                mid = (left + right) / 2;
                if (a[i] > a[mid])
                    left = mid + 1;
                else
                    right = mid - 1;
            }

            for (int j = i; j > left; j--) {
                a[j] = a[j - 1];
            }

            a[left] = temp;
        }

        for (int i : a) {
            System.out.println(i);
        }
    }
}
