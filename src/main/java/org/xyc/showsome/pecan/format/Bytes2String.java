package org.xyc.showsome.pecan.format;

/**
 * created by wks on date: 2017/6/1
 */
public class Bytes2String {

    public static String toHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            //加 & 0xFF的原因
            //Integer.toHexString的参数本来是int，如果不进行&0xff，那么当一个byte会转换成int时，对于负数，会做位扩展，举例来说，一个byte的-1（即0xff），会被转换成int的-1（即0xffffffff）
            //byte的-1和int的-1什么区别？
            //int i = 0xff; System.out.println(i);打印：255，这个其实是0x000000ff
            //int j = 0xffffffff; System.out.println(j); 打印：-1
            String hex = Integer.toHexString(bytes[i] & 0xFF);  //0xff即字节（1111 1111）
//            String hex = Integer.toHexString(byteformat[i]);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        byte[] bytes = { 18, 1, 1, 3, 4, 12, 90, -9};
        System.out.println(toHexString(bytes));
    }
}
