package org.xyc.showsome.solr.test;

import org.apache.solr.client.solrj.beans.Field;

/**
 * Created by ccc on 2016/1/13.
 */
public class SolrTestModel {

    @Field
    private String id;

    @Field
    private String subject;

    @Field
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
