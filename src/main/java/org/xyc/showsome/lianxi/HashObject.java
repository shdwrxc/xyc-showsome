package org.xyc.showsome.lianxi;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by CCC on 2016/5/23.
 */
public class HashObject {

    private Set<Integer> serialList = new HashSet<Integer>();
    private String a;
    private String b;

    @Override
    public int hashCode() {
        return 31 * a.hashCode() + 31 * b.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof HashObject))
            return false;
        HashObject hashObject = (HashObject)obj;
        hashObject.serialList.addAll(serialList);
        if (hashObject.a.equals(a) && hashObject.b.equals(b)) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        HashObject h1 = new HashObject();
        h1.serialList.add(10001);
        h1.a = "a";
        h1.b = "b";
        HashObject h2 = new HashObject();
        h2.serialList.add(20001);
        h2.a = "a";
        h2.b = "b";

        Set<HashObject> set = new HashSet<HashObject>();
        set.add(h1);
        set.add(h2);

        System.out.println("abc");
    }
}
