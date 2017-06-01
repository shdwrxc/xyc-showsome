package org.xyc.showsome.pea;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * created by wks on date: 2017/6/1
 */

public class HttpClientPea {

    private final static int CLIENT_MAX_TOTAL = 20;
    private final static int TIMEOUT_CONNECTION = 1000 * 10;

    private final static CloseableHttpClient httpClient = getClient();


    public static void sendPost() {
        HttpPost post = new HttpPost("http://ip.taobao.com/service/getIpInfo.php");
        List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        params.add(new BasicNameValuePair("ip", "180.168.198.139"));

        post.setEntity(new UrlEncodedFormEntity(params, Charset.defaultCharset()));
        //        HttpEntity entity = new StringEntity("abc", Charset.defaultCharset());

        try {
            CloseableHttpResponse response = httpClient.execute(post);

            System.out.println("----------------------------------------");
            System.out.println(response.getStatusLine());

            // Get hold of the response entity
            HttpEntity entity = response.getEntity();

            // If the response does not enclose an entity, there is no need
            // to bother about connection release
            StringBuilder sb = new StringBuilder();
            if (entity != null) {
                InputStream instream = entity.getContent();
                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(instream));
                    String str;
                    while ((str = br.readLine()) != null) {
                        sb.append(str);
                    }
                    System.out.println(sb.toString());
                    //                        instream.read();
                    // do something useful with the response
                } catch (IOException ex) {
                    // In case of an IOException the connection will be released
                    // back to the connection manager automatically
                    throw ex;
                } finally {
                    // Closing the input stream will trigger connection release
                    instream.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendGet() throws Exception {

        try {
            HttpGet httpget = new HttpGet("http://ip.taobao.com/service/getIpInfo.php?ip=180.168.198.139");

            System.out.println("Executing request " + httpget.getRequestLine());
            CloseableHttpResponse response = httpClient.execute(httpget);
            try {
                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());

                // Get hold of the response entity
                HttpEntity entity = response.getEntity();

                // If the response does not enclose an entity, there is no need
                // to bother about connection release
                StringBuilder sb = new StringBuilder();
                if (entity != null) {
                    InputStream instream = entity.getContent();
                    try {
                        BufferedReader br = new BufferedReader(new InputStreamReader(instream));
                        String str;
                        while ((str = br.readLine()) != null) {
                            sb.append(str);
                        }
                        System.out.println(sb.toString());
//                        instream.read();
                        // do something useful with the response
                    } catch (IOException ex) {
                        // In case of an IOException the connection will be released
                        // back to the connection manager automatically
                        throw ex;
                    } finally {
                        // Closing the input stream will trigger connection release
                        instream.close();
                    }
                }
            } finally {
                response.close();
            }
        } finally {
//            httpclient.close();
        }
    }

    public static void sendGet2() throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpGet httpget = new HttpGet("http://httpbin.org/get");

            System.out.println("Executing request " + httpget.getRequestLine());
            CloseableHttpResponse response = httpclient.execute(httpget);
            try {
                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());

                // Get hold of the response entity
                HttpEntity entity = response.getEntity();

                // If the response does not enclose an entity, there is no need
                // to bother about connection release
                if (entity != null) {
                    InputStream instream = entity.getContent();
                    try {
                        instream.read();
                        // do something useful with the response
                    } catch (IOException ex) {
                        // In case of an IOException the connection will be released
                        // back to the connection manager automatically
                        throw ex;
                    } finally {
                        // Closing the input stream will trigger connection release
                        instream.close();
                    }
                }
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
    }

    public static void sendGet1() throws Exception {

        try {
            HttpGet httpget = new HttpGet("http://ip.taobao.com/service/getIpInfo.php?ip=180.168.198.139");

            System.out.println("Executing request " + httpget.getRequestLine());
            try {

                // Create a custom response handler
                ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

                    @Override
                    public String handleResponse(
                            final HttpResponse response) throws ClientProtocolException, IOException {
                        int status = response.getStatusLine().getStatusCode();
                        if (status >= 200 && status < 300) {
                            HttpEntity entity = response.getEntity();
                            return entity != null ? EntityUtils.toString(entity) : null;
                        } else {
                            throw new ClientProtocolException("Unexpected response status: " + status);
                        }
                    }

                };

                String responseBody = httpClient.execute(httpget, responseHandler);
                System.out.println(responseBody);
                JSONObject obj = JSON.parseObject(responseBody);
                System.out.println(JSON.toJSONString(obj));
            } finally {
//                response.close();
            }
        } finally {
            //            httpclient.close();
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
//        CloseableHttpClient httpClient = HttpClients.createDefault();


        return httpClient;
    }

    public static String decodeUnicode(final String dataStr) {
        int start = 0;
        int end = 0;
        final StringBuffer buffer = new StringBuffer();
        while (start > -1) {
            end = dataStr.indexOf("\\u", start + 2);
            String charStr = "";
            if (end == -1) {
                charStr = dataStr.substring(start + 2, dataStr.length());
            } else {
                charStr = dataStr.substring(start + 2, end);
            }
            char letter = (char) Integer.parseInt(charStr, 16); // 16进制parse整形字符串。
            buffer.append(new Character(letter).toString());
            start = end;
        }
        return buffer.toString();
    }

    private static String getUnicode(String s) {
        try {
            StringBuffer out = new StringBuffer("");
            byte[] bytes = s.getBytes("unicode");
            for (int i = 0; i < bytes.length - 1; i += 2) {
                out.append("\\u");
                String str = Integer.toHexString(bytes[i + 1] & 0xff);
                for (int j = str.length(); j < 2; j++) {
                    out.append("0");
                }
                String str1 = Integer.toHexString(bytes[i] & 0xff);
                out.append(str1);
                out.append(str);

            }
            return out.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        try {
            sendPost();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
