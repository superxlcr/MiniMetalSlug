package com.github.superxlcr.minimetalslug.model.Stage;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.github.superxlcr.minimetalslug.Utils.ResourceManager;
import com.github.superxlcr.minimetalslug.model.Player;

/**
 * 初始化场景
 * Created by superxlcr on 2016/5/20.
 */
public class StageInit extends Stage {

    @Override
    public void doInit() {
        // 资源初始化
        ResourceManager.loadResource();
        // TODO 玩家初始化
        Player.player = new Player("no name boy", Player.MAX_HP);
        // 下一场景为登录
        stageList.add(new StageLogin());
    }

    @Override
    public void doLogic() {

    }

    @Override
    public void doClean() {

    }

    @Override
    public void doPaint(Canvas canvas, Paint paint) {

    }
}
