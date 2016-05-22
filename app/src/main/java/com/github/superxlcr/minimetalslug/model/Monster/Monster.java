package com.github.superxlcr.minimetalslug.model.Monster;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.github.superxlcr.minimetalslug.Utils.Graphics;
import com.github.superxlcr.minimetalslug.Utils.ResourceManager;
import com.github.superxlcr.minimetalslug.Utils.Utils;
import com.github.superxlcr.minimetalslug.model.Player;

/**
 * 怪物的抽象基类
 * Created by superxlcr on 2016/5/7.
 */
public abstract class Monster {
    // 怪物的坐标
    private int x = 0;
    private int y = 0;
    // 是否死亡
    private boolean isDie = false;
    // 绘制怪物相关的坐标
    // 左上角x,y
    private int startX = 0;
    private int startY = 0;
    // 右下角x,y
    private int endX = 0;
    private int endY = 0;
    // 目前绘制帧数
    private int drawIndex = 0;
    // 死亡绘制动画帧数
    private int drawDieIndex = 0;
    // 绘制速度记录
    private int drawCount = 0;
    // 死亡动画播放完毕
    private boolean dieFinish = false;
    // 已完成的位移
    private int alreadyShift = 0;

    public Monster() {
        x = initX();
        y = initY();
        // 获取角色位移
        alreadyShift = Player.player.getShift();
    }

    /**
     * 返回初始化时默认x
     *
     * @return x
     */
    protected int initX() {
        return ResourceManager.SCREEN_WIDTH + Utils
                .rand(ResourceManager.SCREEN_WIDTH >> 1) - Utils
                .rand(ResourceManager.SCREEN_WIDTH >> 2);
    }

    /**
     * 返回初始化时默认y
     *
     * @return y
     */
    protected int initY() {
        return Player.Y_DEFAULT;
    }

    /**
     * 绘制怪物
     *
     * @param canvas 画布
     */
    public abstract void draw(Canvas canvas);

    /**
     * 返回动画刷新速度，越小越快
     *
     * @return 动画刷新速度
     */
    protected abstract int getDrawSpeed();

    /**
     * 返回怪物射击帧号
     *
     * @return 射击帧
     */
    protected abstract int getShootIndex();

    /**
     * 绘制怪物动画
     *
     * @param canvas     画布
     * @param bitmaps    普通动画帧组
     * @param dieBitmaps 死亡动画帧组
     */
    protected void drawAni(Canvas canvas, Bitmap bitmaps[],
            Bitmap dieBitmaps[]) {
        if (canvas == null)
            return;
        Bitmap bitmap = null;
        // 判读是否绘制下一帧
        drawCount++;
        if (drawCount >= getDrawSpeed()) {
            if (isDie && !dieFinish) // 已死亡
                drawDieIndex++;
            else // 未死亡
                drawIndex = drawIndex + 1 >= bitmaps.length ? 0 : drawIndex + 1;
            drawCount = 0;
        }
        // 获取绘制图片
        if (isDie) { // 已死亡
            if (dieBitmaps != null && drawDieIndex < dieBitmaps.length)
                bitmap = dieBitmaps[drawDieIndex];
            if (drawDieIndex >= dieBitmaps.length)
                dieFinish = true;
        } else { // 未死亡
            if (bitmaps != null)
                bitmap = bitmaps[drawIndex];
        }
        // 绘制怪物
        if (bitmap != null && !bitmap.isRecycled()) {
            int drawX = x;
            int drawY = y - bitmap.getHeight();
            Graphics.drawMatrixImage(canvas, bitmap, 0, 0, bitmap.getWidth(),
                                     bitmap.getHeight(),
                                     Graphics.Trans.TRANS_NONE, drawX, drawY, 0,
                                     Graphics.DEFAULT_TIMES_SCALE);
            startX = drawX;
            startY = drawY;
            endX = drawX + bitmap.getWidth();
            endY = drawY + bitmap.getHeight();
        }
        // 判读是否射击
        if (!isDie && drawIndex == getShootIndex()) {
            //TODO addBullet();
        }
    }

    /**
     * 判断是否怪物子弹被击中
     *
     * @param x 子弹x
     * @param y 子弹y
     * @return 是否被击中
     */
    public boolean isHit(int x, int y) {
        return x >= startX && x <= endX && y >= startY && y <= endY;
    }

    /**
     * 移动怪物
     *
     * @param shift x左移大小，可为负值
     */
    public void updateShift(int shift) {
        x -= (shift - alreadyShift);
        alreadyShift = shift;
    }

    /**
     * 获取怪物x坐标
     *
     * @return x坐标
     */
    public int getX() {
        return x;
    }

    /**
     * 设置怪物是否死亡
     *
     * @param isDie 是否死亡
     */
    public void setIsDie(boolean isDie) {
        this.isDie = isDie;
    }

    /**
     * 判断怪物死亡动画是否播放完毕
     *
     * @return 是否播放完毕
     */
    public boolean isDieFinish() {
        return isDie & dieFinish;
    }
}
