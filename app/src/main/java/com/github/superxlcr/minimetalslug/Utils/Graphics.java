package com.github.superxlcr.minimetalslug.Utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
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

    public static final float INTERVAL_SCALE = 0.05f; // 缩放梯度
    public static final int DEFAULT_TIMES_SCALE = 20; // 默认缩放大小,不缩放

    /**
     * 从源位图截取图片，变换后绘制
     *
     * @param canvas 画布
     * @param src    源位图
     * @param srcX   源位图x
     * @param srcY   源位图y
     * @param width  截取长度
     * @param height 截取高度
     * @param trans  变换类型
     * @param drawX  绘制x
     * @param drawY  绘制y
     * @param degree 旋转角度
     * @param scale  缩放大小
     */
    public synchronized static void drawMatrixImage(Canvas canvas, Bitmap src,
            int srcX, int srcY, int width, int height, Trans trans, int drawX,
            int drawY, int degree, int scale) {
        if (canvas == null || src == null || src.isRecycled())
            return;
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        // 截取长度高度超出上限
        if (srcX + width > srcWidth)
            width = srcWidth - srcX;
        if (srcY + height > srcHeight)
            height = srcHeight - srcY;
        if (width <= 0 || height <= 0)
            return;
        // 判断变换状态
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
        // 无需变换情况
        if (rotate == 0 && degree == 0 && scaleX == DEFAULT_TIMES_SCALE)
            drawImage(canvas, src, srcX, srcY, width, height, drawX, drawY);
        else {
            Matrix matrix = new Matrix();
            // 缩放
            matrix.postScale(scaleX * INTERVAL_SCALE, scaleY * INTERVAL_SCALE);
            // 旋转
            matrix.postRotate(rotate);
            matrix.postRotate(degree);
            // 截取
            RectF srcRectF = new RectF();
            srcRectF.set(srcX, srcY, srcX + width, srcY + height);
            matrix.mapRect(srcRectF);
            // 移动
            matrix.postTranslate(drawX - srcRectF.left, drawY - srcRectF.top);
            // 左上，右上，右下，左下
            float[] pts = new float[]{srcX, srcY, srcX + width, srcY,
                    srcX + width, srcY + height, srcX, srcY + height};
            matrix.mapPoints(pts);
            // 截取画布
            canvas.save();
            Path path = new Path();
            path.moveTo(pts[0], pts[1]);
            path.lineTo(pts[2], pts[3]);
            path.lineTo(pts[4], pts[5]);
            path.lineTo(pts[6], pts[7]);
            path.close();
            canvas.clipPath(path);
            // 使用matrix绘制位图
            canvas.drawBitmap(src, matrix, null);
            canvas.restore();
        }
    }

    /**
     * 图片缩放
     *
     * @param bitmap    原图
     * @param newWidth  缩放后长度
     * @param newHeight 缩放后宽度
     * @return 缩放后图片
     */
    public static Bitmap scale(Bitmap bitmap, float newWidth, float newHeight) {
        if (bitmap == null || bitmap.isRecycled())
            return null;
        float width = bitmap.getWidth();
        float height = bitmap.getHeight();
        if (width == 0 || height == 0 || newWidth == 0 || newHeight == 0)
            return null;
        // 缩放所需Matrix
        Matrix matrix = new Matrix();
        matrix.postScale(newWidth / width, newHeight / height);
        try {
            return Bitmap.createBitmap(bitmap, 0, 0, (int) width, (int) height,
                                       matrix, true);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 绘制位图
     *
     * @param canvas 画布
     * @param src    源位图
     * @param srcX   位图截取x
     * @param srcY   位图截取y
     * @param width  截取长度
     * @param height 截取高度
     * @param drawX  绘制x
     * @param drawY  绘制y
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

    /**
     * 字符串对齐方式
     */
    public enum Anchor {
        LEFT, RIGHT, HCENTER
    }

    /**
     * 绘制字符串
     *
     * @param canvas   画布
     * @param text     字符串
     * @param textSize 字符串大小
     * @param x        绘制x
     * @param y        绘制y
     * @param anchor   字符串对齐方式
     * @param paint    画笔
     */
    public static void drawString(Canvas canvas, String text, float textSize,
            float x, float y, Anchor anchor, Paint paint) {
        // 设置字符串对齐方式
        switch (anchor) {
            case LEFT:
                paint.setTextAlign(Paint.Align.LEFT);
                break;
            case RIGHT:
                paint.setTextAlign(Paint.Align.RIGHT);
                break;
            case HCENTER:
                paint.setTextAlign(Paint.Align.CENTER);
                break;
        }
        // 设置字符串字体大小
        paint.setTextSize(textSize);
        // 绘制字符串
        canvas.drawText(text, x, y, paint);
    }

    /**
     * 绘制包边字符串
     *
     * @param canvas      画布
     * @param borderColor 包边颜色
     * @param textColor   字符串颜色
     * @param text        字符串
     * @param x           绘制x
     * @param y           绘制y
     * @param borderWidth 包边宽度
     * @param paint       画笔
     */
    public static void drawBorderString(Canvas canvas, int borderColor,
            int textColor, String text, float x, float y, int borderWidth,
            Paint paint) {
        // 开启抗锯齿
        paint.setAntiAlias(true);
        // 绘制字符串包边
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(borderWidth);
        paint.setColor(Color.rgb((borderColor & 0xff0000) >> 16,
                                 (borderColor & 0x00ff00) >> 8,
                                 (borderColor & 0x0000ff)));
        canvas.drawText(text, x, y, paint);
        // 绘制字符串
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.rgb((textColor & 0xff0000) >> 16,
                                 (textColor & 0x00ff00) >> 8,
                                 (textColor & 0x0000ff)));
        canvas.drawText(text, x, y, paint);
    }

    /**
     * 绘制矩形
     *
     * @param canvas      画布
     * @param left        左
     * @param top         上
     * @param right       右
     * @param bottom      下
     * @param borderColor 边界颜色
     * @param borderWidth 边界粗细
     */
    public static void drawRectengle(Canvas canvas, int left, int top,
            int right, int bottom, int borderColor, int borderWidth) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(borderWidth);
        paint.setColor(Color.rgb((borderColor & 0xff0000) >> 16,
                                 (borderColor & 0x00ff00) >> 8,
                                 (borderColor & 0x0000ff)));
        canvas.drawRect(left, top, right, bottom, paint);
    }

    /**
     * 绘制点
     *
     * @param canvas 画布
     * @param x      x坐标
     * @param y      y坐标
     * @param color  颜色
     */
    public static void drawPoint(Canvas canvas, int x, int y, int color,
            int width) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(
                Color.rgb((color & 0xff0000) >> 16, (color & 0x00ff00) >> 8,
                          (color & 0x0000ff)));
        canvas.drawCircle(x, y, width, paint);
    }
}
