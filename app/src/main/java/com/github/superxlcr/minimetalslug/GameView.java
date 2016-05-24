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
import android.widget.Button;
import android.widget.RelativeLayout;

import com.github.superxlcr.minimetalslug.Utils.ResourceManager;
import com.github.superxlcr.minimetalslug.model.Bullet.BulletManager;
import com.github.superxlcr.minimetalslug.model.Monster.MonsterManager;
import com.github.superxlcr.minimetalslug.model.Player;
import com.github.superxlcr.minimetalslug.model.Stage.Stage;
import com.github.superxlcr.minimetalslug.model.Stage.StageInit;
import com.github.superxlcr.minimetalslug.model.Stage.StageQuit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 游戏界面
 * Created by superxlcr on 2016/5/11.
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    // 画布和画笔
    private Paint paint = null;
    private Canvas canvas = null;
    private SurfaceHolder surfaceHolder;
    // 场景
    private Stage gameStage = null;

    // 设置view
    public static final int SET_VIEW = 0;
    // 删除view
    public static final int DEL_VIEW = 1;
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

    // SurfaceHolder 回调方法
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
        // 保存上下文
        Stage.mainContext = context;
        // TODO
        // 初始化场景状态
        gameStage = new StageInit();
        gameStage.doInit();
        // 初始化画笔
        paint = new Paint();
        // 设置抗锯齿
        paint.setAntiAlias(true);
        // 设置该组件会保持屏幕常量，避免游戏过程中出现黑屏。
        setKeepScreenOn(true);
        // 设置焦点，相应事件处理
        setFocusable(true);
        // 获取SurfaceHolder并添加回调函数
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
    }

    // TODO 睡眠时间
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
                while (!(gameStage instanceof StageQuit) && !needStop) {
                    try {
                        // 处理逻辑
                        stageLogic();
                        t1 = System.currentTimeMillis();
                        // 获取画布
                        canvas = surfaceHolder.lockCanvas();
                        // 场景绘制
                        if (canvas != null)
                            gameStage.doPaint(canvas, paint);
                        // 计算绘制时间
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
    private void stageLogic() {
        // 处理逻辑
        gameStage.doLogic();
        // 是否需要切换场景
        if (Stage.stageList.size() > 0) {
            // 取出新场景
            Stage newStage = Stage.stageList.get(0);
            Stage.stageList.remove(0);
            // 清除旧的场景
            gameStage.doClean();
            gameStage = newStage;
            gameStage.doInit();
        }
    }

}