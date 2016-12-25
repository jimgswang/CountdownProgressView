package com.jimgswang.countdownprogressview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

/**
 * Created by jim on 12/22/16.
 */

public class CountdownProgressView extends FrameLayout {

    public interface OnCompleteListener {
        void onComplete();
    }

    // The underlying progressbar widget
    private ProgressBar progressBar;

    // The animator used to update the progressbar widget
    private ObjectAnimator progressAnimator;

    // The listener to trigger after countdown is finished
    private OnCompleteListener onCompleteListener;

    // Saves the currently paused time for the animation. Used to resume pre-19
    private long currentPlayTime;

    // Linear interpolator for consistent animation speed
    private LinearInterpolator interpolator = new LinearInterpolator();

    // The max value for the progress bar widget. Set to max so animation is smooth
    // for long durations
    private int progressbarMax = Integer.MAX_VALUE;

    // The total duration of the countdown animation
    private int duration = 4000;


    public CountdownProgressView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public CountdownProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public CountdownProgressView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CountdownProgressView, 0, 0);

        try {
            duration = array.getInteger(R.styleable.CountdownProgressView_cpv_duration, 0);
        } finally {
            array.recycle();
        }

        LayoutInflater.from(context).inflate(R.layout.view_countdown_progress, this, true);

        progressBar = (ProgressBar) findViewById(R.id.cpv_progress);
        progressBar.setMax(progressbarMax);
        progressAnimator = ObjectAnimator.ofInt(progressBar, "progress", 0, progressbarMax);
        progressAnimator.setInterpolator(interpolator);
        progressAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (onCompleteListener != null) {
                    onCompleteListener.onComplete();
                }
            }
        });

        setDuration(duration);
    }

    public void setOnCompleteListener(OnCompleteListener listener) {
        onCompleteListener = listener;
    }

    public void play() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (progressAnimator.isPaused()) {
                progressAnimator.resume();
            } else if (!progressAnimator.isRunning()){
                progressAnimator.start();
            }
        } else {
            progressAnimator.setCurrentPlayTime(currentPlayTime);
            progressAnimator.start();
            currentPlayTime = 0;
        }
    }

    public void pause() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            progressAnimator.pause();
        } else {
            currentPlayTime = progressAnimator.getCurrentPlayTime();
            progressAnimator.cancel();
        }
    }

    public void reset() {
        currentPlayTime = 0;
        progressAnimator.setCurrentPlayTime(currentPlayTime);
        progressAnimator.cancel();
    }

    public void setDuration(int duration) {
        progressAnimator.setDuration(duration);
    }
}
