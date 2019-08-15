package com.virgil.study.cqplayer.Music;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import com.virgil.study.cqplayer.EventMsg;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

public class CQPlayer {
    private final String TAG = "CQPlayer";
    private static CQPlayer player;
    private static MediaPlayer mediaPlayer;
    private String url;
    private Context context;
    private boolean isOnCompletion;
    private MusicInfo music;

    private CQPlayer(Context context) {
        this.context = context.getApplicationContext();
    }


    public MusicInfo getMusic() {
        return music;
    }

    public static CQPlayer getInstance(Context context) {
        synchronized (CQPlayer.class) {
            if (player == null) {
                player = new CQPlayer(context);
            }
            return player;
        }
    }

    public static CQPlayer getPlayer() {
        if (player == null) {
            return null;
        }
        return player;
    }

    public void seekTo(int position) {
        mediaPlayer.seekTo(position);
    }

    public void set() throws IOException {
        this.music = MusicData.getInstance().getMusic();
        url = music.getUrl();
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, Uri.parse(url));
        }
        Log.i(TAG, "文件路径： " + url);
//        mediaPlayer.setDataSource(context, Uri.parse(url));
        if (music == null) {
            Log.e(TAG, "播放错误");
        } else {
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    isOnCompletion = true;
//                    stop();
                }
            });
        }
    }

    public int getDuration() {//获取音频长度
        return mediaPlayer.getDuration();
    }

    public void play() throws IOException {//播放
        if (isOnCompletion && mediaPlayer.isPlaying()) {
            mediaPlayer.prepare();
        }
    }

    public void restart() {//开始
        mediaPlayer.start();
    }

    public void stop() {//停止
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public void next() throws IOException {//下一首
        mediaPlayer.reset();
        stop();
        set();
        play();
    }

    public void pause() {//暂停
        mediaPlayer.pause();
    }

    public boolean isPlaying() {//是否播放
        return mediaPlayer.isPlaying();
    }

    public int getCurrenPosition() {//播放进度
        return mediaPlayer.getCurrentPosition();
    }
}
