package org.xyc.showsome.util;

import java.util.HashMap;
import java.util.Map;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;

/**
 * created by wks on date: 2018/4/2
 */
public class EhcacheUtils {

    private static class CacheInstance {
        private static Cache cache = new Cache(new CacheConfiguration("NotDuplication", 999)
                .timeToLiveSeconds(5));

        static {
            CacheManager cm = CacheManager.getInstance();
            cm.addCache(cache);
        }
    }

    /**
     *
     * @param key   null is nothing
     * @param value
     */
    public static void put(String key, Object value) {
        CacheInstance.cache.put(new Element(key, value));
    }

    public static Object get(String key) {
        Element e = CacheInstance.cache.get(key);
        if (e != null) {
            return e.getValue();
        }
        return null;
    }

    private static String convertMapKey(Map<String, String> map) {
        StringBuilder sb = new StringBuilder("getSerialBySerialId");
        if (map == null || map.size() == 0) {
            return sb.toString();
        }
        for (Map.Entry<String, String> entry : map.entrySet()) {
            sb.append(entry.getKey()).append(entry.getValue());
        }
        return sb.toString();
    }

    public static void main(String[] args) {

//        put("1", 2);
//
//        while (true) {
//            System.out.println((int)get("1"));
//            try {
//                Thread.sleep(1500);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }

        Map<String, String> map = new HashMap<>();
        map.put("serialNo", "12345668");
        map.put("cliamNo", "abcdefgh123");
        map.put("accidentNo", "No12321kdskfs");
        map.put(null, "123");
        map.put(null, "456");

        System.out.println(convertMapKey(map));

    }
}
