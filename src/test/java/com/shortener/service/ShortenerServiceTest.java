package com.shortener.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.Random;

public class ShortenerServiceTest {

    @Test
    public void test() {
        String url = "https://";
        int[] array = {2, 3, 4};
        int pos = new Random().nextInt(array.length);
        int randomValue = array[pos];
        int min = 5;
        int max = 32;
        if (url.length() < 32) {
            max = url.length();
        }
        if (max >= url.length() / 2) {
            max /= randomValue;
        }
        if (min > max) {
            max += min;
        }
        String generated = RandomStringUtils.randomAlphabetic(min, max);
        System.out.println(generated);
    }

}
