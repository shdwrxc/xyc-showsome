package org.xyc.showsome.util;

import javax.servlet.http.HttpServletRequest;

public class HttpRequestUtil {

    private final static String BROWSER_IE = "MSIE";
    private final static String BROWSER_EDGE = "Edge";
    private final static String BROWSER_CHROME = "Chrome";
    private final static String BROWSER_FIREFOX = "Firefox";
    private final static String BROWSER_IE11 = "Trident/7.0";

    private final static String BROWSER_IE_SHOW = "Internet Explorer ";
    private final static String BROWSER_IE_11_SHOW = "Internet Explorer 11";

    private final static String OS_TIP_IN_USER_AGENT = "Windows";

    private final static String SEMICOLON = ";";
    private final static String BLANK = " ";

    /**
     * mac的useragent
     * Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_2; en-us) AppleWebKit/531.21.8 (KHTML, like Gecko) Version/4.0.4 Safari/531.21.10
     *
     * chrome（54.0.2840.59 m (64-bit)）
     * Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.59 Safari/537.36
     * 为什么会这样？
     * Chrome 使用了 WebKit 作为渲染引擎，又希望收到给 Safari 提供的网页。WebKit 基于 KHTML 开发，而 KHTML 的开发者希望获得提供给 Firefox 的页面，所以声明自己“like Gecko”……于是就变成了这样
     *
     * firefox（50.0.1）
     * Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:50.0) Gecko/20100101 Firefox/50.0
     *
     * edge（Microsoft Edge 38.14393.0.0）（Microsoft EdgeHTML 14.14393）
     * Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.79 Safari/537.36 Edge/14.14393
     *
     * ie11（11.447.14393.0）
     * Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko
     *
     * ie8
     * Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; WOW64; Trident/4.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; InfoPath.3; .NET4.0E)
     *
     * 从ie6到ie10，获取的agent一直MSIE 版本号，如MSIE 8.0，MSIE 10.0
     * 从ie11开始，就没有这样的标识了。取而代之的是“like Gecko”
     *
     * Mozilla 是互联网上古时代流行的浏览器，IE 最早为了获取提供给 Mozilla 的网页，于是宣称自己“兼容”Mozilla，在 User-Agent 里面加入了 Mozilla 和 compatible 的字样，也是开创了 User-Agent 伪装的先河。
     * MSIE 是 IE 系列浏览器（曾经）的固有标识，基本上所有检测用户代理的代码里面都能看到它。Trident 则是 IE 所使用的渲染引擎的标识
     *
     * Firefox 是 Mozilla 的正统后继，Gecko 则是 Firefox 使用的渲染引擎。因为曾经 Firefox 的流行，“Gecko”也成了下一个被伪装的对象
     * 所以说，IE 11 去掉了“MSIE”，改成了“like Gecko”，也就是说，它不想被当成 IE，而是想被当成 Firefox
     *
     * ie的判断
     * Token	    Description
     * Edge	        Windows Edge
     * Trident/7.0	IE11
     * Trident/6.0	Internet Explorer 10
     * Trident/5.0	Internet Explorer 9
     * Trident/4.0	Internet Explorer 8
     *
     * @param userAgent
     * @return
     */
    public static String getClientBrowser(String userAgent) {
        if (StringUtil.isEmpty(userAgent)) {
            return "";
        }
        int i = 0;
        if ((i = userAgent.indexOf(BROWSER_IE)) > -1) {
            return BROWSER_IE_SHOW + userAgent.substring(i + 5, i + 6);
        }
        if ((i = userAgent.indexOf(BROWSER_EDGE)) > -1) {
            String edgeSplit = userAgent.substring(i);
            return edgeSplit.substring(0, StringUtil.findFirstSplit(edgeSplit, BLANK)).replace("/", " ");
        }
        if ((i = userAgent.indexOf(BROWSER_CHROME)) > -1) {
            String chromeSplit = userAgent.substring(i);
            return chromeSplit.substring(0, StringUtil.findFirstSplit(chromeSplit, BLANK)).replace("/", " ");
        }
        if ((i = userAgent.indexOf(BROWSER_FIREFOX)) > -1) {
            String firefoxSplit = userAgent.substring(i);
            return firefoxSplit.substring(0, StringUtil.findFirstSplit(firefoxSplit, BLANK)).replace("/", " ");
        }
        if (userAgent.contains(BROWSER_IE11)) {
            return BROWSER_IE_11_SHOW;
        }
        return "";
    }

    /**
     *
     * @param request
     * @return
     */
    public static String getClientBrowser(HttpServletRequest request) {
        if (request == null)
            return "";
        return getClientBrowser(request.getHeader("user-agent"));
    }

    /**
     * xp是windows nt5.1，vista是windows nt6.0，win7是windows nt6.1，win8是windows nt6.2, win10是windows nt 10.0
     * @param userAgent
     * @return
     */
    public static String getClientOsKernel(String userAgent) {
        if (StringUtil.isEmpty(userAgent)) {
            return "";
        }
        int i = 0;
        if ((i = userAgent.indexOf(OS_TIP_IN_USER_AGENT)) > -1) {
            String split = userAgent.substring(i);
            return split.substring(0, StringUtil.findFirstSplit(split, SEMICOLON));
        }
        return "";
    }

    public static String getClientOsKernel(HttpServletRequest request) {
        if (request == null)
            return "";
        return getClientOsKernel(request.getHeader("user-agent"));
    }

    public static String getClientIp(HttpServletRequest request) {
        if (request.getHeader("x-forwarded-for") == null) {
            return request.getRemoteAddr();
        }
        return request.getHeader("x-forwarded-for");
    }

    public static void main(String[] args) {
        String str = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.59 Safari/537.36";
        System.out.println(getClientBrowser(str));
        System.out.println(getClientOsKernel(str));
    }
}
