package com.eric.util;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by eric on 2016/9/9.
 */
public class HttpClientUtils {

    protected static Logger logger = (Logger
            .getLogger(HttpClientUtils.class));

    private static PoolingClientConnectionManager clientConnectionManager;
    /**
     * OK: Success!
     */
    public static final int OK = 200;
    /**
     * Not Modified: There was no new data to return.
     */
    public static final int NOT_MODIFIED = 304;
    /**
     * Bad Request: The request was invalid. An accompanying error message will
     * explain why. This is the status code will be returned during rate
     * limiting.
     */
    public static final int BAD_REQUEST = 400;
    /**
     * Not Authorized: Authentication credentials were missing or incorrect.
     */
    public static final int NOT_AUTHORIZED = 401;
    /**
     * Forbidden: The request is understood, but it has been refused. An
     * accompanying error message will explain why.
     */
    public static final int FORBIDDEN = 403;
    /**
     * Not Found: The URI requested is invalid or the resource requested, such
     * as a user, does not exists.
     */
    public static final int NOT_FOUND = 404;
    /**
     * Not Acceptable: Returned by the Search API when an invalid format is
     * specified in the request.
     */
    public static final int NOT_ACCEPTABLE = 406;
    /**
     * Internal Server Error: Something is broken. Please post to the group so
     * the Weibo team can investigate.
     */
    public static final int INTERNAL_SERVER_ERROR = 500;
    /**
     * Bad Gateway: Weibo is down or being upgraded.
     */
    public static final int BAD_GATEWAY = 502;
    /**
     * Service Unavailable: The Weibo servers are up, but overloaded with
     * requests. Try again later. The search and trend methods use this to
     * indicate when you are being rate limited.
     */
    public static final int SERVICE_UNAVAILABLE = 503;

    public static final int TIME_OUT = 15000;

    public static final String SOCKET_TIMEOUT = "http.socket.timeout";
    public static final String COLLECTION_TIMEOUT = "http.connection.timeout";
    public static final String COLLECTION_MANAGER_TIMEOUT = "http.connection-manager.timeout";

    static {
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory
                .getSocketFactory()));
        schemeRegistry.register(new Scheme("https", 443, SSLSocketFactory
                .getSocketFactory()));

        clientConnectionManager = new PoolingClientConnectionManager(
                schemeRegistry);
        clientConnectionManager.setMaxTotal(32);
        clientConnectionManager.setDefaultMaxPerRoute(200);
    }

