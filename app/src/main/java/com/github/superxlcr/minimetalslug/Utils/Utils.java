package com.github.superxlcr.minimetalslug.Utils;

import java.util.Random;

/**
 * 工具类
 * Created by superxlcr on 2016/5/7.
 */
public final class Utils {
    private static Random random = new Random();

    /**
     * 返回一个0～range的随机数
     * @param range 范围
     * @return 随机数
     */
    public static int rand(int range) {
        if (range == 0)
            return 0;
        return Math.abs(random.nextInt() % range);
    }
}
