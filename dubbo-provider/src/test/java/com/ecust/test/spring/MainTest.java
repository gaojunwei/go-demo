package com.ecust.test.spring;

import com.google.gson.Gson;
import com.jdjr.crawler.tcpj.bean.CaseTwoA;
import com.jdjr.crawler.tcpj.bean.TestA;
import com.jdjr.crawler.tcpj.utils.ThreadSleepUtils;
import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;

public class MainTest {

    private Gson gson = new Gson();

    @Test
    public void test002() {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring-bean.xml");
        CaseTwoA caseTwoA = ctx.getBean("caseTwoA", CaseTwoA.class);
        caseTwoA.print();
        ThreadSleepUtils.sleep(1);
    }

    @Test
    public void test001() {
        BeanFactory beanFactory = new XmlBeanFactory(new ClassPathResource("spring-bean.xml"));
        TestA testA = beanFactory.getBean("testA", TestA.class);
        testA.print();
    }

    @Test
    public void test000() {
        BeanFactory beanFactory = new FileSystemXmlApplicationContext(new ClassPathResource("spring-bean.xml").getPath());
        TestA testA = beanFactory.getBean("testA", TestA.class);
        testA.print();
    }
}
