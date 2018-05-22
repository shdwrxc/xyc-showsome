package org.xyc.showsome.pecan.copy;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.sf.cglib.beans.BeanCopier;
import net.sf.cglib.core.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * created by wks on date: 2018/4/13
 *
 * copy的效率
 * Cglib的BeanCopier最好
 * Apache的BeanUtils稍差
 * Fastjson第三，接近Apache的BeanUtils
 * ObjectOutputStream第四
 *
 * 以上4种方法，前面2种浅拷贝，只复制最外层的对象，后面两种深拷贝
 *
 * 本实例中扩展后的BeanCopier的copy方法和fastjson，ObjectOutputStream的区别
 * 如果原实例中，属性a包含了实例abc，属性b也包含了实例abc，并且和属性a中的实例abc的引用一致
 * 这种情况下，BeanCopier的copy方法不能让新生成的实例中两个abc的引用一致，新实例中的两个实例abc肯定是两个对象，引用肯定不一致
 * fastjson和ObjectOutputStream可以保持和原实例完全一致
 *
 */
public class BeanCopyUtils {

    private static final Logger logger = LoggerFactory.getLogger(BeanCopyUtils.class);

    private static final Map<String, BeanCopier> classCopier = Maps.newHashMap();

    public static <T, S> List<T> copyList(Collection<S> source, Class<T> targetType) {
        List<T> result = Lists.<T>newArrayList();
        if (source == null) {
            return result;
        }
        for (S obj : source) {
            T t = copy(obj, targetType);
            if (t != null) {
                result.add(t);
            }
        }
        return result;
    }
    /**
     * 1.本方法主要是实现深拷贝，BeanCopier和apache的BeanUtils都是浅拷贝，只生成最外层的实例，里面的属性全是之前实例的引用.
     * 2.不支持没有默认构造方法的类
     * 3.如果原实例中，属性a包含了实例abc，属性b也包含了实例abc，并且和属性a中的实例abc的引用一致
     *   这种情况下，本方法不能让新生成的实例中两个abc的引用一致，新实例中的两个实例abc肯定是两个对象，引用肯定不一致
     * @param source
     * @param targetType
     * @param <T>
     * @return
     */
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
                        } else if (clazz.isEnum()) {
                            return Enum.valueOf(clazz, value.toString());
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
        } else if (value instanceof BigDecimal) {
            return new BigDecimal(value.toString());
        } else if (value instanceof BigInteger) {
            return new BigInteger(value.toString());
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

    /**
     *
     * @param source
     * @param targetType
     * @param <T>
     * @return
     */
    public static <T> List<T> copyObjects(Object source, Class<T> targetType) {
        return JSON.parseArray(JSON.toJSONString(source), targetType);
    }

    public static <T> T copyObject(Object source, Class<T> targetType) {
        return JSON.parseObject(JSON.toJSONString(source), targetType);
    }

    public static void main(String[] args) {
        BigInteger bi = new BigInteger("123");

        BigInteger bi1 = new BigInteger(bi.toString());

        System.out.println(bi.intValue());
        System.out.println(bi1.intValue());

        System.out.println(Integer.parseInt("A", 16));



        BigDecimal bd = new BigDecimal(12.45678567).setScale(5, BigDecimal.ROUND_HALF_UP);
        System.out.println(bd.doubleValue());

        BigDecimal hd = new BigDecimal(bd.toString());
        System.out.println(hd.doubleValue());
    }
}
