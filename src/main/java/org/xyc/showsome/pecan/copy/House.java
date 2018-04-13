package org.xyc.showsome.pecan.copy;

import java.io.Serializable;

/**
 * created by wks on date: 2018/4/13
 */
public class House implements Serializable {

    private String people;

    public House() {
    }

    public House(String people) {
        this.people = people;
    }

    public String getPeople() {
        return people;
    }

    public void setPeople(String people) {
        this.people = people;
    }
}
