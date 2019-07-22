package org.xyc.showsome.pea;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

/**
 * created by wks on date: 2018/12/12
 */
public class CopyExcelPea {

    public static void main(String[] args) throws Exception {
        String path = "D:\\profile(cui).xls";

        FileOutputStream fos = new FileOutputStream("d:\\profile(again).xls");

        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(path));
        BufferedOutputStream bos = new BufferedOutputStream(fos);   //error if use this

        byte[] bytes = new byte[1000];
        while (bis.read(bytes) > 0) {
            fos.write(bytes);
        }
    }
}
