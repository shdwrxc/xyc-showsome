package org.xyc.showsome.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class FastjsonUtils {

    public static String toJsonString(Object obj) {
        return JSON.toJSONString(obj, SerializerFeature.DisableCircularReferenceDetect);
    }

    public static <T> T parseObject(String text, Class<T> clazz) {
        return JSON.parseObject(text, clazz);
    }
}
