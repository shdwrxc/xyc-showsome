package org.xyc.showsome.lianxi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * Created by CCC on 2016/5/31.
 */
public class FileEncrpt {


    public static void read1() throws Exception{
        String fileName = "13744-normal.json";
        String filePath = "D:\\temp\\20160815\\" + fileName;

        BufferedReader reader = new BufferedReader(new FileReader(new File(filePath)));
        StringBuilder sb = new StringBuilder();
        String str;
        while ((str = reader.readLine()) != null) {
            sb.append(str);
        }

        String finalStr = sb.toString();
        System.out.println(finalStr.length());
        byte[] bytes = finalStr.getBytes("UTF-8");
        System.out.println(bytes.length);
        for (int i = 0; i < 100; i++) {
            long l = System.currentTimeMillis();
            String tempStr = SymmetricEncoder.aesEncode(finalStr);
            long time1 = System.currentTimeMillis() - l;
            l = System.currentTimeMillis();
            SymmetricEncoder.aesDecode(tempStr);
            System.out.println(tempStr.length() + "," + time1 + "," + (System.currentTimeMillis() - l));
        }
    }

    public static void main(String[] args) throws Exception {
        read1();
    }
}
