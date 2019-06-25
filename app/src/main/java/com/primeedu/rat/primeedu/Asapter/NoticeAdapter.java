package com.primeedu.rat.primeedu.Asapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.primeedu.rat.primeedu.Class.NoticeDetails;
import com.primeedu.rat.primeedu.Class.ZoomAbleImageActivity;
import com.primeedu.rat.primeedu.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NoticeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Pair<String,NoticeDetails>>list;
    Context context;
    private int image_type = 1;
    private int statas_type = 2;
    String SchoolId;
    String type;


    public NoticeAdapter(List<Pair<String,NoticeDetails>> lsit, Context context, String SchoolId,String type) {
        this.list = lsit;
        this.context = context;
        this.SchoolId = SchoolId;
        this.type = type;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        if(i == statas_type){
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.noticewithoutimage, viewGroup, false);

            return new ViewHolder(v);
        }
        else if(i == image_type){
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.noticewithimage, viewGroup, false);
            return new ViewHolder2(v);
        }else{
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        SimpleDateFormat myFormat = new SimpleDateFormat("ddMMyyyyHHmmssS");
        Date date = null;
        try {
            date =myFormat.parse(list.get(i).first);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(viewHolder.getItemViewType());
        if(viewHolder.getItemViewType() == statas_type){
            ViewHolder v = (ViewHolder) viewHolder;
            v.txts.setText(list.get(i).second.getMsg());
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy |(HH:mm)");
            v.date.setText(formatter.format(date));


            if(type.equals("3"))v.lin.setEnabled(true);
            else v.lin.setEnabled(false);

            v.lin.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    PopupMenu popup = new PopupMenu(v.getContext(), v, Gravity.RIGHT);

                    popup.getMenuInflater().inflate(R.menu.delete,popup.getMenu());

                    popup.getMenu().findItem(R.id.edit).setVisible(false);
                    popup.getMenu().findItem(R.id.edit).setEnabled(false);

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            if(item.getItemId() == R.id.deletepost){

                                String path = "Schools/" + SchoolId + "/Notice/"+list.get(i).first+"/";
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
        if(viewHolder.getItemViewType() == image_type){
            ViewHolder2 v = (ViewHolder2) viewHolder;
            v.txt.setText(list.get(i).second.getMsg());
            Glide.with(context)
                    .load(list.get(i).second.getMsgUrl())
                    .into(v.notiImage);

            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            v.date.setText(formatter.format(date));

            v.notiImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println(i);
                    Intent intent = new Intent(context, ZoomAbleImageActivity.class);
                    intent.putExtra("url",list.get(i).second.getMsgUrl());
                    context.startActivity(intent);
                }
            });

            if(type.equals("3"))v.lin.setEnabled(true);
            else v.lin.setEnabled(false);

            v.lin.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    PopupMenu popup = new PopupMenu(v.getContext(), v, Gravity.RIGHT);

                    popup.getMenuInflater().inflate(R.menu.delete,popup.getMenu());

                    popup.getMenu().findItem(R.id.edit).setVisible(false);
                    popup.getMenu().findItem(R.id.edit).setEnabled(false);

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            if(item.getItemId() == R.id.deletepost){

                                String path = "Schools/" + SchoolId + "/Notice/"+list.get(i).first+"/";
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
//        if(list.get(i).getMsgUrl().equals("")){
//            ViewHolder v = (ViewHolder) viewHolder;
//            v.txts.setText(list.get(i).getMsg());
//        }else{
//            ViewHolder2 v = (ViewHolder2) viewHolder;
//            v.txt.setText(list.get(i).getMsg());
//            Glide.with(context)
//                    .load(list.get(i).getMsgUrl())
//                    .into(v.notiImage);
//        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(list.get(position).second.getMsgUrl().equals("")){
            return statas_type;
        }
        else{
            return image_type;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView txts, txt1, date;
        LinearLayout lin;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            date = (TextView)itemView.findViewById(R.id.date);
            lin = (LinearLayout) itemView.findViewById(R.id.lin);
            txts = (TextView) itemView.findViewById(R.id.txtId);
            txt1 = (TextView) itemView.findViewById(R.id.txtId2);
        }
    }
    public class ViewHolder2 extends RecyclerView.ViewHolder{

        public TextView txt, txt1,date;
        LinearLayout lin;
        ImageButton notiImage;

        public ViewHolder2(@NonNull View itemView) {
            super(itemView);
            date = (TextView)itemView.findViewById(R.id.date);
            lin = (LinearLayout) itemView.findViewById(R.id.lin);
            txt = (TextView) itemView.findViewById(R.id.txtId);
            notiImage = (ImageButton)itemView.findViewById(R.id.notiImage);
        }
    }
}
