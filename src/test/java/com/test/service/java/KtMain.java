/*
package com.test.service.java;

import com.test.service.kt.KLazilyDCLSingleton;

public class KtMain {
    public static void main(String[] args) {
        //加了@JvmStatic注解后，可以直接KLazilyDCLSingleton.getInstance()，不会打破Java中调用习惯，和Java调用方式一样。
        System.out.println(KLazilyDCLSingleton.getInstance()==KLazilyDCLSingleton.getInstance());
        System.out.println(KLazilyDCLSingleton.getInstance());
        //没有加@JvmStatic注解，只能这样通过Companion调用
        System.out.println(KLazilyDCLSingleton.Companion.getInstance());
        System.out.println(KLazilyDCLSingleton.Companion.getInstance());
        System.out.println();
        System.out.println(KLazilyDCLSingleton.Companion.getStr()== KLazilyDCLSingleton.Companion.getStr());
    }
}
*/
