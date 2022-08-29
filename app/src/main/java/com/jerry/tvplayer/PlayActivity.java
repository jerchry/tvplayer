package com.jerry.tvplayer;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.SeekBar;

import com.jerry.tvplayer.player.TVPlayer;

/**
 * @author Jerry
 */
public class PlayActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {
    private TVPlayer tvPlayer;
    public String url;
    private SeekBar seekBar;

    private int progress;
    private boolean isTouch;
    private boolean isSeek;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager
                .LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_play);
        SurfaceView surfaceView = findViewById(R.id.surfaceView);
        tvPlayer = new TVPlayer();
        tvPlayer.setSurfaceView(surfaceView);
        tvPlayer.setOnPrepareListener(new TVPlayer.OnPrepareListener() {
            /**
             * 视频信息获取完成 随时可以播放的时候回调
             */
            @Override
            public void onPrepared() {
                //获得时间
                int duration = tvPlayer.getDuration();
                //直播： 时间就是0
                if (duration != 0){
                   runOnUiThread(new Runnable() {
                       @Override
                       public void run() {
                           //显示进度条
                           seekBar.setVisibility(View.VISIBLE);
                       }
                   });
                }
                tvPlayer.start();
            }
        });
        tvPlayer.setOnErrorListener(new TVPlayer.OnErrorListener() {
            @Override
            public void onError(int error) {

            }
        });
        tvPlayer.setOnProgressListener(new TVPlayer.OnProgressListener() {

            @Override
            public void onProgress(final int progress2) {
                if (!isTouch) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int duration = tvPlayer.getDuration();
                            //如果是直播
                            if (duration != 0) {
                                if (isSeek){
                                    isSeek = false;
                                    return;
                                }
                                //更新进度 计算比例
                                seekBar.setProgress(progress2 * 100 / duration);
                            }
                        }
                    });
                }
            }
        });
        seekBar = findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(this);
        url = getIntent().getStringExtra("url");
//        dnPlayer.setDataSource("/sdcard/aaa/a.mp4");
//        dnPlayer.setDataSource("http://39.134.115.163:8080/PLTV/88888910/224/3221225618/index.m3u8");
        tvPlayer.setDataSource(url);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager
                    .LayoutParams.FLAG_FULLSCREEN);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_play);
        SurfaceView surfaceView = findViewById(R.id.surfaceView);
        tvPlayer.setSurfaceView(surfaceView);
        tvPlayer.setDataSource(url);
        seekBar = findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(this);
        seekBar.setProgress(progress);

    }

    @Override
    protected void onResume() {
        super.onResume();
        tvPlayer.prepare();
    }

    @Override
    protected void onStop() {
        super.onStop();
        tvPlayer.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tvPlayer.release();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        isTouch = true;
    }

    /**
     * 停止拖动的时候回调
     * @param seekBar
     */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        isSeek = true;
        isTouch = false;
        progress = tvPlayer.getDuration() * seekBar.getProgress() / 100;
        //进度调整
        tvPlayer.seek(progress);
    }
}