//    public static HttpClient getHttpClient() {
//        return new DefaultHttpClient(clientConnectionManager);
//    }

    @SuppressWarnings("unused")
    private static String getCause(int statusCode) {
        String cause = null;
        switch (statusCode) {
            case NOT_MODIFIED:
                break;
            case BAD_REQUEST:
                cause = "The request was invalid.  An accompanying error message will explain why. This is the status code will be returned during rate limiting.";
                break;
            case NOT_AUTHORIZED:
                cause = "Authentication credentials were missing or incorrect.";
                break;
            case FORBIDDEN:
                cause = "The request is understood, but it has been refused.  An accompanying error message will explain why.";
                break;
            case NOT_FOUND:
                cause = "The URI requested is invalid or the resource requested, such as a user, does not exists.";
                break;
            case NOT_ACCEPTABLE:
                cause = "Returned by the Search API when an invalid format is specified in the request.";
                break;
            case INTERNAL_SERVER_ERROR:
                cause = "Something is broken.  Please post to the group so the liushijie can investigate.";
                break;
            case BAD_GATEWAY:
                cause = "image server is down or being upgraded.";
                break;
            case SERVICE_UNAVAILABLE:
                cause = "Service Unavailable: img servers are up, but overloaded with requests. Try again later. The search and trend methods use this to indicate when you are being rate limited.";
                break;
            default:
                cause = "";
        }

        return statusCode + ":" + cause;
    }

    /**
     * 根据URL发送get请求获取数据
     *
     * @param url
     * @return
     */
    public static String doGet(String url) {
        String result = null;
        HttpGet get = new HttpGet(url);
        try {
            HttpResponse response = getHttpClient().execute(get);
            HttpEntity resEntity = response.getEntity();
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != OK) {
                get.abort();
                return null;
            }
            if (resEntity != null) {
                String respBody = EntityUtils.toString(resEntity);
                try {
                    result = respBody;
                } catch (Exception e) {
                    logger.error("+++++==> respBody:" + respBody + " <==+++++",
                            e);
                }
            }
        } catch (IOException e) {
            logger.error("++++ doGet:" + url + " ++++++", e);
        } finally {
            get.releaseConnection();
        }
        return result;
    }

    /**
     * 根据URL发送post请求获取数据
     *
     * @param url
     * @param paramsMap
     * @return
     */
    public static String doPost(String url, Map<String, String> paramsMap) {
        String result = null;
        HttpPost post = new HttpPost(url);
        try {
            if (paramsMap != null && paramsMap.size() > 0) {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                for (Map.Entry<String, String> m : paramsMap.entrySet()) {
                    params.add(new BasicNameValuePair(m.getKey(), m.getValue()));
                }
                UrlEncodedFormEntity reqEntity = new UrlEncodedFormEntity(params,
                        "UTF-8");
                post.setEntity(reqEntity);
            }
            HttpResponse response = getHttpClient().execute(post);

            HttpEntity resEntity = response.getEntity();
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != OK) {
                post.abort();
                return null;
            }
            if (resEntity != null) {
                String respBody = EntityUtils.toString(resEntity);
                try {
                    result = respBody;
                } catch (Exception e) {
                    logger.error("+++++==> respBody:" + respBody + " <==+++++",
                            e);
                }
            }
        } catch (IOException e) {
            logger.error("+++++==> doPost:" + url + " <==+++++", e);
        } finally {
            post.releaseConnection();
        }
        return result;
    }


    public static String doPostWithException(String url, Map<String, String> paramsMap) throws Exception {
        String result = null;
        HttpPost post = new HttpPost(url);
        try {
            if (paramsMap != null && paramsMap.size() > 0) {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                for (Map.Entry<String, String> m : paramsMap.entrySet()) {
                    params.add(new BasicNameValuePair(m.getKey(), m.getValue()));
                }
                UrlEncodedFormEntity reqEntity = new UrlEncodedFormEntity(params,
                        "UTF-8");
                post.setEntity(reqEntity);
            }
            HttpResponse response = getHttpClient().execute(post);

            HttpEntity resEntity = response.getEntity();
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != OK) {
                post.abort();
                return null;
            }
            if (resEntity != null) {
                result = EntityUtils.toString(resEntity);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            post.releaseConnection();
        }
        return result;
    }

    public static String doPostByJson(String url, String json) {
        String result = null;
        HttpPost post = new HttpPost(url);
        try {
            StringEntity entity = new StringEntity(json, "utf-8");
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            post.setEntity(entity);
            HttpResponse response = getHttpClient().execute(post);
            HttpEntity resEntity = response.getEntity();
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != OK) {
                post.abort();
                return null;
            }
            if (resEntity != null) {
                String respBody = EntityUtils.toString(resEntity);
                try {
                    result = respBody;
                } catch (Exception e) {
                    logger.error("+++++==> respBody:" + respBody + " <==+++++",
                            e);
                }
            }
        } catch (IOException e) {
            logger.error("+++++==> doPost:" + url + " <==+++++", e);
        } finally {
            post.releaseConnection();
        }
        return result;
    }

    public static HttpClient getHttpClient() {

        HttpClient httpClient = new DefaultHttpClient(clientConnectionManager);
        httpClient.getParams().setParameter(SOCKET_TIMEOUT, TIME_OUT);
        httpClient.getParams().setParameter(COLLECTION_TIMEOUT, TIME_OUT);
        httpClient.getParams().setParameter(COLLECTION_MANAGER_TIMEOUT, 10000000l);
        return httpClient;
    }


    /**
     * 下载文件
     *
     * @param urlString
     * @param path
     * @param name
     */
    public static boolean downloadPicture(String urlString, String path, String name) {
        try {
            URL url = new URL(urlString);
            DataInputStream dataInputStream = new DataInputStream(url.openStream());
            File sf = new File(path);
            if (!sf.exists()) {
                sf.mkdirs();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(new File(path + File.separator + name));
            byte[] buffer = new byte[1024];
            int length;
            while ((length = dataInputStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, length);
            }
            dataInputStream.close();
            fileOutputStream.close();
            return true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
