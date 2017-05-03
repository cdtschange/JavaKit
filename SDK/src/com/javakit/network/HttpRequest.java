package com.javakit.network;

import org.json.JSONObject;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by cdts on 17/03/2017.
 */
public class HttpRequest {

    private static class TrustAnyTrustManager implements X509TrustManager {

        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }
    }

    private static class TrustAnyHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url
     *            发送请求的URL
     * @param data
     *            请求参数
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, Map<String, String> data) {
        return sendGet(url, data, null);
    }
    public static String sendGet(String url, Map<String, String> data, RequestHandler handler) {
        if (url.startsWith("https://")) {
            return sendHttps(url, "GET", data, handler);
        } else if (url.startsWith("http://")) {
            return sendHttpGet(url, data, handler);
        } else {
            return "";
        }
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url
     *            发送请求的 URL
     * @param data
     *            请求参数
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, Map<String, String> data) {
        return sendPost(url, data, null);
    }
    public static String sendPost(String url, JSONObject json) {
        return sendPost(url, json, null);
    }
    public static String sendPost(String url, Map<String, String> data, RequestHandler handler) {
        if (url.startsWith("https://")) {
            return sendHttps(url, "POST", data, handler);
        } else if (url.startsWith("http://")) {
            return sendHttpPost(url, data, handler);
        } else {
            return "";
        }
    }
    public static String sendPost(String url, JSONObject json, RequestHandler handler) {
        if (url.startsWith("https://")) {
            return sendHttps(url, "POST", json, handler);
        } else if (url.startsWith("http://")) {
            return sendHttpPost(url, json, handler);
        } else {
            return "";
        }
    }

    /**
     * 参数编码
     * @param data
     * @return
     */
    public static String httpBuildQuery(Map<String, String> data) {
        if (data == null) {
            return null;
        }
        String ret = "";
        String k, v;
        Iterator<String> iterator = data.keySet().iterator();
        while (iterator.hasNext()) {
            k = iterator.next();
            v = data.get(k);
            try {
                ret += URLEncoder.encode(k, "utf8") + "=" + URLEncoder.encode(v, "utf8");
            } catch (UnsupportedEncodingException e) {
            }
            ret += "&";
        }
        return ret.substring(0, ret.length() - 1);
    }

    protected static String sendHttpGet(String url, Map<String, String> data, RequestHandler handler) {
        String result = "";
        BufferedReader in = null;
        try {
            String param = "";
            if (data != null && data.size() > 0) {
                param = httpBuildQuery(data);
            }
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }
    protected static String sendHttpPost(String url, Map<String, String> data, RequestHandler handler) {
        String param = "";
        if (data != null && data.size() > 0) {
            param = httpBuildQuery(data);
        }
        return sendHttpPost(url, param, handler);
    }
    protected static String sendHttpPost(String url, JSONObject json, RequestHandler handler) {
        return sendHttpPost(url, json.toString(), handler);
    }
    protected static String sendHttpPost(String url, String data, RequestHandler handler) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            String param = data;
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);

            if (handler != null) {
                handler.willRequest(conn);
            }
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }



    protected static String sendHttps(String url, String method, Map<String, String> data, RequestHandler handler) {
        return sendHttps(url, method, httpBuildQuery(data), handler);
    }
    protected static String sendHttps(String url, String method, JSONObject json, RequestHandler handler) {
        return sendHttps(url, method, json.toString(), handler);
    }
    protected static String sendHttps(String url, String method, String data, RequestHandler handler) {
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, new TrustManager[]{new HttpRequest.TrustAnyTrustManager()}, new java.security.SecureRandom());
            URL console = new URL(url);
            HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();
            conn.setRequestMethod(method);
            conn.setSSLSocketFactory(sc.getSocketFactory());
            conn.setHostnameVerifier(new HttpRequest.TrustAnyHostnameVerifier());
            // conn.connect();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            conn.setDoInput(true);
            conn.setDoOutput(true);

            if (handler != null) {
                handler.willRequest(conn);
            }

            PrintWriter writer = new PrintWriter(conn.getOutputStream());
            if (data != null) {
                writer.print(data);
            }
            writer.flush();
            writer.close();

            String line;
            BufferedReader bufferedReader;
            StringBuilder sb = new StringBuilder();
            InputStreamReader streamReader = null;
            try {
                streamReader = new InputStreamReader(conn.getInputStream(), "UTF-8");
            } catch (IOException e) {
            /*
            Boolean ret2 = true;
            if (ret2) {
                return e.getMessage();
            }
            */
                streamReader = new InputStreamReader(conn.getErrorStream(), "UTF-8");
            } finally {
                if (streamReader != null) {
                    bufferedReader = new BufferedReader(streamReader);
                    sb = new StringBuilder();
                    while ((line = bufferedReader.readLine()) != null) {
                        sb.append(line);
                    }
                }
            }
            return sb.toString();
        } catch (Exception e) {
            return e.toString();
        }
    }

    public interface RequestHandler {
        void willRequest(URLConnection conn);
    }

}
