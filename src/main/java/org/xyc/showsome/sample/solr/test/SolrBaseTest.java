package org.xyc.showsome.sample.solr.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xyc.showsome.sample.solr.SolrBaseDao;
import org.xyc.showsome.sample.solr.model.SimpleResponse;
import org.xyc.showsome.sample.solr.utils.SolrSearch;
import org.xyc.showsome.sample.solr.utils.SolrSort;

/**
 * Created by ccc on 2016/1/18.
 */
public class SolrBaseTest {

    SolrBaseDao solrBaseDao;

    @Before
    public void before() {
        solrBaseDao = new SolrBaseDao("http://192.168.200.11:18080/solr/collection1");
    }

    @Test
    public void addTest() {
        SolrTestModel model = new SolrTestModel();
        model.setId("NE20160101876216");
        model.setSubject("晚上吃饭");
        model.setName("海通证券");
        boolean b = solrBaseDao.add(model);
        Assert.assertTrue(b);
    }

    @Test
    public void searchTest() {
        SolrSearch solrSearch = new SolrSearch().build("name", "海通").build("subject", "吃饭");
        SimpleResponse sr = solrBaseDao.query(solrSearch, new SolrSort(), 0, 10, SolrTestModel.class);
        Assert.assertNotNull(sr);
    }

    @Test
    public void deleteTest() {
        boolean b = solrBaseDao.delete(new SolrSearch().build("name", "海通"));
        Assert.assertTrue(b);
    }
}
