package com.skiptti.app.control;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Paint.Style;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Scroll selector
 *
 * @author chenjing
 *
 */
public class ScrollSelectorView extends View
{

    public static final String TAG = "PickerView";
    /**
     * textSpacing betweenminTextSizeThe ratio of
     */
    public static final float MARGIN_ALPHA = 2.8f;
    /**
     * Automatically roll back to the middle of the speed
     */
    public static final float SPEED = 2;

    private List<String> mDataList;
    /**
     * Selected location,This position ismDataListCenter location,Has been the same
     */
    private int mCurrentSelected;
    private Paint mPaint;

    private float mMaxTextSize = 300;
    private float mMinTextSize = 10;

    private float mMaxTextAlpha = 255;
    private float mMinTextAlpha = 80;

    private int mColorText = 0x000000;

    private int mViewHeight;
    private int mViewWidth;

    private float mLastDownY;
    /**
     * Sliding distance
     */
    private float mMoveLen = 0;
    private boolean isInit = false;
    private onSelectListener mSelectListener;
    private Timer timer;
    private MyTimerTask mTask;

    Handler updateHandler = new Handler()
    {

        @Override
        public void handleMessage(Message msg)
        {
            if (Math.abs(mMoveLen) < SPEED)
            {
                mMoveLen = 0;
                if (mTask != null)
                {
                    mTask.cancel();
                    mTask = null;
                    performSelect();
                }
            } else
                // HeremMoveLen / Math.abs(mMoveLen)In order to keepmMoveLenPositive and negative numbers,To achieve on or under the rolling
                mMoveLen = mMoveLen - mMoveLen / Math.abs(mMoveLen) * SPEED;
            invalidate();
        }

    };

    public ScrollSelectorView(Context context)
    {
        super(context);
        init();
    }

    public ScrollSelectorView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public void setOnSelectListener(onSelectListener listener)
    {
        mSelectListener = listener;
    }

    private void performSelect()
    {
        if (mSelectListener != null)
            mSelectListener.onSelect(mDataList.get(mCurrentSelected));
    }

    public void setData(List<String> datas)
    {
        mDataList = datas;
        mCurrentSelected = datas.size() / 2;
        invalidate();
    }

    public void setSelected(int selected)
    {
        mCurrentSelected = selected;
    }

    private void moveHeadToTail()
    {
        String head = mDataList.get(0);
        mDataList.remove(0);
        mDataList.add(head);
    }

    private void moveTailToHead()
    {
        String tail = mDataList.get(mDataList.size() - 1);
        mDataList.remove(mDataList.size() - 1);
        mDataList.add(0, tail);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewHeight = getMeasuredHeight();
        mViewWidth = getMeasuredWidth();
        // according toViewHeight to calculate font size
        mMaxTextSize = mViewHeight / 4.0f;
        mMinTextSize = mMaxTextSize / 2f;
        isInit = true;
        invalidate();
    }

