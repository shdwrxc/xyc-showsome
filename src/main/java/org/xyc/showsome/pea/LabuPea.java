package org.xyc.showsome.pea;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * created by wks on date: 2017/6/22
 */
public class LabuPea {

    private static final Logger logger = LoggerFactory.getLogger(LabuPea.class);

    private static void shadowclaw1() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2016, 11, 20);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        logger.info(sdf.format(calendar.getTime()));

        long l = System.currentTimeMillis() - calendar.getTimeInMillis();
        int i = (int) (l / 1000 / 60 / 60 / 24);
        logger.info("week:{}, day:{}", i / 7, i % 7);
    }

    private static void shadowclaw2() {
        Calendar start = Calendar.getInstance();
        start.set(2016, 11, 20);

        Calendar end = Calendar.getInstance();
        end.set(2017, 8, 26);

        long l = end.getTimeInMillis() - start.getTimeInMillis();
        logger.info(l / 1000 / 60 / 60 / 24 + "");
        logger.info(l / 1000 / 60 / 60 / 24 / 7 + "");
    }

    public static void main(String[] args) {
        shadowclaw1();
        shadowclaw2();
    }
}
