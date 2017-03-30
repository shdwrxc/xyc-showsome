package org.xyc.showsome.solr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.MapSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.xyc.showsome.solr.model.SimpleResponse;
import org.xyc.showsome.solr.utils.SolrSearch;
import org.xyc.showsome.solr.utils.SolrSort;

/**
 * Created by ccc on 2016/1/15.
 */
public class SolrBaseDao {

    public static final int httpConnectionTimeout = 10000;

    public static final int httpSoTimeout = 10000;

    private static final Logger logger = Logger.getLogger(SolrBaseDao.class);

    private String solrUrl;

    public SolrBaseDao(String solrUrl) {
        this.solrUrl = solrUrl;
    }

    /**
     * @param object
     * @param <T>
     * @return
     */
    public <T> boolean add(T object) {
        boolean result = false;
        try {
            HttpSolrServer httpSolrServer = getSolrServer(solrUrl);
            httpSolrServer.addBean(object);
            UpdateResponse updateResponse = httpSolrServer.commit();
            if (updateResponse.getStatus() == 0) {
                result = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param list
     * @param <T>
     * @return
     */
    public <T> boolean add(List<T> list) {
        boolean result = false;
        try {
            HttpSolrServer httpSolrServer = getSolrServer(solrUrl);
            httpSolrServer.addBean(list);
            UpdateResponse updateResponse = httpSolrServer.commit();
            if (updateResponse.getStatus() == 0) {
                result = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param solrSearch 搜索字段条件
     * @param solrSort   排序条件
     * @param start      第几个开始
     * @param rows       返回记录数
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> SimpleResponse<T> query(SolrSearch solrSearch, SolrSort solrSort, int start, int rows, Class<T> clazz) {
        SimpleResponse sr = new SimpleResponse();
        List<T> list = new ArrayList<T>();
        sr.setRecords(list);
        try {
            HttpSolrServer httpSolrServer = getSolrServer(solrUrl);
            SolrParams solrParams = new MapSolrParams(getQueryMap(solrSearch, solrSort, start, rows));
            logger.info("solr query : " + solrSearch.toString());
            QueryResponse response = httpSolrServer.query(solrParams);
            list.addAll(response.getBeans(clazz));
            SolrDocumentList solrDocumentList = response.getResults();
            if (solrDocumentList != null)
                sr.setTotal((int) solrDocumentList.getNumFound());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sr;
    }

    /**
     * @param solrSearch 删除条件
     * @return
     */
    public boolean delete(SolrSearch solrSearch) {
        boolean result = false;
        try {
            HttpSolrServer httpSolrServer = getSolrServer(solrUrl);
            httpSolrServer.deleteByQuery(solrSearch.toString());
            UpdateResponse updateResponse = httpSolrServer.commit();
            if (updateResponse.getStatus() == 0) {
                result = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param solrSearch 搜索字段条件
     * @param solrSort   排序字段条件
     * @param start      开始数，开始位为0
     * @param rows       记录数
     * @return
     */
    private Map<String, String> getQueryMap(SolrSearch solrSearch, SolrSort solrSort, int start, int rows) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("q", solrSearch.toString());
        params.put("start", String.valueOf(start));
        params.put("rows", String.valueOf(rows));
        params.put("sort", solrSort.toString());
        return params;
    }

    /**
     * 创建solr连接
     *
     * @param url
     * @return
     */
    public HttpSolrServer getSolrServer(String url) {
        HttpSolrServer httpSolrServer = new HttpSolrServer(url);
        httpSolrServer.setConnectionTimeout(httpConnectionTimeout);
        httpSolrServer.setSoTimeout(httpSoTimeout);
        return httpSolrServer;
    }
}
