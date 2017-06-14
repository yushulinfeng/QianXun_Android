package com.szdd.qianxun.advertise;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.RelativeLayout;

import com.szdd.qianxun.R;
import com.szdd.qianxun.advertise.customview.CustomMediaController;
import com.szdd.qianxun.advertise.customview.CustomVideoView;
import com.szdd.qianxun.advertise.tools.Constants;

/**
 * Created by LLX on 2015/11/20.
 */
public class VideoPlayer extends Activity {
    private RelativeLayout video_play_layout;
    private CustomVideoView videoView;
    private MediaController mediaController;
    private Button btn_play;
    private Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        setContentView(R.layout.ad_player);
        this.mActivity = this;
        //布局
        video_play_layout = (RelativeLayout) findViewById(R.id.video_play_layout);
        //视频
        videoView = (CustomVideoView) findViewById(R.id.ad_video_player);
        //播放按钮
        btn_play = (Button) findViewById(R.id.video_btn_play);
        //控制
        mediaController = new CustomMediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setMediaPlayer(videoView);
        //从上个界面获取uri
        Intent intent = getIntent();
        String uri_string = intent.getStringExtra("video_uri");
        //播放
        Uri uri;
        if (!uri_string.equals("")) {
            uri = Uri.parse(uri_string);
            videoView.setVideoURI(uri);
//            videoView.start();
//            if (Constants.playPosition == -1) {
//                videoView.seekTo(100);
//                Constants.playPosition = videoView.getCurrentPosition();
//                videoView.pause();
//            } else {
//                videoView.seekTo(Constants.playPosition);
//                videoView.pause();
//            }
            btn_play.setVisibility(View.VISIBLE);
            videoView.requestFocus();
        }
        //播放按钮监听
        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (videoView.isPlaying())
                    videoView.pause();
                else
                    videoView.start();
            }
        });
        //播放状态监听
        videoView.setPlayPauseListener(new CustomVideoView.PlayPauseListener() {
            public void onPlay() {
                btn_play.setVisibility(View.INVISIBLE);
            }

            public void onPause() {
                btn_play.setVisibility(View.VISIBLE);
                Constants.playPosition = videoView.getCurrentPosition();
            }
        });
        //播放结束监听
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //播放结束后的动作
                videoView.start();
                videoView.pause();
            }
        });
        //布局监听双击暂停
        video_play_layout.setOnTouchListener(
                new View.OnTouchListener() {
                    long count = 0;
                    long firClick = 0;
                    long secClick = 0;

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (MotionEvent.ACTION_DOWN == event.getAction()) {
                            count++;
                            if (count == 1) {
                                firClick = System.currentTimeMillis();

                            } else if (count == 2) {
                                secClick = System.currentTimeMillis();
                                if (secClick - firClick < 500) {
                                    //双击事件
                                    if (videoView.isPlaying())
                                        videoView.pause();
                                    else
                                        videoView.start();
                                }
                                count = 0;
                                firClick = 0;
                                secClick = 0;
                            }
                        }
                        return true;
                    }
                }
        );
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}