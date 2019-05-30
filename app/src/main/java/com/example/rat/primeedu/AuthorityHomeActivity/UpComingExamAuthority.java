package com.example.rat.primeedu.AuthorityHomeActivity;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rat.primeedu.Asapter.ExamAdapter;
import com.example.rat.primeedu.Asapter.NoticeAdapter;
import com.example.rat.primeedu.Class.ExamDetails;
import com.example.rat.primeedu.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UpComingExamAuthority extends AppCompatActivity implements View.OnClickListener{

    ProgressDialog dialog;
    RecyclerView recyclerview;
    String SchoolId,Schoolname;
    List<Pair<String,ExamDetails> >exam = new ArrayList<>();
    ExamAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_coming_exam_authority);

        getIntentData();
        init();
        getDatas();
    }
    private void getIntentData() {
        SchoolId = getIntent().getStringExtra("SchoolId").toString();
        Schoolname = getIntent().getStringExtra("SchoolName").toString();

        System.out.println(SchoolId);
    }


    private void getDatas() {
        showLoading();
        String path = "Exams/"+SchoolId+"/";
        System.out.println(path);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(path);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.getValue()==null){
                    dialog.dismiss();
                    Toast.makeText(UpComingExamAuthority.this, "No Exam Available.", Toast.LENGTH_SHORT).show();
                }else{
                    exam.clear();
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        System.out.println(snapshot);
                        ExamDetails ex = snapshot.getValue(ExamDetails.class);
                        Pair<String, ExamDetails> p = new Pair<>(snapshot.getKey(),ex);
                        exam.add(p);
                    }
                    adapter.notifyDataSetChanged();
                    Collections.reverse(exam);
                    dialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                dialog.dismiss();
            }
        });
    }

    private void init() {
        recyclerview = (RecyclerView)findViewById(R.id.recyclerview);
        recyclerview.setHasFixedSize(true);

        findViewById(R.id.back).setOnClickListener(this);
        TextView scl = (TextView)findViewById(R.id.school_name);
        scl.setText(Schoolname);
        adapter = new ExamAdapter(exam,this);
        LinearLayoutManager mManager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(mManager);
        recyclerview.setAdapter(adapter);
    }

    private void showLoading() {

        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Please Wait ...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();
        dialog.show();

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.back){
            finish();
        }
    }
}
