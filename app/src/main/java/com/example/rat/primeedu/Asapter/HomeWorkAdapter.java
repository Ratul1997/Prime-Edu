package com.example.rat.primeedu.Asapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rat.primeedu.Class.AnnouncementWithClass;
import com.example.rat.primeedu.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class HomeWorkAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Pair<String,AnnouncementWithClass>>HomeWork = new ArrayList<>();
    Context context;
    String type;
    String SchoolId;

    public HomeWorkAdapter(List<Pair<String,AnnouncementWithClass>> homeWork, Context context, String type, String SchoolId) {
        HomeWork = homeWork;
        this.context = context;
        this.type = type;
        this.SchoolId = SchoolId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.renderannouncement, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        ((ViewHolder) holder).txt.setText(HomeWork.get(position).second.getMSg());

        String s = "";
        if(type.equals("1"))s = HomeWork.get(position).second.getClasses();
        else s = "Class: "+HomeWork.get(position).second.getClasses();

        ((ViewHolder) holder).cls.setText(s);

        if(type.equals("1"))((ViewHolder)holder).lin.setEnabled(false);
        ((ViewHolder)holder).lin.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu popup = new PopupMenu(v.getContext(), v, Gravity.RIGHT);

                popup.getMenuInflater().inflate(R.menu.delete,popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        if(item.getItemId() == R.id.deletepost){

                            String path = "Classes/"+SchoolId+"/class/"+HomeWork.get(position).second.getClasses()+"/AnnounceMents/HomeWork/"+HomeWork.get(position).first;
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference(path);

                            ref.removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(context, "Successfully deleted.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }

                        return true;
                    }
                });

                popup.show();

                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return HomeWork.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView txt,cls;
        public CardView lin;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            lin = (CardView)itemView.findViewById(R.id.lin);
                cls  = (TextView)itemView.findViewById(R.id.cls);
               txt = (TextView) itemView.findViewById(R.id.txt);
        }
    }

}
