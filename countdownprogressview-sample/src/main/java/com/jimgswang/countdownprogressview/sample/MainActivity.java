package com.jimgswang.countdownprogressview.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.jimgswang.countdownprogressview.CountdownProgressView;

import me.zhanghai.android.materialprogressbar.HorizontalProgressDrawable;
import me.zhanghai.android.materialprogressbar.IndeterminateHorizontalProgressDrawable;

public class MainActivity extends AppCompatActivity {

    private CountdownProgressView countdownProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        countdownProgressView = (CountdownProgressView) findViewById(R.id.cpv);
        Button btnPlay = (Button) findViewById(R.id.btn_play);
        Button btnPause = (Button) findViewById(R.id.btn_pause);
        Button btnReset = (Button)  findViewById(R.id.btn_reset);


        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countdownProgressView.play();
            }
        });

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countdownProgressView.pause();
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countdownProgressView.reset();
            }
        });

        HorizontalProgressDrawable drawable = new HorizontalProgressDrawable(this);
        countdownProgressView.getProgressBar().setProgressDrawable(drawable);
        countdownProgressView.setOnCompleteListener(new CountdownProgressView.OnCompleteListener() {
            @Override
            public void onComplete() {
                Toast.makeText(MainActivity.this, "Completed!", Toast.LENGTH_SHORT)
                    .show();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        countdownProgressView.pause();
    }
}
