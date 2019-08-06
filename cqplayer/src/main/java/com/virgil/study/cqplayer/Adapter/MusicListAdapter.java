package com.virgil.study.cqplayer.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.virgil.study.cqplayer.Music.MusicData;
import com.virgil.study.cqplayer.R;

public class MusicListAdapter extends RecyclerView.Adapter {
    private OnItemClickListener listaner;
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_music_list, viewGroup, false);

        return new MusicListHodler(view);
    }

    public void setOnItemClickListaner(OnItemClickListener listaner){
        this.listaner = listaner;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((MusicListHodler)viewHolder).setTitle(MusicData.getInstance().getMusicList().get(i).getTitle());
        ((MusicListHodler)viewHolder).setPosition(i);
        ((MusicListHodler)viewHolder).setOnclickListaner(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listaner.onItemClick(v, (Integer) v.getTag());
            }
        });
    }

    @Override
    public int getItemCount() {
        return MusicData.getInstance().getMusicList().size();
    }

    public interface OnItemClickListener{
        public void onItemClick(View view, int postition);
    }

    public class MusicListHodler extends RecyclerView.ViewHolder{
        TextView title;
        View itemView;
        public MusicListHodler(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            title = itemView.findViewById(R.id.item_music_list_title);

        }

        public void setOnclickListaner(View.OnClickListener listaner){
            itemView.setOnClickListener(listaner);
        }

        public void setPosition(int position){
            itemView.setTag(position);
        }

        public void setTitle(String title){
            this.title.setText(title);
        }
    }
}
