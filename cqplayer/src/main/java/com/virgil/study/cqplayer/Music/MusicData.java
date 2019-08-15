package com.virgil.study.cqplayer.Music;

import java.util.List;

public class MusicData {
    private List<MusicInfo> musicList;
    private static MusicData instance;
    public static int index = 0;

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

    public static int getIndex() {
        return index;
    }

    public static void setIndex(int index) {
        MusicData.index = index;
    }

    public List<MusicInfo> getMusicList() {
        return musicList;
    }

    public void setMusicList(List<MusicInfo> musicList) {
        this.musicList = musicList;
    }

    public MusicInfo getMusic(){
        return musicList.get(index);
    }

    public void random(){
        index = (int) (Math.random() * (musicList.size() - 1));
    }

    public void next(){
        if(index < musicList.size()){
            index ++;
        }else{
            index = musicList.size() - 1;
        }
    }

    public void back(){
        if(index > 0){
          index --;
        }else{
            index = 0;
        }
    }

    public void clear(){
        musicList.clear();
        musicList = null;
    }
}
