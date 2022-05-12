package com.jdjr.crawler.tcpj.bean;

public class CaseTwoB {
    private CaseTwoA caseTwoA;

    public CaseTwoB(CaseTwoA caseTwoA) {
        this.caseTwoA = caseTwoA;
    }

    public void print() {
        System.out.println("输出 CaseTwoB " + caseTwoA);
    }
}
