package com.github.superxlcr.minimetalslug;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.github.superxlcr.minimetalslug.Utils.ResourceManager;
import com.github.superxlcr.minimetalslug.model.Bullet.Bullet;
import com.github.superxlcr.minimetalslug.model.Bullet.BulletManager;
import com.github.superxlcr.minimetalslug.model.Monster.Monster;
import com.github.superxlcr.minimetalslug.model.Monster.MonsterManager;
import com.github.superxlcr.minimetalslug.model.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by superxlcr on 2016/5/11.
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    public static final Player player = new Player("test", Player.MAX_HP);
    // 当前上下文
    private Context mainContext = null;
    // 画布和画笔
    private Paint paint = null;
    private Canvas canvas = null;
    private SurfaceHolder surfaceHolder;
    // 场景状态
    // 不变
    public static final int STAGE_NO_CHANGE = 0;
    // 初始化
    public static final int STAGE_INIT = 1;
    // 登录
    public static final int STAGE_LOGIN = 2;
    // 游戏
    public static final int STAGE_GAME = 3;
    // 失败
    public static final int STAGE_LOSE = 4;
    // 退出
    public static final int STAGE_QUIT = 99;
    // 错误
    public static final int STAGE_ERROR = 255;
    // 游戏当前场景状态
    private int gameStage = 0;
    // 保存游戏加载的场景
    // 待处理场景
    public static final List<Integer> stageList = Collections
            .synchronizedList(new ArrayList<Integer>());

    // 场景处理步骤
    // 初始化
    private static final int INIT = 1;
    // 逻辑
    private static final int LOGIC = 2;
    // 清除
    private static final int CLEAN = 3;
    // 绘图
    private static final int PAINT = 4;

    // 设置view
    private static final int SET_VIEW = 0;
    // 删除view
    private static final int DEL_VIEW = 1;
    // 与主界面通信设置View
    public static Handler viewHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SET_VIEW: { // 设置View
                    RelativeLayout layout = (RelativeLayout) msg.obj;
                    if (layout != null) {
                        RelativeLayout.LayoutParams params = new
                                RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.MATCH_PARENT,
                                RelativeLayout.LayoutParams.MATCH_PARENT);
                        MainActivity.mainLayout.addView(layout, params);
                    }
                    break;
                }
                case DEL_VIEW: {
                    RelativeLayout layout = (RelativeLayout) msg.obj;
                    MainActivity.mainLayout.removeView(layout);
                    break;
                }
                default:
                    break;
            }
        }
    };

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        paint.setTextSize(15);
        if (gameThread != null)
            gameThread.needStop = true;
        gameThread = new GameThread(surfaceHolder);
        gameThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
            int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    public GameView(Context context) {
        super(context);
        this.mainContext = context;
        // TODO
    }

    // 两次调度间隔
    public static final int SLEEP_TIME = 40;
    // 最小暂停时间
    public static final int MIN_SLEEP = 5;
    // 游戏更新线程
    private GameThread gameThread = null;

    class GameThread extends Thread {

        public SurfaceHolder surfaceHolder = null;
        public boolean needStop = false;

        public GameThread(SurfaceHolder surfaceHolder) {
            this.surfaceHolder = surfaceHolder;
        }

        @Override
        public void run() {
            long t1, t2;
            synchronized (surfaceHolder) {
                while (gameStage != STAGE_QUIT && !needStop) {
                    try {
                        // 处理逻辑
                        stageLogic();
                        t1 = System.currentTimeMillis();
                        // 获取画布
                        canvas = surfaceHolder.lockCanvas();
                        // 场景绘制
                        if (canvas != null)
                            doStage(gameStage, PAINT);
                        t2 = System.currentTimeMillis();
                        int paintTime = (int) (t2 - t1);
                        // 睡眠时间
                        long sleepTime = SLEEP_TIME - paintTime;
                        if (sleepTime < MIN_SLEEP)
                            sleepTime = MIN_SLEEP;
                        sleep(sleepTime);
                    } catch (Exception e) {
                    } finally {
                        if (canvas != null)
                            surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
            }
        }
    }

    /**
     * 处理场景逻辑
     */
    public void stageLogic() {
        int newStage = doStage(gameStage, LOGIC);
        if (newStage != STAGE_NO_CHANGE && newStage != gameStage) {
            doStage(gameStage, CLEAN); // 清除旧的场景
            gameStage = newStage;
            doStage(gameStage, INIT);
        } else if (stageList.size() > 0) {
            newStage = STAGE_NO_CHANGE;
            synchronized (stageList) {
                newStage = stageList.get(0);
                stageList.remove(0);
            }
            if (newStage == STAGE_NO_CHANGE)
                return;
            doStage(gameStage, CLEAN); // 清除旧的场景
            gameStage = newStage;
            doStage(gameStage, INIT);
        }
    }

    /**
     * 场景处理
     *
     * @param stage 场景
     * @param step  步骤
     * @return 场景
     */
    public int doStage(int stage, int step) {
        int nextStage = 0;
        switch (stage) {
            case STAGE_INIT:
                nextStage = doInit();
                break;
            case STAGE_LOGIN:
                nextStage = doLogin(step);
                break;
            case STAGE_GAME:
                nextStage = doGame(step);
                break;
            case STAGE_LOSE:
                nextStage = doLose(step);
                break;
            default:
                nextStage = STAGE_ERROR;
                break;
        }
        return nextStage;
    }

    /**
     * 执行初始化操作
     *
     * @return 登录状态
     */
    public int doInit() {
        ResourceManager.loadResource();
        return STAGE_LOGIN;
    }

    // 登录界面
    private RelativeLayout loginView;

    /**
     * 执行登录操作
     *
     * @param step 步骤
     * @return 场景
     */
    public int doLogin(int step) {
        switch (step) {
            case INIT: // 初始化
                // 初始化血量
                player.setHp(Player.MAX_HP);
                if (loginView == null) {
                    loginView = new RelativeLayout(mainContext);
                    loginView.setBackgroundResource(R.mipmap.game_back);
                    // 创建按钮
                    Button button = new Button(mainContext);
                    // 设置按钮背景
                    button.setBackgroundResource(R.drawable.button_selector);
                    RelativeLayout.LayoutParams params = new RelativeLayout
                            .LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
                    params.addRule(RelativeLayout.CENTER_IN_PARENT);
                    // 添加按钮
                    loginView.addView(button, params);
                    button.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // 开启游戏场景
                            stageList.add(STAGE_GAME);
                        }
                    });
                    // 通知主界面加载loginView
                    viewHandler.sendMessage(
                            viewHandler.obtainMessage(SET_VIEW, loginView));
                }
                break;
            case CLEAN:
                // 清除登录界面
                if (loginView != null) {
                    // 通知主界面删除loginView
                    viewHandler.sendMessage(
                            viewHandler.obtainMessage(DEL_VIEW, loginView));
                    loginView = null;
                }
                break;
            default:
                break;
        }
        return STAGE_NO_CHANGE;
    }

    // 游戏界面
    RelativeLayout gameLayout = null;

    /**
     * 执行游戏操作
     *
     * @param step 步骤
     * @return 场景
     */
    public int doGame(int step) {
        switch (step) {
            case INIT: // 初始化
                if (gameLayout == null) {
                    gameLayout = new RelativeLayout(mainContext);
                    // 左移按钮
                    Button leftButton = new Button(mainContext);
                    // 生成id，用于右移按钮定位
                    leftButton.setId(View.generateViewId());
                    leftButton.setBackgroundResource(R.mipmap.left);
                    // 添加按钮
                    RelativeLayout.LayoutParams params = new RelativeLayout
                            .LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
                    params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    params.setMargins((int) ResourceManager.scale * 20, 0, 0,
                                      (int) ResourceManager.scale * 10);
                    gameLayout.addView(leftButton, params);
                    // 事件监听器
                    leftButton.setOnTouchListener(new OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            switch (event.getAction()) {
                                case MotionEvent.ACTION_DOWN: // 按下
                                    // TODO left
                                    break;
                                case MotionEvent.ACTION_UP: // 松开
                                    // TODO stand
                                    break;
                                default:
                                    break;
                            }
                            return false;
                        }
                    });
                    // 右移按钮
                    Button rightButton = new Button(mainContext);
                    rightButton.setBackgroundResource(R.mipmap.right);
                    // 添加按钮
                    params = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
                    params.addRule(RelativeLayout.RIGHT_OF, leftButton.getId());
                    params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    params.setMargins((int) ResourceManager.scale * 20, 0, 0,
                                      (int) ResourceManager.scale * 10);
                    gameLayout.addView(rightButton, params);
                    // 事件监听器
                    rightButton.setOnTouchListener(new OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            switch (event.getAction()) {
                                case MotionEvent.ACTION_DOWN: // 按下
                                    // TODO right
                                    break;
                                case MotionEvent.ACTION_UP: // 松开
                                    // TODO stand
                                    break;
                                default:
                                    break;
                            }
                            return false;
                        }
                    });
                    // 射击按钮
                    Button fireButton = new Button(mainContext);
                    // 生成id，用于跳跃按钮定位
                    fireButton.setId(View.generateViewId());
                    fireButton.setBackgroundResource(R.mipmap.fire);
                    // 添加按钮
                    params = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
                    params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    params.setMargins(0, 0, (int) ResourceManager.scale * 20,
                                      (int) ResourceManager.scale * 10);
                    gameLayout.addView(fireButton, params);
                    // 事件监听器
                    fireButton.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // TODO fire
                        }
                    });
                    // 跳跃按钮
                    Button jumpButton = new Button(mainContext);
                    jumpButton.setBackgroundResource(R.mipmap.jump);
                    // 添加按钮
                    params = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
                    params.addRule(RelativeLayout.LEFT_OF, fireButton.getId());
                    params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    params.setMargins(0, 0, (int) ResourceManager.scale * 20,
                                      (int) ResourceManager.scale * 10);
                    gameLayout.addView(jumpButton, params);
                    // 事件监听器
                    jumpButton.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // TODO jump
                        }
                    });
                    // 设置view
                    viewHandler.sendMessage(
                            viewHandler.obtainMessage(SET_VIEW, gameLayout));
                }
                break;
            case LOGIC: // 处理逻辑
                // 随机生成怪物
                MonsterManager.generateMonster();
                // 移动子弹
                BulletManager.movePosition();
                // 检查子弹命中
                BulletManager.checkHit();
                // 角色逻辑
                // TODO player logic
                // 处理角色死亡
                if (player.isDie())
                    stageList.add(STAGE_LOSE);
                break;
            case CLEAN: // 清除游戏界面
                if (gameLayout != null) {
                    viewHandler.sendMessage(
                            viewHandler.obtainMessage(DEL_VIEW, gameLayout));
                    gameLayout = null;
                }
                break;
            case PAINT: // 绘制游戏界面
                // TODO ResourceManager
                break;
        }
        return STAGE_NO_CHANGE;
    }

    // 游戏失败界面
    private RelativeLayout loseView;

    /**
     * 失败场景操作
     *
     * @param step 步骤
     * @return 场景
     */
    public int doLose(int step) {
        switch (step) {
            case INIT: // 初始化
                if (loseView == null) {
                    // 创建失败界面
                    loseView = new RelativeLayout(mainContext);
                    loseView.setBackgroundResource(R.mipmap.game_back);
                    // 复活按钮
                    Button button = new Button(mainContext);
                    button.setBackgroundResource(R.mipmap.again);
                    RelativeLayout.LayoutParams params = new RelativeLayout
                            .LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
                    params.addRule(RelativeLayout.CENTER_IN_PARENT);
                    loseView.addView(button, params);
                    button.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // 开启游戏场景
                            stageList.add(STAGE_GAME);
                            // 恢复角色血量
                            player.setHp(Player.MAX_HP);
                        }
                    });
                }
                break;
            case CLEAN:
                // 清除界面
                if (loseView != null) {
                    viewHandler.sendMessage(
                            viewHandler.obtainMessage(DEL_VIEW, loseView));
                    loseView = null;
                }
                break;
            case PAINT:
                break;
        }
        return STAGE_NO_CHANGE;
    }
}