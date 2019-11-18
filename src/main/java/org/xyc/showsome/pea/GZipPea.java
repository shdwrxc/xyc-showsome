package org.xyc.showsome.pea;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * created by wks on date: 2019/7/20
 *
 * 要生成.gz文件，跑linux下，执行这个命令
 * gzip test.json
 * 会生成test.json.gz，同时会删除源文件
 */
public class GZipPea {

    public static byte[] compress(String str, String encoding) {
        if (str == null || str.length() == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip;
        try {
            gzip = new GZIPOutputStream(out);
            gzip.write(str.getBytes(encoding));
            gzip.close();
        } catch ( Exception e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }

    public static byte[] uncompress(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        try {
            GZIPInputStream ungzip = new GZIPInputStream(in);
            byte[] buffer = new byte[256];
            int n;
            while ((n = ungzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }

    private static void printGzFile() throws Exception {
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(new File("D:\\temp\\2019\\07\\test1.json.gz")));
        byte[] bytes = new byte[3072];
        ByteBuffer byteBuffer = ByteBuffer.allocate(500000);
        int i = 0;
        int j = 0;
        while ((j = bis.read(bytes)) != -1) {
            i += j;
            byteBuffer.put(bytes);
        }
        System.out.println(i);

        byteBuffer.flip();
        byte[] result = new byte[i];
        byteBuffer.get(result);

        System.out.println(Arrays.toString(result));
    }

    private static void compressAndPrint() throws Exception {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("D:\\temp\\2019\\07\\test1.json")));
        StringBuilder sb = new StringBuilder();
        String str = null;
        while ((str = bufferedReader.readLine()) != null) {
            sb.append(str);
        }
        System.out.println(sb.toString());
        byte[] bytes = compress(sb.toString(), "UTF-8");
        System.out.println(bytes.length);
        System.out.println(Arrays.toString(bytes));

        saveFile(bytes);
    }

    public static void saveFile(byte[] bytes) throws Exception {
        FileOutputStream os = new FileOutputStream("D:\\temp\\2019\\07\\world.gz");
        os.write(bytes);
    }

    public static void main(String[] args) throws Exception{
        printGzFile();
        compressAndPrint();
    }
}
