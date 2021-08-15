package client;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import server.HttpServerStart;

import java.io.IOException;

/**
 * @Description:
 * @ProjectName: week02
 * @Package: client
 * @ClassName: HttpClientUtil
 * @Author: huxing
 * @DateTime: 2021-08-15 下午5:43
 */
public class HttpClientUtil {

    /**
     * @Description: Http 请求处理
     * @Author: huxing
     * @param ip
     * @param port
     * @Date: 2021/8/15 下午9:11
     **/
    public void sendHttpRequest(String ip, int port){
        // 获得Http客户端
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // 创建Get请求
        HttpGet httpGet = new HttpGet(formatURL(ip, port));
        // 响应模型
        CloseableHttpResponse response = null;
        try {
            // 由客户端执行(发送)Get请求
            response = httpClient.execute(httpGet);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();
            System.out.println("响应状态为:" + response.getStatusLine());
            if (responseEntity != null) {
                System.out.println("响应内容长度为:" + responseEntity.getContentLength());
                System.out.println("响应内容为:" + EntityUtils.toString(responseEntity));
            }
        } catch (ParseException | IOException e) {
            System.out.println("连接失败"+e.getMessage());
        } finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /** url 模版 **/
    private final String url = "http://%s:%s/";

    /**
     * @Description: 生成url访问连接
     * @Author: huxing
     * @param ip
     * @param port
     * @return java.lang.String
     * @Date: 2021/8/15 下午7:22
     **/
    private String formatURL(String ip, int port){
        return String.format(url, ip, port);
    }

    public static void main(String[] args) {
        // 先启动 3个http 服务
        new HttpServerStart().run();
        // 访问nio1
        new HttpClientUtil().sendHttpRequest("localhost", 8801);
        // 访问nio2
        new HttpClientUtil().sendHttpRequest("localhost", 8802);
        // 访问nio3
        new HttpClientUtil().sendHttpRequest("localhost", 8803);
    }
}
