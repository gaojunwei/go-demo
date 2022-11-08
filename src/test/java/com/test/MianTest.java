package com.test;

import org.junit.Test;

import java.io.File;

public class MianTest {

    @Test
    public void test001() {
        String fileSavePath = "D:/www/aa/bb/1.txt";
        File path = new File(fileSavePath);
        if (!path.getParentFile().getParentFile().exists()) {
            path.mkdirs();
        }

    }
}
