package org.xyc.showsome.sample.encrypt;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import sun.misc.BASE64Encoder;

/**
 * created by wks on date: 2016/12/23
 */
public class Md5Encrypt {

    public static String encode(String str) {
        byte[] md5Bytes = new byte[0];
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] byteArray = str.getBytes("UTF-8");
            md5Bytes = md5.digest(byteArray);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //        md5.update(byteArray);
//        byte[] md5Bytes = md5.digest();
        return new BASE64Encoder().encode(md5Bytes);
    }

    public static void main(String[] args) {
        System.out.println(encode("ererfeiisgod"));
        long l = System.currentTimeMillis();
        int loop = 1;
        for (int i = 0; i < loop; i++) {
            encode("ererfeiisgod");
        }
        System.out.println(System.currentTimeMillis() - l);
    }
}
