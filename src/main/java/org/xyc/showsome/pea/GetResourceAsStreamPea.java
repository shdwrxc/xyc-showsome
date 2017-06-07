package org.xyc.showsome.pea;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * created by wks on date: 2017/6/6
 *
 * 第一次getResourceAsStream一个文件
 * 修改这个文件
 * 第二次getResourceAsStream这个文件，不能加载出增加的部分
 *
 * 主要是classloader在defineClass的时候有缓存，虽然文件变了，但是没法重新加载
 *
 * 如果要重新加载，使用File，参考ChangeFileContextPea
 */
public class GetResourceAsStreamPea {

    /**
     * class.getClassLoader().getResourceAsStream获取classpath下的文件
     * java -cp "path;path2;path3;path4/some.jar" your.main.Clazz
     * 上面各个path下的文件都可以通过class.getClassLoader().getResourceAsStream读到
     * @throws Exception
     */
    public static void eat1() throws Exception {
        //默认去找classpath的根目录，本项目中即resources文件夹下和java文件夹下的文件
        InputStream is1 = GetResourceAsStreamPea.class.getClassLoader().getResourceAsStream("bigday.txt");
        //默认去找类当前目录下的文件，本项目中即classes\org\xyc\showsome\pea下的文件
        InputStream is2 = GetResourceAsStreamPea.class.getResourceAsStream("bigday.txt");
        //默认去找classpath的根目录，本项目中即resources文件夹下和java文件夹下的文件
        InputStream is3 = GetResourceAsStreamPea.class.getResourceAsStream("/bigday.txt");

        print(is2);
    }

    private static void print(InputStream is) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String str = null;
        while ((str = br.readLine()) != null) {
            System.out.println(str);
        }
    }

    public static void main(String[] args) throws Exception {
        eat1();
    }
}
