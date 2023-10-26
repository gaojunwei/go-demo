package com.gjw.common.result.cache;

import java.util.concurrent.ConcurrentHashMap;

public class MapCache {
    private static final ConcurrentHashMap<String,Object> cacheMap = new ConcurrentHashMap<>();

    public static void put(String key,Object value){
        cacheMap.put(key,value);
    }

    public static <T> T get(String key){
        return (T)cacheMap.get(key);
    }

    public static void remove(String key){
        cacheMap.remove(key);
    }
}
