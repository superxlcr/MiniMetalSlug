package com.github.superxlcr.minimetalslug.model;

/**
 * 怪物的抽象基类
 * Created by superxlcr on 2016/5/7.
 */
public abstract class Monster {
    // 怪物的坐标
    protected int x = 0;
    protected int y = 0;
    // 是否死亡
    protected  boolean isDie = false;
    // 绘制怪物相关的坐标
    // 左上角x,y
    protected int startX = 0;
    protected int startY = 0;
    // 右下角x,y
    protected int endX = 0;
    protected int endY = 0;
    // 动画刷新速度
    int drawCount = 0;
    // 目前绘制帧数
    protected int drawIndex = 0;

    public void draw() {};

}
