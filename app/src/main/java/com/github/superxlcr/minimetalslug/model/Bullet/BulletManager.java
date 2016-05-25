package com.github.superxlcr.minimetalslug.model.Bullet;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.github.superxlcr.minimetalslug.MainActivity;
import com.github.superxlcr.minimetalslug.Utils.Graphics;
import com.github.superxlcr.minimetalslug.Utils.ResourceManager;
import com.github.superxlcr.minimetalslug.model.Monster.MonsterManager;
import com.github.superxlcr.minimetalslug.model.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * 子弹管理类
 * Created by superxlcr on 2016/5/11.
 */
public class BulletManager {

    // 怪物的子弹
    private static List<Bullet> monsterBulletList = new ArrayList<>();
    // 玩家的子弹
    private static List<Bullet> playerBulletList = new ArrayList<>();

    /**
     * 通过shift更新子弹位置
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
     * 更新子弹位置
     */
    public static void movePosition() {
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
        for (Bullet bullet : monsterBulletList) {
            Bitmap bitmap = bullet.getBitmap();
            if (bitmap == null || bitmap.isRecycled())
                continue;
            Graphics.drawImage(canvas, bitmap, 0, 0, bitmap.getWidth(),
                               bitmap.getHeight(), bullet.getX(),
                               bullet.getY());
            if (MainActivity.DEBUG) // 调试模式绘制框
                Graphics.drawRectengle(canvas, bullet.getLeft(),
                                       bullet.getTop(), bullet.getRight(),
                                       bullet.getBottom(), 0x00ff00, 5);
        }
        for (Bullet bullet : playerBulletList) {
            Bitmap bitmap = bullet.getBitmap();
            if (bitmap == null || bitmap.isRecycled())
                continue;
            if (bullet.getDir() == Player.DIR_RIGHT) // 向右射击
                Graphics.drawImage(canvas, bitmap, 0, 0, bitmap.getWidth(),
                                   bitmap.getHeight(), bullet.getX(),
                                   bullet.getY());
            else // 向左射击
                Graphics.drawMatrixImage(canvas, bitmap, 0, 0,
                                         bitmap.getWidth(), bitmap.getHeight(),
                                         Graphics.Trans.TRANS_MIRROR,
                                         bullet.getX(), bullet.getY(), 0,
                                         Graphics.DEFAULT_TIMES_SCALE);
            if (MainActivity.DEBUG) // 调试模式绘制框
                Graphics.drawRectengle(canvas, bullet.getLeft(),
                                       bullet.getTop(), bullet.getRight(),
                                       bullet.getBottom(), 0xff0000, 5);
        }
    }

    /**
     * 添加怪物的子弹
     *
     * @param bullet 子弹
     */
    public static void addMonsterBullet(Bullet bullet) {
        monsterBulletList.add(bullet);
    }

    /**
     * 添加玩家的子弹
     *
     * @param bullet 子弹
     */
    public static void addPlayerBullet(Bullet bullet) {
        playerBulletList.add(bullet);
    }
}
