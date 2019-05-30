package com.example.rat.primeedu.Student;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rat.primeedu.Asapter.ViewPagerAdapter;
import com.example.rat.primeedu.Class.FatherDetails;
import com.example.rat.primeedu.Class.GurdianDetails;
import com.example.rat.primeedu.Class.MotherDetails;
import com.example.rat.primeedu.Class.SchoolDetails;
import com.example.rat.primeedu.Class.Student;
import com.example.rat.primeedu.Class.StudentDetails;
import com.example.rat.primeedu.R;
import com.example.rat.primeedu.Teacher.TeacherActivity;
import com.example.rat.primeedu.Teacher.TeacherProfile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import de.hdodenhof.circleimageview.CircleImageView;

public class AboutStudent extends AppCompatActivity implements View.OnClickListener,AboutStudentFragment.OnFragmentInteractionListener{

    TabLayout tablayout;
    ViewPager viewPager;
    ProgressDialog dialog;
    Student student;
    private TextView nameOfStudent;
    String Schoolname,Code;
    CircleImageView profilePic;
    private String CurrentSection,Currentclass,CurrentStudent,SchoolCode,StudentCodde;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_student);



        getIntentData();
        fetchingData();
    }
    private void getIntentData() {

        StudentCodde = getIntent().getStringExtra("code").toString();
        Code = getIntent().getStringExtra("Id").toString();
        Schoolname = getIntent().getStringExtra("SchoolName").toString();
        Currentclass = getIntent().getStringExtra("class");
        CurrentSection = getIntent().getStringExtra("section");
        CurrentStudent = getIntent().getStringExtra("roll");
        SchoolCode = getIntent().getStringExtra("school");
        String img = getIntent().getStringExtra("img");
        String name = getIntent().getStringExtra("name");

        nameOfStudent = (TextView)findViewById(R.id.nameOfStudent);
        profilePic = findViewById(R.id.profilePic);

        nameOfStudent.setText(name);
        Glide.with(getApplicationContext())
                .load(img)
                .into(profilePic);

        TextView scl = findViewById(R.id.school_name);
        scl.setText(Schoolname);

        findViewById(R.id.back).setOnClickListener(this);

        findViewById(R.id.back).setOnClickListener(this);
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
    private void fetchingData(){

        showLoading();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users/Student/"+Code+"/");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dialog.dismiss();
                student = dataSnapshot.getValue(Student.class);
                createTabbar();
                }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                dialog.dismiss();
            }

        })
        ;
    }


    private void createTabbar() {

        tablayout = (TabLayout)findViewById(R.id.tablayout);
        viewPager = (ViewPager)findViewById(R.id.viewPager);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.AddFragment(new AboutStudentFragment(SchoolCode,student,"1"),"About");

        viewPager.setAdapter(adapter);
        tablayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.back){
            bacck();
        }
    }

    public void bacck(){
        finish();
        Intent intent = new Intent(AboutStudent.this, StudentActivity.class);
        intent.putExtra("code",StudentCodde);
        intent.putExtra("Id",Code);
        startActivity(intent);
    }

    @Override
    public void onFragmentInteraction(String fNames, String lNames) {
        nameOfStudent.setText(fNames+" "+lNames);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK){
            bacck();
            return true;
        }
        return false;
    }
}
