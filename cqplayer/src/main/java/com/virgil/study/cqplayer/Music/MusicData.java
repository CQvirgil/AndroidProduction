package com.virgil.study.cqplayer.Music;

import java.util.List;

public class MusicData {
    private List<MusicInfo> musicList;
    private static MusicData instance;
    private int index;

    private MusicData() {
    }

    public static MusicData getInstance(){
        synchronized (MusicData.class){
            if(instance == null){
                instance = new MusicData();
            }
        }
        return instance;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public List<MusicInfo> getMusicList() {
        return musicList;
    }

    public void setMusicList(List<MusicInfo> musicList) {
        this.musicList = musicList;
    }
}
