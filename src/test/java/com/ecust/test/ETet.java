package com.ecust.test;

import com.jdjr.crawler.tcpj.Application;
import com.jdjr.crawler.tcpj.common.util.ChromeDriverUtils;
import com.jdjr.crawler.tcpj.service.TCPJService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * 类描述
 *
 * @Author gaojunwei
 * @Date 2020/7/15 15:00
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Slf4j
public class ETet {
    @Resource
    private TCPJService tcpjService;

    @Test
    public void test001() {
        for (int i = 0; i < 1; i++) {
            new Task(tcpjService).start();
        }
        sleepMM(5000);
    }


    public class Task extends Thread {
        private String url;
        private String account;
        private String password;
        private TCPJService tcpjService;

        public Task(TCPJService tcpjService) {
            this.url = "https://www.tcpjw.com/passport/login";
            this.tcpjService = tcpjService;
            this.account = "13910099494";
            this.password = "123QWEasd";
        }

        @Override
        public void run() {
            try {
                String result = tcpjService.getLoginToken(url, account, password);
                System.out.println(Thread.currentThread().getName() + " result=" + result);
            }catch (Exception e){
                System.out.println("发生异常");
                e.printStackTrace();
            }
        }
    }

    public void sleepMM(Integer second) {
        try {
            Thread.sleep(second * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void sleepMS(Integer second) {
        try {
            Thread.sleep(second);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
