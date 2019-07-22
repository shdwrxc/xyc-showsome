package org.xyc.showsome.pea;

import java.io.FileOutputStream;

/**
 * created by wks on date: 2019/7/22
 */
public class BytesToFilePea {

    public static void main(String[] args) throws Exception {
        String str = "abc";

        FileOutputStream os = new FileOutputStream("D:\\temp\\2019\\07\\abc.json");
        os.write(str.getBytes());
    }
}

