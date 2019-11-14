package com.evideostb.training.chenhuan.mediaplayer.audiorecorder_demo;

/**
 * Created by ChenHuan on 2018/2/7.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.evideostb.training.chenhuan.mediaplayer.R;
import com.evideostb.training.chenhuan.mediaplayer.utils.ThreadPool;

import java.util.LinkedList;
import java.util.List;

/**
 * 语音录制的动画效果
 *
 */
public class LineWaveVoiceView extends View {
    private static final String TAG = LineWaveVoiceView.class.getSimpleName();

    private Paint paint;
    //矩形波纹颜色
    private int lineColor;
    //矩形波纹宽度
    private float lineWidth;
    private float textSize;
    private static final String DEFAULT_TEXT = " 倒计时 9:59 ";
    private String text = DEFAULT_TEXT;
    private int textColor;
    private boolean isStart = false;
    private Runnable mRunable;

    private int LINE_W = 9;//默认矩形波纹的宽度，9像素, 原则上从layout的attr获得
    private int MIN_WAVE_H = 2;//最小的矩形线高，是线宽的2倍，线宽从lineWidth获得
    private int MAX_WAVE_H = 7;//最高波峰，是线宽的4倍

    //默认矩形波纹的高度，总共10个矩形，左右各有10个
    private int[] DEFAULT_WAVE_HEIGHT = {2, 3, 4, 3, 2, 2, 2, 2, 2, 2};
    private LinkedList<Integer> mWaveList = new LinkedList<>();

    private RectF rectRight = new RectF();//右边波纹矩形的数据，10个矩形复用一个rectF
    private RectF rectLeft = new RectF();//左边波纹矩形的数据

    LinkedList<Integer> list = new LinkedList<>();

    private static final int UPDATE_INTERVAL_TIME = 100;//100ms更新一次

    public LineWaveVoiceView(Context context) {
        super(context);
    }

    public LineWaveVoiceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineWaveVoiceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
        resetList(list, DEFAULT_WAVE_HEIGHT);

        mRunable = new Runnable() {
            @Override
            public void run() {
                while (isStart){
                    refreshElement();
                    try {
                        Thread.sleep(UPDATE_INTERVAL_TIME);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    postInvalidate();
                }
            }
        };
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.LineWaveVoiceView);
        lineColor = mTypedArray.getColor(R.styleable.LineWaveVoiceView_voiceLineColor, Color.parseColor("#ff9c00"));
        lineWidth = mTypedArray.getDimension(R.styleable.LineWaveVoiceView_voiceLineWidth, LINE_W);
        textSize = mTypedArray.getDimension(R.styleable.LineWaveVoiceView_voiceTextSize, 42);
        textColor = mTypedArray.getColor(R.styleable.LineWaveVoiceView_voiceTextColor, Color.parseColor("#666666"));
        mTypedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int widthcentre = getWidth() / 2;
        int heightcentre = getHeight() / 2;

        //更新时间
        paint.setStrokeWidth(0);
        paint.setColor(textColor);
        paint.setTextSize(textSize);
        float textWidth = paint.measureText(text);
        canvas.drawText(text, widthcentre - textWidth / 2, heightcentre - (paint.ascent() + paint.descent())/2, paint);

        //更新左右两边的波纹矩形
        paint.setColor(lineColor);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(lineWidth);
        paint.setAntiAlias(true);
        int nValue = 0;
        for(int i = 0 ; i < 10 ; i++) {
            nValue = list.get(i);
//            Log.i(TAG,Integer.toString(nValue));
            //右边矩形
            rectRight.left = widthcentre + 2 * i * lineWidth + textWidth / 2 + lineWidth;
            rectRight.top = heightcentre - list.get(i) * lineWidth / 2;
            rectRight.right = widthcentre  + 2 * i * lineWidth +2 * lineWidth + textWidth / 2;
            rectRight.bottom = heightcentre + list.get(i) * lineWidth /2;

            //左边矩形
            rectLeft.left = widthcentre - (2 * i * lineWidth +  textWidth / 2 + 2 * lineWidth );
            rectLeft.top = heightcentre - list.get(i) * lineWidth / 2;
            rectLeft.right = widthcentre  -( 2 * i * lineWidth + textWidth / 2 + lineWidth);
            rectLeft.bottom = heightcentre + list.get(i) * lineWidth / 2;

            canvas.drawRoundRect(rectRight, 6, 6, paint);
            canvas.drawRoundRect(rectLeft, 6, 6, paint);
        }
    }

    /**
     * 该方法返回的是0到32767范围的16位整型，
     * 原理可能是对一段值域为-32768到32767的音源数据取其中绝对值最大的值并返回。
     * 这个值与单位为帕斯卡的声压值是有线性函数关系的。
     * 另外需要注意的是第一次调用这个方法取得的值是0，
     * 代入公式中算出的分贝值是负无穷大，
     * 故需要在代码中对这种情况做判断。
     * 可以算出，由于getMaxAmplitude返回的数值最大是32767，因此算出的最大分贝值是90.3。
     * 也就是说，博主令参考振幅值为1，计算出的分贝值正常值域为0 dB 到90.3 dB。
     */
    private synchronized void refreshElement() {
        float maxAmp = AudioRecordManager.getInstance().getMaxAmplitude();
        int waveH = MIN_WAVE_H + (int)((maxAmp*(MAX_WAVE_H-2))/(float) 90.3);//wave 在 2 ~ 7 之间
        list.add(0, waveH);
        list.removeLast();
    }

    public synchronized void setText(String text) {
        this.text = text;
        postInvalidate();
    }

    public synchronized void startRecord(){
        isStart = true;
        ThreadPool.getInstance().getCachedPools().execute(mRunable);
    }

    public synchronized void stopRecord(){
        isStart = false;
        mWaveList.clear();
        resetList(list, DEFAULT_WAVE_HEIGHT);
        text = DEFAULT_TEXT;
        postInvalidate();
    }

    private void resetList(List list, int[] array) {
        list.clear();
        for(int i = 0 ; i < array.length; i++ ){
            list.add(array[i]);
        }
    }
}