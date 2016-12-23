package com.jimgswang.countdownprogressview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

/**
 * Created by jim on 12/22/16.
 */

public class CountdownProgressView extends FrameLayout {

    private ProgressBar progressBar;
    private Context context;
    private ObjectAnimator progressAnimator;
    private long currentPlayTime;
    private OnCompleteListener onCompleteListener;


    public CountdownProgressView(Context context) {
        super(context);
        this.context = context;

        init();
    }

    public CountdownProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        init();
    }

    public CountdownProgressView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;

        init();
    }

    private void init() {
        LayoutInflater.from(context).inflate(R.layout.view_countdown_progress, this, true);

        progressBar = (ProgressBar) findViewById(R.id.cpv_progress);
        progressAnimator = ObjectAnimator.ofInt(progressBar, "progress", 0, 100);
        progressAnimator.setDuration(4000);
        progressAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (onCompleteListener != null) {
                    onCompleteListener.onComplete();
                }
            }
        });
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

    public interface OnCompleteListener {
        void onComplete();
    }
}
