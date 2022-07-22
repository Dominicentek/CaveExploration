package com.caveexp.util;

import java.util.Random;

public class RNG {
    private static final Random random = new Random();
    public static void setSeed(long seed) {
        random.setSeed(seed);
    }
    public static int range(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }
    public static boolean chance(int chance) {
        return random.nextInt(chance) == 0;
    }
    public static long random() {
        return random.nextLong();
    }
}
