package com.github.superxlcr.minimetalslug.model.Monster;

import android.graphics.Canvas;
import android.util.Log;

import com.github.superxlcr.minimetalslug.Utils.Utils;
import com.github.superxlcr.minimetalslug.model.Bullet.Bullet;

import java.util.ArrayList;
import java.util.List;

/**
 * 怪物管理类
 * Created by superxlcr on 2016/5/11.
 */
public class MonsterManager {

    private static final String TAG = "MonsterManager";

    // 已死亡的怪物，正在播放死亡动画
    public static final List<Monster> dieMonsterList = new ArrayList<>();
    // 未死亡的怪物
    public static final List<Monster> monsterList = new ArrayList<>();
    // 所有怪物类型
    public static final Class monsterClass[] = new Class[]{Man.class,
            Plane.class, Bomb.class};

    /**
     * 生成怪物
     * @return 是否生成成功
     */
    public static boolean generateMonster() {
        // TODO
        if (monsterList.size() > 20)
            return false;
        //TODO 个数
        Class myClass = monsterClass[Utils.rand(monsterClass.length)];
        try {
            Object object = myClass.newInstance();
            // 类型检查
            if (object instanceof Monster) {
                monsterList.add((Monster)object);
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, e.toString());
            return false;
        }
    }

    /**
     * 更新怪物坐标
     * @param shift x左移大小，可为负值
     */
    public static void updatePosition(int shift) {
        // 需要删除的怪物
        List<Monster> delList = new ArrayList<>();
        // 活着的怪物
        for (Monster monster : monsterList) {
            if (monster == null)
                continue;
            // 更新位置
            monster.updateShift(shift);
            // 删除左边越界的怪物
            if (monster.getX() < 0)
                delList.add(monster);
        }
        monsterList.removeAll(delList);
        delList.clear();
        // 死去的怪物
        for (Monster monster : dieMonsterList) {
            if (monster == null)
                continue;
            // 更新位置
            monster.updateShift(shift);
            // 删除越界的怪物
            if (monster.getX() < 0)
                delList.add(monster);
        }
        dieMonsterList.removeAll(delList);
    }

    /**
     * 检查怪物是否被子弹击中
     * @param bullet 子弹
     * @return 是否被击中
     */
    public static boolean checkMonsterHit(Bullet bullet) {
        if (bullet == null)
            return false;
        for (Monster monster : monsterList) {
            if (monster.isHit(bullet.getX(), bullet.getY())) {
                // 怪物被击中
                monsterList.remove(monster);
                dieMonsterList.add(monster);
                monster.setIsDie(true);
                // TODO dieMusic
                return true;
            }
        }
        return false;
    }

    /**
     * 绘制怪物
     * @param canvas 画布
     */
    public static void drawMonster(Canvas canvas) {
        if (canvas == null)
            return;
        // 活着的怪物
        for (Monster monster : monsterList)
            monster.draw(canvas);
        // 死亡的怪物
        List<Monster> delList = new ArrayList<>();
        for (Monster monster : dieMonsterList) {
            monster.draw(canvas);
            // 死亡动画是否播放完毕
            if (monster.isDieFinish())
                delList.add(monster);
        }
        dieMonsterList.removeAll(delList);
    }

}
