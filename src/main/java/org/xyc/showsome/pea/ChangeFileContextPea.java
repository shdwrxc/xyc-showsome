package org.xyc.showsome.pea;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * created by wks on date: 2017/6/6
 *
 * main方法中，执行了两次同一个方法
 * 第一次执行，打印
 * 1
 * 2
 * 文件添加3
 * 第二次执行，打印
 * 1
 * 2
 * 3
 *
 * 通过File的方式可以实时加载变化的文件
 */
public class ChangeFileContextPea {

    public static void shadowclaw1() throws Exception {
        File file = new File("d:\\abc.txt");
        FileReader fr = new FileReader(file);
        char[] bytes = new char[100];
        while (fr.read(bytes) > 0) {
            System.out.println(bytes);
        }
    }

    public static void main(String[] args) throws Exception {
        shadowclaw1();
        shadowclaw1();
    }
}
