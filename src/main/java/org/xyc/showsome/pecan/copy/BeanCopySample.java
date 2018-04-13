package org.xyc.showsome.pecan.copy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.sun.org.apache.xpath.internal.operations.Bool;
import net.sf.cglib.beans.BeanCopier;
import net.sf.cglib.core.Converter;
import org.apache.commons.beanutils.BeanUtils;

/**
 * created by wks on date: 2018/3/27
 */
public class BeanCopySample {

    private static final Map<String, BeanCopier> classCopier = Maps.newHashMap();

    public void copy() {
        BeanCopier beanCopier = BeanCopier.create(Source.class, Destiny.class, false);

        Source source = new Source();
        source.setI(6);
        source.setStr("hello");
        source.setStr2("world");
        source.setHouse(new House("jack"));
        List<Space> list = Lists.newArrayList();
        list.add(new Space("earth"));
        source.setList(list);
        Map<String, String> map = Maps.newHashMap();
        map.put("where", "here");
        source.setMap(map);

        Destiny destiny = new Destiny();

        beanCopier.copy(source, destiny, null);

        System.out.println(JSON.toJSONString(source));
        System.out.println(JSON.toJSONString(destiny));

        source.getList().add(new Space("sun"));
        source.getHouse().setPeople("tom");

        System.out.println(JSON.toJSONString(source));
        System.out.println(JSON.toJSONString(destiny));
    }

    public void copyByConverter() {

        BeanCopier beanCopier = getCopier(Source.class, Destiny.class);

        Source source = new Source();
        source.setI(6);
        source.setStr("hello");
        source.setStr2("world");
        source.setHouse(new House("jack"));
        List<Space> list = Lists.newArrayList();
        list.add(new Space("earth"));
        source.setList(list);
        Map<String, String> map = Maps.newHashMap();
        map.put("where", "here");
        source.setMap(map);

        Destiny destiny = new Destiny();

        beanCopier.copy(source, destiny, new MyConverter());

        System.out.println(JSON.toJSONString(source));
        System.out.println(JSON.toJSONString(destiny));

        source.setI(16);
        source.setStr("shanghai");
        source.setStr2("hai");
        source.getHouse().setPeople("tom");
        source.getList().add(new Space("sun"));
        source.getMap().put("agmin", "yes");

        System.out.println(JSON.toJSONString(source));
        System.out.println(JSON.toJSONString(destiny));
    }

    public void copySelfByConverter() {

        BeanCopier beanCopier = getCopier(Source.class, Source.class);

        Source source = new Source();
        source.setI(6);
        source.setStr("hello");
        source.setStr2("world");
        source.setHouse(new House("jack"));
        List<Space> list = Lists.newArrayList();
        list.add(new Space("earth"));
        source.setList(list);
        Map<String, String> map = Maps.newHashMap();
        map.put("where", "here");
        source.setMap(map);

        Source destiny = new Source();

        beanCopier.copy(source, destiny, new MyConverter());

        System.out.println(JSON.toJSONString(source));
        System.out.println(JSON.toJSONString(destiny));

        source.setI(16);
        source.setStr("shanghai");
        source.setStr2("hai");
        source.getHouse().setPeople("tom");
        source.getList().add(new Space("sun"));
        source.getMap().put("agmin", "yes");

        System.out.println(JSON.toJSONString(source));
        System.out.println(JSON.toJSONString(destiny));
    }

    private Object copyPrimitive(Object value) {
        if (value instanceof Integer) {
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
        } else if (value instanceof String) {
            return value;
        }
        return null;
    }

    private BeanCopier getCopier(Class source, Class destiny) {
        String key = source.getCanonicalName() + destiny.getCanonicalName();
        BeanCopier bc = classCopier.get(key);
        if (bc != null) {
            return bc;
        }
        bc = BeanCopier.create(source, destiny, true);
        classCopier.putIfAbsent(key, bc);
        return bc;
    }

    private void copyUsingUtil() {
        Source source = new Source();
        source.setI(6);
        source.setStr("hello");
        source.setStr2("world");
        source.setHouse(new House("jack"));
        List<Space> list = Lists.newArrayList();
        list.add(new Space("earth"));
        source.setList(list);
        Map<String, String> map = Maps.newHashMap();
        map.put("where", "here");
        source.setMap(map);
        Set<String> set = Sets.newHashSet();
        set.add("lunch");
        source.setSet(set);
        source.setHouses(new House[]{new House("bruce")});

        Source destiny = BeanCopyUtils.copy(source, Source.class);

        System.out.println(JSON.toJSONString(source));
        System.out.println(JSON.toJSONString(destiny));

        source.setI(16);
        source.setStr("shanghai");
        source.setStr2("hai");
        source.setHouse(new House("alan"));
//        source.getHouse().setPeople("tom");
        source.getList().add(new Space("sun"));
        source.getMap().put("agmin", "yes");
        source.getSet().add("dinner");
        source.setHouses(new House[]{new House("will"), new House("rose")});

        System.out.println(JSON.toJSONString(source));
        System.out.println(JSON.toJSONString(destiny));
    }

