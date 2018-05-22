package org.xyc.showsome.pecan.copy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
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
        source.setMonth(MonthType.August);

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
        source.setMonth(MonthType.July);

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
        source.setMonth(MonthType.August);
        source.setBigOne(new BigDecimal(66.878786).setScale(3, BigDecimal.ROUND_HALF_UP));

        Source destiny = BeanCopyUtils.copy(source, Source.class);
        Source destiny1 = BeanCopyUtils.copyObject(source, Source.class);
        Source destiny2 = new Source();
        try {
            BeanUtils.copyProperties(destiny2, source);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        System.out.println("source:" + JSON.toJSONString(source));
        System.out.println("copyyy:" + JSON.toJSONString(destiny));
        System.out.println("jsonnn:" + JSON.toJSONString(destiny1));
        System.out.println("apache:" + JSON.toJSONString(destiny2));

        source.setI(16);
        source.setStr("shanghai");
        source.setStr2("hai");
        source.setHouse(new House("alan"));
        //        source.getHouse().setPeople("tom");
        source.getList().add(new Space("sun"));
        source.getMap().put("agmin", "yes");
        source.getSet().add("dinner");
        source.setHouses(new House[]{new House("will"), new House("rose")});
        source.setMonth(MonthType.July);
        source.setBigOne(new BigDecimal(88.8927621).setScale(3, BigDecimal.ROUND_HALF_UP));

        System.out.println("source:" + JSON.toJSONString(source));
        System.out.println("copyyy:" + JSON.toJSONString(destiny));
        System.out.println("jsonnn:" + JSON.toJSONString(destiny1));
        System.out.println("apache:" + JSON.toJSONString(destiny2));

        long l = System.currentTimeMillis();
        int loop = 999;
        for (int i = 0; i < loop; i++) {
            Source target = BeanCopyUtils.copy(source, Source.class);
        }
        System.out.println("copyyy:" + (System.currentTimeMillis() - l));

        l = System.currentTimeMillis();
        for (int i = 0; i < loop; i++) {
            try {
                BeanUtils.copyProperties(destiny1, source);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("apache:" + (System.currentTimeMillis() - l));

        l = System.currentTimeMillis();
        for (int i = 0; i < loop; i++) {
            Source target = BeanCopyUtils.copyObject(source, Source.class);
        }
        System.out.println("jsonnn:" + (System.currentTimeMillis() - l));

    }

    private void copyListPerformance() {
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
        source.setMonth(MonthType.August);

        Source source1 = new Source();
        source1.setI(8);
        source1.setStr("bye");
        source1.setStr2("yesterday");
        source1.setHouse(new House("marry"));
        List<Space> list1 = Lists.newArrayList();
        list1.add(new Space("diqiu"));
        source1.setList(list1);
        Map<String, String> map1 = Maps.newHashMap();
        map1.put("zheli", "nali");
        source1.setMap(map1);
        Set<String> set1 = Sets.newHashSet();
        set1.add("wanfan");
        source1.setSet(set1);
        source1.setHouses(new House[]{new House("bulusi")});
        source1.setMonth(MonthType.June);

        List sources = new ArrayList<>();
        sources.add(source);
        sources.add(source1);

        for (int i = 0; i < 100; i++) {
            sources.add(source.cloneOne());
        }

        List<Source> destinies = BeanCopyUtils.copyList(sources, Source.class);
        List<Source> destinies1 = BeanCopyUtils.copyObjects(sources, Source.class);

        System.out.println("source:" + JSON.toJSONString(sources));
        System.out.println("source:" + JSON.toJSONString(destinies));
        System.out.println("source:" + JSON.toJSONString(destinies1));

        source.setI(16);
        source.setStr("shanghai");
        source.setStr2("hai");
        source.setHouse(new House("alan"));
        //        source.getHouse().setPeople("tom");
        source.getList().add(new Space("sun"));
        source.getMap().put("agmin", "yes");
        source.getSet().add("dinner");
        source.setHouses(new House[]{new House("will"), new House("rose")});
        source.setMonth(MonthType.July);

        System.out.println("source:" + JSON.toJSONString(sources));
        System.out.println("source:" + JSON.toJSONString(destinies));
        System.out.println("source:" + JSON.toJSONString(destinies1));

        long l = System.currentTimeMillis();
        int loop = 999;
        for (int i = 0; i < loop; i++) {
            destinies = BeanCopyUtils.copyList(sources, Source.class);
        }
        System.out.println("copyyy:" + (System.currentTimeMillis() - l));

        l = System.currentTimeMillis();
        for (int i = 0; i < loop; i++) {
            destinies1 = BeanCopyUtils.copyObjects(sources, Source.class);
        }
        System.out.println("jsonnn:" + (System.currentTimeMillis() - l));

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
        source.setMonth(MonthType.August);

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
        source.setMonth(MonthType.July);

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
        source.setMonth(MonthType.August);

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
        source.setMonth(MonthType.July);

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
        beanCopy.copyPerformance();
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
