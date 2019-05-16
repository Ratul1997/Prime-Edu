package com.example.rat.primeedu;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AnnounceMentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    List<AnnouncementWithClass>Announcement = new ArrayList<>();
    Context context;

    public AnnounceMentAdapter(List<AnnouncementWithClass> announcement, Context context) {
        Announcement = announcement;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.renderannouncement, parent, false);


        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ((ViewHolder) holder).txt.setText(Announcement.get(position).getMSg());
        ((ViewHolder) holder).cls.setText("Class: "+Announcement.get(position).getClasses());
    }

    @Override
    public int getItemCount() {
        return Announcement.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView txt,cls;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cls = (TextView)itemView.findViewById(R.id.cls);
            txt = (TextView) itemView.findViewById(R.id.txt);
        }
    }
}
