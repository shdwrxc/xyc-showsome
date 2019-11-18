package org.xyc.showsome.pea;

import java.util.Arrays;

/**
 * created by wks on date: 2019/7/23
 */
public class PrintArrayPea {

    /**
     *
     * @param s 16进制字符串
     * @return
     */
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public static void main(String[] args) {
        Byte[] bs = {0x0F, 0x1F, 0x2F, 0x3F, 0x4F, 0x5F, 0x6F};
        System.out.println(Arrays.toString(bs));

        byte[] bytes = "你好，这是一段字符串！".getBytes();
        System.out.println(Arrays.toString(bytes));

        System.out.println(Arrays.toString(hexStringToByteArray("e04fd020ea3a6910a2d808002b30309d")));
    }
}
