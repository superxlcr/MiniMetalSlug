package com.github.superxlcr.minimetalslug.model.Monster;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.github.superxlcr.minimetalslug.MainActivity;
import com.github.superxlcr.minimetalslug.R;
import com.github.superxlcr.minimetalslug.Utils.ResourceManager;
import com.github.superxlcr.minimetalslug.Utils.Utils;
import com.github.superxlcr.minimetalslug.model.Bullet.Bullet;
import com.github.superxlcr.minimetalslug.model.Bullet.ManBullet;
import com.github.superxlcr.minimetalslug.model.Player;

/**
 * 普通人类怪物
 * Created by superxlcr on 2016/5/10.
 */
public class Man extends Monster {

    // 活动动画帧数
    public static Bitmap[] image = null;
    // 死亡动画帧数
    public static Bitmap[] dieImage = null;

    public Man() {
        // 初始化动画帧组
        if (image == null) {
            image = new Bitmap[3];
            image[0] = ResourceManager
                    .createBitmapByID(MainActivity.resources, R.mipmap.man_1,
                                      ResourceManager.scale);
            image[1] = ResourceManager
                    .createBitmapByID(MainActivity.resources, R.mipmap.man_2,
                                      ResourceManager.scale);
            image[2] = ResourceManager
                    .createBitmapByID(MainActivity.resources, R.mipmap.man_3,
                                      ResourceManager.scale);
        }
        // 初始化死亡动画帧组
        if (dieImage == null) {
            dieImage = new Bitmap[5];
            dieImage[0] = ResourceManager
                    .createBitmapByID(MainActivity.resources,
                                      R.mipmap.man_die_1,
                                      ResourceManager.scale);
            dieImage[1] = ResourceManager
                    .createBitmapByID(MainActivity.resources,
                                      R.mipmap.man_die_2,
                                      ResourceManager.scale);
            dieImage[2] = ResourceManager
                    .createBitmapByID(MainActivity.resources,
                                      R.mipmap.man_die_3,
                                      ResourceManager.scale);
            dieImage[3] = ResourceManager
                    .createBitmapByID(MainActivity.resources,
                                      R.mipmap.man_die_4,
                                      ResourceManager.scale);
            dieImage[4] = ResourceManager
                    .createBitmapByID(MainActivity.resources,
                                      R.mipmap.man_die_5,
                                      ResourceManager.scale);
        }
    }

    // 绘制方法
    @Override
    public void draw(Canvas canvas) {
        drawAni(canvas, image, dieImage);
    }

    // 6个时间间隔刷新一次动画
    @Override
    protected int getDrawSpeed() {
        return 6;
    }

    // 第三帧发射子弹
    @Override
    protected int getShootIndex() {
        return 2;
    }

    @Override
    public Bullet getBullet() {
        return new ManBullet(getX(),
                             getY() - (int) (80 * ResourceManager.scale),
                             Player.DIR_LEFT);
    }

    @Override
    public boolean shootOrNot() {
        // 50%几率射击
        if (Utils.rand(100) > 50)
            return false;
        return true;
    }
}
