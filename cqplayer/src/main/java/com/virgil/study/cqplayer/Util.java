package com.virgil.study.cqplayer;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import com.virgil.study.cqplayer.Music.MusicInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Util {
    private final String TAG = "Util";
    Context context;

    public Util(Context context) {
        this.context = context;
    }

    public List<MusicInfo> getMusic() throws IOException {
        List<MusicInfo> list = new ArrayList<>();
        Log.i(TAG, "getMusic: getMusicURL");
        ContentResolver cr = context.getContentResolver();
        if (cr == null) {
            Log.e(TAG, "getMusic: null");
            return null;
        }

// 获取所有歌曲
        Cursor cursor = cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

        if (cursor == null) {
            Log.e(TAG, "getMusic: 没找到文件");
            return null;
        }

        if (cursor.moveToFirst()) {
            do {
                //音乐标题
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                //艺术家
                String singer = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                //专辑
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                //文件大小
                long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
                //时长
                int duration = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                //文件路径
                String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                 MusicInfo musicInfo = new MusicInfo(title, url, duration, singer, album, size);
                list.add(musicInfo);
                Log.i(TAG,title + "\n" + url + "\n" + duration);
            } while (cursor.moveToNext());
        } else {
            Log.e("getmusic", "null");
            return null;
        }
        return list;
    }
}
