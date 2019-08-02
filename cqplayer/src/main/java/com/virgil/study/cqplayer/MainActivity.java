package com.virgil.study.cqplayer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.virgil.study.cqplayer.Music.CQPlayer;
import com.virgil.study.cqplayer.Music.MusicData;
import com.virgil.study.cqplayer.Music.MusicInfo;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Util util;
    private ImageButton btn_player;
    private TextView music_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init(){
        initData();
        initView();
    }

    private void initData(){
        util = new Util(this);
        try {
            MusicData.getInstance().setMusicList(util.getMusic());
            CQPlayer.getInstance(this)
                    .set(MusicData.getInstance().getMusicList()
                            .get(MusicData.getInstance().getMusicList().size() - 1));
            CQPlayer.getInstance(this).play();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initView(){
        btn_player = findViewById(R.id.music_btn);
        btn_player.setOnClickListener(this);
        music_title = findViewById(R.id.music_title);
        music_title.setText(CQPlayer.getInstance(this).getMusic().getTitle());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CQPlayer.getInstance(this).stop();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.music_btn:
                    if(CQPlayer.getInstance(this).isPlaying()){
                        CQPlayer.getInstance(this).pause();
                        btn_player.setBackgroundResource(R.mipmap.ic_launcher);
                    }else{
                        CQPlayer.getInstance(this).restart();
                        btn_player.setBackgroundResource(R.mipmap.btn_music_pause);
                    }
                break;
        }
    }
}
