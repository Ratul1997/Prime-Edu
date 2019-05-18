package com.example.rat.primeedu.Teacher;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.example.rat.primeedu.Student.AboutStudentFragment;
import com.example.rat.primeedu.R;
import com.example.rat.primeedu.Student.StudentAttendenceFragment;
import com.example.rat.primeedu.Student.StudentMarkDragment;
import com.example.rat.primeedu.Asapter.ViewPagerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StudentProfileByTeacher extends AppCompatActivity {

    TabLayout tablayout;
    ViewPager viewPager;
    private String CurrentSection,Currentclass,CurrentStudent;
    private String url = "SchoolName/Class/";
    TextView nameOfStudent;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile_by_teacher);
        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.colorAccent));

        getIntentDatas();
        showLoading();
        getFireBaseData();
        createTabbar();
        init();
    }
    private void showLoading() {

        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.loadingprogressbar);
        dialog.show();
    }

    private void getFireBaseData() {
        String path = url+Currentclass+"/Section/"+CurrentSection+"/"+CurrentStudent+"/Details/studentsName";
        System.out.println("asasas"+path);
        FirebaseDatabase  firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference(path);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dialog.dismiss();
                nameOfStudent.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getIntentDatas() {

        Currentclass = getIntent().getStringExtra("class").toString();
        CurrentSection = getIntent().getStringExtra("section").toString();
        CurrentStudent = getIntent().getStringExtra("id").toString();
    }

    private void init() {
        nameOfStudent = (TextView)findViewById(R.id.nameOfStudent);
    }

    private void createTabbar() {
        tablayout = (TabLayout)findViewById(R.id.tablayout);
        viewPager = (ViewPager)findViewById(R.id.viewPager);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.AddFragment(new StudentAttendenceFragment(CurrentStudent,Currentclass,CurrentSection),"Attendence");
        adapter.AddFragment(new StudentMarkDragment(CurrentStudent,Currentclass,CurrentSection),"Marks");
        adapter.AddFragment(new AboutStudentFragment(CurrentStudent,Currentclass,CurrentSection),"About");

        viewPager.setAdapter(adapter);
        tablayout.setupWithViewPager(viewPager);
    }
}
