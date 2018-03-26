package org.xyc.showsome.pea;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Locale;

/**
 * created by wks on date: 2018/2/23
 *
 * 符号	位置	本地化？	含义
 0	数字	是	阿拉伯数字
 #	数字字	是	阿拉伯数字，如果不存在则显示为空
 .	数字	是	小数分隔符或货币小数分隔符
 -	数字	是	减号
 ,	数字	是	分组分隔符
 E	数字	是	分隔科学计数法中的尾数和指数。在前缀或后缀中无需加引号。
 ;	子模式边界	是	分隔正数和负数子模式
 %	前缀或后缀	是	乘以 100 并显示为百分数
 /u2030	前缀或后缀	是	乘以 1000 并显示为千分数
 ¤(/u00A4)	前缀或后缀	否	货币记号，由货币符号替换。如果两个同时出现，则用国际货币符号替换。如果出现在某个模式中，则使用货币小数分隔符，而不使用小数分隔符。
 '	前缀或后缀	否	用于在前缀或或后缀中为特殊字符加引号，例如 "'#'#" 将 123 格式化为 "#123"。要创建单引号本身，请连续使用两个单引号："# o''clock"。
 *
 *
 * 如果使用具有多个分组字符的模式，则最后一个分隔符和整数结尾之间的间隔才是使用的分组大小。所以 "#,##,###,####" == "######,####" == "##,####,####"。
 */
public class DecimalFormatPea {

    private static void shadowclaw1() {
        DecimalFormat format = new DecimalFormat();
        System.out.println(format.format(12.356789));   //print 12.357
        format.setRoundingMode(RoundingMode.DOWN);
        System.out.println(format.format(12.356789));

        DecimalFormat df1 = new DecimalFormat("0.0");
        DecimalFormat df2 = new DecimalFormat("#.#");
        DecimalFormat df3 = new DecimalFormat("000.000");
        DecimalFormat df4 = new DecimalFormat("###.###");

        System.out.println(df1.format(12.356789));
        System.out.println(df2.format(12.356789));
        System.out.println(df3.format(12.356789));
        System.out.println(df4.format(12.356789));
        System.out.println(df4.format(12.35));

        DecimalFormat df5 = new DecimalFormat("###,####.000");
        System.out.println(df5.format(111111123456.1227222));

        Locale.setDefault(Locale.US);
        DecimalFormat usFormat = new DecimalFormat("###,###.000");
        System.out.println(usFormat.format(111111123456.1227222));

        DecimalFormat addPattenFormat = new DecimalFormat();
        addPattenFormat.applyPattern("##,###.000");
        System.out.println(addPattenFormat.format(111111123456.1227));

        DecimalFormat zhiFormat = new DecimalFormat();
        zhiFormat.applyPattern("0.000E0000");
        System.out.println(zhiFormat.format(10000));
        System.out.println(zhiFormat.format(12345678.345));

        DecimalFormat percentFormat = new DecimalFormat();
        percentFormat.applyPattern("#0.000%");
        System.out.println(percentFormat.format(0.3052222));
    }

    public static void main(String[] args) {
        shadowclaw1();
    }
}
