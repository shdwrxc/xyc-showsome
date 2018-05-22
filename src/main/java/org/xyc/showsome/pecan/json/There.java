package org.xyc.showsome.pecan.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * created by wks on date: 2018/4/16
 */
public class There {

    public static void main(String[] args) {
        This t1 = new This();
        t1.setI(6);

        That t2 = new That();
        t2.setStr("hello");
        t2.setT1(t1);

        t1.setT2(t2);

        Here here = new Here();
        here.setStr("world");

        Far far = new Far();
        far.setStr("but");
        here.setF1(far);
        here.setF2(far);

//        System.out.println(JSON.toJSONString(t1));
//        System.out.println(JSON.toJSONString(t2));

//        System.out.println(JSON.toJSONString(t1, SerializerFeature.DisableCircularReferenceDetect));
//        System.out.println(JSON.toJSONString(t2, SerializerFeature.DisableCircularReferenceDetect));

        System.out.println(JSON.toJSONString(here));

        System.out.println(JSON.toJSONString(here, SerializerFeature.DisableCircularReferenceDetect));
    }
}
