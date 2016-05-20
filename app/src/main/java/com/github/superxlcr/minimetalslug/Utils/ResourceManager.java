package com.github.superxlcr.minimetalslug.Utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.AudioManager;
import android.media.SoundPool;

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
    // 音效管理
    public static SoundPool soundPool;
    public static HashMap<Integer, Integer> soundMap = new HashMap<>();
    // 地图图片
    public static Bitmap map = null;

    // 游戏对图片的缩放比例
    public static float scale = 0f;
    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;

    /**
     * 加载资源
     */
    public static void loadResource() {
        // 大小初始化
        SCREEN_WIDTH = MainActivity.windowWidth;
        SCREEN_HEIGHT = MainActivity.windowHeight;
        // 音效相关初始化
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        // TODO soundMap
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
        if (canvas == null)
            return;
        if (map != null && !map.isRecycled()) {
            int width = map.getWidth() + Player.player.getShift();
            // 绘制地图
            Graphics.drawImage(canvas, map, 0, 0, -Player.player.getShift(), 0,
                               width, map.getHeight());
            int totalWidth = width;
            // 拼接地图,绘制剩下的长度
            while (totalWidth < SCREEN_WIDTH) {
                int mapWidth = map.getWidth();
                int drawWidth = SCREEN_WIDTH - totalWidth;
                if (mapWidth < drawWidth)
                    drawWidth = mapWidth;
                Graphics.drawImage(canvas, map, totalWidth, 0, 0, 0, drawWidth,
                                   map.getHeight());
                totalWidth += drawWidth;
            }
        }
    }
}
