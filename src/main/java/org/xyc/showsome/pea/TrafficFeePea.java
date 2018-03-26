package org.xyc.showsome.pea;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * created by wks on date: 2018/2/23
 */
public class TrafficFeePea {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private abstract class Cal {

        public BigDecimal payMonth() {
            Calendar calendar = getMonth();
            int month = calendar.get(Calendar.MONTH);
            double amount = 0;
            while (month == calendar.get(Calendar.MONTH)) {
                int weekday = calendar.get(Calendar.DAY_OF_WEEK);
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                if (weekday == Calendar.SATURDAY || weekday == Calendar.SUNDAY)
                    continue;
                amount = pay(amount);
            }
            BigDecimal payMonth = new BigDecimal(amount).setScale(2, BigDecimal.ROUND_HALF_EVEN);
            return payMonth;
        }

        public abstract double pay(double amount);
    }

    private class PayUsingCard extends Cal {

        @Override
        public double pay(double amount) {
            amount += payMetro(amount);
            amount += payMetro(amount);
            return amount;
        }
    }

    private class PayUsingApp extends Cal {

        @Override
        public double pay(double amount) {
            amount += payMetro2();
            amount += payMetro2();
            amount += payBus2();
            return amount;
        }
    }

    /**
     * user traffic card
     * 每月累积满70元，可打9折
     */
    public BigDecimal shadowclaw1() {
        PayUsingCard card = new PayUsingCard();
        return card.payMonth();
    }

    /**
     * use app
     */
    public BigDecimal shadowclaw2() {
        PayUsingApp app = new PayUsingApp();
        return app.payMonth();
    }

    /**
     * 每月累积满70元，可打9折
     * @param amount
     * @return
     */
    private static double payMetro(double amount) {
        return amount + 6 < 70 ? 6 : 6 * 0.9;
    }

    private static double payMetro2() {
        return 6 * 0.8;
    }

    private static double payBus2() {
        return 1;
    }

    private static Calendar getMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        //0:January，11:December
        calendar.set(Calendar.MONTH, 2);
        System.out.println(sdf.format(calendar.getTime()));
        return calendar;
    }

    public static void main(String[] args) {
        TrafficFeePea traffic = new TrafficFeePea();
        System.out.println(traffic.shadowclaw1());
        System.out.println(traffic.shadowclaw2());
    }
}
