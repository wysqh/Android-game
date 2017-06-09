package com.example.gtk.thebattleofplane.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.example.gtk.thebattleofplane.R;
import com.example.gtk.thebattleofplane.utils.DensityUtil;

/**
 * Created by gutia on 2017-06-02.
 */

public class GameView extends SurfaceView {
    private String tags = "gtk";
    private Bitmap background, gameStartLogo;
    private SurfaceHolder holder;
    private Context context;
    private MediaPlayer mediaPlayer;
    private boolean bGameStart = false;
    private Integer[] enemySmall = new Integer[] {R.drawable.enemy1, R.drawable.enemy1_down1, R.drawable.enemy1_down2,
            R.drawable.enemy1_down3, R.drawable.enemy1_down4};
    private Integer[] enemyMiddle = new Integer[] {R.drawable.enemy2, R.drawable.enemy2_hit, R.drawable.enemy2_down1, R.drawable.enemy2_down2,
            R.drawable.enemy2_down3, R.drawable.enemy2_down4};
    private Integer[] enemyLarge = new Integer[] {R.drawable.enemy3_n1, R.drawable.enemy3_hit, R.drawable.enemy3_down1, R.drawable.enemy2_down3,
                R.drawable.enemy3_down3, R.drawable.enemy3_down4};
    private Integer[] loadings = new Integer[] {R.drawable.game_loading1, R.drawable.game_loading2, R.drawable.game_loading3, R.drawable.game_loading4};
    private Integer[] gamePause = new Integer[] {R.drawable.game_pause_nor, R.drawable.game_pause_pressed};
    private Integer[] gameResume = new Integer[] {R.drawable.game_resume_nor, R.drawable.game_resume_pressed};
    private Integer[] heroPlane = new Integer[] {R.drawable.hero_blowup_n1, R.drawable.hero_blowup_n2, R.drawable.hero_blowup_n3, R.drawable.hero_blowup_n4};
    private Integer[] effects = new Integer[] {R.drawable.ufo1, R.drawable.ufo2};
    private Integer[] bullets = new Integer[] {R.drawable.bullet1, R.drawable.bullet2, R.drawable.bomb};
    private Integer[] backToGame = new Integer[] {R.drawable.btn_finish};
    public GameView(Context context) {
        super(context);
        this.context = context;

        //加载背景图片资源
        background = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        gameStartLogo = BitmapFactory.decodeResource(getResources(), R.drawable.shoot_copyright);
        Matrix matrix = new Matrix();
        matrix.postScale(0.5f, 0.5f);
        gameStartLogo = Bitmap.createBitmap(gameStartLogo, 0, 0, gameStartLogo.getWidth(),
                gameStartLogo.getHeight(), matrix, true);
        Log.i(tags, ""+background.getHeight());
        //加载全局背景音乐
        mediaPlayer = MediaPlayer.create(context, R.raw.game_music);
        //获取holder对象
        holder = this.getHolder();
        //监听SurfaceView
        holder.addCallback(new WelcomeSurfaceHolder());

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                bGameStart = true;
            }
        });
    }

    private class WelcomeSurfaceHolder implements SurfaceHolder.Callback {
        private String tags = "gtk";
        private int moveStep;   //背景移动尺寸
        private Canvas canvas;
        private Paint paint;
        boolean runFlag = true;
        private int boundary = 0;
        private int textBoundary = getWidth();  //文字起始位置
        private int textScrollSpeed = 10;

        public WelcomeSurfaceHolder() {
            paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setDither(true);  //防止抖动
            paint.setFilterBitmap(true);
            paint.setColor(Color.BLACK);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTextSize(60);

            moveStep = DensityUtil.px2dip(context, 40);  //px 转 dp
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    while(runFlag){
                        if (!bGameStart) {
                            showGameScene();
                        }else{
                            initResource();
                            showGameBattle();
                        }
                    }
                }
            };

            //开启音乐
            mediaPlayer.start();
            // 开启渲染线程
            new Thread(runnable).start();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            runFlag = false;
        }

        void showGameScene(){
            canvas = holder.lockCanvas();
            boundary += moveStep;
            if (boundary >= getHeight()){
                boundary = 0;
            }
            if (textBoundary <= -getWidth()/2) {
                textBoundary = getWidth() * 3 / 2;
            }
            //绘制背景
            canvas.drawColor(Color.WHITE);
            canvas.drawBitmap(background, new Rect(0, -boundary, background.getWidth(), background.getHeight()-boundary), new Rect(0, 0, getWidth(), getHeight()), paint);
            canvas.drawBitmap(background, new Rect(0, background.getHeight() - boundary, background.getWidth(), 2 * background.getHeight() - boundary),new Rect(0, 0, getWidth(), getHeight()), paint);

            canvas.drawBitmap(gameStartLogo, null, new Rect((getWidth()-gameStartLogo.getWidth())/2, 0, (getWidth()+gameStartLogo.getWidth())/2, 0+gameStartLogo.getHeight()), paint);
            canvas.drawText("触摸任意区域进入游戏", textBoundary -= textScrollSpeed, getHeight() - 10, paint);
            holder.unlockCanvasAndPost(canvas);
        }

        void showGameBattle(){
            canvas = holder.lockCanvas();
            boundary += moveStep;
            if (boundary >= getHeight()){
                boundary = 0;
            }
            //绘制背景
            canvas.drawColor(Color.WHITE);
            canvas.drawBitmap(background, new Rect(0, -boundary, background.getWidth(), background.getHeight()-boundary), new Rect(0, 0, getWidth(), getHeight()), paint);
            canvas.drawBitmap(background, new Rect(0, background.getHeight() - boundary, background.getWidth(), 2 * background.getHeight() - boundary),new Rect(0, 0, getWidth(), getHeight()), paint);


            holder.unlockCanvasAndPost(canvas);
        }

        void initResource(){
           new AsyncTask<Integer[], Void, Void>(){
               @Override
               protected Void doInBackground(Integer[]... params) {
                   return null;
               }
           }.execute(enemyLarge,enemyMiddle);
        }
    }
}
