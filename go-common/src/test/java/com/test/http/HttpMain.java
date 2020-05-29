package com.test.http;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class HttpMain {

    public static void main(String[] args) throws IOException {

        String url = "http://upos-hz-mirrorakam.akamaized.net/upgcxcode/62/95/195949562/195949562-1-80.flv?e=ig8euxZM2rNcNbe17zUVhoMHhWuBhwdEto8g5X10ugNcXBlqNxHxNEVE5XREto8KqJZHUa6m5J0SqE85tZvEuENvNC8xNEVE9EKE9IMvXBvE2ENvNCImNEVEK9GVqJIwqa80WXIekXRE9IMvXBvEuENvNCImNEVEua6m2jIxux0CkF6s2JZv5x0DQJZY2F8SkXKE9IB5QK==&deadline=1590760381&gen=playurl&nbs=1&oi=2806470600&os=akam&platform=pc&trid=d31e69166aaf47e18db30aedc4fd890b&uipk=5&upsig=edc8ed4a938079dc8df55aeec120c635&uparams=e,deadline,gen,nbs,oi,os,platform,trid,uipk&hdnts=exp=1590760381~hmac=73240efddb24ec16bbf090e90eff7668ee788d9002ab67211b4468aa88525f42&mid=0";

        HttpGet httpGet = new HttpGet(url);

        //httpGet.setHeader("Proxy-Connection", "keep-alive");
        httpGet.setHeader("Range", "bytes=163840-");
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 10_3 like Mac OS X) AppleWebKit/602.1.50 (KHTML, like Gecko) CriOS/56.0.2924.75 Mobile/14E5239e Safari/602.1");

        CloseableHttpResponse response = null;
        try {
            response = HttpClientTool.getHttpClient().execute(httpGet);
            System.out.println("getStatusCode : " + response.getStatusLine().getStatusCode());
            System.out.println("getContentType : " + response.getEntity().getContentType());
            System.out.println("getContentType.available : " + response.getEntity().getContent().available());

            writeToLocal("D:\\www\\aa.mp4", response.getEntity().getContent());
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    public static void writeToLocal(String destination, InputStream input) throws IOException {
        System.out.println("开始下载数据...");
        int index;
        byte[] bytes = new byte[1024];
        FileOutputStream downloadFile = new FileOutputStream(destination);
        while ((index = input.read(bytes)) != -1) {
            downloadFile.write(bytes, 0, index);
            downloadFile.flush();
            System.out.println("下载中 "+index);
        }
        input.close();
        downloadFile.close();
        System.out.println("下载数据完毕 END...");
    }

}
