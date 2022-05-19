package com.ecust.test.zk;

import java.util.Random;

public class RandomUtils {

    public static long nextLong(){
        Random rand = new Random();
        return rand.nextInt(400)+ 100;
    }
}
