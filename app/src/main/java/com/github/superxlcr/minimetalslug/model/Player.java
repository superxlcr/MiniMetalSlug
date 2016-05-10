package com.github.superxlcr.minimetalslug.model;

import android.graphics.Bitmap;

import com.github.superxlcr.minimetalslug.MainActivity;
import com.github.superxlcr.minimetalslug.R;
import com.github.superxlcr.minimetalslug.Utils.ResourceManager;
import com.github.superxlcr.minimetalslug.model.Bullet.Bullet;

/**
 * 玩家类
 * Created by superxlcr on 2016/5/11.
 */
public class Player {
    // 玩家角色图片组
    // 站立，腿部
    public static Bitmap legStandImage[] = null;
    // 站立，头部
    public static Bitmap headStandImage[] = null;
    // 跑动，腿部
    public static Bitmap legRunImage[] = null;
    // 跑动，头部
    public static Bitmap headRunImage[] = null;
    // 跳动，腿部
    public static Bitmap legJumpImage[] = null;
    // 跳动，头部
    public static Bitmap headJumpImage[] = null;
    // 射击，头部
    public static Bitmap headShootImage[] = null;
    // 头像
    public static Bitmap head = null;

    // 角色的最高生命值
    public static final int MAX_HP = 500;
    // 角色控制动作
    public static final int ACTION_STAND_RIGHT = 1;
    public static final int ACTION_STAND_Left = 2;
    public static final int ACTION_RUN＿RIGHT = 3;
    public static final int ACTION_RUN_LEFT = 4;
    public static final int ACTION_JUMP_RIGHT = 5;
    public static final int ACTION_JUMP_LEFT = 6;

    // 角色方向
    public static final int DIR_RIGHT = 1;
    public static final int DIR_LEFT = -1;

    // 角色坐标
    public static int X_DEFAULT = 0;
    public static int Y_DEFAULT = 0;
    public static int Y_JUMP_MAX = 0;

    // 角色名称
    private String name;
    // 角色生命值
    private int hp;
    // 角色发射的子弹类型
    private Bullet gun;
    // 角色当前动作
    private int action = ACTION_STAND_RIGHT;
    // 角色坐标
    private int x = -1;
    private int y = -1;

    // 角色移动相关的常量
    // 站立，左移，右移
    public static final int MOVE_STAND = 0;
    public static final int MOVE_RIGHT = 1;
    public static final int MOVE_LEFT = 2;
    // 角色移动方式
    public int move = MOVE_STAND;

    // 角色射击相关
    // 射击时间冷却间隔
    public static final int MAX_LEFT_SHOOT_TIME = 6;
    // 射击时间冷却间隔计数
    private int leftShootTime = 0;

    // 角色是否处于跳跃状态
    public boolean isJump = false;
    // 角色是否跳到最高点
    public boolean isJumpMax = false;
    // TODO

    public Player() {
        // 初始化图片组
        intitalBitmapArrays();
    }

