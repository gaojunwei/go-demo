package com.jdjr.crawler.tcpj.utils;

import java.util.Random;

public class RandomUtils {
    private final static Random random = new Random();

    public static int nextInt(int bound) {
        return random.nextInt(bound);
    }

    public static int nextInt(int startBound, int endBound) {
        return random.nextInt(endBound - startBound) + startBound;
    }
}
