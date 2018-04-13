package org.xyc.showsome.pecan.copy;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import net.sf.cglib.beans.BeanCopier;
import net.sf.cglib.core.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * created by wks on date: 2018/4/13
 */
public class BeanCopyUtils {

    private static final Logger logger = LoggerFactory.getLogger(BeanCopyUtils.class);

    private static final Map<String, BeanCopier> classCopier = Maps.newHashMap();

    public static <T> T copy(Object source, Class<T> targetType) {
        if (source == null || targetType == null) {
            return null;
        }
        T t = null;
        try {
            t = targetType.newInstance();
            getCopier(source.getClass(), targetType).copy(source, t, new Converter() {
                @Override
                public Object convert(Object value, Class target, Object context) {
                    //value = value, target = value's type, context = setValue method string
                    return copy(value);
                }

                private Object copy(Object value) {
                    if (value == null) {
                        return null;
                    }
                    Object obj = copyPrimitive(value);
                    if (obj != null) {
                        return obj;
                    }
                    Class clazz = value.getClass();
                    try {
                        if (clazz.isArray()) {
                            int length = Array.getLength(value);
                            obj = Array.newInstance(clazz.getComponentType(), length);
                            for (int i = 0; i < length; i++) {
                                Array.set(obj, i, copy(Array.get(value, i)));
                            }
                        } else {
                            obj = clazz.newInstance();
                            if (value instanceof Collection) {
                                for (Object o : (Collection) value) {
                                    ((Collection) obj).add(copy(o));
                                }
                            } else if (value instanceof Map) {
                                for (Map.Entry entry : (Set<Map.Entry>) ((Map) value).entrySet()) {
                                    ((Map) obj).put(copy(entry.getKey()), copy(entry.getValue()));
                                }
                            } else {
                                getCopier(clazz, clazz).copy(value, obj, this);
                            }
                        }
                    } catch (Exception e) {
                        logger.warn("{} copy error. {}", clazz, e.toString());
                    }
                    return obj;
                }
            });
        } catch (Exception e) {
            logger.error("{} instantiate error, caused by {}", targetType, e.toString());
        }
        return t;
    }

    private static Object copyPrimitive(Object value) {
        if (value instanceof String) {
            return value;
        } else if (value instanceof Integer) {
            return ((Integer) value).intValue();
        } else if (value instanceof Double) {
            return ((Double) value).doubleValue();
        } else if (value instanceof Float) {
            return ((Float) value).floatValue();
        } else if (value instanceof Long) {
            return ((Long) value).longValue();
        } else if (value instanceof Short) {
            return ((Short) value).shortValue();
        } else if (value instanceof Byte) {
            return ((Byte) value).byteValue();
        } else if (value instanceof Boolean) {
            return ((Boolean) value).booleanValue();
        } else if (value instanceof Character) {
            return ((Character) value).charValue();
        }
        return null;
    }

    private static BeanCopier getCopier(Class source, Class destiny) {
        String key = source.getCanonicalName() + destiny.getCanonicalName();
        BeanCopier bc = classCopier.get(key);
        if (bc != null) {
            return bc;
        }
        bc = BeanCopier.create(source, destiny, true);
        classCopier.putIfAbsent(key, bc);
        return bc;
    }
}
