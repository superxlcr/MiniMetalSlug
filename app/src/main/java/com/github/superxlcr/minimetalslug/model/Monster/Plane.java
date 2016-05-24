package com.github.superxlcr.minimetalslug.model.Monster;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.github.superxlcr.minimetalslug.MainActivity;
import com.github.superxlcr.minimetalslug.R;
import com.github.superxlcr.minimetalslug.Utils.ResourceManager;
import com.github.superxlcr.minimetalslug.Utils.Utils;
import com.github.superxlcr.minimetalslug.model.Bullet.Bullet;
import com.github.superxlcr.minimetalslug.model.Bullet.PlaneBullet;
import com.github.superxlcr.minimetalslug.model.Player;

/**
 * 飞机怪物
 * Created by superxlcr on 2016/5/10.
 */
public class Plane extends Monster {

    // 活动动画帧数
    public static Bitmap[] image = null;
    // 死亡动画帧数
    public static Bitmap[] dieImage = null;

    public Plane() {
        // 初始化动画帧组
        if (image == null) {
            image = new Bitmap[6];
            image[0] = ResourceManager
                    .createBitmapByID(MainActivity.resources, R.mipmap.fly_1,
                                      ResourceManager.scale);
            image[1] = ResourceManager
                    .createBitmapByID(MainActivity.resources, R.mipmap.fly_2,
                                      ResourceManager.scale);
            image[2] = ResourceManager
                    .createBitmapByID(MainActivity.resources, R.mipmap.fly_3,
                                      ResourceManager.scale);
            image[3] = ResourceManager
                    .createBitmapByID(MainActivity.resources, R.mipmap.fly_4,
                                      ResourceManager.scale);
            image[4] = ResourceManager
                    .createBitmapByID(MainActivity.resources, R.mipmap.fly_5,
                                      ResourceManager.scale);
            image[5] = ResourceManager
                    .createBitmapByID(MainActivity.resources, R.mipmap.fly_6,
                                      ResourceManager.scale);
        }
        // 初始化死亡动画帧组
        if (dieImage == null) {
            dieImage = new Bitmap[10];
            dieImage[0] = ResourceManager
                    .createBitmapByID(MainActivity.resources,
                                      R.mipmap.fly_die_1,
                                      ResourceManager.scale);
            dieImage[1] = ResourceManager
                    .createBitmapByID(MainActivity.resources,
                                      R.mipmap.fly_die_2,
                                      ResourceManager.scale);
            dieImage[2] = ResourceManager
                    .createBitmapByID(MainActivity.resources,
                                      R.mipmap.fly_die_3,
                                      ResourceManager.scale);
            dieImage[3] = ResourceManager
                    .createBitmapByID(MainActivity.resources,
                                      R.mipmap.fly_die_4,
                                      ResourceManager.scale);
            dieImage[4] = ResourceManager
                    .createBitmapByID(MainActivity.resources,
                                      R.mipmap.fly_die_5,
                                      ResourceManager.scale);
            dieImage[5] = ResourceManager
                    .createBitmapByID(MainActivity.resources,
                                      R.mipmap.fly_die_6,
                                      ResourceManager.scale);
            dieImage[6] = ResourceManager
                    .createBitmapByID(MainActivity.resources,
                                      R.mipmap.fly_die_7,
                                      ResourceManager.scale);
            dieImage[7] = ResourceManager
                    .createBitmapByID(MainActivity.resources,
                                      R.mipmap.fly_die_8,
                                      ResourceManager.scale);
            dieImage[8] = ResourceManager
                    .createBitmapByID(MainActivity.resources,
                                      R.mipmap.fly_die_9,
                                      ResourceManager.scale);
            dieImage[9] = ResourceManager
                    .createBitmapByID(MainActivity.resources,
                                      R.mipmap.fly_die_10,
                                      ResourceManager.scale);
        }
    }

    // 飞机高度不一样
    @Override
    protected int initY() {
        return ResourceManager.SCREEN_HEIGHT * 50 / 100 - Utils
                .rand((int) ResourceManager.scale * 100);
    }

    @Override
    public void draw(Canvas canvas) {
        drawAni(canvas, image, dieImage);
    }

    @Override
    protected int getDrawSpeed() {
        return 4;
    }

    // 最后一帧射击
    @Override
    protected int getShootIndex() {
        return image.length - 1;
    }

    @Override
    public Bullet getBullet() {
        return new PlaneBullet(getX(), getY(), Player.DIR_LEFT);
    }

    @Override
    public boolean shootOrNot() {
        // 50%几率射击
        if (Utils.rand(100) > 50)
            return false;
        return true;
    }
}
