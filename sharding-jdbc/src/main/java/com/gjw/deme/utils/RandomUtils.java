package com.gjw.deme.utils;

import java.util.Random;

public class RandomUtils {
    private final static Random random = new Random();

    public static int nextInt(int bound) {
        return random.nextInt(bound);
    }

    public static long nextLong(int bound) {
        return nextInt(bound);
    }

    public static int nextInt(int startBound, int endBound) {
        return random.nextInt(endBound - startBound) + startBound;
    }

    public static long nextLong(int startBound, int endBound) {
        return nextInt(startBound, endBound);
    }
}
