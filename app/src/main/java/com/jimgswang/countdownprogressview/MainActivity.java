package com.jimgswang.countdownprogressview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final CountdownProgressView progressBar = (CountdownProgressView) findViewById(R.id.cpv);
        Button btnPlay = (Button) findViewById(R.id.btn_play);
        Button btnPause = (Button) findViewById(R.id.btn_pause);
        Button btnReset = (Button)  findViewById(R.id.btn_reset);


        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.play();
            }
        });

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.pause();
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.reset();
            }
        });

        progressBar.setOnCompleteListener(new CountdownProgressView.OnCompleteListener() {
            @Override
            public void onComplete() {
                Toast.makeText(MainActivity.this, "Completed!", Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }
}
