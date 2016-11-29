package org.xyc.showsome.net;

import org.xyc.showsome.util.FastjsonUtil;
import org.xyc.showsome.util.HttpClientUtil;

/**
 * 此类用于获取taobao的ip信息
 */
public class IpParserByTaobao {

    private final static String URL_PREFIX = "http://ip.taobao.com/service/getIpInfo.php?ip=";

    public static IpInfoByTaobao getInfo(String ip) {
        String str = HttpClientUtil.doGet(URL_PREFIX + ip);
        IpInfoByTaobao ipInfo = FastjsonUtil.parseObject(str, IpInfoByTaobao.class);
        return ipInfo;
    }

    public static void main(String[] args) {
        IpInfoByTaobao ipInfo = getInfo("180.168.198.139");
        System.out.println(ipInfo);
    }
}
