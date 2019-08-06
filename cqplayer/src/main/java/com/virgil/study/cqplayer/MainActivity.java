package com.virgil.study.cqplayer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.virgil.study.cqplayer.Adapter.MusicListAdapter;
import com.virgil.study.cqplayer.Dailog.BaseDialog;
import com.virgil.study.cqplayer.Music.CQPlayer;
import com.virgil.study.cqplayer.Music.MusicData;
import com.virgil.study.cqplayer.Music.MusicInfo;
import com.virgil.study.cqplayer.mView.CircularProgress;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Util util;
    private ImageButton btn_player, btn_music_list;
    private TextView music_title, music_text;
    private CircularProgress music_circularProgress;
    private Timer timer;
    private int music_index;
    private NotificationManager notificationManager;
    private RemoteViews remoteViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        WindowManager windowManager = getWindowManager();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        Util.width = displayMetrics.widthPixels;
        Util.height = displayMetrics.heightPixels;
    }

    private void init() {
        getPermission();
        //setNotification();
        initView();
    }

    private void setNotification(){
        remoteViews = new RemoteViews(getPackageName(), R.layout.customnotice);
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        // 点击跳转到主界面

        Intent intent = new Intent(this, MainActivity.class);

        PendingIntent intent_go = PendingIntent.getActivity(this, 5, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.notice, intent_go);

        Notification notify = builder.build();
        notify.contentView = remoteViews; // 设置下拉图标
        notify.bigContentView = remoteViews; // 防止显示不完全,需要添加apisupport
        notify.flags = Notification.FLAG_ONGOING_EVENT;
        notify.icon = R.drawable.ic_launcher_background;
        notificationManager.notify(100, notify);
    }

    private void getPermission(){
        int hasWriteStoragePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (Build.VERSION.SDK_INT >= 23) {
            if (hasWriteStoragePermission == PackageManager.PERMISSION_GRANTED) {
                initData();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.MEDIA_CONTENT_CONTROL}, 123);
            }
        } else {
            initData();
        }
    }

    private void initData() {
        util = new Util(this);
        try {
            MusicData.getInstance().setMusicList(util.getMusic());
            music_index = MusicData.getInstance().getMusicList().size() - 1;
            CQPlayer.getInstance(this)
                    .set(MusicData.getInstance().getMusicList()
                            .get(music_index));
            CQPlayer.getInstance(this).play();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("HandlerLeak")
    private void initView() {
        btn_player = findViewById(R.id.music_btn);
        btn_player.setOnClickListener(this);
        //btn_player.setBackgroundResource(R.mipmap.btn_music_play);
        music_text = findViewById(R.id.music_text);
        music_text.setText(MusicData.getInstance().getMusicList().get(music_index).getSinger());
        music_title = findViewById(R.id.music_title);
        music_title.setText(CQPlayer.getInstance(this).getMusic().getTitle());
        btn_music_list = findViewById(R.id.imgbtn_music_list);
        btn_music_list.setOnClickListener(this);
        music_circularProgress = findViewById(R.id.music_circularprogress);
        music_circularProgress.setMax(CQPlayer.getInstance(this).getMusic().getDuration());
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                music_circularProgress.setValue(CQPlayer.getInstance(getApplicationContext()).getCurrenPosition());
            }
        }, 0, 2000);

    }

    @Override
    protected void onStop() {
        super.onStop();
        CQPlayer.getInstance(this).stop();
        timer.cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        notificationManager.cancel(100);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.music_btn:
                if (CQPlayer.getInstance(this).isPlaying()) {
                    CQPlayer.getInstance(this).pause();
                    btn_player.setBackgroundResource(R.mipmap.btn_music_play);
                } else {
                    CQPlayer.getInstance(this).restart();
                    btn_player.setBackgroundResource(R.mipmap.btn_music_pause);
                }
                break;
            case R.id.imgbtn_music_list:
                BaseDialog dialog = new BaseDialog(this);
                dialog.show();
                break;
        }
    }
}
