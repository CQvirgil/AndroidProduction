package com.virgil.study.cqplayer.Dailog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.virgil.study.cqplayer.Adapter.MusicListAdapter;
import com.virgil.study.cqplayer.Music.CQPlayer;
import com.virgil.study.cqplayer.Music.MusicData;
import com.virgil.study.cqplayer.R;

import java.io.IOException;

public class BaseDialog extends Dialog {
    private RecyclerView music_list;
    public BaseDialog(Context context) {
        super(context, R.style.MyDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_base);
        RelativeLayout root = findViewById(R.id.dialog_base_root);
        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.BaseDialog);

        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        int window_width = metrics.widthPixels;
        ViewGroup.LayoutParams params1 = root.getLayoutParams();
        params1.width = window_width;
        root.setLayoutParams(params1);
        initView();
    }

    private void initView(){
        music_list = findViewById(R.id.dialog_base_music_list);
        music_list.setLayoutManager(new LinearLayoutManager(getContext()));
        MusicListAdapter adapter = new MusicListAdapter();
        adapter.setOnItemClickListaner(new MusicListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int postition) {
                try {
                    CQPlayer.getInstance(getContext()).next(MusicData.getInstance().getMusicList().get(postition));
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i("CQPlayerError", "文件IO错误： " + e.getMessage());
                }
            }
        });
        music_list.setAdapter(adapter);
    }
}
