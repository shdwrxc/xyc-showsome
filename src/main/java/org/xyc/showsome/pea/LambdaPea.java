package org.xyc.showsome.pea;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

/**
 * created by wks on date: 2017/7/7
 */
public class LambdaPea {

    /**
     * (params) -> expression
     * (params) -> statement
     * (params) -> { statements }
     * 例如，如果你的方法不对参数进行修改、重写，只是在控制台打印点东西的话，那么可以这样写：
     * () -> System.out.println("Hello Lambda Expressions");
     * 如果你的方法接收两个参数，那么可以写成如下这样：
     * (int even, int odd) -> even + odd
     * 通常都会把lambda表达式内部变量的名字起得短一些。这样能使代码更简短，放在同一行。所以，在上述代码中，变量名选用a、b或者x、y会比even、odd要好
     */
    public static void shadowClaw1() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("hello java 7");
            }
        }).start();

        new Thread(() -> System.out.println("hello java 8")).start();
    }

    public static void shadowClaw2() {
        List<Integer> list = Lists.newArrayList(5, 15, 10);
        List<Integer> list2 = Lists.newArrayList(list);
        System.out.println(list);
        Collections.sort(list, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2 - o1;
            }
        });
        System.out.println(list);

        System.out.println(list2);
        //        Collections.sort(list2, (Integer o1, Integer o2) -> o2 - o1);
        Collections.sort(list2, (o1, o2) -> o2 - o1);
        //        Collections.sort(list2::innerMethod of <T>, here is ArrayList);
        //        Comparator<Integer> comparator = (o1, o2) -> o2 - o1;
        //        Collections.sort(list2, comparator.reversed());
        //        Collections.sort(list2, (o1, o2) -> {
        //            if (o1 == 0)
        //                return 0;
        //            if (o2 == 0)
        //                return 0;
        //            return o2 - o1;
        //        });
        System.out.println(list2);
    }

    public static void shadowClaw3() {
        List<String> list = Lists.newArrayList("hello", "world", "songjiang");
        for (String str : list)
            System.out.println(str);

        list.forEach(n -> System.out.println(n));

        list.forEach(System.out::println);
    }

    public static void shadowClaw4() {
        //        List<String> languages = Arrays.asList("Java", "Scala", "C++", "Haskell", "Lisp");
        List<String> languages = Lists.newArrayList("Java", "Scala", "C++", "Haskell", "Lisp");

        System.out.println("Languages which starts with J :");
        filter(languages, (str) -> str.startsWith("J"));

        System.out.println("Languages which ends with a ");
        filter(languages, (str) -> str.endsWith("a"));

        System.out.println("Print all languages :");
        filter(languages, (str) -> true);

        System.out.println("Print no language : ");
        filter(languages, (str) -> false);

        System.out.println("Print language whose length greater than 4:");
        filter(languages, (str) -> str.length() > 4);

        System.out.println("Print other");
        moreFilter(languages);
    }

    public static void filter(List<String> names, Predicate<String> condition) {
        //        for (String name : names) {
        //            if (condition.test(name)) {
        //                System.out.println(name + " ");
        //            }
        //        }
        names.stream().filter(name -> condition.test(name)).forEach(System.out::println);
    }

    public static void moreFilter(List<String> names) {
        // 可以用and()、or()和xor()逻辑函数来合并Predicate，
        // 例如要找到所有以J开始，长度为四个字母的名字，你可以合并两个Predicate并传入
        Predicate<String> startsWithJ = (n) -> n.startsWith("J");
        Predicate<String> fourLetterLong = (n) -> n.length() == 4;
        names.stream().filter(startsWithJ.and(fourLetterLong)).forEach((n) -> System.out.print("which starts with 'J' and four letter long is : " + n));
    }

    public static void shadowClaw5() {
        // 不使用lambda表达式为每个订单加上12%的税
        List<Integer> costBeforeTax = Arrays.asList(100, 200, 300, 400, 500);
        for (Integer cost : costBeforeTax) {
            double price = cost + .12 * cost;
            System.out.println(price);
        }

        // 使用lambda表达式
        List<Integer> costBeforeTax1 = Arrays.asList(100, 200, 300, 400, 500);
        costBeforeTax1.stream().map((cost) -> cost + .12 * cost).forEach(System.out::println);
    }

    public static void shadowClaw6() {
        // 为每个订单加上12%的税
        // 老方法：
        List<Integer> costBeforeTax = Arrays.asList(100, 200, 300, 400, 500);
        double total = 0;
        for (Integer cost : costBeforeTax) {
            double price = cost + .12 * cost;
            total = total + price;
        }
        System.out.println("Total : " + total);

        // 新方法：
        List<Integer> costBeforeTax1 = Arrays.asList(100, 200, 300, 400, 500);
        //        double bill = costBeforeTax1.stream().map((cost) -> cost + .12*cost).reduce((sum, cost) -> sum + cost).get();
        double bill = costBeforeTax1.stream().map((cost) -> cost + .12 * cost).reduce((a, b) -> a + b).get();
        System.out.println("Total : " + bill);
    }

    public static void shadowClaw7() {
        // 创建一个字符串列表，每个字符串长度大于2
        List<String> strList = Lists.newArrayList("abc", "ab", "a", "txa", "tx", "tyo");
        List<String> filtered = strList.stream().filter(x -> x.length()> 2).collect(Collectors.toList());
        System.out.printf("Original List : %s, filtered list : %s %n", strList, filtered);
    }

    /**
     * 通常需要对列表的每个元素使用某个函数，例如逐一乘以某个数、除以某个数或者做其它操作。这些操作都很适合用 map() 方法
     */
    public static void shadowClaw8() {
        // 将字符串换成大写并用逗号链接起来
        List<String> G7 = Arrays.asList("USA", "Japan", "France", "Germany", "Italy", "U.K.","Canada");
        String G7Countries = G7.stream().map(x -> x.toUpperCase()).collect(Collectors.joining(", "));
        System.out.println(G7Countries);
    }

    /**
     * 利用流的 distinct() 方法来对集合进行去重
     */
    public static void shadowClaw9() {
        // 用所有不同的数字创建一个正方形列表
        List<Integer> numbers = Arrays.asList(9, 10, 3, 4, 7, 3, 4);
        List<Integer> distinct = numbers.stream().map( i -> i*i).distinct().collect(Collectors.toList());
        System.out.printf("Original List : %s,  Square Without duplicates : %s %n", numbers, distinct);
    }

    public static void shadowClaw10() {
        //获取数字的个数、最小值、最大值、总和以及平均值
        List<Integer> primes = Arrays.asList(2, 3, 5, 7, 11, 13, 17, 19, 23, 29);
        IntSummaryStatistics stats = primes.stream().mapToInt((x) -> x).summaryStatistics();
        System.out.println("Highest prime number in List : " + stats.getMax());
        System.out.println("Lowest prime number in List : " + stats.getMin());
        System.out.println("Sum of all prime numbers : " + stats.getSum());
        System.out.println("Average of all prime numbers : " + stats.getAverage());
    }

    public static void main(String[] args) {
        shadowClaw9();
    }
}
