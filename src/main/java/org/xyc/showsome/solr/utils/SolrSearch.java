package org.xyc.showsome.solr.utils;

import org.apache.commons.lang.StringUtils;

/**
 * Created by ccc on 2016/1/18.
 * 用于solr字段搜索
 */
public class SolrSearch {

    private StringBuilder sb = new StringBuilder();

    public SolrSearch build(String field, String value) {
        if (StringUtils.isNotBlank(field) && StringUtils.isNotBlank(value))
            append(field, value);
        return this;
    }

    private StringBuilder append(String field, String value) {
        if (sb.length() > 0)
            sb.append(" AND ");
        return sb.append(field).append(":").append(value);
    }

    public String toString() {
        return sb.toString();
    }
}
