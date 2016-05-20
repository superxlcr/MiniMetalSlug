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
 * 处理失败场景
 * Created by superxlcr on 2016/5/20.
 */
public class StageLose extends Stage {

    // 游戏失败界面
    private static RelativeLayout loseView = null;

    @Override
    public void doInit() {
        if (loseView == null) {
            // 创建失败界面
            loseView = new RelativeLayout(mainContext);
            loseView.setBackgroundResource(R.mipmap.game_back);
            // 复活按钮
            Button button = new Button(mainContext);
            button.setBackgroundResource(R.mipmap.again);
            RelativeLayout.LayoutParams params = new RelativeLayout
                    .LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            loseView.addView(button, params);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 开启游戏场景,复活
                    stageList.add(new StageGame());
                    // 恢复角色血量
                    Player.player.setHp(Player.MAX_HP);
                }
            });
            // 设置场景
            GameView.viewHandler.sendMessage(
                    GameView.viewHandler.obtainMessage(GameView.SET_VIEW, loseView));
        }
    }

    @Override
    public void doLogic() {

    }

    @Override
    public void doClean() {
        // 清除界面
        if (loseView != null) {
            GameView.viewHandler.sendMessage(
                    GameView.viewHandler.obtainMessage(GameView.DEL_VIEW, loseView));
            loseView = null;
        }
    }

    @Override
    public void doPaint(Canvas canvas, Paint paint) {

    }
}
