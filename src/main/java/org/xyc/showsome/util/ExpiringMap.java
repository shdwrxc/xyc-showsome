package org.xyc.showsome.util;

import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

/**
 * Usage example
 * ExpiringMap map = ExpiringMap.build(5);  //timeout after 5 seconds
 * ExpiringMap map = ExpiringMap.build();  //default timeout after 10 minutes
 * map.put(key, value);
 * map.get(key)
 *
 * @param <V>
 */
public class ExpiringMap<V> {

    public final static int timeout = 600;

    private Cache<String, V> cache;

    private ExpiringMap(int timeout) {
        cache = CacheBuilder.newBuilder().expireAfterWrite(timeout, TimeUnit.SECONDS).build();
    }

    public static ExpiringMap build() {
        return build(timeout);
    }

    public static ExpiringMap build(int timeout) {
        return new ExpiringMap(timeout);
    }

    public void put(String key, V value) {
        cache.put(key, value);
    }

    public V get(String key) {
        return cache.getIfPresent(key);
    }
}
