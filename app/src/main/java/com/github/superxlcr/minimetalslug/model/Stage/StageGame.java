package com.github.superxlcr.minimetalslug.model.Stage;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.github.superxlcr.minimetalslug.GameView;
import com.github.superxlcr.minimetalslug.MainActivity;
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
            rightButton.setId(View.generateViewId());
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
            // 下蹲按钮
            Button downButton = new Button(mainContext);
            downButton.setBackgroundResource(R.mipmap.dowm);
            // 添加按钮
            params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.RIGHT_OF, rightButton.getId());
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            params.setMargins((int) ResourceManager.scale * 20, 0, 0,
                              (int) ResourceManager.scale * 10);
            gameLayout.addView(downButton, params);
            // 事件监听器
            downButton.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN: // 按下时下蹲
                            Player.player.dowm();
                            break;
                        case MotionEvent.ACTION_UP: // 松开时起身
                            Player.player.up();
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
                    Player.player.fire();
                }
            });
            // 跳跃按钮
            Button jumpButton = new Button(mainContext);
            jumpButton.setId(View.generateViewId());
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
                    Player.player.jump();
                }
            });
            // 调试按钮
            Button settingsButton = new Button(mainContext);
            // 生成id
            settingsButton.setId(View.generateViewId());
            settingsButton.setBackgroundResource(R.mipmap.settings);
            // 添加按钮
            params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            params.setMargins(0, 0, (int) ResourceManager.scale * 20,
                              (int) ResourceManager.scale * 10);
            gameLayout.addView(settingsButton, params);
            // 事件监听器
            settingsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (MainActivity.DEBUG)
                        MainActivity.DEBUG = false;
                    else
                        MainActivity.DEBUG = true;
                }
            });
            // 设置view
            GameView.viewHandler.sendMessage(GameView.viewHandler.obtainMessage(
                    GameView.SET_VIEW, gameLayout));
        }
    }

    @Override
    public void doLogic() {
        // 角色逻辑
        Player.player.logic();
        // 随机生成怪物
        MonsterManager.generateMonster();
        // 处理角色引起的怪物位移
        MonsterManager.updatePosition(Player.player.getShift());
        // 检查怪物是否与玩家发生碰撞
        MonsterManager.checkMonsterHitByPlayer();
        // 移动子弹
        BulletManager.movePosition();
        // 处理角色引起的子弹位移
        BulletManager.updatePosition(Player.player.getShift());
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
        MonsterManager.drawMonster(canvas);
        // 绘制子弹
        BulletManager.drawBullet(canvas);
        // 绘制角色
        Player.player.draw(canvas);
        if (MainActivity.DEBUG) // 绘制判定角色框
            Player.player.drawPlayerRect(canvas);
    }
}
