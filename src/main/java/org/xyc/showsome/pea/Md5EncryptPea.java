package org.xyc.showsome.pea;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5EncryptPea {

    public static char[] hexChar = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    public static String getMd5Hash(String str) {
        try {
            byte[] bytes = str.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(bytes);
//            return new String(digest, "UTF-8");
//            return new String(new BASE64Encoder().encode(digest));
            return toHexString(digest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getMd5HashBig(String str) {
        try {
            byte[] bytes = str.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.reset();
            md.update(bytes);
            byte[] digest = md.digest();
//            return new String(digest, "UTF-8");
//                        return new String(new BASE64Encoder().encode(digest));
            return toHexString(digest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String toHexString(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(hexChar[(b[i] & 0xf0) >>> 4]);
            sb.append(hexChar[b[i] & 0x0f]);
        }
        return sb.toString();
    }

    public static String toHexString1(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(Integer.toHexString(b[i]));
        }
        return sb.toString();
    }

    public static String getHash(String fileName, String hashType) throws Exception {
        InputStream fis = new FileInputStream(fileName);
        byte[] buffer = new byte[1024];
        MessageDigest md5 = MessageDigest.getInstance(hashType);
        int numRead = 0;
        while ((numRead = fis.read(buffer)) > 0) { // 瓶颈
            md5.update(buffer, 0, numRead);
        }
        fis.close();
        return toHexString(md5.digest());
    }

    public static String getHashNio(String fileName, String hashType) throws Exception {
        FileInputStream fStream = null;
        String hash = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance(hashType);
            fStream = new FileInputStream(fileName);
            FileChannel fChannel = fStream.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(8 * 1024);
            for (int count = fChannel.read(buffer); count != -1; count = fChannel.read(buffer)) {
                buffer.flip();
                md5.update(buffer);
                if (!buffer.hasRemaining()) {
                    // System.out.println("count:"+count);
                    buffer.clear();
                }
            }
            hash = toHexString(md5.digest());

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fStream != null)
                    fStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return hash;
    }

    public static void main(String[] args) throws Exception {
        System.out.println(Md5EncryptPea.getMd5HashBig("123456abcdefg"));
        System.out.println(Md5EncryptPea.getMd5Hash("123456abcdefg"));
    }
}
