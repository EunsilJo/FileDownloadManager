package com.github.eunsiljo.filedownloadmanager.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;

/**
 * Created by EunsilJo on 2016. 11. 4..
 */

public class CustomProgressBar extends ProgressBar implements CustomProgress{

    public CustomProgressBar(Context context) {
        super(context);
    }

    public CustomProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setProgressWithAnimation(int progress, int duration) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(this, "progress", progress);
        objectAnimator.setDuration(duration);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.start();

        if(progress == getMax()){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(mListener != null) {
                        mListener.onProgressFinish(CustomProgressBar.this);
                    }
                }
            }, duration);
        }
    }

    private OnProgressFinishListener mListener;
    @Override
    public void setOnProgressFinish(OnProgressFinishListener listener) {
        mListener = listener;
    }
}
