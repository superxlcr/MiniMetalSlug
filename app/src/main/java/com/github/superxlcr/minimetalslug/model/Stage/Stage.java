package com.github.superxlcr.minimetalslug.model.Stage;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 场景基类
 * Created by superxlcr on 2016/5/20.
 */
public abstract class Stage {

    // 场景列表
    public static List<Stage> stageList = new CopyOnWriteArrayList<>();
    // 上下文
    public static Context mainContext = null;

    public abstract void doInit();

    public abstract void doLogic();

    public abstract void doClean();

    public abstract void doPaint(Canvas canvas, Paint paint);

}
