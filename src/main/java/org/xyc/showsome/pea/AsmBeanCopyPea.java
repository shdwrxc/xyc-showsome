package org.xyc.showsome.pea;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.google.common.collect.Lists;
import net.sf.cglib.beans.BeanCopier;

/**
 * created by wks on date: 2018/3/27
 */
public class AsmBeanCopyPea {

    public void copy() {

        BeanCopier beanCopier = BeanCopier.create(Table.class, Chair.class, false);

        ExecutorService es = Executors.newFixedThreadPool(100);

        Random random = new Random();

        int loop = 10000000;

        List<Future> list = Lists.newArrayList();

        for (int i = 0; i < loop; i++) {
            list.add(es.submit(new Runnable() {
                @Override
                public void run() {
                    int i = random.nextInt(1000);
                    Table table = new Table();
                    table.setA(i);
                    table.setB(i + "");
                    Chair chair = new Chair();
                    beanCopier.copy(table, chair, null);
                    //                    System.out.println(chair.toString());
                    if (!String.valueOf(chair.getA()).equals(chair.getB())) {
                        System.out.println("------------------------------------find one." + chair.toString());
                    }
                }
            }));
        }

        for (Future future : list) {
            try {
                future.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        es.shutdown();
    }

    public static void main(String[] args) {
        AsmBeanCopyPea beanCopy = new AsmBeanCopyPea();
        beanCopy.copy();
    }

    private class Table {
        private int a;

        private String b;

        public int getA() {
            return a;
        }

        public void setA(int a) {
            this.a = a;
        }

        public String getB() {
            return b;
        }

        public void setB(String b) {
            this.b = b;
        }
    }

    private class Chair {
        private int a;

        private String b;

        public int getA() {
            return a;
        }

        public void setA(int a) {
            this.a = a;
        }

        public String getB() {
            return b;
        }

        public void setB(String b) {
            this.b = b;
        }

        @Override
        public String toString() {
            return "Chair{" +
                    "a=" + a +
                    ", b='" + b + '\'' +
                    '}';
        }
    }
}