    private void init()
    {
        timer = new Timer();
        mDataList = new ArrayList<String>();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Style.FILL);
        mPaint.setTextAlign(Align.CENTER);
        mPaint.setColor(mColorText);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        // according toindexDrawview
        if (isInit)
            drawData(canvas);
    }

    private void drawData(Canvas canvas)
    {
        // Draw the selectedtextAnd then go up and down to the rest of thetext
        float scale = parabola(mViewHeight / 4.0f, mMoveLen);
        float size = (mMaxTextSize - mMinTextSize) * scale + mMinTextSize;
        mPaint.setTextSize(size);
        mPaint.setAlpha((int) ((mMaxTextAlpha - mMinTextAlpha) * scale + mMinTextAlpha));
        // textCenter drawing,Be carefulbaselineThe calculation can be achieved in the middle,yThe value istextCenter coordinate
        float x = (float) (mViewWidth / 2.0);
        float y = (float) (mViewHeight / 2.0 + mMoveLen);
        FontMetricsInt fmi = mPaint.getFontMetricsInt();
        float baseline = (float) (y - (fmi.bottom / 2.0 + fmi.top / 2.0));

        canvas.drawText(mDataList.get(mCurrentSelected), x, baseline, mPaint);
        // Above drawdata
        for (int i = 1; (mCurrentSelected - i) >= 0; i++)
        {
            drawOtherText(canvas, i, -1);
        }
        // Draw belowdata
        for (int i = 1; (mCurrentSelected + i) < mDataList.size(); i++)
        {
            drawOtherText(canvas, i, 1);
        }

    }

    /**
     * @param canvas
     * @param position
     *      distancemCurrentSelectedDifference value
     * @param type
     *      1Draw down,-1Show up
     */
    private void drawOtherText(Canvas canvas, int position, int type)
    {
        float d = (float) (MARGIN_ALPHA * mMinTextSize * position + type * mMoveLen);
        float scale = parabola(mViewHeight / 4.0f, d);
        float size = (mMaxTextSize - mMinTextSize) * scale + mMinTextSize;
        mPaint.setTextSize(size);
        mPaint.setAlpha((int) ((mMaxTextAlpha - mMinTextAlpha) * scale + mMinTextAlpha));
        float y = (float) (mViewHeight / 2.0 + type * d);
        FontMetricsInt fmi = mPaint.getFontMetricsInt();
        float baseline = (float) (y - (fmi.bottom / 2.0 + fmi.top / 2.0));
        canvas.drawText(mDataList.get(mCurrentSelected + type * position), (float) (mViewWidth / 2.0), baseline, mPaint);
    }

    /**
     * parabola
     *
     * @param zero
     *      Zero point coordinate
     * @param x
     *      Offset
     * @return scale
     */
    private float parabola(float zero, float x)
    {
        float f = (float) (1 - Math.pow(x / zero, 2));
        return f < 0 ? 0 : f;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch (event.getActionMasked())
        {
            case MotionEvent.ACTION_DOWN:
                doDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                doMove(event);
                break;
            case MotionEvent.ACTION_UP:
                doUp(event);
                break;
        }
        return true;
    }

    private void doDown(MotionEvent event)
    {
        if (mTask != null)
        {
            mTask.cancel();
            mTask = null;
        }
        mLastDownY = event.getY();
    }

    public int getSelected(){
        return Integer.valueOf(mDataList.get(mCurrentSelected));
    }

    private void doMove(MotionEvent event)
    {

        mMoveLen += (event.getY() - mLastDownY);

        if (mMoveLen > MARGIN_ALPHA * mMinTextSize / 2)
        {
            // Down more than to leave the distance
            moveTailToHead();
            mMoveLen = mMoveLen - MARGIN_ALPHA * mMinTextSize;
        } else if (mMoveLen < -MARGIN_ALPHA * mMinTextSize / 2)
        {
            // To go up and over to leave.
            moveHeadToTail();
            mMoveLen = mMoveLen + MARGIN_ALPHA * mMinTextSize;
        }

        mLastDownY = event.getY();
        invalidate();
    }

    private void doUp(MotionEvent event)
    {
        // Raise a handmCurrentSelectedPosition from the current positionmoveTo the middle of the selected location
        if (Math.abs(mMoveLen) < 0.0001)
        {
            mMoveLen = 0;
            return;
        }
        if (mTask != null)
        {
            mTask.cancel();
            mTask = null;
        }
        mTask = new MyTimerTask(updateHandler);
        timer.schedule(mTask, 0, 10);
    }

    class MyTimerTask extends TimerTask
    {
        Handler handler;

        public MyTimerTask(Handler handler)
        {
            this.handler = handler;
        }

        @Override
        public void run()
        {
            handler.sendMessage(handler.obtainMessage());
        }

    }

    public interface onSelectListener
    {
        void onSelect(String text);
    }
}