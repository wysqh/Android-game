package com.example.gtk.thebattleofplane.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewTreeObserver;

import com.example.gtk.thebattleofplane.R;
import com.example.gtk.thebattleofplane.model.Bullet;
import com.example.gtk.thebattleofplane.model.BulletField;
import com.example.gtk.thebattleofplane.model.BulletSpeed;
import com.example.gtk.thebattleofplane.model.BulletType;
import com.example.gtk.thebattleofplane.model.HeroPlane;
import com.example.gtk.thebattleofplane.model.InitialSmallEnemy;
import com.example.gtk.thebattleofplane.model.LargeEnemyPlane;
import com.example.gtk.thebattleofplane.model.MiddleEnemyPlane;
import com.example.gtk.thebattleofplane.model.Plane;
import com.example.gtk.thebattleofplane.model.PlaneFactory;
import com.example.gtk.thebattleofplane.model.SmallEnemyPlane;
import com.example.gtk.thebattleofplane.model.SmallEnemyPlaneManager;
import com.example.gtk.thebattleofplane.model.StatisticsInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentNavigableMap;

/**
 * Created by gutia on 2017-06-08.
 */

public class FightView extends SurfaceView implements SurfaceHolder.Callback {
    private String tags = "fight.View";
    private SurfaceHolder holder;
    private Paint paint;
    private Bitmap background;
    private boolean runFlag = false;
    private int boundary = 0;
    private final int movesteps = 20;
    private int screenWidth;

    //游戏音乐
    private MediaPlayer mediaPlayer;
    private SoundPool soundPool;
    HashMap<Integer, Integer> soundMap;

    //游戏实体类
    private HeroPlane heroPlane;
    private List<InitialSmallEnemy> initialSmallEnemies = new ArrayList<InitialSmallEnemy>();
    private List<SmallEnemyPlaneManager> smallEnemyPlaneManagers = new ArrayList<SmallEnemyPlaneManager>();
    private Bitmap smallInitialBmp;
    private Bitmap smallHitBmp;
    private Bitmap smallHit2Bmp;
    private Bitmap smallHit3Bmp;
    private Bitmap smallCrashedBmp;
    private Integer[] scores = new Integer[] {R.drawable.zero, R.drawable.one, R.drawable.two, R.drawable.three, R.drawable.four,
                                    R.drawable.five, R.drawable.six, R.drawable.seven, R.drawable.eight, R.drawable.nine};
    private List<Bitmap> scoresBmp;
    private Matrix matrix;

    //子弹区域
    private BulletField bulletField;

    //固定摇杆背景圆形的X,Y坐标以及半径
    private float mRockerBg_X;
    private float mRockerBg_Y;
    private float mRockerBg_R;

    //摇杆的X,Y坐标以及摇杆的半径
    private int srcx = 0;
    private int srcy = 0;
    private float mRockerBtn_X;
    private float mRockerBtn_Y;
    private float mRockerBtn_R;

    private Bitmap mBmpRockerBg;
    private Bitmap mBmpRockerBtn;

    private PointF mCenterPoint;

    //飞机
    private Bitmap herosBmp;
    private int offset_X;
    private int offset_Y;
    private int offset_X_Radius;
    private int offset_Y_Radius;
    private final int plane_speed = 10;

    //子弹
    private Bitmap bulletBmp;
    private List<Bullet> bullets;

    //自定义动画
    List<Bitmap> smallPlaneAnimation;

    //统计区域
    StatisticsInfo statisticsInfo;

    public FightView(Context context) {
        super(context);
        holder = this.getHolder();
        holder.addCallback(this);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);

        // 加载图片资源
        initResources();

        //初始化音乐播放器
        mediaPlayer = MediaPlayer.create(context, R.raw.game_music);
        mediaPlayer.setLooping(true);
        soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
        soundMap = new HashMap<Integer, Integer>();
        soundMap.put(1, soundPool.load(context, R.raw.bullet, 1));
        soundMap.put(2, soundPool.load(context, R.raw.enemy1_down, 1));

