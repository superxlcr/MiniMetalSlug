package com.github.superxlcr.minimetalslug.model.Stage;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.superxlcr.minimetalslug.GameView;
import com.github.superxlcr.minimetalslug.R;
import com.github.superxlcr.minimetalslug.Utils.ResourceManager;
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
        // 初始化分数
        Player.player.setPoint(0);
        if (loginView == null) {
            loginView = new RelativeLayout(mainContext);
            loginView.setBackgroundResource(R.mipmap.game_back);
            // 添加输入框
            final EditText editText = new EditText(mainContext);
            editText.setId(View.generateViewId());
            editText.setHint("Please enter your name");
            editText.setWidth((int) ResourceManager.scale * 800);
            editText.setBackgroundColor(Color.WHITE);
            editText.setTextColor(Color.BLACK);
            editText.setHintTextColor(Color.GRAY);
            RelativeLayout.LayoutParams params = new RelativeLayout
                    .LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            params.setMargins(0, 0, 0, (int) ResourceManager.scale * 50);
            // 添加输入框
            loginView.addView(editText, params);
            // 创建按钮
            Button button = new Button(mainContext);
            // 设置按钮背景
            button.setBackgroundResource(R.drawable.button_selector);
            RelativeLayout.LayoutParams params2 = new RelativeLayout
                    .LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            params2.addRule(RelativeLayout.CENTER_HORIZONTAL);
            params2.addRule(RelativeLayout.BELOW, editText.getId());
            // 添加按钮
            loginView.addView(button, params2);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = editText.getText().toString();
                    if (name.isEmpty()) // 名称为空
                        Toast.makeText(mainContext, "Please enter your name",
                                       Toast.LENGTH_SHORT).show();
                    else {
                        Player.player.setName(name);
                        // 开启游戏场景
                        stageList.add(new StageGame());
                    }
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
