package org.xyc.showsome.sample.taobaoip;

import org.xyc.showsome.util.FastjsonUtils;
import org.xyc.showsome.util.HttpClientUtils;

/**
 * 此类用于获取taobao的ip信息
 */
public class IpParserByTaobao {

    private final static String URL_PREFIX = "http://ip.taobao.com/service/getIpInfo.php?ip=";

    public static IpInfoByTaobao getInfo(String ip) {
        String str = HttpClientUtils.doGet(URL_PREFIX + ip);
        IpInfoByTaobao ipInfo = FastjsonUtils.parseObject(str, IpInfoByTaobao.class);
        return ipInfo;
    }

    public static void main(String[] args) {
        IpInfoByTaobao ipInfo = getInfo("180.168.198.139");
        System.out.println(ipInfo);
    }
}
