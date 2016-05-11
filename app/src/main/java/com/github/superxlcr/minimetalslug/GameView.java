package com.github.superxlcr.minimetalslug;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.github.superxlcr.minimetalslug.model.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by superxlcr on 2016/5/11.
 */
public class GameView extends SurfaceView {
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
    public static final List<Integer> stageList = Collections
            .synchronizedList(new ArrayList<Integer>());

    public GameView(Context context) {
        super(context);
        this.mainContext = context;
        // TODO
    }

    public int doStage(int stage, int step) {
        // TODO
        return 0;
    }


}
