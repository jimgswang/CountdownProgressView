# CountdownProgressView

![](http://i.imgur.com/TWQmuIv.gif)

A progress bar that acts as a timer. When the specified duration is complete, a callback is triggered.

#### Install

    compile 'com.wangjim:countdownprogressview:0.2.0'
    
#### Usage

    <com.wangjim.countdownprogressview.CountdownProgressView
        android:id="@+id/cpv"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:cpv_duration="5000"
        />
        
See sample for full usage details.


#### Styling

This uses the stock progress bar as the default. It exposes the progress bar view so you can set your own drawable if you wish. For example, to use the drawable from the [MaterialProgressBar](https://github.com/DreaminginCodeZH/MaterialProgressBar) library, you can do the following:

     countdownProgressView = (CountdownProgressView) findViewById(R.id.cpv);
     HorizontalProgressDrawable drawable = new HorizontalProgressDrawable(this);
     countdownProgressView.getProgressBar().setProgressDrawable(drawable);
     
See the sample for more details.

#### License

MIT
