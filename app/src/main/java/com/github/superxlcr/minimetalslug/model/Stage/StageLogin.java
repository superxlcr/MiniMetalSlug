package com.github.superxlcr.minimetalslug.model.Stage;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.github.superxlcr.minimetalslug.GameView;
import com.github.superxlcr.minimetalslug.R;
import com.github.superxlcr.minimetalslug.model.Player;

/**
 * 处理登录场景
 * Created by superxlcr on 2016/5/20.
 */
public class StageLogin extends Stage {

    // 登录界面
    private static RelativeLayout loginView = null;

    @Override
    public void doInit() {
        // 初始化血量
        Player.player.setHp(Player.MAX_HP);
        if (loginView == null) {
            loginView = new RelativeLayout(mainContext);
            loginView.setBackgroundResource(R.mipmap.game_back);
            // 创建按钮
            Button button = new Button(mainContext);
            // 设置按钮背景
            button.setBackgroundResource(R.drawable.button_selector);
            RelativeLayout.LayoutParams params = new RelativeLayout
                    .LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            // 添加按钮
            loginView.addView(button, params);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 开启游戏场景
                    stageList.add(new StageGame());
                }
            });
            // 通知主界面加载loginView
            GameView.viewHandler.sendMessage(GameView.viewHandler.obtainMessage(
                    GameView.SET_VIEW, loginView));
        }
    }

    @Override
    public void doLogic() {

    }

    @Override
    public void doClean() {
        // 清除登录界面
        if (loginView != null) {
            // 通知主界面删除loginView
            GameView.viewHandler.sendMessage(GameView.viewHandler.obtainMessage(
                    GameView.DEL_VIEW, loginView));
            loginView = null;
        }
    }

    @Override
    public void doPaint(Canvas canvas, Paint paint) {

    }
}
