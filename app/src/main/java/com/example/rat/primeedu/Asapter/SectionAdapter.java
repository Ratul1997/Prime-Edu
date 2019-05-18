package com.example.rat.primeedu.Asapter;

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

import com.example.rat.primeedu.Asapter.ClassNamesAdapter;
import com.example.rat.primeedu.R;
import com.example.rat.primeedu.Teacher.GiveMarksToStudent;
import com.example.rat.primeedu.Teacher.InsideSectionActivity;
import com.example.rat.primeedu.Teacher.TakeAttendenceActivity;
import com.example.rat.primeedu.Teacher.ViewStudentActivity;

import java.lang.reflect.Method;
import java.util.List;

public class SectionAdapter extends RecyclerView.Adapter<ClassNamesAdapter.ViewHolder> {

    List<String>Section;
    List<Integer>Student;
    Context context;
    String CurrentClass;

    public SectionAdapter(List<String> section, List<Integer> student, Context context, String currentClass) {
        Section = section;
        Student = student;
        this.context = context;
        CurrentClass = currentClass;
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
                context.startActivity(intent);
            }
        });

        ((ClassNamesAdapter.ViewHolder) holder).lin.setOnLongClickListener(new View.OnLongClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public boolean onLongClick(View v) {

                PopupMenu popup = new PopupMenu(v.getContext(), v, Gravity.RIGHT);
                try {
                    Method method = popup.getMenu().getClass().getDeclaredMethod("setOptionalIconsVisible", boolean.class);
                    method.setAccessible(true);
                    method.invoke(popup.getMenu(), true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                final int p = position;

                popup.getMenuInflater().inflate(R.menu.classactivityselection,popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()){
                            case R.id.Vmarks:
                                viewMarks(p);
                                break;
                            case R.id.Gmarks:
                                giveMarks(p);
                                break;
                            case R.id.takeAttendence:
                                takeAttendence(p);
                                break;
                        }


                        Toast.makeText(context, "ASAS", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });

                popup.show();
                return true;
            }
        });
    }

    private void takeAttendence(int p){
        Intent intent = new Intent(context, TakeAttendenceActivity.class);
        System.out.println(Student.get(p));
        intent.putExtra("stdNo",Integer.toString(Student.get(p)));
        intent.putExtra("sectionNo",Section.get(p));
        intent.putExtra("classNo",CurrentClass);
        context.startActivity(intent);
    }
    private void viewMarks(int p){
        Intent intent = new Intent(context, ViewStudentActivity.class);
        System.out.println(Student.get(p));
        intent.putExtra("stdNo",Integer.toString(Student.get(p)));
        intent.putExtra("sectionNo",Section.get(p));
        intent.putExtra("clsname",CurrentClass);
        context.startActivity(intent);
    }
    private void giveMarks(int p){
        Intent intent = new Intent(context, GiveMarksToStudent.class);
        System.out.println(Student.get(p));
        intent.putExtra("section",Section.get(p));
        intent.putExtra("class",CurrentClass);
        intent.putExtra("id","");
        context.startActivity(intent);
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
