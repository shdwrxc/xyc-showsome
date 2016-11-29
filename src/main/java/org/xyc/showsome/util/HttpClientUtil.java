package org.xyc.showsome.util;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpClientUtil {

    private final static int CLIENT_MAX_TOTAL = 20;
    private final static int TIMEOUT_CONNECTION = 1000 * 10;

    private final static CloseableHttpClient httpClient = getClient();

    public static String doPost(String url, Map<String, String> param) {
        HttpPost httpPost = new HttpPost(url);
        List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();

        if (param != null && param.size() > 0) {
            for (String key : param.keySet()) {
                params.add(new BasicNameValuePair(key, param.get(key)));
            }
        }
        httpPost.setEntity(new UrlEncodedFormEntity(params, Charset.defaultCharset()));

        //往body中存放json形式的参数
//        JSONObject postData = new JSONObject();
//        if (param != null && param.size() > 0) {
//            for (String key : param.keySet()) {
//                postData.put(key, param.get(key));
//            }
//        }
//        StringEntity stringEntity = new StringEntity(postData.toString(), Charset.defaultCharset());
//        stringEntity.setContentType("application/json");
//        httpPost.setEntity(stringEntity);

        try {
            return httpClient.execute(httpPost, new XycResponseHandler());
        } catch (Exception e) {

        }

        return "";
    }

    @Deprecated
    private static String respString(HttpEntity entity) {
        StringBuilder sb = new StringBuilder();

        try {
            if (entity != null) {
                InputStream in = entity.getContent();
                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));
                    String str;
                    while ((str = br.readLine()) != null) {
                        sb.append(str);
                    }
                } catch (IOException ex) {
                    throw ex;
                } finally {
                    in.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static String doGet(String url) {
        HttpGet httpGet = new HttpGet(url);

        try {
            return httpClient.execute(httpGet, new XycResponseHandler());
        } catch (Exception e) {

        }
        return "";
    }

    private static class XycResponseHandler implements ResponseHandler<String> {
        @Override
        public String handleResponse(HttpResponse response) throws IOException {
            int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                return entity != null ? EntityUtils.toString(entity) : "";
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
        }
    }

    private static CloseableHttpClient getClient() {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(CLIENT_MAX_TOTAL);
        cm.setDefaultMaxPerRoute(CLIENT_MAX_TOTAL);

        RequestConfig config = RequestConfig.custom()
                .setSocketTimeout(TIMEOUT_CONNECTION)
                .setConnectTimeout(TIMEOUT_CONNECTION)
                .build();

        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(cm)
                .setDefaultRequestConfig(config)
                .build();
        return httpClient;
    }

    private static CloseableHttpClient getDefaultClient() {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        return httpClient;
    }

    public static void main(String[] args) {
        Map<String, String> map = Maps.newHashMap();
        map.put("ip", "180.168.198.139");
        String str = doPost("http://ip.taobao.com/service/getIpInfo.php", map);

        String str2 = doGet("http://ip.taobao.com/service/getIpInfo.php?ip=180.168.198.139");

        System.out.println(str);
        System.out.println(str2);
    }
}
