package org.xyc.showsome.pecan.copy;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * created by wks on date: 2018/4/13
 */
public class Destiny implements Serializable {

    private int i;
    private String str;
    private House house;
    private List<Space> list;
    private Map<String, String> map;

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public House getHouse() {
        return house;
    }

    public void setHouse(House house) {
        this.house = house;
    }

    public List<Space> getList() {
        return list;
    }

    public void setList(List<Space> list) {
        this.list = list;
    }

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }
}
