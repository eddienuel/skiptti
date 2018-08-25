package com.skiptti.app.control;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by emmanuel on 28/04/2018.
 */

public class ArcProgressView extends View {

    private static double M_PI_2 = Math.PI / 2;
    private int mBackgroundColor;
    private int mPrimaryColor;
    private int mTargetColor;

    private int mBackgroundWidth;
    private int mTargetLength;
    private int mPrimaryWidth;
    private Paint mArcPaintBackground, mArcPaintPrimary;

    public ArcProgressView(Context context) {
        super(context);

    }

    public ArcProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ArcProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        Resources res = context.getResources();
        float density = res.getDisplayMetrics().density;

        mBackgroundColor = res.getColor(android.R.color.darker_gray);
        mPrimaryColor = res.getColor(android.R.color.holo_orange_dark);
        mTargetColor = res.getColor(android.R.color.holo_orange_light);
        mBackgroundWidth = (int) (8 * density);       // default to 8dp
        mPrimaryWidth = (int) (8 * density);          // default to 8dp
        mTargetLength = (int) (mPrimaryWidth * 1.50); // 20% longer than arc line width

        mArcPaintBackground = new Paint() {
            {
                setDither(true);
                setStyle(Style.STROKE);
                setStrokeCap(Cap.BUTT);
                setStrokeJoin(Join.BEVEL);
                setAntiAlias(true);
            }
        };

        mArcPaintPrimary = new Paint() {
            {
                setDither(true);
                setStyle(Style.STROKE);
                setStrokeCap(Cap.BUTT);
                setStrokeJoin(Join.BEVEL);
                setAntiAlias(true);
            }
        };
        mArcPaintBackground.setColor(mBackgroundColor);
        mArcPaintBackground.setStrokeWidth(mBackgroundWidth);
        mArcPaintPrimary.setColor(mPrimaryColor);
        mArcPaintPrimary.setStrokeWidth(mPrimaryWidth / 3);


    }
}


//http://tech.taskrabbit.com/blog/2014/11/07/android-custom-progress-view/