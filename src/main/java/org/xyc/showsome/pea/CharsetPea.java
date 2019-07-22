package org.xyc.showsome.pea;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;

/**
 * created by wks on date: 2018/12/11
 */
public class CharsetPea {

    public static void main(String[] args) throws Exception {
        System.out.println("file.encoding:"+System.getProperty("file.encoding"));
        System.out.println("sun.jnu.encoding:"+System.getProperty("sun.jnu.encoding"));

        doSome();
    }

    private static void doSome() throws Exception {
        File file = new File("D:\\temp\\readfile");
        String sysEncoding = (String) System.getProperties().get("file.encoding");
        System.out.println(sysEncoding);
        for (File f : file.listFiles()) {
            System.out.println(f.getName());
//            BufferedReader br = new BufferedReader(new FileReader(f));
//            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
//            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f), "GBK"));
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
            String str = "";
            while ((str = br.readLine()) != null) {
//                System.out.println(str);
                //对内容无效，不知道对文件名有没有效
//                System.out.println(new String(str.getBytes(sysEncoding), "GBK"));
            }
        }

        String str = "福建分公司-定核损员KPI指标排名-2018-07";
        String str1 = "甘肃省分公司-定核损员KPI指标排名-2018-09";
        String str2 = "内蒙古自治区分公司-定核损员KPI指标排名-2018-08.xlsx";
        byte[] bytes = str.getBytes("UTF-8");
        byte[] bytes1 = str1.getBytes("UTF-8");
        byte[] bytes2 = str2.getBytes("UTF-8");
        System.out.println(new String(bytes, "GBK"));
        System.out.println(new String(bytes1, "GBK"));
        System.out.println(new String(bytes2, "GBK"));

        String sstr = "A-福建分公司绿-定核损员KPI指标排名-2018-067";
        byte[] sbytes = sstr.getBytes("UTF-8");
        String sstr1 = new String(sbytes, "GBK");
        System.out.println(sstr1);
        byte[] sbytes1 = sstr1.getBytes("GBK");
        String sstr2 = new String(sbytes1, "UTF-8");
        System.out.println(sstr2);

        String newFileName = new String(str.getBytes(System.getProperty("file.encoding")), "UTF-8");
    }
}
