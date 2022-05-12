package com.jdjr.crawler.tcpj.bean;

public class TestC {
    public TestA testA;
    public TestC(TestA testA){
        this.testA = testA;
    }

    public void print(){
        System.out.println("输出 TestC");
    }
}