    private void copyPerformance() {
        Source source = new Source();
        source.setI(6);
        source.setStr("hello");
        source.setStr2("world");
        source.setHouse(new House("jack"));
        List<Space> list = Lists.newArrayList();
        list.add(new Space("earth"));
        source.setList(list);
        Map<String, String> map = Maps.newHashMap();
        map.put("where", "here");
        source.setMap(map);
        Set<String> set = Sets.newHashSet();
        set.add("lunch");
        source.setSet(set);
        source.setHouses(new House[]{new House("bruce")});

        Source destiny = BeanCopyUtils.copy(source, Source.class);

        System.out.println(JSON.toJSONString(source));
        System.out.println(JSON.toJSONString(destiny));

        source.setI(16);
        source.setStr("shanghai");
        source.setStr2("hai");
        source.setHouse(new House("alan"));
        //        source.getHouse().setPeople("tom");
        source.getList().add(new Space("sun"));
        source.getMap().put("agmin", "yes");
        source.getSet().add("dinner");
        source.setHouses(new House[]{new House("will"), new House("rose")});

        System.out.println(JSON.toJSONString(source));
        System.out.println(JSON.toJSONString(destiny));

        Source destiny1 = new Source();
        try {
            BeanUtils.copyProperties(destiny1, source);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        long l = System.currentTimeMillis();
        int loop = 999;
        for (int i = 0; i < loop; i++) {
            Source target = BeanCopyUtils.copy(source, Source.class);
        }
        System.out.println(System.currentTimeMillis() - l);

        l = System.currentTimeMillis();
        for (int i = 0; i < loop; i++) {
            try {
                BeanUtils.copyProperties(destiny1, source);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println(System.currentTimeMillis() - l);
    }

    private void copyByStream() throws Exception {
        Source source = new Source();
        source.setI(6);
        source.setStr("hello");
        source.setStr2("world");
        source.setHouse(new House("jack"));
        List<Space> list = Lists.newArrayList();
        list.add(new Space("earth"));
        source.setList(list);
        Map<String, String> map = Maps.newHashMap();
        map.put("where", "here");
        source.setMap(map);
        Set<String> set = Sets.newHashSet();
        set.add("lunch");
        source.setSet(set);
        source.setHouses(new House[]{new House("bruce")});

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(source);
        oos.flush();
        oos.close();

        byte[] arrByte = baos.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(arrByte);
        ObjectInputStream ois = new ObjectInputStream(bais);
        Source destiny = (Source)ois.readObject();
        ois.close();

        System.out.println(JSON.toJSONString(source));
        System.out.println(JSON.toJSONString(destiny));

        source.setI(16);
        source.setStr("shanghai");
        source.setStr2("hai");
        source.setHouse(new House("alan"));
        //        source.getHouse().setPeople("tom");
        source.getList().add(new Space("sun"));
        source.getMap().put("agmin", "yes");
        source.getSet().add("dinner");
        source.setHouses(new House[]{new House("will"), new House("rose")});

        System.out.println(JSON.toJSONString(source));
        System.out.println(JSON.toJSONString(destiny));

        long l = System.currentTimeMillis();
        int loop = 999;
        for (int i = 0; i < loop; i++) {
            ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
            ObjectOutputStream oos1 = new ObjectOutputStream(baos1);
            oos1.writeObject(source);
            oos1.flush();
            oos1.close();

            byte[] arrByte1 = baos1.toByteArray();
            ByteArrayInputStream bais1 = new ByteArrayInputStream(arrByte1);
            ObjectInputStream ois1 = new ObjectInputStream(bais1);
            Source destiny1 = (Source)ois1.readObject();
            ois1.close();
        }
        System.out.println(System.currentTimeMillis() - l);
    }

    private void copyByJson() throws Exception {
        Source source = new Source();
        source.setI(6);
        source.setStr("hello");
        source.setStr2("world");
        source.setHouse(new House("jack"));
        List<Space> list = Lists.newArrayList();
        list.add(new Space("earth"));
        source.setList(list);
        Map<String, String> map = Maps.newHashMap();
        map.put("where", "here");
        source.setMap(map);
        Set<String> set = Sets.newHashSet();
        set.add("lunch");
        source.setSet(set);
        source.setHouses(new House[]{new House("bruce")});

        Source destiny = JSON.parseObject(JSON.toJSONString(source), Source.class);

        System.out.println(JSON.toJSONString(source));
        System.out.println(JSON.toJSONString(destiny));

        source.setI(16);
        source.setStr("shanghai");
        source.setStr2("hai");
        source.setHouse(new House("alan"));
        //        source.getHouse().setPeople("tom");
        source.getList().add(new Space("sun"));
        source.getMap().put("agmin", "yes");
        source.getSet().add("dinner");
        source.setHouses(new House[]{new House("will"), new House("rose")});

        System.out.println(JSON.toJSONString(source));
        System.out.println(JSON.toJSONString(destiny));

        long l = System.currentTimeMillis();
        int loop = 999;
        for (int i = 0; i < loop; i++) {
            Source destiny1 = JSON.parseObject(JSON.toJSONString(source), Source.class);
        }
        System.out.println(System.currentTimeMillis() - l);
    }

    public static void main(String[] args) throws Exception {
        BeanCopySample beanCopy = new BeanCopySample();
        beanCopy.copyByJson();
    }

    private class MyConverter implements Converter {

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
            } catch (Exception e) {
            }
            return obj;
        }
    }
}
