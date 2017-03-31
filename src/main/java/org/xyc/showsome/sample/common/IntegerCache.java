package org.xyc.showsome.sample.common;

import java.lang.reflect.Field;

/**
 * Created by IntelliJ IDEA.
 * Author: wks
 * Date: 2016/12/2
 *
 * Integer从-128到127是放在缓存里的，不会重新创建对象。由于这一特性，改变缓存的值，可以使得计算不正确
 */
public class IntegerCache {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {

        Class cache = Integer.class.getDeclaredClasses()[0]; //1
        Field myCache = cache.getDeclaredField("cache"); //2
        myCache.setAccessible(true);//3

        Integer[] newCache = (Integer[]) myCache.get(cache); //4
        newCache[132] = newCache[133]; //5

        int a = 2;
        int b = a + a;
        System.out.printf("%d + %d = %d", a, a, b); //
    }
}
