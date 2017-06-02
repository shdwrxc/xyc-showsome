package org.xyc.showsome.pea;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by CCC on 2016/5/25.
 */
public class ListOperatePea {

    private static final List<String> list = new ArrayList<String>();

    private static ReentrantLock lock = new ReentrantLock();

    public static void op1() {
        System.out.println("start");
        while (list.size() > 0) {
            System.out.println(take() + "," + list.size());
        }
        System.out.println("end");
    }

    private static String take() {
        lock.lock();
        try {
            String s = null;
            Iterator<String> iter = list.iterator();
            if (iter.hasNext()) {
                s = iter.next();
                iter.remove();
            }
            return s;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return null;
    }

    public static void addMember(String str) {
        list.add(str);
    }

    public static void main(String[] args) {
        addMember("1");
        addMember("3");
        addMember("4");
        addMember("6");
        addMember("8");
        op1();
        addMember("2");
    }
}
