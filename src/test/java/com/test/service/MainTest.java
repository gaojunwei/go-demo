package com.test.service;

import java.math.BigDecimal;
import java.util.Random;

public class MainTest {

    public static void main(String[] args) {
        Random random = new Random();
        BigDecimal maxlong = new BigDecimal("116.59939285809254");
        BigDecimal minlong = new BigDecimal("116.59729756625961");
        for (int i = 0; i < 10; i++) {
            double s = random.nextDouble(minlong.doubleValue(),maxlong.doubleValue());
            System.out.println(s+" - "+(s>=minlong.doubleValue() && s< maxlong.doubleValue()));
        }

    }
}
