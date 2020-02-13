package org.xyc.showsome.corn;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.Maps;
import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator;
import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

/**
 * created by wks on date: 2019/11/23
 *
 * sample:
 * http://hg.openjdk.java.net/code-tools/jol/file/tip/jol-samples/src/main/java/org/openjdk/jol/samples/
 * http://openjdk.java.net/projects/code-tools/jol/
 * https://stackoverflow.com/questions/52353/in-java-what-is-the-best-way-to-determine-the-size-of-an-object/52682#52682
 *
 * notice:
 * Note that ObjectSizeCalculator is only supported on HotSpot VM
 */
public class ObjectMemorySize implements Serializable {

    /**
     * get by jol and ObjectSizeCalculator
     * @throws Exception
     */
    private void calculateSize() throws Exception {
        Element e = new Element();
        e.put("a", "hello");
        e.put("b", "klsfjklasowporqpopowqpoeriwopriwpowopesjfsklfjslkjfakljpweowopeiqopjsaklfs");
        e.setStr("helloworldhelloworldhelloworldhelloworldhelloworldhelloworldhelloworld");
        e.setI(123);
        System.out.println(VM.current().details());
        System.out.println("---------------------------------------");
        System.out.println(ClassLayout.parseClass(Element.class).toPrintable());
        System.out.println("---------------------------------------");
        System.out.println(ClassLayout.parseInstance(e).toPrintable());
        System.out.println("---------------------------------------");
        System.out.println(ObjectSizeCalculator.getObjectSize(e));
        System.out.println("---------------------------------------");
        System.out.println(ObjectSizeCalculator.getObjectSize("helloworld"));
        System.out.println("---------------------------------------");
        System.out.println(ObjectSizeCalculator.getObjectSize("helloworldhelloworldhelloworldhelloworldhelloworldhelloworldhelloworldhelloworldhelloworldhelloworld"));
        System.out.println("---------------------------------------");
        calculateByStream(e);
        System.out.println("---------------------------------------");
        System.out.println(ObjectSizeCalculatorTwitter.getObjectSize(e));
    }

    /**
     * https://github.com/JetBrains/jdk8u_nashorn/blob/master/src/jdk/nashorn/internal/ir/debug/ObjectSizeCalculator.java
     * 测试的数据和实际看到的有点差别
     * 主要测试了字符串，字符串放在记事本是6m左右，这个方法输出的是12m左右
     */
    public void calculateByObjectSizeCalculator() {
        System.out.println(ObjectSizeCalculator.getObjectSize(new HashMap<String, Integer>(100000)));
        System.out.println(ObjectSizeCalculator.getObjectSize(3));
        System.out.println(ObjectSizeCalculator.getObjectSize(new int[]{1, 2, 3, 4, 5, 6, 7 }));
        System.out.println(ObjectSizeCalculator.getObjectSize(new int[100]));
    }

    /**
     * Serialization won't keep track of transient variables,
     * and the default serialization method writes strings in UTF-8,
     * so any ANSI characters will only take one byte.
     * If you have many strings, your size will be so far off as to be useless
     * @param e
     * @throws Exception
     */
    public void calculateByStream(Element e) throws Exception {
        Serializable ser = e;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(ser);
        System.out.println(baos.size());
    }

    public void calculateTotal () {
        // Get current size of heap in bytes
        long heapSize = Runtime.getRuntime().totalMemory();

        // Get maximum size of heap in bytes. The heap cannot grow beyond this size.
        // Any attempt will result in an OutOfMemoryException.
        long heapMaxSize = Runtime.getRuntime().maxMemory();

        // Get amount of free memory within the heap in bytes. This size will increase
        // after garbage collection and decrease as new objects are created.
        long heapFreeSize = Runtime.getRuntime().freeMemory();
        System.out.println(heapSize);
        System.out.println(heapMaxSize);
        System.out.println(heapFreeSize);
    }

    public static void main(String[] args) throws Exception {
        ObjectMemorySize size = new ObjectMemorySize();
        size.calculateSize();
    }

    private class Element implements Serializable {

        private static final long serialVersionUID = -1L;

        private Map<String, String> map = Maps.newHashMap();
        private String str;
        private int i;

        public void put(String key, String value) {
            map.put(key, value);
        }

        public String get(String key) {
            return map.get(key);
        }

        public String getStr() {
            return str;
        }

        public void setStr(String str) {
            this.str = str;
        }

        public int getI() {
            return i;
        }

        public void setI(int i) {
            this.i = i;
        }
    }
}
