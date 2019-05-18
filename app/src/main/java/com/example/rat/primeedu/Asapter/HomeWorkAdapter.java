package com.example.rat.primeedu.Asapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rat.primeedu.Class.AnnouncementWithClass;
import com.example.rat.primeedu.R;

import java.util.ArrayList;
import java.util.List;

public class HomeWorkAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<AnnouncementWithClass>HomeWork = new ArrayList<>();
    Context context;

    public HomeWorkAdapter(List<AnnouncementWithClass> homeWork, Context context) {
        HomeWork = homeWork;
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
        ((ViewHolder) holder).txt.setText(HomeWork.get(position).getMSg());
        ((ViewHolder) holder).cls.setText("Class: "+HomeWork.get(position).getClasses());
    }

    @Override
    public int getItemCount() {
        return HomeWork.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView txt,cls;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
                cls  = (TextView)itemView.findViewById(R.id.cls);
               txt = (TextView) itemView.findViewById(R.id.txt);
        }
    }

}
