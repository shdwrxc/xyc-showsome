package org.xyc.showsome.solr.utils;

import org.apache.commons.lang.StringUtils;

/**
 * Created by ccc on 2016/1/18.
 * 用于solr排序
 */
public class SolrSort {

    private StringBuilder sb = new StringBuilder();

    public SolrSort build(String field, String order) {
        if (StringUtils.isNotBlank(field) && StringUtils.isNotBlank(order))
            append(field, order);
        return this;
    }

    private StringBuilder append(String field, String order) {
        if (sb.length() > 0)
            sb.append(",");
        return sb.append(field).append(" ").append(order);
    }

    public String toString() {
        return sb.toString();
    }
}
