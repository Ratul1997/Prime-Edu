package com.primeedu.rat.primeedu.Asapter;

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

import com.primeedu.rat.primeedu.Class.ClassName;
import com.primeedu.rat.primeedu.R;
import com.primeedu.rat.primeedu.Teacher.ClassActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ClassNamesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<ClassName> listClassName = new ArrayList<>();
    Context context;
    String SchoolId;
    String SchoolName;
    public ClassNamesAdapter(List<ClassName> listClassName, Context context, String schoolId,String SchoolName) {
        this.listClassName = listClassName;
        this.context = context;
        SchoolId = schoolId;
        this.SchoolName = SchoolName;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final ClassName currentItem = listClassName.get(position);

        ((ViewHolder) holder).txt.setText("Class : " + currentItem.getClasNaMEs());
        ((ViewHolder) holder).txt1.setText("Total Section: " + currentItem.getTotalSection());


        ((ViewHolder) holder).lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(currentItem.getClasNaMEs());
                Intent intent = new Intent(context,ClassActivity.class);

                intent.putExtra("clsname",currentItem.getClasNaMEs());
                intent.putExtra("type","3");
                intent.putExtra("id",SchoolId);
                intent.putExtra("schoolname",SchoolName);
                context.startActivity(intent);
            }
        });

        ((ViewHolder)holder).lin.setOnLongClickListener(
                new View.OnLongClickListener() {
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

                                    String path = "Classes/"+SchoolId+"/class/"+listClassName.get(position).getClasNaMEs()+"/";
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
                }
        );

    }

    @Override
    public int getItemCount() {
        return listClassName.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{

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
