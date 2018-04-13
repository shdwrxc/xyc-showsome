package org.xyc.showsome.pecan.copy;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * created by wks on date: 2018/4/13
 */
public class Source implements Serializable {

    private int i;
    private String str;
    private String str2;
    private House house;
    private List<Space> list;
    private Map<String, String> map;
    private String str3;
    private Set<String> set;
    private House[] houses;

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

    public String getStr2() {
        return str2;
    }

    public void setStr2(String str2) {
        this.str2 = str2;
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

    public String getStr3() {
        return str3;
    }

    public void setStr3(String str3) {
        this.str3 = str3;
    }

    public Set<String> getSet() {
        return set;
    }

    public void setSet(Set<String> set) {
        this.set = set;
    }

    public House[] getHouses() {
        return houses;
    }

    public void setHouses(House[] houses) {
        this.houses = houses;
    }
}
