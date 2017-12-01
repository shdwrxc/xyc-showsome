package org.xyc.showsome.pea;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;

/**
 * created by wks on date: 2017/11/28
 */
public class StreamPea {

    public static ByteArrayOutputStream shadowclaw1(String path) throws Exception {
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(path));
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] bytes = new byte[1024];
        while (bis.read(bytes) > 0) {
            bos.write(bytes);
        }
        System.out.println(new String(bos.toByteArray(), "UTF-8"));
        return bos;
    }

    public static void shadowclaw2(String path) throws Exception {
        ByteArrayInputStream bis = new ByteArrayInputStream(shadowclaw1(path).toByteArray());
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] bytes = new byte[1024];
        while (bis.read(bytes) > 0) {
            bos.write(bytes);
        }
        System.out.println(new String(bos.toByteArray(), "UTF-8"));
    }

    public static void main(String[] args) throws Exception {
        shadowclaw2("d:\\temp\\test.txt");
    }
}
