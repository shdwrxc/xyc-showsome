package org.xyc.showsome.pea;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * created by wks on date: 2017/12/8
 */
public class ScheduledExecutorPea {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledExecutorPea.class);

    private static ScheduledExecutorService ses = Executors.newScheduledThreadPool(10);

    /**
     * 延迟执行，延迟5秒执行打印语句
     */
    public static void shadowclaw1() {
        ses.schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println("hello delay");
            }
        }, 5, TimeUnit.SECONDS);

        ses.schedule(()-> System.out.println("hello lambda delay"), 5, TimeUnit.SECONDS);
    }

    /**
     * 固定的按照某个频率执行
     * 不管要做的任务要花费多少时间，固定的隔5秒钟就重新做一次
     */
    public static void shadowclaw2() {
        ses.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                logger.info("start");
                try {
                    Thread.sleep(3999);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                logger.info("end");
            }
        }, 1, 5, TimeUnit.SECONDS);
    }

    /**
     * 做完之后等待延迟再执行
     * 如果任务要做一个小时，就先做一个小时，然后等待5秒钟，在继续循环
     */
    public static void shadowclaw3() {
        ses.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                logger.info("start");
                try {
                    Thread.sleep(3999);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                logger.info("end");
            }
        }, 1, 5, TimeUnit.SECONDS);
    }

    public static void main(String[] args) {
        shadowclaw3();
    }
}
