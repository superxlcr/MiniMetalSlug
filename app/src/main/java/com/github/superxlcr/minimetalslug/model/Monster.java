package com.github.superxlcr.minimetalslug.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.github.superxlcr.minimetalslug.Utils.Graphics;

/**
 * 怪物的抽象基类
 * Created by superxlcr on 2016/5/7.
 */
public abstract class Monster {
    // 怪物的坐标
    protected int x = 0;
    protected int y = 0;
    // 是否死亡
    protected boolean isDie = false;
    // 绘制怪物相关的坐标
    // 左上角x,y
    protected int startX = 0;
    protected int startY = 0;
    // 右下角x,y
    protected int endX = 0;
    protected int endY = 0;
    // 目前绘制帧数
    protected int drawIndex = 0;
    // 死亡绘制动画帧数
    protected int drawDieIndex = 0;
    // 绘制速度记录
    protected int drawCount = 0;
    // 射击速度记录
    protected int shootCount = 0;

    /**
     * 绘制怪物
     */
    public abstract void draw();

    /**
     * 返回动画刷新速度，越小越快
     *
     * @return 动画刷新速度
     */
    protected abstract int getDrawSpeed();

    /**
     * 返回怪物射击速度，越小越快
     *
     * @return 射击速度
     */
    protected abstract int getShootSpeed();

    protected void drawAni(Canvas canvas, Bitmap bitmaps[],
            Bitmap dieBitmaps[]) {
        if (canvas == null)
            return;
        Bitmap bitmap = null;
        // 判读是否绘制下一帧
        drawCount++;
        if (drawCount >= getDrawSpeed()) {
            if (isDie) // 已死亡
                drawDieIndex++;
            else // 未死亡
                drawIndex = drawIndex + 1 >= bitmaps.length ? 0 : drawIndex + 1;
            drawCount = 0;
        }
        // 获取绘制图片
        if (isDie) { // 已死亡
            if (dieBitmaps != null && drawDieIndex <= dieBitmaps.length)
                bitmap = dieBitmaps[drawDieIndex];
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
        shootCount++;
        if (shootCount >= getShootSpeed()) {
            //TODO addBullet();
        }
        //TODO drawBullet();
    }



}
