package com.github.superxlcr.minimetalslug.Utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Vibrator;

import com.github.superxlcr.minimetalslug.GameView;
import com.github.superxlcr.minimetalslug.MainActivity;
import com.github.superxlcr.minimetalslug.R;
import com.github.superxlcr.minimetalslug.model.Monster.Plane;
import com.github.superxlcr.minimetalslug.model.Player;

import java.util.HashMap;

/**
 * 管理游戏图片、音效资源，并负责绘制游戏主界面
 * Created by superxlcr on 2016/5/10.
 */
public class ResourceManager {

    private static Context mContext = null;

    // 音效管理
    private static SoundPool soundPool;
    private static HashMap<Integer, Integer> soundMap = new HashMap<>();
    private static Vibrator vibrator = null;
    // 地图图片
    public static Bitmap map = null;

    // 游戏对图片的缩放比例
    public static float scale = 0f;
    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;

    public static final int BOMB = 0;
    public static final int OH = 1;
    public static final int SHOT = 2;

    /**
     * 加载资源
     */
    public static void loadResource() {
        // 大小初始化
        SCREEN_WIDTH = MainActivity.windowWidth;
        SCREEN_HEIGHT = MainActivity.windowHeight;
        // 音效相关初始化
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        soundMap.put(BOMB, soundPool.load(getmContext(), R.raw.bomb, 1));
        soundMap.put(OH, soundPool.load(getmContext(), R.raw.oh, 1));
        soundMap.put(SHOT, soundPool.load(getmContext(), R.raw.shot, 1));
        // 解析地图，确定缩放大小
        Bitmap temp = BitmapFactory
                .decodeResource(MainActivity.resources, R.mipmap.map);
        if (temp != null && !temp.isRecycled()) {
            int height = temp.getHeight();
            if (height != SCREEN_HEIGHT && SCREEN_HEIGHT != 0) {
                scale = (float) SCREEN_HEIGHT / (float) height;
                map = Graphics.scale(temp, temp.getWidth() * scale,
                                     temp.getHeight() * scale);
                temp.recycle();
            } else {
                map = temp;
            }
        }
    }

    /**
     * 按照scale初始化并缩放位图
     *
     * @param resources 资源
     * @param resId     初始化图片id
     * @param scale     缩放比例
     * @return 位图
     */
    public static Bitmap createBitmapByID(Resources resources, int resId,
            float scale) {
        // scale错误
        if (scale == 0)
            return null;
        Bitmap bitmap = BitmapFactory.decodeResource(resources, resId);
        // 需要缩放
        if (bitmap != null && !bitmap.isRecycled() && scale > 0 && scale != 1) {
            int width = (int) (bitmap.getWidth() * scale);
            int height = (int) (bitmap.getHeight() * scale);
            Bitmap newBitmap = Graphics.scale(bitmap, width, height);
            if (!bitmap.isRecycled() && !bitmap.equals(newBitmap))
                bitmap.recycle();
            return newBitmap;
        }
        return bitmap;
    }

    /**
     * 绘制地图
     *
     * @param canvas 画布
     */
    public static void drawMap(Canvas canvas) {
        if (map != null && !map.isRecycled()) {
            int shift = Player.player.getShift();
            int mapWidth = map.getWidth();
            int srcX = shift % mapWidth;
            int drawX = 0;
            // 绘制地图
            while (drawX < SCREEN_WIDTH) {
                // 实际需要绘制宽度
                int drawWidth = Math.min(mapWidth - srcX, SCREEN_WIDTH - drawX);
                Graphics.drawImage(canvas, map, srcX, 0, drawWidth,
                                   map.getHeight(), drawX, 0);
                // 下次绘图不需要截取
                srcX = 0;
                drawX += drawWidth;
            }
        }
    }

    /**
     * 播放音效
     *
     * @param index
     */
    public static void SoundPoolPlay(int index, float volume) {
        int value = soundMap.get(index);
        soundPool.play(value, volume, volume, 1, 0, 1);
    }

    /**
     * 执行手机振动
     *
     * @param time 振动时间
     */
    public static void Shake(long time) {
        if (vibrator == null)
            vibrator = (Vibrator) getmContext()
                    .getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(time);
    }

    private static Context getmContext() {
        if (mContext == null)
            mContext = MainActivity.context.getApplicationContext();
        return mContext;
    }
}
