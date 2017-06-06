package org.xyc.showsome.pecan.sort;

import java.util.Random;

/**
 * 快速排序
 */
public class QuickSort {

    public static void main(String[] args) {
        // int[] iArgs = new int[]{72,6,57,88,60,42,83,73,48,85};
        int iLength = 5;
        int[] iArgs = new int[iLength];
        for(int i = 0; i < iLength; i++){
            Random objRandom = new Random();
            iArgs[i] = objRandom.nextInt(100);
        }
        int left = 0; //数组第一个位置
        int right = iArgs.length - 1;//数组最后一个位置;
        QuickSort quickSort = new QuickSort();
        //快速排序
        quickSort.recursive(iArgs,left,right);

        for(int i = 0; i < iArgs.length; i++) {
            System.out.print(iArgs[i] + " ");
        }
    }

    /**
     * 递归循环数据
     *
     * @param args 数组
     * @param left 数组左下标
     * @param right 数组右下标
     * @return
     */
    private void recursive(int[] args,int left,int right) {
        if( left < right) {
            //数据从left到right坐标的数据进行排序
            int iIndex = qucikSort(args,left,right); //iIndex 是基数放在数据位置

            //递归算法，对于基数左边排序
            recursive(args,left,iIndex-1); //为什么left不能从0
            //递归算法，对于基数右边排序
            recursive(args,iIndex+1,right);//为什么 right不等于length
        }
    }

    /**
     * 确定基数左边的数都比它小，右边的数都比它大
     *
     * @param args 数组
     * @param left 数组左下标
     * @param right 数组右下标
     * @return
     */
    private int qucikSort(int[] args,int left,int right) {
        int iBase = args[left];; //基准数
        while (left < right) {
            //从右向左找出第一个比基准数小的数
            while( left < right && args[right] >= iBase) {
                right--;
            }
            args[left] = args[right];

            //从左向右找出第一个比基准数小的数
            while( left < right && args[left] <= iBase) {
                left++;
            }
            args[right] = args[left];
        }
        args[left]= iBase;
        return left;
    }
}
