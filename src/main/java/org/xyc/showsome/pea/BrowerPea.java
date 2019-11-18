package org.xyc.showsome.pea;

import java.util.Iterator;
import java.util.Map;

/**
 * created by wks on date: 2019/9/9
 */
public class BrowerPea {

    public static void first() throws Exception {
        String firefox = "C:\\Program Files\\Mozilla Firefox\\firefox.exe";
        Map map = System.getenv();
        for (Iterator itr = map.keySet().iterator(); itr.hasNext();) {
            String value = (String) map.get((String) itr.next());
            if (value.contains("firefox.exe")) {
                firefox = value;
                break;
            }
        }
        Runtime.getRuntime().exec(new String[] { firefox, "www.baidu.com" });
    }

    public static void main(String[] args) throws Exception {
        first();
    }
}
