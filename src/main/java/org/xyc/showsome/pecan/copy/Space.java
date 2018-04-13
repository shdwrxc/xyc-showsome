package org.xyc.showsome.pecan.copy;

import java.io.Serializable;

/**
 * created by wks on date: 2018/4/13
 */
public class Space implements Serializable {

    private String star;

    public Space() {
    }

    public Space(String star) {
        this.star = star;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }
}
