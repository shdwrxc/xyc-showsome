package org.xyc.showsome.corn;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * created by wks on date: 2019/4/23
 */
public class PickSql {

    public static void main(String[] args) throws Exception {

        String path = "D:\\document\\apd\\平安\\";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(path + "20190428-1.new.txt")));
             BufferedReader reader = new BufferedReader(new FileReader(new File(path + "20190428-1.txt")))) {

            String str = "";
            boolean append = false;
            while ((str = reader.readLine()) != null) {
                if (append && !str.startsWith("2019")) {
                    writer.write(" " + str);
                } else {
                    if (str.contains("sqlonly")) {
                        writer.newLine();
                        writer.write(str);
                        append = true;
                    } else {
                        append = false;
                    }
                }
            }
        }
    }
}
