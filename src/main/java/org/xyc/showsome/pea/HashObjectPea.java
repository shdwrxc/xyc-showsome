package org.xyc.showsome.pea;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by CCC on 2016/5/23.
 */
public class HashObjectPea {

    private Set<Integer> serialList = new HashSet<Integer>();
    private String a;
    private String b;

    @Override
    public int hashCode() {
        return 31 * a.hashCode() + 31 * b.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof HashObjectPea))
            return false;
        HashObjectPea hashObject = (HashObjectPea)obj;
        hashObject.serialList.addAll(serialList);
        if (hashObject.a.equals(a) && hashObject.b.equals(b)) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        HashObjectPea h1 = new HashObjectPea();
        h1.serialList.add(10001);
        h1.a = "a";
        h1.b = "b";
        HashObjectPea h2 = new HashObjectPea();
        h2.serialList.add(20001);
        h2.a = "a";
        h2.b = "b";

        Set<HashObjectPea> set = new HashSet<HashObjectPea>();
        set.add(h1);
        set.add(h2);

        System.out.println("abc");
    }
}