        //初始化统计器
        statisticsInfo = new StatisticsInfo();

        //初始化飞机
        heroPlane = (HeroPlane)PlaneFactory.createPlane(PlaneFactory.HERO_PLANE);

        //初始化动画 private Bitmap smallInitialBmp;
        smallPlaneAnimation = new ArrayList<>();
        smallPlaneAnimation.add(smallHitBmp);
        smallPlaneAnimation.add(smallHit2Bmp);
        smallPlaneAnimation.add(smallHit3Bmp);
        smallPlaneAnimation.add(smallCrashedBmp);

        bullets = new ArrayList<Bullet>();

        //获取view实际的宽和高
        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                getViewTreeObserver().removeOnPreDrawListener(this);
                Log.e("FightView", getWidth() + "/" + getHeight());
                screenWidth = getWidth();

                mCenterPoint = new PointF(320 / 2, 320 / 2);
                mRockerBg_X = mCenterPoint.x;
                mRockerBg_Y = mCenterPoint.y;

                mRockerBtn_X = mCenterPoint.x;
                mRockerBtn_Y = mCenterPoint.y;

                float tmp_f = mBmpRockerBg.getWidth() / (float) (mBmpRockerBg.getWidth() + mBmpRockerBtn.getWidth());
                mRockerBg_R = tmp_f * 320 / 2;
                mRockerBtn_R = (1.0f - tmp_f) * 320 / 2;

                offset_X = herosBmp.getWidth() / 2;
                offset_Y = getHeight()/2;
                offset_X_Radius = herosBmp.getWidth() / 2;
                offset_Y_Radius = herosBmp.getHeight() / 2;
                //初始化飞机参数
                heroPlane.setPlane_X(offset_X);
                heroPlane.setPlane_X_Radius(offset_X_Radius);
                heroPlane.setPlane_Y(offset_Y);
                heroPlane.setPlane_Y_Radius(offset_Y_Radius);

                bulletField = new BulletField((int)getWidth() - (int)(mRockerBg_X + mRockerBg_R) * 2 / 3,
                        getWidth() - (int)(mRockerBg_X - mRockerBg_R) * 2 / 3,
                        getHeight() - (int)(mRockerBg_Y + mRockerBg_R) * 2 / 3,
                        getHeight() - (int)(mRockerBg_Y - mRockerBg_R)* 2 / 3);

                //初始化敌机
                Random random = new Random();
                for (int i = 0; i < 5; ++i) {
                    InitialSmallEnemy enemy = new InitialSmallEnemy();
                    enemy.setEnemyBitmap(smallInitialBmp);
                    initialSmallEnemies.add(enemy);
                }
                for (InitialSmallEnemy initEnemy : initialSmallEnemies) {
                    initEnemy.setPlane_X_Radius(smallInitialBmp.getWidth()/2);
                    initEnemy.setPlane_Y_Radius(smallInitialBmp.getHeight()/2);
                    initEnemy.setPlane_X(random.nextInt(300) + getWidth());
                    initEnemy.setPlane_Y(random.nextInt(getHeight()));
                }
                for (int i = 0; i < initialSmallEnemies.size(); ++i) {
                    smallEnemyPlaneManagers.add(new SmallEnemyPlaneManager(initialSmallEnemies.get(i)));
                }
                for (int i = 0; i < initialSmallEnemies.size(); ++i) {
                    InitialSmallEnemy initialSmallEnemy = initialSmallEnemies.get(i);
                    initialSmallEnemy.setUpEnemyPlaneManager(smallEnemyPlaneManagers.get(i));
                }
                for (int i = 0; i < smallEnemyPlaneManagers.size(); ++i) {
                    SmallEnemyPlaneManager smg = smallEnemyPlaneManagers.get(i);
                    smg.setState(SmallEnemyPlane.INITIAL);
                    Log.e("Status", smg.getSmallPlaneState());
                }
                return true;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        Log.e("Points", (int)event.getX() + ", " + (int)event.getY() );
        Log.i("Points", event.getPointerCount()+ "");

