package com.github.superxlcr.minimetalslug.model;

import android.graphics.Bitmap;

/**
 * 子弹基类
 * Created by superxlcr on 2016/5/10.
 */
public abstract class Bullet {
    // 子弹坐标
    protected int x = 0;
    protected int y = 0;
    // 子弹射击方向
    protected int dir;
    // 子弹是否有效
    protected boolean isEffect = true;

    /**
     * 获取子弹位图
     *
     * @return 子弹位图
     */
    public abstract Bitmap getBitmap();

    /**
     * 获取子弹x方向速度
     *
     * @return x方向速度
     */
    public abstract int getSpeedX();

    /**
     * 获取子弹y方向速度
     *
     * @return y方向速度
     */
    public abstract int getSpeedY();

    /**
     * 控制子弹移动
     */
    public final void move() {
        x += getSpeedX();
        y += getSpeedY();
    }
}
