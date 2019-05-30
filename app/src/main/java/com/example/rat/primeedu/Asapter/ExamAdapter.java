package com.example.rat.primeedu.Asapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.rat.primeedu.Class.ExamDetails;
import com.example.rat.primeedu.Class.ZoomAbleImageActivity;
import com.example.rat.primeedu.R;

import java.util.ArrayList;
import java.util.List;

public class ExamAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Pair<String,ExamDetails> > exam = new ArrayList<>();
    Context context;

    public ExamAdapter(List<Pair<String, ExamDetails>> exam, Context context) {
        this.exam = exam;
        this.context = context;
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
