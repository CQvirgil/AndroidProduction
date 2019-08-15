package com.virgil.study.cqplayer.Music;

public interface LinkMusicService {
    public void StartMusic();
    public void PauseMusic();
    public void NextMusic();
    public void BackMusic();
    public int getDuration();
    public int getCurrenPosition();
    public void Seekto(int mesc);
    public void resetMusic(int index);
    public void Continue();
    public boolean getisOnCompletion();
}
