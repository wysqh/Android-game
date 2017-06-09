package com.example.gtk.thebattleofplane.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
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

import java.util.ArrayList;
import java.util.List;

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

    //子弹区域
    private BulletField bulletField;

    //固定摇杆背景圆形的X,Y坐标以及半径
    private float mRockerBg_X;
    private float mRockerBg_Y;
    private float mRockerBg_R;

    //摇杆的X,Y坐标以及摇杆的半径
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

    public FightView(Context context) {
        super(context);
        holder = this.getHolder();
        holder.addCallback(this);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);

        // 加载图片资源
        initResources();

        bullets = new ArrayList<Bullet>();

        //获取view实际的宽和高
        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                getViewTreeObserver().removeOnPreDrawListener(this);
                Log.e("FightView", getWidth() + "/" + getHeight());

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

                bulletField = new BulletField((int)getWidth() - (int)(mRockerBg_X + mRockerBg_R) * 2 / 3,
                        getWidth() - (int)(mRockerBg_X - mRockerBg_R) * 2 / 3,
                        getHeight() - (int)(mRockerBg_Y + mRockerBg_R) * 2 / 3,
                        getHeight() - (int)(mRockerBg_Y - mRockerBg_R)* 2 / 3);
                return true;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        Log.i("Points", (int)event.getX() + ", " + (int)event.getY() );
        if (event.getPointerCount() == 1) {
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
                    if (offset_Y + Math.sin(heroAngle) * plane_speed >= 0 &&
                            offset_Y + Math.sin(heroAngle) * plane_speed <= getHeight()) {
                        offset_Y -= Math.sin(heroAngle) * plane_speed;
                    }
                }
                //子弹区
                else {
                    Bullet bullet = new Bullet(offset_X + offset_X_Radius, offset_Y,
                            bulletBmp.getWidth() / 2, bulletBmp.getHeight() / 2,
                            BulletSpeed.SLOW_SPEED, BulletType.NORMAL_BULLET,
                            getWidth());
                    bullets.add(bullet);
                }
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                //当释放按键时摇杆要恢复摇杆的位置为初始位置
                mRockerBtn_X = mCenterPoint.x;
                mRockerBtn_Y = mCenterPoint.y;
            }
        }
        else if (event.getPointerCount() == 2) {
//            int move_index =
//                    bulletField.inBounds((int) event.getX(0), (int) event.getY(0)) ?
//                            1 : 0;
            int move_X =
                    bulletField.inBounds((int) event.getX(0), (int) event.getY(0)) ?
                            (int) event.getX(1) : (int) event.getX(0);
            int move_Y =
                    bulletField.inBounds((int) event.getX(0), (int) event.getY(0)) ?
                            (int) event.getY(1) : (int) event.getY(0);
//            int bullet_index =
//                    bulletField.inBounds((int) event.getX(0), (int) event.getY(0)) ?
//                            0 : 1;
//            int bullet_X =
//                    bulletField.inBounds((int) event.getX(0), (int) event.getY(0)) ?
//                            (int) event.getX(0) : (int) event.getX(1);
//            int bullet_Y =
//                    bulletField.inBounds((int) event.getX(0), (int) event.getY(0)) ?
//                            (int) event.getY(0) : (int) event.getY(1);

            if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
                // 当触屏区域不在活动范围内
                if (Math.sqrt(Math.pow((mRockerBg_X - move_X), 2) +
                        Math.pow((mRockerBg_Y - (getHeight() - move_X)), 2)) >= mRockerBg_R) {
                    //得到摇杆与触屏点所形成的角度
                    double tempRad = getRad(mRockerBg_X, mRockerBg_Y, event.getX(), (getHeight() - move_Y));
                    //保证内部小圆运动的长度限制
                    getXY(mRockerBg_X, mRockerBg_Y, mRockerBg_R, tempRad);
                } else {//如果小球中心点小于活动区域则随着用户触屏点移动即可
                    mRockerBtn_X = (int) move_X;
                    mRockerBtn_Y = getHeight() - move_Y;
                }
                //判断飞机移动XY分量
                double heroAngle = getRad(mRockerBg_X, mRockerBg_Y, move_X, (getHeight() - move_Y));
                if (offset_X + Math.cos(heroAngle) * plane_speed >= 0 &&
                        offset_X + Math.cos(heroAngle) * plane_speed <= getWidth()) {
                    offset_X += Math.cos(heroAngle) * plane_speed;
                }
                if (offset_Y + Math.sin(heroAngle) * plane_speed >= 0 &&
                        offset_Y + Math.sin(heroAngle) * plane_speed <= getHeight()) {
                    offset_Y -= Math.sin(heroAngle) * plane_speed;
                }
                //子弹区
                Bullet bullet = new Bullet(offset_X + offset_X_Radius, offset_Y,
                        bulletBmp.getWidth() / 2, bulletBmp.getHeight() / 2,
                        BulletSpeed.SLOW_SPEED, BulletType.NORMAL_BULLET,
                        getWidth());
                bullets.add(bullet);
            }
        }
        return true;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
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
    }

    private void gameLogic() {

    }

    private void gameShow() {
        Canvas canvas = holder.lockCanvas();

        scrollBackground(canvas);   //滚动背景
        drawRocker(canvas); //绘制摇杆
        drawHeroPlane(canvas);  //绘制我方飞机
        drawBullet(canvas);

        holder.unlockCanvasAndPost(canvas);
    }

    private void drawBullet(Canvas canvas) {
        List<Bullet> tmpBullets = new ArrayList<Bullet>();

        for(Bullet bullet : bullets) {
            if (bullet.getBullet_X() + Bullet.BULLET_SPEED <= bullet.getBullet_range_capacity()){
                bullet.setBullet_X(bullet.getBullet_X() + Bullet.BULLET_SPEED);
                tmpBullets.add(bullet);
            }
        }
        bullets.clear();
        bullets = tmpBullets;

        for (Bullet bullet : bullets) {
            canvas.drawBitmap(bulletBmp, null, new Rect(bullet.getBullet_X() - bullet.getBullet_X_Radius(),
                    bullet.getBullet_Y() - bullet.getBullet_Y_Radius(),
                    bullet.getBullet_X() + bullet.getBullet_X_Radius(),
                    bullet.getBullet_Y() + bullet.getBullet_Y_Radius()),
                    null);
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
