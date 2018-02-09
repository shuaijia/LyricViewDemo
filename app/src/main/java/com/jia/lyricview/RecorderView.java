package com.jia.lyricview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Description: 留声机 view
 * Created by jia on 2018/2/9.
 * 人之所以能，是相信能
 */
public class RecorderView extends View {

    // 播放状态时唱针的旋转角度
    private static final int PLAY_DEGREE = -15;
    // 暂停状态时唱针的旋转角度
    private static final int PAUSE_DEGREE = -45;

    // 唱针 画笔
    private Paint needlePaint;
    // 圆形图片path
    private Path clipPath;
    // 唱片 画笔
    private Paint discPaint;
    // 唱片 图片
    private Bitmap bitmap;
    // 唱片
    private Rect srcRect, dstRect;

    // view 宽度
    private int width;
    // view 高度
    private int height;
    // view 宽度一半
    private int halfMeasureWidth;
    // 长臂 长度
    private int longArmLength = 240;
    // 短臂 长度
    private int shortArmLength = 120;
    // 长针头 长度
    private int longHeadLength = 60;
    // 短针头 长度
    private int shortHeadLength = 30;
    // 大圆圈 半径
    private int bigCircleRadius = 50;
    // 小圆圈 半径
    private int smallCircleRadius = 37;
    // 圆形图片半径
    private int pictureRadius = 360;
    // 唱片圆环半径
    private int ringWidth = 190;

    // 唱片旋转速度
    private float diskRotateSpeed = 0.5f;

    // 状态控制相关变量
    private boolean isPlaying;            // 是否处于播放状态
    private int needleDegreeCounter;      // 唱针旋转角度计数器
    private float diskDegreeCounter;      // 唱片旋转角度计数器

    public RecorderView(Context context) {
        super(context);
        init();
    }

    public RecorderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RecorderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        needlePaint = new Paint();
        discPaint = new Paint();

        clipPath = new Path();
        clipPath.addCircle(0, 0, pictureRadius, Path.Direction.CW);

        bitmap = BitmapFactory.decodeResource(this.getResources(), R.mipmap.lyric_bg);

        needleDegreeCounter = PAUSE_DEGREE;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 绘制唱片
        drawDisk(canvas);
        // 绘制唱针
        drawNeedle(canvas);

        if (needleDegreeCounter > PAUSE_DEGREE) {
            // 刷新view 使得开始
            invalidate();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        halfMeasureWidth = width / 2;
    }

    // 绘制唱针
    private void drawNeedle(Canvas canvas) {
        // 由于PLAY_DEGREE和PAUSE_DEGREE之间的差值是30，所以每次增/减值应当是30的约数即可
        if (isPlaying) {
            if (needleDegreeCounter < PLAY_DEGREE) {
                needleDegreeCounter += 3;
            }
        } else {
            if (needleDegreeCounter > PAUSE_DEGREE) {
                needleDegreeCounter -= 3;
            }
        }
        drawNeedle(canvas, needleDegreeCounter);
    }

    // 绘制唱片（胶片）
    private void drawDisk(Canvas canvas) {
        diskDegreeCounter = diskDegreeCounter % 360 + diskRotateSpeed;
        drawDisk(canvas, diskDegreeCounter);
    }

    /**
     * 绘制旋转了指定角度的唱针。
     * 说明一下旋转了指定角度什么意思，看上面的流程图可以知道，
     * 长的那段手臂和垂直方向是成角15°的，实际上这个角度不是一成不变的，
     * 通过控制这个角度变化，可以达到唱针处于播放/暂停状态或者在两个状态之间摆动的效果。
     */
    private void drawNeedle(Canvas canvas, int degree) {

        /**
         * 用来保存Canvas的状态。save之后，可以调用Canvas的平移、放缩、旋转、错切、裁剪等操作。
         */
        canvas.save();
        // 移动坐标到水平中点
        canvas.translate(halfMeasureWidth, 0);
        // 准备绘制唱针手臂
        needlePaint.setStrokeWidth(16);
        needlePaint.setColor(Color.parseColor("#C0C0C0"));
        // 绘制第一段臂
        canvas.rotate(degree);
        canvas.drawLine(0, 0, 0, longArmLength, needlePaint);
        // 绘制第二段臂
        canvas.translate(0, longArmLength);
        canvas.rotate(-30);
        canvas.drawLine(0, -4, 0, shortArmLength, needlePaint);
        // 绘制唱针头
        // 绘制第一段唱针头
        canvas.translate(0, shortArmLength);
        needlePaint.setStrokeWidth(40);
        canvas.drawLine(0, 0, 0, longHeadLength, needlePaint);
        // 绘制第二段唱针头
        canvas.translate(0, longHeadLength);
        needlePaint.setStrokeWidth(60);
        canvas.drawLine(0, 0, 0, shortHeadLength, needlePaint);
        canvas.restore();

        // 两个重叠的圆形，即唱针顶部的旋转点
        canvas.save();
        canvas.translate(halfMeasureWidth, 0);
        needlePaint.setStyle(Paint.Style.FILL);
        needlePaint.setColor(Color.parseColor("#C0C0C0"));
        canvas.drawCircle(0, 0, bigCircleRadius, needlePaint);
        needlePaint.setColor(Color.parseColor("#8A8A8A"));
        canvas.drawCircle(0, 0, smallCircleRadius, needlePaint);
        // restore：用来恢复Canvas之前保存的状态。防止save后对Canvas执行的操作对后续的绘制有影响。
        canvas.restore();
    }


    /**
     * 绘制旋转了指定角度的唱片（类似唱针，唱片里面的图片是会旋转不同角度的）
     *
     * @param canvas
     * @param degree
     */
    private void drawDisk(Canvas canvas, float degree) {
        // 移动坐标系到唱针下方合适位置，然后旋转指定角度
        canvas.save();
        canvas.translate(halfMeasureWidth, pictureRadius + ringWidth + longArmLength-35);
        canvas.rotate(degree);
        // 绘制圆环
        canvas.drawCircle(0, 0, pictureRadius + ringWidth / 2, discPaint);
        // 绘制图片
        canvas.clipPath(clipPath);

        srcRect = new Rect();
        dstRect = new Rect();

        srcRect.set(0, 0, bitmap.getWidth(), bitmap.getHeight());
        dstRect.set(-pictureRadius, -pictureRadius, pictureRadius, pictureRadius);

        canvas.drawBitmap(bitmap, srcRect, dstRect, discPaint);
        canvas.restore();
    }

    /**
     * 设置是否处于播放状态
     *
     * @param isPlaying true:播放，false:暂停
     */
    public void setPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
        invalidate();
    }
}
