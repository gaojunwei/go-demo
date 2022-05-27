package com.gjw.deme.bean;

public class TestB {
    private TestC testC;
    public TestB(TestC testC){
        this.testC = testC;
    }

    public void print(){
        System.out.println("输出 TestB");
    }
}
