package com.github.superxlcr.minimetalslug.Utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * 绘图工具类
 * Created by superxlcr on 2016/5/7.
 */
public final class Graphics {

    /**
     * 图片翻转常量
     */
    public enum Trans {
        TRANS_NONE, TRANS_ROT90, TRANS_ROT180, TRANS_ROT270,
        TRANS_MIRROR, TRANS_MIRROR_ROT90, TRANS_MIRROR_ROT180,
        TRANS_MIRROR_ROT270
    }

    public static final float INTERBAL_SCALE = 0.05f; // 缩放梯度
    public static final int TIMES_SCALE = 20;
    private static final float[] pts = new float[8];
    private static final Path paht = new Path();
    private static final RectF srcRect = new RectF();

    public synchronized static void drawMatrixImage(Canvas canvas, Bitmap src,
            int srcX, int srcY, int width, int height, Trans trans, int drawX,
            int drawY, int degree, int scale) {
        if (canvas == null || src == null || src.isRecycled())
            return;
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        if (srcX + width > srcWidth)
            width = srcWidth - srcX;
        if (srcY + height > srcHeight)
            height = srcHeight - srcY;
        if (width <= 0 || height <= 0)
            return;
        int scaleX = scale;
        int scaleY = scale;
        int rotate = 0;
        switch (trans) {
            case TRANS_ROT90:
                rotate = 90;
                break;
            case TRANS_ROT180:
                rotate = 180;
                break;
            case TRANS_ROT270:
                rotate = 270;
                break;
            case TRANS_MIRROR:
                scaleX = -scaleX;
                break;
            case TRANS_MIRROR_ROT90:
                scaleX = -scaleX;
                rotate = 90;
                break;
            case TRANS_MIRROR_ROT180:
                scaleX = -scaleX;
                rotate = 180;
                break;
            case TRANS_MIRROR_ROT270:
                scaleX = -scaleX;
                rotate = 270;
                break;
        }

    }

    /**
     * 绘制位图
     * @param canvas 画布
     * @param src 源位图
     * @param srcX 位图截取x
     * @param srcY 位图截取y
     * @param width 截取长度
     * @param height 截取高度
     * @param drawX 绘制x
     * @param drawY 绘制y
     */
    public synchronized static void drawImage(Canvas canvas, Bitmap src,
            int srcX, int srcY, int width, int height, int drawX, int drawY) {
        if (canvas == null || src == null || src.isRecycled())
            return;
        // 无需截取情况
        if (srcX == 0 && srcY == 0 && src.getWidth() <= width && src
                .getHeight() <= height) {
            canvas.drawBitmap(src, drawX, drawY, null);
            return;
        }
        // 源矩形
        Rect srcRect = new Rect();
        srcRect.left = srcX;
        srcRect.top = srcY;
        srcRect.right = srcX + width;
        srcRect.bottom = srcY + height;
        // 目标矩形
        Rect dstRect = new Rect();
        dstRect.left = drawX;
        dstRect.top = drawY;
        dstRect.right = drawX + width;
        dstRect.bottom = drawY + height;
        // 将源位图的srcRect区域取出绘制在dstRect
        canvas.drawBitmap(src, srcRect, dstRect, null);
    }

}
