package com.primeedu.rat.primeedu.Student;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import com.primeedu.rat.primeedu.Asapter.AnnounceMentAdapter;
import com.primeedu.rat.primeedu.Asapter.HomeWorkAdapter;
import com.primeedu.rat.primeedu.Class.AnnouncementWithClass;
import com.primeedu.rat.primeedu.Class.TeachersPostClass;
import com.primeedu.rat.primeedu.R;
import com.primeedu.rat.primeedu.Teacher.TeacherPost;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TeacherNotice extends AppCompatActivity implements View.OnClickListener {

    String Currentclass,SchoolCode;
    ProgressDialog dialog;

    List<Pair<String,AnnouncementWithClass>> Homework = new ArrayList<>();
    List<Pair<String,AnnouncementWithClass>> Announcement = new ArrayList<>();

    RecyclerView recyclerView2,recyclerView1;
    AnnounceMentAdapter adapter1;
    HomeWorkAdapter adapter2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_notice);
        getIntentData();
        init();
        HomeWorks();
    }

    private void HomeWorks(){
        showLoading();
        String path = "Classes/"+SchoolCode+"/class/"+Currentclass+"/AnnounceMents/HomeWork/";
        System.out.println(path);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(path);
        Query query = ref;
        query.limitToLast(35).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                    TeachersPostClass posts = snapshot.getValue(TeachersPostClass.class);
                    AnnouncementWithClass withClass = new AnnouncementWithClass(posts.getMsg(),posts.getTeachername());
                    Homework.add(new Pair<String, AnnouncementWithClass>(snapshot.getKey(),withClass));
                }
                Collections.reverse(Homework);
                adapter2.notifyDataSetChanged();
                Announcement();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void Announcement(){
        String path = "Classes/"+SchoolCode+"/class/"+Currentclass+"/AnnounceMents/AnnounceMent/";
        System.out.println(path);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(path);
        Query query = ref;
        query.limitToLast(35).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                    TeachersPostClass posts = snapshot.getValue(TeachersPostClass.class);
                    AnnouncementWithClass withClass = new AnnouncementWithClass(posts.getMsg(),posts.getTeachername());
                    Announcement.add(new Pair<String, AnnouncementWithClass>(snapshot.getKey(),withClass));
                }
                Collections.reverse(Announcement);
                adapter1.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void init() {
        TextView classNo = findViewById(R.id.classNo);
        classNo.setText(Currentclass);

        findViewById(R.id.back).setOnClickListener(this);

        recyclerView1 = findViewById(R.id.recyclerView1);
        recyclerView2 = findViewById(R.id.recyclerView2);

        adapter1 = new AnnounceMentAdapter(Announcement,TeacherNotice.this,"1",SchoolCode);
        final LinearLayoutManager mLayoutManager= new LinearLayoutManager(TeacherNotice.this, LinearLayoutManager.VERTICAL, true);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerView1.setLayoutManager(mLayoutManager);
        recyclerView1.setAdapter(adapter1);



        adapter2 = new HomeWorkAdapter(Homework,TeacherNotice.this,"1",SchoolCode);
        final LinearLayoutManager mLayoutManagers= new LinearLayoutManager(TeacherNotice.this, LinearLayoutManager.VERTICAL, true);
        mLayoutManagers.setReverseLayout(true);
        mLayoutManagers.setStackFromEnd(true);
        recyclerView2.setLayoutManager(mLayoutManagers);
        recyclerView2.setAdapter(adapter2);

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


    private void getIntentData() {
        Currentclass = getIntent().getStringExtra("class");
        SchoolCode = getIntent().getStringExtra("school");

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.back){
            finish();
        }
    }
}
