package com.virgil.study.cqplayer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
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

import com.squareup.leakcanary.RefWatcher;
import com.virgil.study.cqplayer.Adapter.MusicListAdapter;
import com.virgil.study.cqplayer.Dailog.BaseDialog;
import com.virgil.study.cqplayer.Music.CQPlayer;
import com.virgil.study.cqplayer.Music.LinkMusicService;
import com.virgil.study.cqplayer.Music.MusicData;
import com.virgil.study.cqplayer.Music.MusicInfo;
import com.virgil.study.cqplayer.Music.MyMusicService;
import com.virgil.study.cqplayer.mView.CircularProgress;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = "CQplayer_MainActivity";
    private ImageButton btn_player, btn_music_list;
    private TextView music_title, music_text;
    private CircularProgress music_circularProgress;
    private Timer timer;
    private LinkMusicService musicService;
    private MusicConnection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RefWatcher refWatcher = mApplication.getRefWatcher(this);
        refWatcher.watch(this);
        init();
        EventBus.getDefault().register(this);
    }

    private void init() {
        getPermission();
        initView();
    }

    private void getPermission() {
        int hasWriteStoragePermission = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
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
        BindMusicService();
    }

    private void BindMusicService() {
        Intent intent = new Intent(MainActivity.this, MyMusicService.class);
        connection = new MusicConnection();
        bindService(intent, connection, BIND_AUTO_CREATE);
    }

    private void initView() {
        //播放按钮
        btn_player = findViewById(R.id.music_btn);
        btn_player.setOnClickListener(this);
        music_text = findViewById(R.id.music_text);
        music_title = findViewById(R.id.music_title);
        //歌曲列表的开关按钮
        btn_music_list = findViewById(R.id.imgbtn_music_list);
        btn_music_list.setOnClickListener(this);
        //圆形进度条
        music_circularProgress = findViewById(R.id.music_circularprogress);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
        unbindService(connection);
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void EventBusMsgHandler(EventMsg messageEvent) {
        Log.i(TAG, "EventBusMsgHandler: " + messageEvent);
        music_text.setText(MusicData.getInstance().getMusic().getSinger());
        music_title.setText(MusicData.getInstance().getMusic().getTitle());
        music_circularProgress.setMax(MusicData.getInstance().getMusic().getDuration());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.music_btn:
                if (CQPlayer.getPlayer().isPlaying()) {
                    musicService.PauseMusic();
                    btn_player.setBackgroundResource(R.mipmap.btn_music_play);
                } else {
                    musicService.Continue();
                    btn_player.setBackgroundResource(R.mipmap.btn_music_pause);
                }
                break;
            case R.id.imgbtn_music_list:
                BaseDialog dialog = new BaseDialog(this);
                MusicListAdapter adapter = new MusicListAdapter();
                dialog.show();
                adapter.setOnItemClickListaner(new MusicListAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int postition) {
                        MusicData.setIndex(postition);
                        musicService.resetMusic(postition);
                    }
                });
                dialog.setMusicAdapter(adapter);
                break;
        }
    }

    private class MusicConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicService = (LinkMusicService) service;
            musicService.StartMusic();
            Log.i(TAG, "onServiceConnected: ");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }
}
