package com.github.superxlcr.minimetalslug.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.ViewManager;

import com.github.superxlcr.minimetalslug.MainActivity;
import com.github.superxlcr.minimetalslug.R;
import com.github.superxlcr.minimetalslug.Utils.Graphics;
import com.github.superxlcr.minimetalslug.Utils.ResourceManager;
import com.github.superxlcr.minimetalslug.model.Bullet.Bullet;
import com.github.superxlcr.minimetalslug.model.Bullet.MyBullet;
import com.github.superxlcr.minimetalslug.model.Monster.MonsterManager;

/**
 * 玩家类
 * Created by superxlcr on 2016/5/11.
 */
public class Player {
    // 目前只有一个玩家
    public static Player player = null;

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
    public static final int ACTION_STAND_LEFT = 2;
    public static final int ACTION_RUN_RIGHT = 3;
    public static final int ACTION_RUN_LEFT = 4;
    public static final int ACTION_JUMP_RIGHT = 5;
    public static final int ACTION_JUMP_LEFT = 6;

    // 角色方向
    public static final int DIR_RIGHT = 1;
    public static final int DIR_LEFT = -1;

    // 角色初始坐标
    public static int X_DEFAULT = 0;
    public static int Y_DEFAULT = 0;
    // 最大跳跃高度
    public static int Y_JUMP_MAX = 0;
    // 最高处滞留时间
    public static int JUMP_MAX_STOP = 4;

    // 角色名称
    private String name;
    // 角色生命值
    private int hp;
    // 角色发射的子弹类型
    private Class<? extends Bullet> gun;
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
    // 角色移动状态
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
    // 最高处滞留计数器
    public int jumpStopCount = 0;

    // 腿部绘制帧数计数
    private int legIndex = 0;
    // 头部绘制帧数计数
    private int headIndex = 0;
    // 头部图片x
    private int currentHeadDrawX = 0;
    // 头部图片y
    private int currentHeadDrawY = 0;
    // 当前绘制腿部动画帧
    private Bitmap currentLegBitmap = null;
    // 当前绘制头部动画帧
    private Bitmap currentHeadBitmap = null;
    // 动画刷新时间
    private static int DRAW_COUNT_TIME = 4;
    // 动画刷新计数器
    private int drawCount = 0;

    public Player(String name, int maxHp) {
        // 初始化图片组
        intitalBitmapArrays();
        // 设置姓名与血量
        this.name = name;
        this.hp = maxHp;
        // 目前只有一种枪
        this.gun = MyBullet.class;
    }

    /**
     * 执行跳跃方法
     */
    public void jump() {
        // 不处于跳跃状态
        if (!isJump)
            isJump = true;
    }

    /**
     * 处理角色移动和跳跃逻辑
     */
    public void logic() {
        // 角色移动
        move();
        // 处理角色高度变化
        if (isJump) {
            if (!isJumpMax) { // 未跳到最高点,上升
                y = y - (int) (8 * ResourceManager.scale);
                // TODO 子弹加速度
                if (y <= Y_JUMP_MAX) { // 跳跃至最大高度
                    y = Y_JUMP_MAX;
                    isJumpMax = true;
                    jumpStopCount = JUMP_MAX_STOP;
                }
            } else { // 已跳到最高点，下降
                jumpStopCount--; // 最高点冷却
                if (jumpStopCount <= 0) {
                    y = y + (int) (8 * ResourceManager.scale);
                    // TODO 子弹加速度2
                    if (y >= Y_DEFAULT) { // 降落至地
                        y = Y_DEFAULT;
                        isJumpMax = false;
                        isJump = false;
                    }
                }
            }
        }
    }

