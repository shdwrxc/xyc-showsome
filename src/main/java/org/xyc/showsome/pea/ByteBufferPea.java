package org.xyc.showsome.pea;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

/**
 * created by wks on date: 2017/5/27
 */
public class ByteBufferPea {

    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        byteBuffer.asCharBuffer().put("abc");
        System.out.println(Arrays.toString(byteBuffer.array()));

        byteBuffer.rewind();
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        byteBuffer.asCharBuffer().put("abc");
        System.out.println(Arrays.toString(byteBuffer.array()));
    }
}
