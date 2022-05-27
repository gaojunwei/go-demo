package com.gjw.deme.bean;

public class TestA {
    private TestB testB;

    public TestA(TestB testB){
        this.testB = testB;
    }

    public void print(){
        System.out.println("输出 TestA");
    }
}