    /**
     * 绘制角色
     *
     * @param canvas 画布
     */
    public void draw(Canvas canvas) {
        if (canvas == null)
            return;

        switch (action) {
            case ACTION_STAND_RIGHT:
                drawAni(canvas, legStandImage, headStandImage, DIR_RIGHT);
                break;
            case ACTION_STAND_LEFT:
                drawAni(canvas, legStandImage, headStandImage, DIR_LEFT);
                break;
            case ACTION_RUN_RIGHT:
                drawAni(canvas, legRunImage, headRunImage, DIR_RIGHT);
                break;
            case ACTION_RUN_LEFT:
                drawAni(canvas, legRunImage, headRunImage, DIR_LEFT);
                break;
            case ACTION_JUMP_RIGHT:
                drawAni(canvas, legJumpImage, headJumpImage, DIR_RIGHT);
                break;
            case ACTION_JUMP_LEFT:
                drawAni(canvas, legJumpImage, headJumpImage, DIR_LEFT);
                break;
            default:
                break;
        }
    }

    public void setMove(int move) {
        this.move = move;
    }

    /**
     * 获取当前角色的方向
     *
     * @return 方向
     */
    public int getDir() {
        if (action % 2 == 0) // 偶数为左
            return DIR_LEFT;
        return DIR_RIGHT; // 奇数为右
    }

    /**
     * 获取角色当前偏移
     *
     * @return 偏移，正为向右，负为向左
     */
    public int getShift() {
        if (x <= 0 || y <= 0)
            initPosition();
        int shift = x - X_DEFAULT;
        // 不能往左走
        if (shift < 0)
            shift = 0;
        return shift;
    }

    /**
     * 绘制左上角的头像、姓名、血量等信息
     *
     * @param canvas
     */
    private void drawHead(Canvas canvas) {
        if (canvas == null || head == null || head.isRecycled())
            return;
        // 头像
        Graphics.drawMatrixImage(canvas, head, 0, 0, head.getWidth(),
                                 head.getHeight(), Graphics.Trans.TRANS_MIRROR,
                                 0, 0, 0, Graphics.DEFAULT_TIMES_SCALE);
        // 名字
        Paint paint = new Paint();
        paint.setTextSize(30);
        Graphics.drawBorderString(canvas, 0xa33e11, 0xffde00, name,
                                  head.getWidth(),
                                  (int) ResourceManager.scale * 50, 3, paint);
        // 血量
        Graphics.drawBorderString(canvas, 0x066a14, 0x91ff1d, "HP: " + hp,
                                  head.getWidth(),
                                  (int) ResourceManager.scale * 100, 3, paint);
        // TODO 测试怪物数
        Graphics.drawBorderString(canvas, 0xa33e11, 0xffde00,
                                  "怪物数: " + MonsterManager.monsterList.size(),
                                  head.getWidth(),
                                  (int) ResourceManager.scale * 150, 3, paint);
    }

    // 角色移动
    private void move() {
        int dis = 0;
        if (move == MOVE_RIGHT) {
            // 向右移动
            dis = (int) (6 * ResourceManager.scale);
            // 更新怪物位置
            MonsterManager.updatePosition(dis);
            // 更新角色位置
            x += dis;
            // 设置动作
            if (!isJump)
                action = ACTION_RUN_RIGHT;
            else
                action = ACTION_JUMP_RIGHT;
        } else if (move == MOVE_LEFT) {
            dis = (int) -(6 * ResourceManager.scale);
            // 更新怪物位置
            MonsterManager.updatePosition(dis);
            // 更新角色位置
            x += dis;
            if (x + dis < Player.X_DEFAULT)
                dis = Player.X_DEFAULT - x;
            if (!isJump)
                action = ACTION_RUN_LEFT;
            else
                action = ACTION_JUMP_LEFT;
        } else if (!isJump) // 不动的时候，初始化动作
            action = getDir() == DIR_LEFT ? ACTION_STAND_LEFT :
                    ACTION_STAND_RIGHT;
        else // 跳跃状态
            action =
                    getDir() == DIR_LEFT ? ACTION_JUMP_LEFT : ACTION_JUMP_RIGHT;
    }

