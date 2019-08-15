package com.virgil.study.cqplayer.Music;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.virgil.study.cqplayer.Util;

import java.io.IOException;

public class MyMusicService extends Service {
    private final String TAG = "MyMusicService";

    public MyMusicService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind: ");
        return new MusicBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: ");
        try {
            Util.getMusic(getApplicationContext());
            CQPlayer.getInstance(getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "onCreate: " + e.getMessage());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        CQPlayer.getPlayer().stop();
        MusicData.getInstance().clear();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand: ");
        return super.onStartCommand(intent, flags, startId);
    }

    public void startMusic() throws IOException {
        Log.i(TAG, "startMusic: ");
        CQPlayer.getPlayer().set();
        CQPlayer.getPlayer().play();
    }

    public void pauseMusic() {
        CQPlayer.getPlayer().pause();
    }

    public void nextMusic() throws IOException {
        MusicData.getInstance().next();
        Log.i(TAG, "nextMusic: "+ MusicData.getIndex());
        CQPlayer.getPlayer().next();
    }

    public void backMusic() throws IOException {
        MusicData.getInstance().back();
        CQPlayer.getPlayer().next();
    }

    private class MusicBinder extends Binder implements LinkMusicService {
        @Override
        public void StartMusic() {
            try {
                startMusic();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "StartMusic: " + e.getMessage());
            }
        }

        @Override
        public void PauseMusic() {
            pauseMusic();
        }

        @Override
        public void NextMusic() {
            try {
                nextMusic();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "NextMusic: " + e.getMessage());
            }
        }

        @Override
        public void BackMusic() {
            try {
                backMusic();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "BackMusic: " + e.getMessage());
            }
        }

        @Override
        public int getDuration() {//音乐长度
            return CQPlayer.getPlayer().getDuration();
        }

        @Override
        public int getCurrenPosition() {//音乐当前进度
            return CQPlayer.getPlayer().getCurrenPosition();
        }

        @Override
        public void Seekto(int mesc) {//设置音乐进度
            CQPlayer.getPlayer().seekTo(mesc);
        }

        @Override
        public void resetMusic(int index) {
            try {
                Log.i(TAG, "resetMusic: " + MusicData.getIndex());
                CQPlayer.getPlayer().next();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "resetMusic: " + e.getMessage());
            }
        }

        @Override
        public void Continue() {
            CQPlayer.getPlayer().restart();
        }

        @Override
        public boolean getisOnCompletion() {
            return false;
        }
    }
}