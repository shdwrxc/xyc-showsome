package org.xyc.showsome.pea;

import java.math.BigDecimal;

/**
 * created by wks on date: 2018/2/27
 */
public class FundPea {

    private static final double base = 80000;
    private static final double days = 365;
    private static final double days_in_year = 365;

    public static BigDecimal shadowclaw1(double rate) {
        double result = base * rate * (days / days_in_year);
        return new BigDecimal(result).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal shadowclaw2(double rate, int period) {
        int interval = 7;
        double result = 0;
        double remain = days;
        double actual = period;
        for (;;) {
            if (remain <= 0)
                break;
            if (remain < period)
                actual = remain;
            result += base * rate * (actual / days_in_year);
            remain = remain - period - interval;
        }
        return new BigDecimal(result).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public static void main(String[] args) {
        System.out.println(shadowclaw1(0.046));
        System.out.println(shadowclaw2(0.054, 63));
        System.out.println(shadowclaw2(0.0535, 91));
    }
}
