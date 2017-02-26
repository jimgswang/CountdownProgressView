package com.jimgswang.countdownprogressview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
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

    private static final String KEY_SUPERSTATE = "cpv_superstate";

    private static final String KEY_CURRENT_TIME = "cpv_currenttime";

    private static final int DEFAULT_DURATION = 30000;

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
    private int duration;

    // If the animation was canceled.
    private boolean wasCanceled = false;


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
            duration = array.getInteger(R.styleable.CountdownProgressView_cpv_duration, DEFAULT_DURATION);
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
            public void onAnimationCancel(Animator animation) {
                wasCanceled = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (onCompleteListener != null && !wasCanceled) {
                    onCompleteListener.onComplete();
                }
            }
        });

        setDuration(duration);
    }

    /**
     * Set the OnCompleteListener to be called when the ProgressBar is filled
     * @param listener
     */
    public void setOnCompleteListener(OnCompleteListener listener) {
        onCompleteListener = listener;
    }

    /**
     * Resume the progressbar animation
     */
    public void play() {
        wasCanceled = false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (progressAnimator.isPaused()) {
                progressAnimator.resume();
            } else if (!progressAnimator.isRunning()){
                progressAnimator.start();
            }
        } else {
            progressAnimator.start();
            progressAnimator.setCurrentPlayTime(currentPlayTime);
            currentPlayTime = 0;
        }
    }

    /**
     * Pause the progressbar animation
     */
    public void pause() {
        if (progressAnimator.isRunning()) {
            currentPlayTime = progressAnimator.getCurrentPlayTime();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            progressAnimator.pause();
        } else {
            progressAnimator.cancel();
        }
    }

    /**
     * Reset the progressbar to initial state
     */
    public void reset() {
        currentPlayTime = 0;
        progressAnimator.setCurrentPlayTime(currentPlayTime);
        progressAnimator.cancel();
    }

    /**
     * Set the duration of the progress animation
     * @param duration The duration in ms
     */
    public void setDuration(int duration) {
        progressAnimator.setDuration(duration);
    }

    /**
     * Get the underlying progressbar widget
     * @return ProgressBar the ProgressBar widget
     */
    public ProgressBar getProgressBar() {
        return progressBar;
    }

    @Override
    protected Parcelable onSaveInstanceState()
    {
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_SUPERSTATE, super.onSaveInstanceState());
        bundle.putLong(KEY_CURRENT_TIME, currentPlayTime);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state)
    {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            currentPlayTime = bundle.getLong(KEY_CURRENT_TIME, 0);
            progressAnimator.setCurrentPlayTime(currentPlayTime);
            state = bundle.getParcelable(KEY_SUPERSTATE);
        }
        super.onRestoreInstanceState(state);

    }
}
