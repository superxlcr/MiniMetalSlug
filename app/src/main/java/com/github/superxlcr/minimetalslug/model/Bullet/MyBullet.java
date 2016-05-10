package com.github.superxlcr.minimetalslug.model.Bullet;

import android.graphics.Bitmap;

import com.github.superxlcr.minimetalslug.MainActivity;
import com.github.superxlcr.minimetalslug.R;
import com.github.superxlcr.minimetalslug.Utils.ResourceManager;

/**
 * 玩家的子弹
 * Created by superxlcr on 2016/5/10.
 */
public class MyBullet extends Bullet {

    public static Bitmap bitmap = null;

    public MyBullet() {
        if (bitmap == null)
            bitmap = ResourceManager
                    .createBitmapByID(MainActivity.resources, R.mipmap.bullet_1,
                                      ResourceManager.scale);
    }

    @Override
    public Bitmap getBitmap() {
        return bitmap;
    }

    @Override
    public int getAbsSpeedX() {
        return (int) (ResourceManager.scale * 12);
    }

    @Override
    public int getSpeedY() {
        return 0;
    }
}
