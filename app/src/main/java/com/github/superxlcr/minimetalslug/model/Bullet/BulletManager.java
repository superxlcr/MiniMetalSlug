package com.github.superxlcr.minimetalslug.model.Bullet;

import android.graphics.Canvas;

import com.github.superxlcr.minimetalslug.Utils.ResourceManager;
import com.github.superxlcr.minimetalslug.model.Monster.MonsterManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 子弹管理类
 * Created by superxlcr on 2016/5/11.
 */
public class BulletManager {

    // 怪物的子弹
    public static List<Bullet> monsterBulletList = new ArrayList<>();
    // 玩家的子弹
    public static List<Bullet> playerBulletList = new ArrayList<>();

    /**
     * 更新子弹位置
     *
     * @param shift x左移大小，可为负值
     */
    public static void updatePosition(int shift) {
        List<Bullet> delList = new ArrayList<>();
        for (Bullet bullet : monsterBulletList) {
            bullet.updateShift(shift);
            if (bullet.getX() < 0 || bullet
                    .getX() > ResourceManager.SCREEN_WIDTH ||
                    bullet.getY() < 0)
                delList.add(bullet);
        }
        monsterBulletList.removeAll(delList);
        delList.clear();
        for (Bullet bullet : playerBulletList) {
            bullet.updateShift(shift);
            if (bullet.getX() < 0 || bullet
                    .getX() > ResourceManager.SCREEN_WIDTH ||
                    bullet.getY() < 0)
                delList.add(bullet);
        }
        playerBulletList.removeAll(delList);
    }

    /**
     * 检查子弹是否击中东西
     */
    public static void checkHit() {
        // 要删除的子弹列表
        List<Bullet> delList = new ArrayList<>();
        for (Bullet bullet : monsterBulletList) {
            // TODO check hit player
        }
        for (Bullet bullet : playerBulletList)
            if (MonsterManager.checkMonsterHit(bullet)) // 打中了怪物
                delList.add(bullet);
        playerBulletList.removeAll(delList);
    }

    /**
     * 绘制子弹
     *
     * @param canvas 画布
     */
    public static void drawBullet(Canvas canvas) {
        List<Bullet> delList = new ArrayList<>();
        for (Bullet bullet : monsterBulletList) {
            bullet.move();
            if (bullet.getX() < 0 || bullet
                    .getX() > ResourceManager.SCREEN_WIDTH ||
                    bullet.getY() < 0)
                delList.add(bullet);
        }
        monsterBulletList.removeAll(delList);
        delList.clear();
        for (Bullet bullet : playerBulletList) {
            bullet.move();
            if (bullet.getX() < 0 || bullet
                    .getX() > ResourceManager.SCREEN_WIDTH ||
                    bullet.getY() < 0)
                delList.add(bullet);
        }
        playerBulletList.removeAll(delList);
    }

}
