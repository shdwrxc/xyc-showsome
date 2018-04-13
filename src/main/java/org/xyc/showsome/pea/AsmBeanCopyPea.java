package org.xyc.showsome.pea;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.sf.cglib.beans.BeanCopier;
import net.sf.cglib.core.Converter;

/**
 * created by wks on date: 2018/3/27
 */
public class AsmBeanCopyPea {

    private static final Map<Class, BeanCopier> classCopier = Maps.newHashMap();

    //test thread
    public void copy() {

        BeanCopier beanCopier = BeanCopier.create(Table.class, Chair.class, false);

        ExecutorService es = Executors.newFixedThreadPool(100);

        Random random = new Random();

        int loop = 10000000;

        List<Future> list = Lists.newArrayList();

        for (int i = 0; i < loop; i++) {
            list.add(es.submit(new Runnable() {
                @Override
                public void run() {
                    int i = random.nextInt(1000);
                    Table table = new Table();
                    table.setA(i);
                    table.setB(i + "");
                    Chair chair = new Chair();
                    beanCopier.copy(table, chair, null);
                    //                    System.out.println(chair.toString());
                    if (!String.valueOf(chair.getA()).equals(chair.getB())) {
                        System.out.println("------------------------------------find one." + chair.toString());
                    }
                }
            }));
        }

        for (Future future : list) {
            try {
                future.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        es.shutdown();
    }

    public void deepCopy() {
        BeanCopier beanCopier = BeanCopier.create(DestA.class, DestB.class, false);
        DestA destA = new DestA();
        destA.setStr("str1");
        destA.setStr2("str12");
        destA.getList().add("str2");
        destA.getChildList().add(new ChildA("str3"));
        destA.getMap().put("str4", new ChildA("ste3"));

        DestB destB = new DestB();

        beanCopier.copy(destA, destB, null);

        System.out.println(JSON.toJSONString(destA));
        System.out.println(JSON.toJSONString(destB));

        destA.getList().add("str3");

        System.out.println(JSON.toJSONString(destA));
        System.out.println(JSON.toJSONString(destB));
    }

    public void deepCopySelf() {
        BeanCopier beanCopier = BeanCopier.create(DestA.class, DestA.class, false);
        DestA destA = new DestA();
        destA.setStr("str1");
        destA.getList().add("str2");
        destA.getChildList().add(new ChildA("str3"));
        destA.getMap().put("str4", new ChildA("ste3"));
        destA.setChild(new ChildB("hello"));

        DestA destAOther = new DestA();

        beanCopier.copy(destA, destAOther, null);

        System.out.println(JSON.toJSONString(destA));
        System.out.println(JSON.toJSONString(destAOther));

        destA.getChildList().add(new ChildA("str5"));
        destA.setStr("str1again");
        destAOther.getMap().put("str6", new ChildA("str7"));
        destA.getChild().setStr("world");
        System.out.println(JSON.toJSONString(destA));
        System.out.println(JSON.toJSONString(destAOther));
    }

    public void copyByConverter() {
        BeanCopier beanCopier = BeanCopier.create(DestA.class, DestA.class, true);

        DestA destA = new DestA();
        destA.setStr("str1");
        destA.getList().add("str2");
        destA.getChildList().add(new ChildA("str3"));
        destA.getMap().put("str4", new ChildA("ste3"));
        destA.setChild(new ChildB("hello"));

        DestA destAOther = new DestA();

        beanCopier.copy(destA, destAOther, new Converter() {
            @Override
            public Object convert(Object value, Class target, Object context) {
                //value = value, target = value's type, context = setValue method string
                return copy(value);
            }

            private Object copy(Object value) {
                Object obj = null;
                try {
                    if (value == null) {
                        return obj;
                    }
                    Class clazz = value.getClass();
                    obj = clazz.newInstance();
                    if (value instanceof List) {
                        List newList = (List) obj;
                        List list = (List) value;
                        for (Object o : list) {
                            newList.add(copy(o));
                        }
                    } else if (value instanceof Map) {
                        Map newMap = (Map) obj;
                        Map map = (Map) value;
                        for (Map.Entry entry : (Set<Map.Entry>) map.entrySet()) {
                            newMap.put(copy(entry.getKey()), copy(entry.getValue()));
                        }
                    } else {
//                        getCopier(clazz).copy(value, obj, this);
                        return value;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return obj;
            }

            private BeanCopier getCopier(Class clazz) {
                BeanCopier bc = classCopier.get(clazz);
                if (bc != null) {
                    return bc;
                }
                bc = BeanCopier.create(clazz, clazz, true);
                classCopier.putIfAbsent(clazz, bc);
                return bc;
            }
        });

        System.out.println("");
    }

    public static void main(String[] args) throws Exception {
        AsmBeanCopyPea beanCopy = new AsmBeanCopyPea();
        beanCopy.copyByConverter();
    }

    private class Table {
        private int a;

        private String b;

        public int getA() {
            return a;
        }

        public void setA(int a) {
            this.a = a;
        }

        public String getB() {
            return b;
        }

        public void setB(String b) {
            this.b = b;
        }
    }

    private class Chair {
        private int a;

        private String b;

        public int getA() {
            return a;
        }

        public void setA(int a) {
            this.a = a;
        }

        public String getB() {
            return b;
        }

        public void setB(String b) {
            this.b = b;
        }

        @Override
        public String toString() {
            return "Chair{" +
                    "a=" + a +
                    ", b='" + b + '\'' +
                    '}';
        }
    }

    private class DestA {
        private String str;
        private List<String> list = Lists.newArrayList();
        private List<ChildA> childList = Lists.newArrayList();
        private Map<String, ChildA> map = Maps.newHashMap();
        private ChildB child;
        private String str2;

        public String getStr() {
            return str;
        }

        public void setStr(String str) {
            this.str = str;
        }

        public List<String> getList() {
            return list;
        }

        public void setList(List<String> list) {
            this.list = list;
        }

        public List<ChildA> getChildList() {
            return childList;
        }

        public void setChildList(List<ChildA> childList) {
            this.childList = childList;
        }

        public Map<String, ChildA> getMap() {
            return map;
        }

        public void setMap(Map<String, ChildA> map) {
            this.map = map;
        }

        public ChildB getChild() {
            return child;
        }

        public void setChild(ChildB child) {
            this.child = child;
        }

        public String getStr2() {
            return str2;
        }

        public void setStr2(String str2) {
            this.str2 = str2;
        }
    }

    private class DestB {
        private String str;
        private List<String> list = Lists.newArrayList();
        private List<ChildA> childList = Lists.newArrayList();
        private Map<String, ChildA> map = Maps.newHashMap();
        private ChildB child;

        public String getStr() {
            return str;
        }

        public void setStr(String str) {
            this.str = str;
        }

        public List<String> getList() {
            return list;
        }

        public void setList(List<String> list) {
            this.list = list;
        }

        public List<ChildA> getChildList() {
            return childList;
        }

        public void setChildList(List<ChildA> childList) {
            this.childList = childList;
        }

        public Map<String, ChildA> getMap() {
            return map;
        }

        public void setMap(Map<String, ChildA> map) {
            this.map = map;
        }

        public ChildB getChild() {
            return child;
        }

        public void setChild(ChildB child) {
            this.child = child;
        }
    }

    private class ChildA {

        private String str;

        public ChildA() {
        }

        public ChildA(String str) {
            this.str = str;
        }

        public String getStr() {
            return str;
        }

        public void setStr(String str) {
            this.str = str;
        }
    }

    private class ChildB {
        private String str;

        public ChildB() {
        }

        public ChildB(String str) {
            this.str = str;
        }

        public String getStr() {
            return str;
        }

        public void setStr(String str) {
            this.str = str;
        }
    }
}
