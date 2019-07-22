package org.xyc.showsome.pea;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * created by wks on date: 2018/12/12
 */
public class ByteArrayStreamPea {

    public static void main(String[] args) throws Exception {
        String str = "你好";

        ByteArrayInputStream bais = new ByteArrayInputStream(str.getBytes());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        //Deprecated
        int index = 0;
        while ((index = bais.read()) != -1) {
            baos.write(index);
        }

        byte[] bytes = new byte[1000];
        while (bais.read(bytes) > 0) {
            baos.write(bytes);
        }

        System.out.println(new String(baos.toByteArray()));
    }
}
