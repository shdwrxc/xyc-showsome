package org.xyc.showsome.lianxi;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by CCC on 2016/5/24.
 */
public class CollectionInfo {

    public static void info() throws Exception{
        ArrayBlockingQueue<String> queue1 = new ArrayBlockingQueue<String>(10);
        queue1.put("1");
        queue1.put("1");
        System.out.println(queue1);

        LinkedBlockingQueue<String> queue2 = new LinkedBlockingQueue<String>(10);
        queue2.put("2");
        queue2.put("2");
        System.out.println(queue2);
    }

    public static void iter() {
        Set<String> set = new LinkedHashSet<String>();
//        set.add("1");
//        set.add("2");

        Iterator<String> iterator = set.iterator();
        if (iterator.hasNext())
            System.out.println(iterator.next());
    }

    public static void main(String[] args) throws Exception {
        iter();
    }
}
