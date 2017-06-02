package org.xyc.showsome.pea;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by CCC on 2016/5/17.
 */
public class RegularExpressionPea {

    public static final String value = "~!@#$%^&*()_+-={}[];':\\<>?,./";

    public static String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; //定义script的正则表达式

    public static String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; //定义style的正则表达式

    public static String regEx_html = "<[^>]+>"; //定义HTML标签的正则表达式

    public static String regEx_illegal = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";

    public static String escapeIllegal() {
        Pattern pattern = Pattern.compile(regEx_illegal);
        Matcher matcher = pattern.matcher("skj#$f13~!@#$%^&a*()_+<y>?:f");
        String result = matcher.replaceAll("");
        System.out.println(result);
        return result;
    }

    public static String escapeScript() {
        Pattern pattern = Pattern.compile(regEx_script);
        Matcher matcher = pattern.matcher("567<script>alert('a')</script>abc");
        String result = matcher.replaceAll("");
        System.out.println(result);
        return result;
    }

    public static String filterIllegal(String value) {
        String patternStr = "((?=[\\x21-\\x7e]+)[^A-Za-z0-9&;#])";
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(value);
        String result = matcher.replaceAll("");
        System.out.println("origin:" + value);
        System.out.println("result:" + result);
        return result;
    }

    public static String scriptingFilter(String value) {
        StringBuffer result = new StringBuffer();
        if (value == null) {
        }
        for (int i = 0; i < value.length(); ++i) {
            switch (value.charAt(i)) {
                case '<':
                    result.append("&lt;");
                    break;
                case '>':
                    result.append("&gt;");
                    break;
                case '"':
                    result.append("&quot;");
                    break;
                case '\'':
                    result.append("&#39;");
                    break;
                case '%':
                    result.append("&#37;");
                    break;
                case ';':
                    result.append("&#59;");
                    break;
                case '(':
                    result.append("&#40;");
                    break;
                case ')':
                    result.append("&#41;");
                    break;
                case '&':
                    result.append("&amp;");
                    break;
                case '+':
                    result.append("&#43;");
                    break;
                default:
                    result.append(value.charAt(i));
                    break;
            }
        }
        System.out.println("origin:" + value);
        System.out.println("result:" + result);
        return result.toString();
    }

    public static void main(String[] args) {
        escapeIllegal();
//        filterIllegal(value);
//        scriptingFilter(value);
//        filterIllegal(scriptingFilter(value));
    }

}
