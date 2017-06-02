package org.xyc.showsome.pea;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CCC on 2016/3/22.
 */
public class InstanceAndAssignPea {

    public static void main(String[] args) {
        ArrayList list = new ArrayList();
        System.out.println(list instanceof List);
        System.out.println(list.getClass().isAssignableFrom(List.class));
        System.out.println(List.class.isAssignableFrom(list.getClass()));
        System.out.println();
    }
}
