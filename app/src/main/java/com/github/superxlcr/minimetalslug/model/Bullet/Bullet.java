package com.github.superxlcr.minimetalslug.model.Bullet;

import android.graphics.Bitmap;

import com.github.superxlcr.minimetalslug.model.Player;

/**
 * 子弹基类
 * Created by superxlcr on 2016/5/10.
 */
public abstract class Bullet {
    // 子弹坐标
    private int x = 0;
    private int y = 0;
    // 子弹射击方向
    private int dir = Player.DIR_LEFT;
    // 子弹是否有效
    private boolean isEffect = true;

    public Bullet(int x, int y, int dir) {
        this.x = x;
        this.y = y;
        this.dir = dir;
    }

    /**
     * 获取子弹位图
     *
     * @return 子弹位图
     */
    public abstract Bitmap getBitmap();

    /**
     * 获取子弹x方向速度绝对值
     *
     * @return x方向速度绝对值
     */
    public abstract int getAbsSpeedX();

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
        x += getAbsSpeedX() * dir;
        y += getSpeedY();
    }

    /**
     * 移动子弹
     *
     * @param shift x左移大小，可为负值
     */
    public void updateShift(int shift) {
        x -= shift;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
