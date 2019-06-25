package com.primeedu.rat.primeedu.Asapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
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

import com.primeedu.rat.primeedu.Asapter.ClassNamesAdapter;
import com.primeedu.rat.primeedu.R;
import com.primeedu.rat.primeedu.Teacher.GiveMarksToStudent;
import com.primeedu.rat.primeedu.Teacher.InsideSectionActivity;
import com.primeedu.rat.primeedu.Teacher.TakeAttendenceActivity;
import com.primeedu.rat.primeedu.Teacher.ViewStudentActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Method;
import java.util.List;

public class SectionAdapter extends RecyclerView.Adapter<ClassNamesAdapter.ViewHolder> {

    List<String>Section;
    List<Integer>Student;
    Context context;
    String CurrentClass;
    String type;
    String SchoolName;
    String SchoolId;

    public SectionAdapter(List<String> section, List<Integer> student, Context context, String currentClass, String type, String schoolId,String SchoolName) {
        Section = section;
        Student = student;
        this.context = context;
        CurrentClass = currentClass;
        this.type = type;
        SchoolId = schoolId;
        this.SchoolName = SchoolName;
    }

    @NonNull
    @Override
    public ClassNamesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);

        return new ClassNamesAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ClassNamesAdapter.ViewHolder holder, final int position) {

        ((ClassNamesAdapter.ViewHolder) holder).txt.setText("Section : " + Section.get(position));
        ((ClassNamesAdapter.ViewHolder) holder).txt1.setText("Total Student: " + Student.get(position));

        ((ClassNamesAdapter.ViewHolder) holder).lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context,InsideSectionActivity.class);
                intent.putExtra("stdNo",Integer.toString(Student.get(position)));
                intent.putExtra("sectionNo",Section.get(position));
                intent.putExtra("classNo",CurrentClass);
                intent.putExtra("type",type);
                intent.putExtra("schollId",SchoolId);
                intent.putExtra("schoolname",SchoolName);
                context.startActivity(intent);
            }
        });

        ((ClassNamesAdapter.ViewHolder) holder).lin.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu popup = new PopupMenu(v.getContext(), v, Gravity.RIGHT);

                popup.getMenuInflater().inflate(R.menu.delete,popup.getMenu());

                if(type.equals("2")){
                    popup.getMenu().findItem(R.id.deletepost).setVisible(false);
                    popup.getMenu().findItem(R.id.deletepost).setEnabled(false);
                }

                popup.getMenu().findItem(R.id.edit).setVisible(false);
                popup.getMenu().findItem(R.id.edit).setEnabled(false);

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        if(item.getItemId() == R.id.deletepost){

                            String path = "Classes/"+SchoolId+"/class/"+CurrentClass+"/Section/"+Section.get(position)+"/";
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
        return Section.size();
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
