package com.test;

import java.util.ArrayList;

/**
 * @author: gaojunwei
 * @Date: 2019/2/27 16:12
 * @Description:
 */
public class OutOfMemoryMain {
    public static void main(String[] args) {
        ArrayList list=new ArrayList();
        while(true) {
            list.add(new OutOfMemoryMain());
        }
    }
}