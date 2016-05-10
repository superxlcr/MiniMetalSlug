package com.github.superxlcr.minimetalslug.Utils;

import android.graphics.Bitmap;
import android.media.SoundPool;

import java.util.HashMap;

/**
 * 管理游戏图片、音效资源，并负责绘制游戏主界面
 * Created by superxlcr on 2016/5/10.
 */
public class ViewManager {
    // 音效管理
    public static SoundPool soundPool;
    public static HashMap<Integer, Integer> soundMap = new HashMap<>();
    // 地图图片
    public static Bitmap map = null;
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


}