        // 单点触控
        if (event.getPointerCount() == 1) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                srcx = (int)event.getX();
                srcy = (int)event.getY();
                if (!bulletField.inBounds(srcx, srcy)) {
                    return true;
                }
            }
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                if (Math.sqrt(Math.pow((mRockerBg_X - srcx), 2) +
                        Math.pow((mRockerBg_Y - (getHeight() -  srcy)), 2)) >= mRockerBg_R) {
                    return true;
                }
            }
            if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
                //触控点不在子弹区
                if (!bulletField.inBounds((int) event.getX(), (int) event.getY())) {
                    // 当触屏区域不在活动范围内
                    if (Math.sqrt(Math.pow((mRockerBg_X - (int) event.getX()), 2) +
                            Math.pow((mRockerBg_Y - (getHeight() - (int) event.getY())), 2)) >= mRockerBg_R) {
                        //得到摇杆与触屏点所形成的角度
                        double tempRad = getRad(mRockerBg_X, mRockerBg_Y, event.getX(), (getHeight() - event.getY()));
                        //保证内部小圆运动的长度限制
                        getXY(mRockerBg_X, mRockerBg_Y, mRockerBg_R, tempRad);
                    } else {//如果小球中心点小于活动区域则随着用户触屏点移动即可
                        mRockerBtn_X = (int) event.getX();
                        mRockerBtn_Y = getHeight() - (int) event.getY();
                    }
                    //判断飞机移动XY分量
                    double heroAngle = getRad(mRockerBg_X, mRockerBg_Y, event.getX(), (getHeight() - event.getY()));
                    if (offset_X + Math.cos(heroAngle) * plane_speed >= 0 &&
                            offset_X + Math.cos(heroAngle) * plane_speed <= getWidth()) {
                        offset_X += Math.cos(heroAngle) * plane_speed;
                    }

                    if (offset_Y - Math.sin(heroAngle) * plane_speed >= 0 &&
                            offset_Y - Math.sin(heroAngle) * plane_speed <= getHeight()) {
                        offset_Y -= Math.sin(heroAngle) * plane_speed;
                    }
                }else if (bulletField.inBounds((int)event.getX(), (int)event.getY())) {
                    synchronized (bullets) {
                        //子弹区
                        Bullet bullet = new Bullet(offset_X + offset_X_Radius, offset_Y,
                                bulletBmp.getWidth() / 2, bulletBmp.getHeight() / 2,
                                BulletSpeed.SLOW_SPEED, BulletType.NORMAL_BULLET,
                                getWidth());
                        bullets.add(bullet);
                        soundPool.play(soundMap.get(1), 1, 1, 0, 0, 1);
                    }
                }
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                //当释放按键时摇杆要恢复摇杆的位置为初始位置
                mRockerBtn_X = mCenterPoint.x;
                mRockerBtn_Y = mCenterPoint.y;
            }
        }
        else if (event.getPointerCount() == 2){
//            event.getPointerId()
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_MOVE:
                case MotionEvent.ACTION_DOWN:
                    for (int i = 0; i <  event.getPointerCount(); i++) {
                        int x = (int)event.getX(i);
                        int y = (int)event.getY(i);
                        if (!bulletField.inBounds(x, y)) {
                            // 当触屏区域不在活动范围内
                            if (Math.sqrt(Math.pow((mRockerBg_X - x), 2) +
                                    Math.pow((mRockerBg_Y - (getHeight() - y)), 2)) >= mRockerBg_R) {
                                //得到摇杆与触屏点所形成的角度
                                double tempRad = getRad(mRockerBg_X, mRockerBg_Y, x, (getHeight() - y));
                                //保证内部小圆运动的长度限制
                                getXY(mRockerBg_X, mRockerBg_Y, mRockerBg_R, tempRad);
                            } else {//如果小球中心点小于活动区域则随着用户触屏点移动即可
                                mRockerBtn_X = x;
                                mRockerBtn_Y = getHeight() - y;
                            }
                            //判断飞机移动XY分量
                            double heroAngle = getRad(mRockerBg_X, mRockerBg_Y, x, (getHeight() - y));
                            if (offset_X + Math.cos(heroAngle) * plane_speed >= 0 &&
                                    offset_X + Math.cos(heroAngle) * plane_speed <= getWidth()) {
                                offset_X += Math.cos(heroAngle) * plane_speed;
                            }

                            if (offset_Y - Math.sin(heroAngle) * plane_speed >= 0 &&
                                    offset_Y - Math.sin(heroAngle) * plane_speed <= getHeight()) {
                                offset_Y -= Math.sin(heroAngle) * plane_speed;
                            }
                            break;
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    //当释放按键时摇杆要恢复摇杆的位置为初始位置
                    mRockerBtn_X = mCenterPoint.x;
                    mRockerBtn_Y = mCenterPoint.y;
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    for (int i = 0; i <  event.getPointerCount(); i++) {
                        int x = (int)event.getX(i);
                        int y = (int)event.getY(i);
                        if (bulletField.inBounds(x, y)) {
                            synchronized (bullets) {
                                Bullet bullet = new Bullet(offset_X + offset_X_Radius, offset_Y,
                                        bulletBmp.getWidth() / 2, bulletBmp.getHeight() / 2,
                                        BulletSpeed.SLOW_SPEED, BulletType.NORMAL_BULLET,
                                        getWidth());
                                bullets.add(bullet);
                                soundPool.play(soundMap.get(1), 1, 1, 0, 0, 1);
                                break;
                            }
                        }
                    }
                    break;
                default: break;
            }
        }
        return true;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mediaPlayer.start();
        runFlag = true;

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (runFlag) {
                    Log.i(tags, "entered");
                    gameShow();
                    gameLogic();
                }
            }
        };

        new Thread(runnable).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        runFlag = false;
    }

    private void initResources() {
        //加载背景图片
        background = BitmapFactory.decodeResource(getResources(), R.drawable.background_horizontal);
        //加载摇杆图片
        mBmpRockerBg = BitmapFactory.decodeResource(getResources(), R.drawable.rocker_bg);
        mBmpRockerBtn = BitmapFactory.decodeResource(getResources(), R.drawable.rocker_btn);

        //加载我方飞机
        herosBmp = BitmapFactory.decodeResource(getResources(), R.drawable.hero_horizontal);

        //加载子弹
        bulletBmp = BitmapFactory.decodeResource(getResources(), R.drawable.bullet1_horizontal);

        //加载敌机不同状态位图
        smallInitialBmp = BitmapFactory.decodeResource(getResources(), R.drawable.enemy1_horizontal);
        smallHitBmp = BitmapFactory.decodeResource(getResources(), R.drawable.enemy1_down1_horizontal);
        smallHit2Bmp = BitmapFactory.decodeResource(getResources(), R.drawable.enemy1_down2_horizontal);
        smallHit3Bmp = BitmapFactory.decodeResource(getResources(), R.drawable.enemy1_down3_horizontal);
        smallCrashedBmp = BitmapFactory.decodeResource(getResources(), R.drawable.enemy1_down4_horizontal);

        //加载数字
        matrix = new Matrix();
        matrix.postScale(0.5f, 0.5f);

        scoresBmp = new ArrayList<Bitmap>();
        for (int i = 0; i < scores.length; ++i) {
            Bitmap originBmp = BitmapFactory.decodeResource(getResources(), scores[i]);
            // 得到新的图片
            originBmp  = Bitmap.createBitmap(originBmp, 0, 0, originBmp.getWidth(), originBmp.getHeight(), matrix,
                    true);
            scoresBmp.add(originBmp);
        }
    }

    private void gameLogic() {
        //判断子弹是否击中飞机
        synchronized (bullets) {
            for (SmallEnemyPlaneManager smp : smallEnemyPlaneManagers) {
                for (Bullet bullet : bullets) {
                    if (new Rect(smp.getEnemyPlane().getPlane_X() - smp.getEnemyPlane().getPlane_X_Radius(),
                            smp.getEnemyPlane().getPlane_Y() - smp.getEnemyPlane().getPlane_Y_Radius(),
                            smp.getEnemyPlane().getPlane_X() + smp.getEnemyPlane().getPlane_X_Radius(),
                            smp.getEnemyPlane().getPlane_Y() + smp.getEnemyPlane().getPlane_Y_Radius())
                            .intersect(new Rect(bullet.getBullet_X() - bullet.getBullet_X_Radius(),
                                    bullet.getBullet_Y() - bullet.getBullet_Y_Radius(),
                                    bullet.getBullet_X() + bullet.getBullet_X_Radius(),
                                    bullet.getBullet_Y() + bullet.getBullet_Y_Radius()))) {
                        smp.getEnemyPlane().setDead(true);
                        bullet.setBullet_X(-100);//扔掉
                        //统计信息
                        statisticsInfo.setEnemyNumber(statisticsInfo.getEnemyNumber() + 1);
                        statisticsInfo.setTotalScore(statisticsInfo.getTotalScore() + SmallEnemyPlane.GRADE);
                    }
                }
            }
        }
    }

    private void gameShow() {
        Canvas canvas = holder.lockCanvas();

        scrollBackground(canvas);   //滚动背景
        drawRocker(canvas); //绘制摇杆
        drawHeroPlane(canvas);  //绘制我方飞机
        drawBullet(canvas); //绘制子弹
        drawSmallEnemy(canvas); //绘制敌方飞机
        drawGrade(canvas);  //绘制分数

        holder.unlockCanvasAndPost(canvas);
    }

    private void drawGrade(Canvas canvas) {
        String grade = statisticsInfo.getTotalScore().toString();
        int size = grade.length();
        int index = 0;
        while (--size >= 0){
            Integer single = Integer.parseInt(grade.substring(size, size+1));
            canvas.drawBitmap(scoresBmp.get(single), null,
                    new Rect(screenWidth - (index + 1) * scoresBmp.get(single).getWidth(),
                            0,
                            screenWidth - index * scoresBmp.get(single).getWidth(),
                            scoresBmp.get(single).getHeight()),
                    null);
            index++;
        }
    }

    //绘制小型敌方飞机
    private void drawSmallEnemy(Canvas canvas) {
        Random random = new Random();
        List<SmallEnemyPlaneManager> tmpManagers = new ArrayList<SmallEnemyPlaneManager>();

        for (SmallEnemyPlaneManager smp : smallEnemyPlaneManagers) {
            SmallEnemyPlane smallEnemyPlane = smp.getEnemyPlane();
            if (smallEnemyPlane.getPlane_X() <= 0) {
                SmallEnemyPlane sp = new InitialSmallEnemy(smallInitialBmp);
                sp.setPlane_X(screenWidth + random.nextInt(300));
                sp.setPlane_Y(random.nextInt(getHeight()));
                SmallEnemyPlaneManager smgr = new SmallEnemyPlaneManager(sp);
                sp.setUpEnemyPlaneManager(smgr);
                smgr.setState(SmallEnemyPlane.INITIAL);
                tmpManagers.add(smgr);
            } else {
                tmpManagers.add(smallEnemyPlane.getEnemyPlaneManager());
            }
        }

        for (SmallEnemyPlaneManager smp : tmpManagers) {
             if (!smp.getEnemyPlane().isDead()) {
                SmallEnemyPlane smallEnemyPlane = smp.getEnemyPlane();
                Log.e("enemy", smallEnemyPlane.getPlane_X() + " " + smallEnemyPlane.getPlane_Y_Radius());
                smallEnemyPlane.setPlane_X(smallEnemyPlane.getPlane_X() - Plane.QUICK_SPEED);
                canvas.drawBitmap(smp.getBitmap(), null, new Rect(smallEnemyPlane.getPlane_X() - smallEnemyPlane.getPlane_X_Radius(),
                                smallEnemyPlane.getPlane_Y() - smallEnemyPlane.getPlane_Y_Radius(),
                                smallEnemyPlane.getPlane_X() + smallEnemyPlane.getPlane_X_Radius(),
                                smallEnemyPlane.getPlane_Y() + smallEnemyPlane.getPlane_Y_Radius()),
                        null);
            } else{
                //若飞机逐帧动画未完成
                if (!smp.getSmallPlaneState().equals(SmallEnemyPlane.DISAPPEAR)){
                    smp.getEnemyPlane().changeState();
                    smp.setUpStateObj();    //更新状态
                    Log.e("Status", smp.getSmallPlaneState() + " " + smp.getEnemyPlane().getCurrentState());
                    if(smp.getSmallPlaneState() == SmallEnemyPlane.HIT) {
                        smp.getEnemyPlane().setEnemyBitmap(smallHitBmp);
                    }else if(smp.getSmallPlaneState() == SmallEnemyPlane.HIT2) {
                        smp.getEnemyPlane().setEnemyBitmap(smallHit2Bmp);
                    }else if(smp.getSmallPlaneState() == SmallEnemyPlane.HIT3) {
                        smp.getEnemyPlane().setEnemyBitmap(smallHit3Bmp);
                    }else if (smp.getSmallPlaneState() == SmallEnemyPlane.CRASHED) {
                        smp.getEnemyPlane().setEnemyBitmap(smallCrashedBmp);
                    }
                    Log.e("Status", smp.getEnemyPlane().getPlane_X() + " " + smp.getEnemyPlane().getPlane_Y());
                    canvas.drawBitmap(smp.getBitmap(), null, new Rect(smp.getEnemyPlane().getPlane_X() - smp.getEnemyPlane().getPlane_X_Radius(),
                                    smp.getEnemyPlane().getPlane_Y() - smp.getEnemyPlane().getPlane_Y_Radius(),
                                    smp.getEnemyPlane().getPlane_X() + smp.getEnemyPlane().getPlane_X_Radius(),
                                    smp.getEnemyPlane().getPlane_Y() + smp.getEnemyPlane().getPlane_Y_Radius()),
                            null);
                } else {
                    soundPool.play(soundMap.get(2), 1, 1, 0, 0, 1);
                    smp.getEnemyPlane().setPlane_X(-100);   //扔出去
                }
            }
        }

        smallEnemyPlaneManagers.clear();

        for (SmallEnemyPlaneManager smp : tmpManagers) {
            smallEnemyPlaneManagers.add(smp);
        }
    }

    //绘制子弹
    private void drawBullet(Canvas canvas) {
        List<Bullet> tmpBullets = new ArrayList<Bullet>();

        synchronized (bullets) {
            for (Bullet bullet : bullets) {
                if (bullet.getBullet_X() + bullet.getBullet_speed() <= bullet.getBullet_range_capacity() &&
                        bullet.getBullet_X() >= 0) {
                    bullet.setBullet_X(bullet.getBullet_X() + bullet.getBullet_speed());
                    tmpBullets.add(bullet);
                }
            }
            bullets.clear();

            for (Bullet tmp : tmpBullets) {
                bullets.add(tmp);
            }

            for (Bullet bullet : bullets) {
                canvas.drawBitmap(bulletBmp, null, new Rect(bullet.getBullet_X() - bullet.getBullet_X_Radius(),
                                bullet.getBullet_Y() - bullet.getBullet_Y_Radius(),
                                bullet.getBullet_X() + bullet.getBullet_X_Radius(),
                                bullet.getBullet_Y() + bullet.getBullet_Y_Radius()),
                        null);
            }
        }
    }

    //绘制我方飞机
    private void drawHeroPlane(Canvas canvas) {
        canvas.drawBitmap(herosBmp, null, new Rect(offset_X - offset_X_Radius, offset_Y - offset_Y_Radius,
                offset_X + offset_X_Radius, offset_Y + offset_Y_Radius), null);
    }

    //滚动背景
    private void scrollBackground(Canvas canvas) {
        boundary += movesteps;
        if (boundary >= background.getWidth()) {
            boundary = 0;
        }
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(background, new Rect(boundary, 0, background.getWidth() + boundary, background.getHeight()), new Rect(0, 0, getWidth(), getHeight()), paint);
        canvas.drawBitmap(background, new Rect(-background.getWidth() + boundary, 0, boundary, background.getHeight()), new Rect(0, 0, getWidth(), getHeight()), paint);
    }

    //绘制摇杆
    private void drawRocker(Canvas canvas) {
        canvas.drawBitmap(mBmpRockerBg, null,
                new Rect((int) (mRockerBg_X - mRockerBg_R),
                        (int) (getHeight() - (mRockerBg_Y + mRockerBg_R)),
                        (int) (mRockerBg_X + mRockerBg_R),
                        (int) (getHeight() - (mRockerBg_Y - mRockerBg_R))),
                null);
        canvas.drawBitmap(mBmpRockerBtn, null,
                new Rect((int) (mRockerBtn_X - mRockerBtn_R),
                        (int) (getHeight() - (mRockerBtn_Y + mRockerBtn_R)),
                        (int) (mRockerBtn_X + mRockerBtn_R),
                        (int) (getHeight() - (mRockerBtn_Y - mRockerBtn_R))),
                null);

        //绘制发射区域
        canvas.drawBitmap(mBmpRockerBtn, null,
                new Rect((int) (getWidth() - (mRockerBg_X + mRockerBg_R) * 2 / 3),
                        (int) (getHeight() - (mRockerBg_Y + mRockerBg_R) * 2 / 3),
                        (int) (getWidth() - (mRockerBg_X - mRockerBg_R) * 2 / 3 ),
                        (int) (getHeight() - (mRockerBg_Y - mRockerBg_R)* 2 / 3)),
                null);
    }

    /***
     * 得到两点之间的弧度
     */
    public double getRad(float px1, float py1, float px2, float py2) {
        //得到两点X的距离
        float x = px2 - px1;
        //得到两点Y的距离
        float y = py1 - py2;
        //算出斜边长
        float hypotenuse = (float) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
        //得到这个角度的余弦值（通过三角函数中的定理 ：邻边/斜边=角度余弦值）
        float cosAngle = x / hypotenuse;
        //通过反余弦定理获取到其角度的弧度
        float rad = (float) Math.acos(cosAngle);
        //注意：当触屏的位置Y坐标<摇杆的Y坐标我们要取反值-0~-180
        if (py2 < py1) {
            rad = -rad;
        }
        return rad;
    }

    /**
     *
     * @param R  圆周运动的旋转点
     * @param centerX 旋转点X
     * @param centerY 旋转点Y
     * @param rad 旋转的弧度
     */
    public void getXY(float centerX, float centerY, float R, double rad) {
        //获取圆周运动的X坐标
        mRockerBtn_X = (float) (R * Math.cos(rad)) + centerX;
        //获取圆周运动的Y坐标
        mRockerBtn_Y = (float) (R * Math.sin(rad)) + centerY;
    }
}
