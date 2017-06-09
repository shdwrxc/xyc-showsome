package org.xyc.showsome.pea;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * created by wks on date: 2017/6/9
 */
public class RemoteFilePea {

    public static void shadowclaw1() throws Exception {
        String file = "http://localhost:8080/pages/hello.html";
        URL url = new URL(file);
        HttpURLConnection urlCon = (HttpURLConnection) url.openConnection();
        urlCon.setConnectTimeout(1000 * 6);
        urlCon.setReadTimeout(1000 * 60);
        BufferedReader br = new BufferedReader(new InputStreamReader(urlCon.getInputStream()));
        String str = "";
        while ((str = br.readLine()) != null) {
            System.out.println(str);
        }
    }

    public static void main(String[] args) throws Exception {
        shadowclaw1();
    }
}
