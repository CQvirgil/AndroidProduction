package com.virgil.study.cqplayer.Music;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

public class CQPlayer {
    private static CQPlayer player;
    private static MediaPlayer mediaPlayer;
    private String url;
    private Context context;
    private boolean isOnCompletion;
    private MusicInfo music;

    private CQPlayer(Context context) {
        this.context = context;
    }


    public MusicInfo getMusic() {
        return music;
    }

    public static CQPlayer getInstance(Context context){
        synchronized (CQPlayer.class){
            if (player == null){
                player = new CQPlayer(context);
                mediaPlayer = new MediaPlayer();
            }
            return player;
        }
    }

    public void set(MusicInfo music) throws IOException {
        this.music = music;
        url = music.getUrl();
        Log.i("testmediaplayer", url);
        mediaPlayer.setDataSource(context, Uri.parse(url));
        if(music == null){
            Toast.makeText(context, "播放错误", Toast.LENGTH_SHORT).show();
        }else {
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
                }
            });
        }
    }

    public void play() throws IOException {
        mediaPlayer.prepare();
    }

    public void restart(){
        mediaPlayer.start();
    }

    public void stop(){
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
    }

    public void next(MusicInfo music) throws IOException {
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
        mediaPlayer = new MediaPlayer();
        set(music);
        play();
    }

    public void pause(){
        mediaPlayer.pause();
    }

    public boolean isPlaying(){
        return mediaPlayer.isPlaying();
    }

    public int getCurrenPosition(){
        return mediaPlayer.getCurrentPosition();
    }
}
