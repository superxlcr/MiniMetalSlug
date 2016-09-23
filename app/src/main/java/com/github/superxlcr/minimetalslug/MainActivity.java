package com.github.superxlcr.minimetalslug;

import android.content.Context;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {
    // 调试模式
    public static boolean DEBUG = false;
    // 主布局容器
    public static FrameLayout mainLayout = null;
    // 主布局参数
    public static FrameLayout.LayoutParams lp = null;
    // 游戏主界面
    public static GameView gameView = null;
    // 资源管理核心类
    public static Resources resources = null;
    // 窗口大小
    public static int windowWidth;
    public static int windowHeight;
    // 音乐播放器
    public static MediaPlayer mediaPlayer = null;

    public static Context context = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = MainActivity.this;
        // 全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                             WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        // 获取屏幕大小
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        windowWidth = metrics.widthPixels;
        windowHeight = metrics.heightPixels;
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        // 获取资源类
        resources = getResources();
        // 加载xml
        setContentView(R.layout.activity_main);
        // 获取主布局
        mainLayout = (FrameLayout) findViewById(R.id.main_layout);
        // 创建游戏组件并添加到布局中
        gameView = new GameView(this);
        lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                                          FrameLayout.LayoutParams
                                                  .MATCH_PARENT);
        mainLayout.addView(gameView, lp);
        // MUSIC
        mediaPlayer = MediaPlayer.create(this, R.raw.bgm);
        mediaPlayer.setLooping(true);
        mediaPlayer.setVolume(0.1f, 0.1f);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 启动Music
        if (mediaPlayer != null)
            mediaPlayer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 暂停Music
        if (mediaPlayer != null)
            mediaPlayer.pause();
    }
}