    // 绘制角色的动画帧
    private void drawAni(Canvas canvas, Bitmap[] legArr, Bitmap[] headArr,
            int dir) {
        if (canvas == null)
            return;
        if (legArr == null || headArr == null)
            return;
        // TODO 射击状态图片处理

        legIndex = legIndex % legArr.length;
        headIndex = headIndex % headArr.length;

        // 是否需要翻转图片
        Graphics.Trans trans = dir == DIR_RIGHT ? Graphics.Trans.TRANS_MIRROR :
                Graphics.Trans.TRANS_NONE;

        // 绘制脚部
        currentLegBitmap = legArr[legIndex];
        if (currentLegBitmap == null || currentLegBitmap.isRecycled())
            return;

        int drawX = X_DEFAULT - currentLegBitmap.getWidth() / 2;
        int drawY = y - currentLegBitmap.getHeight();
        Graphics.drawMatrixImage(canvas, currentLegBitmap, 0, 0,
                                 currentLegBitmap.getWidth(),
                                 currentLegBitmap.getHeight(), trans, drawX,
                                 drawY, 0, Graphics.DEFAULT_TIMES_SCALE);

        // 绘制头部
        currentHeadBitmap = headArr[headIndex];
        if (currentHeadBitmap == null || currentHeadBitmap.isRecycled())
            return;
        // 微调图片
        // 取中点
        drawX = X_DEFAULT - currentHeadBitmap.getWidth() / 2;
        switch (action) {
            case ACTION_STAND_RIGHT:
                drawX += (int) (10 * ResourceManager.scale);
                drawY = drawY - currentHeadBitmap
                        .getHeight() + (int) (25 * ResourceManager.scale);
                break;
            case ACTION_STAND_LEFT:
                drawX -= (int) (15 * ResourceManager.scale);
                drawY = drawY - currentHeadBitmap
                        .getHeight() + (int) (25 * ResourceManager.scale);
                break;
            case ACTION_RUN_RIGHT:
                drawX += (int) (18 * ResourceManager.scale);
                drawY = drawY - currentHeadBitmap
                        .getHeight() + (int) (30 * ResourceManager.scale);
                break;
            case ACTION_RUN_LEFT:
                drawX -= (int) (17 * ResourceManager.scale);
                drawY = drawY - currentHeadBitmap
                        .getHeight() + (int) (30 * ResourceManager.scale);
                break;
            case ACTION_JUMP_RIGHT:
                drawX -= (int) (5 * ResourceManager.scale);
                drawY = drawY - currentHeadBitmap
                        .getHeight() + (int) (10 * ResourceManager.scale);
                break;
            case ACTION_JUMP_LEFT:
                drawX += (int) (2 * ResourceManager.scale);
                drawY = drawY - currentHeadBitmap
                        .getHeight() + (int) (10 * ResourceManager.scale);
                break;
        }
        Graphics.drawMatrixImage(canvas, currentHeadBitmap, 0, 0,
                                 currentHeadBitmap.getWidth(),
                                 currentHeadBitmap.getHeight(), trans, drawX,
                                 drawY, 0, Graphics.DEFAULT_TIMES_SCALE);
        currentHeadDrawX = drawX;
        currentHeadDrawY = drawY;

        // drawCount控制该方法每调用4次才会切换到下一帧位图
        drawCount++;
        if (drawCount >= DRAW_COUNT_TIME) {
            drawCount = 0;
            legIndex++;
            headIndex++;
        }
        // 画左上角的角色、名字、血量
        drawHead(canvas);
    }

    /**
     * 初始化角色位置，能跳跃的最大高度
     */
    private void initPosition() {
        x = ResourceManager.SCREEN_WIDTH * 15 / 100;
        y = ResourceManager.SCREEN_HEIGHT * 75 / 100;
        X_DEFAULT = x;
        Y_DEFAULT = y;
        Y_JUMP_MAX = ResourceManager.SCREEN_HEIGHT * 50 / 100;
    }

    /**
     * 初始化图片组
     */
    private void intitalBitmapArrays() {
        if (legStandImage == null) {
            legStandImage = new Bitmap[1];
            legStandImage[0] = ResourceManager
                    .createBitmapByID(MainActivity.resources,
                                      R.mipmap.leg_stand,
                                      ResourceManager.scale);
            //            legStandImage[1] = ResourceManager
            //                    .createBitmapByID(MainActivity.resources,
            //                                      R.mipmap.leg_stand_2,
            //                                      ResourceManager.scale);
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

    /**
     * 判断角色是否死亡
     *
     * @return 是否死亡
     */
    public boolean isDie() {
        return hp <= 0;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }
}