package com.example.yehsc.app;

import android.content.Intent;
import android.graphics.Point;
import android.media.Image;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private TextView scoreLabel;
    private TextView startLabel;
    private ImageView box;
    private ImageView orange;
    private ImageView pink;
    private ImageView black;

    private  int frameHeight;
    private int boxSize;
    private int screenWidth;
    private int screenHeight;

    private int boxY;
    private int orangeX;
    private int orangeY;
    private int pinkX;
    private int pinkY;
    private int blackX;
    private int blackY;

    private int boxSpeed;
    private int orangeSpeed;
    private int pinkSpeed;
    private int blackSpeed;

    private int score = 0;

    //初始
    private Handler handler = new Handler();
    private Timer timer = new Timer();

    private boolean action_flg = false;
    private boolean start_flg = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scoreLabel = (TextView) findViewById(R.id.scoreLabel);
        startLabel = (TextView) findViewById(R.id.startLabel);
        box = (ImageView) findViewById(R.id.box);
        orange = (ImageView) findViewById(R.id.orange);
        pink = (ImageView) findViewById(R.id.pink);
        black = (ImageView) findViewById(R.id.black);

        //取得螢幕大小
        WindowManager wm = getWindowManager();
        Display disp = wm.getDefaultDisplay();
        Point size = new Point();
        disp.getSize(size);

        screenWidth = size.x;
        screenHeight = size.y;

        boxSpeed = Math.round(screenHeight / 60F);
        orangeSpeed = Math.round(screenWidth / 60F);
        pinkSpeed = Math.round(screenWidth / 36F);
        blackSpeed = Math.round(screenWidth / 45F);

        //移動到螢幕外
        orange.setX(-80.0f);
        orange.setY(-80.0f);
        pink.setX(-80.0f);
        pink.setY(-80.0f);
        black.setX(-80.0f);
        black.setY(-80.0f);

        scoreLabel.setText("Score : 0");

    }

    public void changePos(){
        hitCheck();
        //orange
        orangeX -= 18;
        if(orangeX < 0){
            orangeX = screenWidth + 20;
            orangeY = (int) Math.floor(Math.random() * (frameHeight - orange.getHeight()));
        }
        orange.setX(orangeX);
        orange.setY(orangeY);

        //black
        blackX -= 24;
        if(blackX < 0){
            blackX = screenWidth + 10;
            blackY = (int) Math.floor(Math.random() * (frameHeight - black.getHeight()));
        }
        black.setX(blackX);
        black.setY(blackY);

        //pink
        pinkX -= 30;
        if(pinkX < 0){
            pinkX = screenWidth + 5000;
            pinkY = (int) Math.floor(Math.random() * (frameHeight - pink.getHeight()));
        }
        pink.setX(pinkX);
        pink.setY(pinkY);

        if(action_flg == true){
            boxY -= 30;
        }else{
            boxY += 30;
        }
        if(boxY < 0) boxY = 0;

        if(boxY > frameHeight - boxSize) boxY =  frameHeight - boxSize;

        box.setY(boxY);

        scoreLabel.setText("Score : " + score);
    }

    public void hitCheck(){
        //orange
        int orangeCenterX = orangeX + orange.getWidth() / 2;
        int orangeCenterY = orangeY + orange.getHeight() / 2;

        if(0 <= orangeCenterX && orangeCenterX <= boxSize && boxY <=orangeCenterY && orangeCenterY <= boxY +boxSize){
            score += 10;
            orangeX = -10;
        }
        //pink
        int pinkCenterX = pinkX + pink.getWidth() / 2;
        int pinkCenterY = pinkY + pink.getHeight() / 2;

        if(0 <= pinkCenterX && pinkCenterX <= boxSize && boxY <=pinkCenterY && pinkCenterY <= boxY +boxSize){
            score += 30;
            pinkX = -10;
        }
        //black
        int blackCenterX = blackX + black.getWidth() / 2;
        int blackCenterY = blackY + black.getHeight() / 2;

        if(0 <= blackCenterX && blackCenterX <= boxSize && boxY <=blackCenterY && blackCenterY <= boxY +boxSize){
            timer.cancel();
            timer = null;
            //顯示結果
            Intent intent = new Intent(getApplicationContext(),result.class);
            intent.putExtra("SCORE",score);
            startActivity(intent);
        }
    }

    public boolean onTouchEvent(MotionEvent me){
        if(start_flg == false){
            start_flg = true;

            FrameLayout frame = (FrameLayout) findViewById(R.id.frame);
            frameHeight = frame.getHeight();

            boxY = (int)box.getY();
            boxSize = box.getHeight();

            startLabel.setVisibility(View.GONE);

            //start the timer
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            changePos();
                        }
                    });
                }
            },0,20);

        }else{
            if(me.getAction() == MotionEvent.ACTION_DOWN){
                action_flg = true;
            }else if(me.getAction() == MotionEvent.ACTION_UP){
                action_flg = false;
            }
        }
        return true;
    }
    //取消返回建
    @Override
    public boolean dispatchKeyEvent(KeyEvent event){
        if(event.getAction() == KeyEvent.ACTION_DOWN){
            switch (event.getKeyCode()){
                case KeyEvent.KEYCODE_BACK:
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }
}
