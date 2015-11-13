package com.csxz.snowflack.custompulltorefresh;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 * function : URL请求的封装 get post,的请求
 * author : cfg
 * E-mail : cfgnqk@sina.com
 * <p>
 * Created by on : 15-11-13.
 */
public class URLTools {
    //私有构造 不准创建对象
    private URLTools() {
    }

    /**
     * 通过URL get方式 获得数据
     *
     * @param urlpath
     * @return
     */
    public static InputStream getStreamByGet(String urlpath) {
        InputStream inputStream = null;
        URL url = null;
        HttpURLConnection conn = null;
        try {
            if (url != null) {
                url = new URL(urlpath);
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);//设置超时的时间
                conn.setDoInput(true);
                conn.setRequestMethod("GET");//设置请求方式
                int responseCode = conn.getResponseCode();//获取响应码
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    inputStream = conn.getInputStream();
                    String enCoding = conn.getContentEncoding();//检测Content-Encoding 类型
                    if ("gzip".equals(enCoding)) {//如果是压缩的格式
                        inputStream = new GZIPInputStream(inputStream);//转化位压缩的格式
                    }
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputStream;
    }

    /**
     * URL post 提交数据  提交字符串
     *
     * @param urlPath
     * @param params
     * @param encode
     * @return
     */
    public static InputStream getStreamByPostString(String urlPath, String params, String encode) {
        URL url = null;
        InputStream inputStream = null;
        HttpURLConnection conn = null;
        try {
            if (urlPath != null) {
                url = new URL(urlPath);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setConnectTimeout(5000);
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);               //使用Post方式不能使用缓存
                //设置必要的头信息
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("Charset", "UTF-8");// 设置字符编码

                // 设置请求体的类型是文本类型 表示当前的提交的数据为 纯文本类型 不包括 二进制媒体数据 ,根据需求设置
                byte[] paramsbyte = params.getBytes(encode);
                conn.setRequestProperty("Content-Length", String.valueOf(paramsbyte.length));//提交参数 的内容长度

                //向服务器写入流
                OutputStream outputStream = conn.getOutputStream();
                outputStream.write(paramsbyte);
                outputStream.close();

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    inputStream = conn.getInputStream();
                    String encoding = conn.getContentEncoding();//获取内容格式
                    if ("gzip".equals(encoding)) {//如果是压缩的格式
                        inputStream = new GZIPInputStream(inputStream);//转化位压缩的格式
                    }
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return inputStream;
    }

    /**
     * 通过URL POST 提交数据   提交 map格式<br/>
     * 此处直只可以提交普通的字符串拼接数据
     *
     * @param urlPath
     * @param encode
     * @return
     */
    public static InputStream getStreamByPostMap(String urlPath, Map<String, String> params, String encode) {
        InputStream inputStream;
        StringBuilder strBuilder = null;
        try {
            if (params != null && !params.isEmpty()) {
                strBuilder = new StringBuilder();
                //将要提交的数据进行拼接 成key=value&key2=value2 的形式
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    strBuilder.append(entry.getKey())
                            .append("=")
                            .append(URLEncoder.encode(entry.getValue(), encode))
                            .append("&");

                }
                strBuilder.deleteCharAt(strBuilder.length() - 1);// 最后多了一个字符& 删除最后一个字符
            }
        } catch (UnsupportedEncodingException e) {

        }
        inputStream = getStreamByPostString(urlPath, strBuilder.toString(), encode);
        return inputStream;

    }

    /**
     * Url  post 提交json 数据
     * @param urlPath
     * @param jsonObject
     * @param encode
     * @return
     */
    public static InputStream getStreamByPostJSON(String urlPath, JSONObject jsonObject, String encode) {
        InputStream inputStream = null;
        inputStream = getStreamByPostString(urlPath, jsonObject.toString(), encode);
        return inputStream;
    }

}
