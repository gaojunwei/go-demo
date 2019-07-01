package com.test;

/**
 * @author: gaojunwei
 * @Date: 2018/10/12 11:05
 * @Description:
 */
public class MainTest {
    public static void main(String[] args) throws InterruptedException {
        String className = "com.test.MainTest";
        String[] classNames = className.split("\\.");
        System.out.println(classNames[classNames.length-1]);
    }
}