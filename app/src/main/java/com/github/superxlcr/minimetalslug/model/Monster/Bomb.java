package com.github.superxlcr.minimetalslug.model.Monster;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.github.superxlcr.minimetalslug.MainActivity;
import com.github.superxlcr.minimetalslug.R;
import com.github.superxlcr.minimetalslug.Utils.ResourceManager;
import com.github.superxlcr.minimetalslug.Utils.Utils;
import com.github.superxlcr.minimetalslug.model.Bullet.Bullet;
import com.github.superxlcr.minimetalslug.model.Player;

/**
 * 炸弹怪物
 * Created by superxlcr on 2016/5/10.
 */
public class Bomb extends Monster {

    // 活动动画帧数
    public static Bitmap[] image = null;
    // 死亡动画帧数
    public static Bitmap[] dieImage = null;

    public Bomb() {
        // 初始化动画帧组
        if (image == null) {
            image = new Bitmap[2];
            image[0] = ResourceManager
                    .createBitmapByID(MainActivity.resources, R.mipmap.bomb_1,
                                      ResourceManager.scale);
            image[1] = ResourceManager
                    .createBitmapByID(MainActivity.resources, R.mipmap.bomb_2,
                                      ResourceManager.scale);
        }
        // 初始化死亡动画帧组
        if (dieImage == null) {
            dieImage = new Bitmap[6];
            dieImage[0] = ResourceManager
                    .createBitmapByID(MainActivity.resources, R.mipmap.bomb2_1,
                                      ResourceManager.scale);
            dieImage[1] = ResourceManager
                    .createBitmapByID(MainActivity.resources, R.mipmap.bomb2_2,
                                      ResourceManager.scale);
            dieImage[2] = ResourceManager
                    .createBitmapByID(MainActivity.resources, R.mipmap.bomb2_3,
                                      ResourceManager.scale);
            dieImage[3] = ResourceManager
                    .createBitmapByID(MainActivity.resources, R.mipmap.bomb2_4,
                                      ResourceManager.scale);
            dieImage[4] = ResourceManager
                    .createBitmapByID(MainActivity.resources, R.mipmap.bomb2_5,
                                      ResourceManager.scale);
            dieImage[4] = ResourceManager
                    .createBitmapByID(MainActivity.resources, R.mipmap.bomb2_6,
                                      ResourceManager.scale);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        drawAni(canvas, image, dieImage);
    }

    @Override
    protected int getDrawSpeed() {
        return 4;
    }

    // 炸弹没有子弹，返回-1
    @Override
    protected int getShootIndex() {
        return -1;
    }

    // 炸弹没有子弹
    @Override
    public Bullet getBullet() {
        return null;
    }

    @Override
    public boolean shootOrNot() {
        // 不会射击
        return false;
    }

    // 被玩家撞到就爆炸
    @Override
    public boolean hitByPlayer() {
        // 玩家扣血
        Player.player.damagePlayer(20);
        return true;
    }

    @Override
    public void dieMusic() {
        ResourceManager.SoundPoolPlay(ResourceManager.BOMB, 0.1f);
    }

    // 我是炸弹你打不中我！
    @Override
    public boolean hitByBullet() {
        return false;
    }

    // 踩炸弹加什么分……
    @Override
    public int getPoint() {
        return -10;
    }
}
