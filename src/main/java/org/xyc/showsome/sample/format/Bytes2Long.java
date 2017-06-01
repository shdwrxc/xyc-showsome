package org.xyc.showsome.sample.format;

import java.nio.ByteBuffer;

import com.google.common.primitives.Longs;

/**
 * created by wks on date: 2017/5/4
 */
public class Bytes2Long {

    private static byte[] bytes = {0,0,0,49,92,56,-114,33};

    /**
     * Is the first byte the most significant, then it is a little bit different:
     * Replace long with BigInteger, if you have more than 8 byte.
     */
    private static void method1() {
        long value = 0;
        for (int i = 0; i < bytes.length; i++)
        {
            value = (value << 8) + (bytes[i] & 0xff);
        }
        System.out.println(value);
    }

    private static void method2() {
        ByteBuffer bb = ByteBuffer.allocate(8);
        bb.put(bytes);
        bb.flip();
        long l = bb.getLong();
        System.out.println(l);
    }

    private static void method3() throws Exception {
        long content = 212000607777l;
        byte[] numberByte = Longs.toByteArray(content);
        System.out.println(numberByte);
        long l = Longs.fromByteArray(numberByte);
        System.out.printf(l + "");
    }

    public static void main(String[] args) throws Exception {
        method1();
    }
}
