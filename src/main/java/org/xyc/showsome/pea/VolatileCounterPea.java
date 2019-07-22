//package org.xyc.showsome.pea;
//
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.TimeUnit;
//
//import org.junit.After;
//import org.junit.Test;
//
///**
// * created by wks on date: 2019/7/3
// */
//public class VolatileCounterPea {
//
//    volatile int i = 0;
//    @Test
//    public void testCounter() throws InterruptedException   {
//        ExecutorService executor = Executors.newFixedThreadPool(2);
//        executor.execute( () -> { addOne();   }  );
//        executor.execute( () -> { addOne();   }  );
//        executor.shutdown();
//        executor.awaitTermination(10, TimeUnit.MINUTES);
//    }
//    @Interleave(group=TestVolatileCounter.class,threadCount=2)
//    public void addOne() {
//        i++;
//    }
//    @After
//    public void checkResult() {
//        assertEquals(2,i);
//    }
//}
