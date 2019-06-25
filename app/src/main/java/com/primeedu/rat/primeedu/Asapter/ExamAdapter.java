package com.primeedu.rat.primeedu.Asapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.primeedu.rat.primeedu.Class.ExamDetails;
import com.primeedu.rat.primeedu.Class.ZoomAbleImageActivity;
import com.primeedu.rat.primeedu.Login.LoginActivity;
import com.primeedu.rat.primeedu.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ExamAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Pair<String,ExamDetails> > exam = new ArrayList<>();
    ProgressDialog dialog;
    Context context;
    String SchoolId;

    public ExamAdapter(List<Pair<String, ExamDetails>> exam, Context context, String schoolId) {
        this.exam = exam;
        this.context = context;
        SchoolId = schoolId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.examlist, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        ViewHolder v = (ViewHolder)viewHolder;

        System.out.println(exam.get(i).second.getExamenddate());
        v.examname.setText("Exam: "+exam.get(i).second.getExamname());
        v.sdate.setText("Starting Date: "+exam.get(i).second.getExamstartdate());
        v.edate.setText("Ending Date: "+exam.get(i).second.getExamenddate());
        v.rdate.setText("Publish Date: "+exam.get(i).second.getPublishresultdate());
        v.ecode.setText("Code: "+exam.get(i).first);

        v.exams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ZoomAbleImageActivity.class);
                intent.putExtra("url",exam.get(i).second.getRoutine());
                context.startActivity(intent);
            }
        });

        v.exams.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu popup = new PopupMenu(v.getContext(), v, Gravity.RIGHT);

                popup.getMenuInflater().inflate(R.menu.delete,popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        if(item.getItemId() == R.id.deletepost){

                            String path = "Exams/"+SchoolId+"/"+exam.get(i).first+"/";
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference(path);

                            ref.removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(context, "Successfully deleted.", Toast.LENGTH_SHORT).show();
                                                notifyItemRemoved(i);
                                                notifyItemRangeChanged(i, exam.size());
                                                notifyDataSetChanged();
                                            }
                                        }
                                    });
                        }
                        if(item.getItemId() == R.id.edit){
                            editExam(exam.get(i).first);
                        }

                        return true;
                    }
                });

                popup.show();

                return true;
            }
        });
    }

    private void showLoading() {

        dialog = new ProgressDialog(context);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Please Wait ...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();
        dialog.show();
    }

    private void editExam(final String Key) {
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.editexam, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        alertDialogBuilder.setView(promptsView);



        final AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.setCancelable(false);

        final EditText dates = promptsView.findViewById(R.id.publish);
        promptsView.findViewById(R.id.cancel).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                }
        );

        TextView btn = promptsView.findViewById(R.id.ok);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dates.getText().toString().equals("")){
                    Toast.makeText(context, "Empty Field", Toast.LENGTH_SHORT).show();
                }
                else{
                    alertDialog.dismiss();
                    showLoading();
                    String path = "Exams/"+SchoolId+"/"+Key+"/publishresultdate/";
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference(path);
                    ref.setValue(dates.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        dialog.dismiss();
                                        Toast.makeText(context, "Successfully changed.", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        dialog.dismiss();
                                        Toast.makeText(context, "Something is wrong.", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    dialog.dismiss();
                                    Toast.makeText(context, "Something is wrong.", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });


        alertDialog.show();
    }

    @Override
    public int getItemCount() {
        return exam.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        LinearLayout exams ;
        TextView sdate,edate,rdate,ecode,examname;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            examname = (TextView)itemView.findViewById(R.id.examname);
            exams = (LinearLayout)itemView.findViewById(R.id.exams);
            sdate = (TextView)itemView.findViewById(R.id.sdate);
            edate = (TextView)itemView.findViewById(R.id.edate);
            rdate = (TextView)itemView.findViewById(R.id.rdate);
            ecode = (TextView)itemView.findViewById(R.id.ecode);
        }
    }
}
