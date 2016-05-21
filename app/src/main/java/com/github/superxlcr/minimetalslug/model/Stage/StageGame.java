package com.github.superxlcr.minimetalslug.model.Stage;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.github.superxlcr.minimetalslug.GameView;
import com.github.superxlcr.minimetalslug.R;
import com.github.superxlcr.minimetalslug.Utils.ResourceManager;
import com.github.superxlcr.minimetalslug.model.Bullet.BulletManager;
import com.github.superxlcr.minimetalslug.model.Monster.MonsterManager;
import com.github.superxlcr.minimetalslug.model.Player;

/**
 * 处理游戏场景
 * Created by superxlcr on 2016/5/20.
 */
public class StageGame extends Stage {

    // 游戏界面
    private static RelativeLayout gameLayout = null;

    @Override
    public void doInit() {
        if (gameLayout == null) {
            gameLayout = new RelativeLayout(mainContext);
            // 左移按钮
            Button leftButton = new Button(mainContext);
            // 生成id，用于右移按钮定位
            leftButton.setId(View.generateViewId());
            leftButton.setBackgroundResource(R.mipmap.left);
            // 添加按钮
            RelativeLayout.LayoutParams params = new RelativeLayout
                    .LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            params.setMargins((int) ResourceManager.scale * 20, 0, 0,
                              (int) ResourceManager.scale * 10);
            gameLayout.addView(leftButton, params);
            // 事件监听器
            leftButton.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN: // 按下
                            Player.player.setMove(Player.MOVE_LEFT);
                            break;
                        case MotionEvent.ACTION_UP: // 松开
                            Player.player.setMove(Player.MOVE_STAND);
                            break;
                        default:
                            break;
                    }
                    return false;
                }
            });
            // 右移按钮
            Button rightButton = new Button(mainContext);
            rightButton.setBackgroundResource(R.mipmap.right);
            // 添加按钮
            params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.RIGHT_OF, leftButton.getId());
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            params.setMargins((int) ResourceManager.scale * 20, 0, 0,
                              (int) ResourceManager.scale * 10);
            gameLayout.addView(rightButton, params);
            // 事件监听器
            rightButton.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN: // 按下
                            Player.player.setMove(Player.MOVE_RIGHT);
                            break;
                        case MotionEvent.ACTION_UP: // 松开
                            Player.player.setMove(Player.MOVE_STAND);
                            break;
                        default:
                            break;
                    }
                    return false;
                }
            });
            // 射击按钮
            Button fireButton = new Button(mainContext);
            // 生成id，用于跳跃按钮定位
            fireButton.setId(View.generateViewId());
            fireButton.setBackgroundResource(R.mipmap.fire);
            // 添加按钮
            params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            params.setMargins(0, 0, (int) ResourceManager.scale * 20,
                              (int) ResourceManager.scale * 10);
            gameLayout.addView(fireButton, params);
            // 事件监听器
            fireButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO fire
                }
            });
            // 跳跃按钮
            Button jumpButton = new Button(mainContext);
            jumpButton.setBackgroundResource(R.mipmap.jump);
            // 添加按钮
            params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.LEFT_OF, fireButton.getId());
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            params.setMargins(0, 0, (int) ResourceManager.scale * 20,
                              (int) ResourceManager.scale * 10);
            gameLayout.addView(jumpButton, params);
            // 事件监听器
            jumpButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO jump
                }
            });
            // 设置view
            GameView.viewHandler.sendMessage(GameView.viewHandler.obtainMessage(
                    GameView.SET_VIEW, gameLayout));
        }
    }

    @Override
    public void doLogic() {
        // 随机生成怪物
        MonsterManager.generateMonster();
        // 角色逻辑
        Player.player.logic();
        // 移动子弹
        BulletManager.movePosition();
        // 检查子弹命中
        BulletManager.checkHit();
        // 处理角色死亡
        if (Player.player.isDie()) // 下一个场景为失败场景
            stageList.add(new StageLose());
    }

    @Override
    public void doClean() {
        if (gameLayout != null) {
            GameView.viewHandler.sendMessage(GameView.viewHandler.obtainMessage(
                    GameView.DEL_VIEW, gameLayout));
            gameLayout = null;
        }
    }

    @Override
    public void doPaint(Canvas canvas, Paint paint) {
        // 绘制地图
        ResourceManager.drawMap(canvas);
        // 绘制怪物
//        MonsterManager.drawMonster(canvas);
        // TODO 绘制子弹
        // 绘制角色
        Player.player.draw(canvas);
    }
}
