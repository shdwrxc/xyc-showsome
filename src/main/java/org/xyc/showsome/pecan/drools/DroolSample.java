package org.xyc.showsome.pecan.drools;

import java.util.ArrayList;
import java.util.List;

/**
 * created by wks on date: 2017/1/20
 */
public class DroolSample {

    private String str;
    private boolean b;
    private int i;
    private long l;
    private List<String> list = new ArrayList<String>();

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public boolean isB() {
        return b;
    }

    public void setB(boolean b) {
        this.b = b;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public long getL() {
        return l;
    }

    public void setL(long l) {
        this.l = l;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public void addList(String str) {
        list.add(str);
    }

    @Override
    public String toString() {
        return "DroolSample{" +
                "str='" + str + '\'' +
                ", b=" + b +
                ", i=" + i +
                ", l=" + l +
                ", list=" + list +
                '}';
    }
}