    /**
     * 初始化图片组
     */
    private void intitalBitmapArrays() {
        if (legStandImage == null) {
            legStandImage = new Bitmap[2];
            legStandImage[0] = ResourceManager
                    .createBitmapByID(MainActivity.resources,
                                      R.mipmap.leg_stand,
                                      ResourceManager.scale);
            legStandImage[1] = ResourceManager
                    .createBitmapByID(MainActivity.resources,
                                      R.mipmap.leg_stand_2,
                                      ResourceManager.scale);
        }
        if (headStandImage == null) {
            headStandImage = new Bitmap[3];
            headStandImage[0] = ResourceManager
                    .createBitmapByID(MainActivity.resources,
                                      R.mipmap.head_stand_1,
                                      ResourceManager.scale);
            headStandImage[1] = ResourceManager
                    .createBitmapByID(MainActivity.resources,
                                      R.mipmap.head_stand_2,
                                      ResourceManager.scale);
            headStandImage[2] = ResourceManager
                    .createBitmapByID(MainActivity.resources,
                                      R.mipmap.head_stand_3,
                                      ResourceManager.scale);
        }
        if (legRunImage == null) {
            legRunImage = new Bitmap[3];
            legRunImage[0] = ResourceManager
                    .createBitmapByID(MainActivity.resources,
                                      R.mipmap.leg_run_1,
                                      ResourceManager.scale);
            legRunImage[1] = ResourceManager
                    .createBitmapByID(MainActivity.resources,
                                      R.mipmap.leg_run_2,
                                      ResourceManager.scale);
            legRunImage[2] = ResourceManager
                    .createBitmapByID(MainActivity.resources,
                                      R.mipmap.leg_run_3,
                                      ResourceManager.scale);
        }
        if (headRunImage == null) {
            headRunImage = new Bitmap[3];
            headRunImage[0] = ResourceManager
                    .createBitmapByID(MainActivity.resources,
                                      R.mipmap.head_run_1,
                                      ResourceManager.scale);
            headRunImage[1] = ResourceManager
                    .createBitmapByID(MainActivity.resources,
                                      R.mipmap.head_run_2,
                                      ResourceManager.scale);
            headRunImage[2] = ResourceManager
                    .createBitmapByID(MainActivity.resources,
                                      R.mipmap.head_run_3,
                                      ResourceManager.scale);
        }
        if (legJumpImage == null) {
            legJumpImage = new Bitmap[5];
            legJumpImage[0] = ResourceManager
                    .createBitmapByID(MainActivity.resources,
                                      R.mipmap.leg_jum_1,
                                      ResourceManager.scale);
            legJumpImage[1] = ResourceManager
                    .createBitmapByID(MainActivity.resources,
                                      R.mipmap.leg_jum_2,
                                      ResourceManager.scale);
            legJumpImage[2] = ResourceManager
                    .createBitmapByID(MainActivity.resources,
                                      R.mipmap.leg_jum_3,
                                      ResourceManager.scale);
            legJumpImage[3] = ResourceManager
                    .createBitmapByID(MainActivity.resources,
                                      R.mipmap.leg_jum_4,
                                      ResourceManager.scale);
            legJumpImage[4] = ResourceManager
                    .createBitmapByID(MainActivity.resources,
                                      R.mipmap.leg_jum_5,
                                      ResourceManager.scale);
        }
        if (headJumpImage == null) {
            headJumpImage = new Bitmap[5];
            headJumpImage[0] = ResourceManager
                    .createBitmapByID(MainActivity.resources,
                                      R.mipmap.head_jump_1,
                                      ResourceManager.scale);
            headJumpImage[1] = ResourceManager
                    .createBitmapByID(MainActivity.resources,
                                      R.mipmap.head_jump_2,
                                      ResourceManager.scale);
            headJumpImage[2] = ResourceManager
                    .createBitmapByID(MainActivity.resources,
                                      R.mipmap.head_jump_3,
                                      ResourceManager.scale);
            headJumpImage[3] = ResourceManager
                    .createBitmapByID(MainActivity.resources,
                                      R.mipmap.head_jump_4,
                                      ResourceManager.scale);
            headJumpImage[4] = ResourceManager
                    .createBitmapByID(MainActivity.resources,
                                      R.mipmap.head_jump_5,
                                      ResourceManager.scale);
        }
        if (headShootImage == null) {
            headShootImage = new Bitmap[6];
            headShootImage[0] = ResourceManager
                    .createBitmapByID(MainActivity.resources,
                                      R.mipmap.head_shoot_1,
                                      ResourceManager.scale);
            headShootImage[1] = ResourceManager
                    .createBitmapByID(MainActivity.resources,
                                      R.mipmap.head_shoot_2,
                                      ResourceManager.scale);
            headShootImage[2] = ResourceManager
                    .createBitmapByID(MainActivity.resources,
                                      R.mipmap.head_shoot_3,
                                      ResourceManager.scale);
            headShootImage[3] = ResourceManager
                    .createBitmapByID(MainActivity.resources,
                                      R.mipmap.head_shoot_4,
                                      ResourceManager.scale);
            headShootImage[4] = ResourceManager
                    .createBitmapByID(MainActivity.resources,
                                      R.mipmap.head_shoot_5,
                                      ResourceManager.scale);
            headShootImage[5] = ResourceManager
                    .createBitmapByID(MainActivity.resources,
                                      R.mipmap.head_shoot_6,
                                      ResourceManager.scale);
        }
        if (head == null)
            head = ResourceManager
                    .createBitmapByID(MainActivity.resources, R.mipmap.head,
                                      ResourceManager.scale);
    }

}