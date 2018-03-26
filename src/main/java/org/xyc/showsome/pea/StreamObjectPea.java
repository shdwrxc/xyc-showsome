package org.xyc.showsome.pea;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.alibaba.fastjson.JSON;

/**
 * created by wks on date: 2018/2/23
 *
 * output的长度测试
 * 注：toString()默认使用ide自动生成的
 *
 * Element类只有str和i两个字段，值分别为hello world，16
 * ByteArrayOutputStream输出217
 * e.toString().getBytes()输出为32
 * json格式输出28
 *
 * Element类增加str1-str9，9个字段，值和str保持一致，str和i值分别为hello world，16
 * ByteArrayOutputStream输出370
 * e.toString().getBytes()输出为212
 * json格式输出217
 *
 * 在上面的基础上，str的值变更为hello world, i wish happy, but i go to the hell，16
 * ByteArrayOutputStream输出406
 * e.toString().getBytes()输出为572
 * json格式输出577
 *
 * 使用outputStream在对象较小时，原始占用较大，对象较大时，占用会比较有优势
 * toString方法和json格式，主要还是在于怎么构建字符串，乱七八糟的东西越少，占用越小
 * 实际能使用的是outputStream和json，能够反序列化，toString只做测试用
 *
 *
 * 反序列化测试，使用刚刚的测试数据
 * 跑10万次，stream反序列化需要2300ms，fastjon，需要260ms，fastjson要快得多
 *
 */
public class StreamObjectPea {

    public BytesResult output() throws Exception {
        StreamObjectElement e = new StreamObjectElement("hello world, i wish happy, but i go to the hell", 16);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(e);
        byte[] bytes = baos.toByteArray();
        System.out.println(bytes.length);
        String ser1 = e.toString();
        System.out.println(ser1);
        System.out.println(ser1.getBytes().length);
        String ser2 = JSON.toJSONString(e);
        System.out.println(ser2);
        System.out.println(ser2.getBytes().length);
        return new BytesResult(bytes, ser2.getBytes());
    }

    private long start() {
        return System.nanoTime();
    }

    /**
     *
     * @param start
     * @return  毫秒
     */
    private long spend(long start) {
        return (System.nanoTime() - start) / 1000000;
    }

    public void input() throws Exception {
        BytesResult result = output();

        ByteArrayInputStream bais = new ByteArrayInputStream(result.getStream());
        ObjectInputStream ois = new ObjectInputStream(bais);
        StreamObjectElement e1 = (StreamObjectElement)ois.readObject();

        String str = new String(result.getJson());
        StreamObjectElement e2 = JSON.parseObject(str, StreamObjectElement.class);

        int loop = 100000;

        long start = start();
        for (int i = 0; i < loop; i++) {
            String str2 = new String(result.getJson());
            StreamObjectElement e5 = JSON.parseObject(str2, StreamObjectElement.class);
        }
        System.out.println(spend(start));

        start = start();
        for (int i = 0; i < loop; i++) {
            ByteArrayInputStream bais1 = new ByteArrayInputStream(result.getStream());
            ObjectInputStream ois1 = new ObjectInputStream(bais1);
            StreamObjectElement e3 = (StreamObjectElement)ois1.readObject();
        }
        System.out.println(spend(start));

        start = start();
        for (int i = 0; i < loop; i++) {
            String str2 = new String(result.getJson());
            StreamObjectElement e5 = JSON.parseObject(str2, StreamObjectElement.class);
        }
        System.out.println(spend(start));
    }

    public static void main(String[] args) throws Exception {
        StreamObjectPea streamObject = new StreamObjectPea();
        streamObject.input();
    }

    private class BytesResult {
        private byte[] stream;
        private byte[] json;

        public BytesResult(byte[] stream, byte[] json) {
            this.stream = stream;
            this.json = json;
        }

        public byte[] getStream() {
            return stream;
        }

        public void setStream(byte[] stream) {
            this.stream = stream;
        }

        public byte[] getJson() {
            return json;
        }

        public void setJson(byte[] json) {
            this.json = json;
        }
    }
}
