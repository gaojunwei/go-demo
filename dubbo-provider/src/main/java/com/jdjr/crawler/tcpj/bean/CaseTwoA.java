package com.jdjr.crawler.tcpj.bean;

import lombok.Setter;

@Setter
public class CaseTwoA {
    private CaseTwoB caseTwoB;

    public void print(){
        System.out.println("输出 CaseTwoA "+caseTwoB);
    }
}