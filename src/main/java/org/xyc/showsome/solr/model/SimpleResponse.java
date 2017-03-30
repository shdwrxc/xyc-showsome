package org.xyc.showsome.solr.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ccc on 2016/1/18.
 */
public class SimpleResponse<T> {

    private int total;

    private List<T> records = new ArrayList<T>();

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }
}
