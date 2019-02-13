package com.news.today.todayinformation;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.File;
//AppCompatActivity提供对顶部标题栏ActionBar和Fragment等的支持，其向前兼容（Forwards Compatibility）到api7
// （如何叫做兼容到api7有待理解，但可以简单理解为早期的东西可以打开使用后期版本的东西，前是时间轴的前）
//任何Activity都是UI线程的一部分，是主线程的一部分。所以需要把Timer的计算逻辑写在子线程中
public class SplashActivity extends AppCompatActivity {
    private FullScreenVideoView mVideoView; //FullScreenVideoView是自己编写的类，主要重写了onMeasure()方法以使原本偏移的
    private TextView mTvTimer;
    private CustomCountDownTimer timer;
    //视频画面得以填满屏幕。onMeasure()函数具体原理见视频10.

    //onCreate是activity的生命周期。注意Java中所有都以类和对象的形式存在，所以“生命周期”是一个逻辑概念。
    /*
    Bundle和Map有相似之处，都是以Key,Value的键值对形式来存储数据，是一个存储数据的载体。
    * */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView实际上把id为此参数的的View加入到ViewGroup中.
        //换言之，它把编码者写的activity_splash布局加载到系统布局中。系统布局原本就有置顶的“蓝条”。
        //沉浸式布局就是要把该蓝条去掉。方法是在manifest文件中选取注册的activity，以Android:theme参数控制。
        setContentView(R.layout.anctivity_splash); //spell mistake.
        mTvTimer = (TextView)findViewById(R.id.tv_splash_timer);
        mVideoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //启动Activity在理论上有显式/隐式两种模式：在AndroidManifest.xml文件中用“意图过滤器”<intent-filter>
                //以建立意图（隐式）；或者直接调用Intent进行startActivity函数（显式）。
                //???疑问等待Anson回答，为什么这里匿名内部类的参数必须指定SplashActivity.this???
                startActivity(new Intent(SplashActivity.this,MainActivity.class));
            }
        });
        mVideoView =  (FullScreenVideoView) findViewById(R.id.vv_play);//现在已经不要强转了吗？
        //File.separator其实就是斜线（文件路径分隔符），但是Windows和Linux斜的方向不同，故为了跨平台，使用这个函数
        mVideoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + File.separator + R.raw.splash)); //设置视频文件的路径
        //观察者模式
        //***事件源.setListener(), 回调函数：事件源实现onListener函数，在这里是onPrepared()***
        //MediaPlayer用于获取，解码视频文件。其有若干生命周期，其中有prepared和start两者，后者即开始播放视频。
        //要先进入prepared生命周期才可以继续start阶段。
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {     //匿名内部类
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });
        //再设置监听器监听生命周期。视频播放结束后会进入complete生命阶段，此时监听器中用于监听Complete生命阶段的onCompletion
        //函数则会监听到MediaPlayer已经到了complete生命阶段，则触发（执行）mp.start()方法以继续播放视频。
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.start();
            }
        });
        timer = new CustomCountDownTimer(5, new CustomCountDownTimer.ICountDownHandler() {
            @Override
            public void onTicker(int time) {
                mTvTimer.setText(time + "秒");
            }

            @Override
            public void onFinish() {
                mTvTimer.setText("跳过");
            }
        });
        timer.start();  //开始“界面右上角5秒倒计时”业务功能
    }

    @Override
    //界面销毁时会自动调用onDestroy方法。我们将其重写，让timer终止
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    //onSaveInstanceState用于保存界面的状态(例如当要切换界面时，当前界面的数据等需要保存。)数据保存在Bundle对象中。
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        //此处outState的各种方法用于对界面数据做处理。
        super.onSaveInstanceState(outState, outPersistentState);
    }
}
