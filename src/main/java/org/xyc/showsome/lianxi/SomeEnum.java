package org.xyc.showsome.lianxi;

import java.lang.reflect.Field;

/**
 * Created by CCC on 2016/4/22.
 */
public enum SomeEnum {
    A("1"),B("2");

    SomeEnum(String str){
        try {
            Field fieldName = this.getClass().getSuperclass().getDeclaredField("name");
            fieldName.setAccessible(true);
            fieldName.set(this, str);
            fieldName.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println((SomeEnum.A == SomeEnum.A) + "");
        System.out.printf((SomeEnum.A.equals(SomeEnum.A)) + "");
    }
}
