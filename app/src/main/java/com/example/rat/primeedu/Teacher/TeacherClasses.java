package com.example.rat.primeedu.Teacher;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rat.primeedu.Asapter.ClassNamesAdapter;
import com.example.rat.primeedu.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class TeacherClasses extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<String>list;
    Context context;
    String SchoolCode;
    String SchoolName;
    String TeacherId;

    public TeacherClasses(List<String> list, Context context, String schoolCode,String SchoolName,String TeacherId) {
        this.list = list;
        this.context = context;
        SchoolCode = schoolCode;
        this.SchoolName = SchoolName;
        this.TeacherId = TeacherId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        ViewHolder v = (ViewHolder)viewHolder;
        v.txt.setText("Class: " +list.get(i));
        v.txt1.setText("");
        v.lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ClassActivity.class);

                intent.putExtra("clsname",list.get(i));
                intent.putExtra("type","2");
                intent.putExtra("id",SchoolCode);
                intent.putExtra("schoolname",SchoolName);
                context.startActivity(intent);
            }
        });

        v.lin.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                PopupMenu popup = new PopupMenu(v.getContext(), v, Gravity.RIGHT);

                popup.getMenuInflater().inflate(R.menu.delete,popup.getMenu());


                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        if(item.getItemId() == R.id.deletepost){

                            String path = "Users/Teachers/"+TeacherId+"/Class/"+list.get(i)+"/";
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
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txt, txt1;
        CardView lin;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lin = (CardView) itemView.findViewById(R.id.lin);
            txt = (TextView) itemView.findViewById(R.id.txtId);
            txt1 = (TextView) itemView.findViewById(R.id.txtId2);
        }
    }
}
