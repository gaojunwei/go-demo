package com.ecust.test.jsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.io.IOException;

/**
 * 类描述：
 *
 * @Author gaojunwei
 * @Date 2021/6/18
 **/
public class MainTest {

    @Test
    public void test001() throws IOException {
        Document doc = Jsoup.connect("http://www.yiibai.com").get();
        System.out.println(doc.outerHtml());
        String title = doc.title();
        System.out.println("title is: " + title);//原文出自【易百教程】，商业转载请联系作者获得授权，非商业请保留原文链接：https://www.yiibai.com/jsoup/install-jsoup.html


    }
}
