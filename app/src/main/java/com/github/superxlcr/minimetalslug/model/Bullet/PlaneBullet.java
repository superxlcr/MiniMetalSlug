package com.github.superxlcr.minimetalslug.model.Bullet;

import android.graphics.Bitmap;

import com.github.superxlcr.minimetalslug.MainActivity;
import com.github.superxlcr.minimetalslug.R;
import com.github.superxlcr.minimetalslug.Utils.ResourceManager;

/**
 * 飞机怪物子弹
 * Created by superxlcr on 2016/5/10.
 */
public class PlaneBullet extends Bullet {

    public static Bitmap bitmap = null;

    public PlaneBullet(int x, int y, int dir) {
        super(x, y, dir);
        if (bitmap == null)
            bitmap = ResourceManager
                    .createBitmapByID(MainActivity.resources, R.mipmap.bullet_3,
                                      ResourceManager.scale);
    }

    @Override
    public Bitmap getBitmap() {
        return bitmap;
    }

    @Override
    public int getAbsSpeedX() {
        return (int) (ResourceManager.scale * 8);
    }

    @Override
    public int getSpeedY() {
        return (int) (ResourceManager.scale * 6);
    }
}
