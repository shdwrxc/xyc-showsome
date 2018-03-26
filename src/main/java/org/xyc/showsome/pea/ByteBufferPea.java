package org.xyc.showsome.pea;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

/**
 * created by wks on date: 2017/5/27
 *
 * byte，char，1个字节
 * short，2个字节
 * int，4个字节
 * long，double，8个字节
 */
public class ByteBufferPea {

    private static void shadowclaw1() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        byteBuffer.asCharBuffer().put("abc");
        System.out.println(Arrays.toString(byteBuffer.array()));

        byteBuffer.rewind();
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        byteBuffer.asCharBuffer().put("abc");
        System.out.println(Arrays.toString(byteBuffer.array()));
    }

    private static void shadowclaw2() {
        //position=0,limit=20,cap=20
        ByteBuffer byteBuffer = ByteBuffer.allocate(20);
        //position=4,limit=20,cap=20
        byteBuffer.putInt(1);
        //position=8,limit=20,cap=20
        byteBuffer.putInt(2);
        //position=12,limit=20,cap=20
        byteBuffer.putInt(3);
        System.out.println(Arrays.toString(byteBuffer.array()));
        //从写模式转变为读模式
        //position=0,limit=12,cap=20
        byteBuffer.flip();
        //使用getInt无参方法，会让position增加
        //position=4,limit=12,cap=20
        int i = byteBuffer.getInt();
        //使用有参数的get会取出参数位的byte字节，不会导致position增加
        int i1 = byteBuffer.get(3);
        //remaining就是limit-position，余下的字节
        //新建一个字节数组，来获取一个字节集
        byte[] bytes = new byte[byteBuffer.remaining()];
        //position=12,limit=12,cap=20
        ByteBuffer byteBuffer1 = byteBuffer.get(bytes);
        System.out.println(Arrays.toString(bytes));

        //重置position，mark，从头开始
        //position=0,limit=12,cap=20
        byteBuffer.rewind();
        System.out.println(Arrays.toString(byteBuffer.array()));

        //position=4,limit=12,cap=20
        int i2 = byteBuffer.getInt();
        //标记mark=position
        byteBuffer.mark();
        //position=8,limit=12,cap=20
        int i3 = byteBuffer.getInt();
        //position=4,limit=12,cap=20
        byteBuffer.reset();
        //position=8,limit=12,cap=20
        int i5 = byteBuffer.getInt();

    }

    private static void shadowclaw3() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(50);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        byteBuffer.put((byte)0xff);
        byteBuffer.putShort((short)0xff);
        byteBuffer.putInt(0xff);
        byteBuffer.putInt(0xffffffff);
        byteBuffer.putInt(2);
        System.out.println(Arrays.toString(byteBuffer.array()));
    }

    public static void main(String[] args) {
        shadowclaw2();
    }
}
