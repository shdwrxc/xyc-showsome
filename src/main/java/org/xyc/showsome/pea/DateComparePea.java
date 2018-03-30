package org.xyc.showsome.pea;

import java.text.SimpleDateFormat;

/**
 * created by wks on date: 2018/3/28
 */
public class DateComparePea {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static int compareByString(String str1, String str2) {
        return str1.compareTo(str2);
    }

    private static int compareByDate(String str1, String str2) throws Exception {
        return sdf.parse(str1).compareTo(sdf.parse(str2));
    }

    private static boolean compareByDateBefore(String str1, String str2) throws Exception {
        return sdf.parse(str1).after(sdf.parse(str2));
    }

    private static void compare() throws Exception {
        String str1 = "2018-08-08 15:50:16";
        String str2 = "2018-08-06 15:50:16";

        System.out.println(compareByString(str1, str2));
        System.out.println(compareByDate(str1, str2));
        System.out.println(compareByDateBefore(str1, str2));
    }

    private static void comparePerformance() throws Exception {
        int loop = 99999;
        String str1 = "2018-08-08 15:50:16";
        String str2 = "2018-08-06 15:50:16";

        long l = System.currentTimeMillis();
        for (int i = 0; i < loop; i++) {
            compareByString(str1, str2);
        }
        System.out.println(System.currentTimeMillis() - l);
        l = System.currentTimeMillis();
        for (int i = 0; i < loop; i++) {
            compareByDate(str1, str2);
        }
        System.out.println(System.currentTimeMillis() - l);
        l = System.currentTimeMillis();
        for (int i = 0; i < loop; i++) {
            compareByDateBefore(str1, str2);
        }
        System.out.println(System.currentTimeMillis() - l);
    }

    public static void main(String[] args) throws Exception {
        comparePerformance();
        compare();
    }
}
