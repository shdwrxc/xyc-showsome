package org.xyc.showsome.pea;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;

/**
 * created by wks on date: 2017/11/10
 */
public class FilePea {

    public static String read(String path) throws Exception {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(path)))) {
            String str = "";
            while ((str = reader.readLine()) != null) {
                sb.append(str).append(" ");
            }
        }
        return sb.toString();
    }

    public static void read1(String path) throws Exception {
        try (BufferedInputStream is = new BufferedInputStream(new FileInputStream(new File(path)))) {
            byte[] bytes = new byte[2048];
            while (is.read(bytes) != -1) {
                System.out.println(new String(bytes, "UTF-8"));
            }
        }
    }

    public static void main(String[] args) throws Exception {
//        System.out.println(read("d:\\temp\\test.txt"));
        read1("d:\\temp\\test.txt");
    }
}
