package org.xyc.showsome.lianxi;

/**
 * Created by CCC on 2016/5/26.
 */
public class ExceptionExecute {

    public static void ex1() {
        int i = 1;
        System.out.println("start");
        try {
            int j = i / 0;
        } catch (Exception e) {
            System.out.println("exception " + e.toString());
        } finally {
            System.out.println("finish");
        }
        System.out.println("end");
    }

    public void th1() throws Exception{
        System.out.println("1");
        th2();
        System.out.println("2");
    }

    public void th2() throws Exception {
        int i = 1 / 0;
    }

    public static void main(String[] args) throws Exception {
//        ex1();
        ExceptionExecute ee = new ExceptionExecute();
        ee.th1();
    }
}
