package com.example.rat.primeedu.Student;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rat.primeedu.Asapter.ViewPagerAdapter;
import com.example.rat.primeedu.AuthorityHomeActivity.AuthorityMainActivity;
import com.example.rat.primeedu.AuthorityHomeActivity.AuthorityNotice;
import com.example.rat.primeedu.AuthorityHomeActivity.TeachersInSchool;
import com.example.rat.primeedu.Class.SchoolDetails;
import com.example.rat.primeedu.Class.Student;
import com.example.rat.primeedu.Class.ZoomAbleImageActivity;
import com.example.rat.primeedu.Login.LoginActivity;
import com.example.rat.primeedu.R;
import com.example.rat.primeedu.SharedPred;
import com.example.rat.primeedu.Teacher.TeacherActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;


import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentActivity extends AppCompatActivity implements View.OnClickListener {

    TabLayout tablayout;
    ViewPager viewPager;
    private String CurrentSection,Currentclass,CurrentStudent;
    TextView nameOfStudent;
    ProgressDialog dialog;
    CircleImageView profilePic;
    private String CurrentStudUniqueId,Code;
    private Student student;
    SchoolDetails schoolDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        getIntentData();
        sharedPreferrence();
        showLoading();
        init();
    }

    private void login() {

        FirebaseMessaging.getInstance().subscribeToTopic(student.getSchoolcode());
        FirebaseMessaging.getInstance().subscribeToTopic(student.getSchoolcode()+student.getCurrentclass());
    }

    private void sharedPreferrence() {

        SharedPreferences sharedPreferences = getSharedPreferences("Users", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();


        editor.putBoolean("isLogIn",true);
        editor.putString("UserId",CurrentStudUniqueId);
        editor.putString("code",Code);
        editor.putString("UserType", String.valueOf(1));

        editor.commit();
    }

    private void getIntentData() {
        System.out.println(SharedPred.getmInstance(this).getToken());
        System.out.println("Okey");
        System.out.println(FirebaseInstanceId.getInstance().getToken());
        CurrentStudUniqueId = getIntent().getStringExtra("Id").toString();
        Code = getIntent().getStringExtra("code").toString();
    }

    private void showLoading() {

        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Please Wait ...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();
        dialog.show();

        fetchingData();
    }

    private void fetchingData(){


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users/Student/"+CurrentStudUniqueId+"/");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                student = dataSnapshot.getValue(Student.class);

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Schools/"+student.getSchoolcode()+"/Information/");
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dialog.dismiss();
                        setNameAndImage(student);

                        String token = FirebaseInstanceId.getInstance().getToken();
                        updateToken(token);
                        login();
                        schoolDetails = dataSnapshot.getValue(SchoolDetails.class);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        dialog.dismiss();
                    }
                });

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                dialog.dismiss();
            }

        })
        ;
    }

    private void updateToken(String token) {
        DatabaseReference Reference = FirebaseDatabase.getInstance().getReference("Users/Student/"+CurrentStudUniqueId+"/");
        Reference.child("token").setValue(token);
    }

    private void setNameAndImage(Student student) {
        nameOfStudent.setText(student.getStudentDetails().getStudentfname()+" "+student.getStudentDetails().getStudenlname());
        Glide.with(getApplicationContext())
                .load(student.getStudentDetails().getStudentprofilepicture())
                .into(profilePic);

        System.out.println("ssss"+Code);
        TextView idOfStudent = findViewById(R.id.idOfStudent);
        idOfStudent.setText(Code);
    }

    private void init() {
        findViewById(R.id.options).setOnClickListener(this);

        nameOfStudent = (TextView)findViewById(R.id.nameOfStudent);
        profilePic = findViewById(R.id.profilePic);
        profilePic.setOnClickListener(this);

        findViewById(R.id.att).setOnClickListener(this);
        findViewById(R.id.mrks).setOnClickListener(this);
        findViewById(R.id.notice).setOnClickListener(this);
        findViewById(R.id.teachers).setOnClickListener(this);
        findViewById(R.id.payments).setOnClickListener(this);
        findViewById(R.id.student).setOnClickListener(this);
        findViewById(R.id.noticeboard).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.att){
            Intent intent = new Intent(StudentActivity.this,AttendenceActivity.class);
            intent.putExtra("Id",CurrentStudUniqueId);
            intent.putExtra("school",student.getSchoolcode());
            intent.putExtra("class",student.getCurrentclass());
            intent.putExtra("roll",student.getCurrentid());
            intent.putExtra("section",student.getCurrentsection());

            intent.putExtra("SchoolName",schoolDetails.getSchoolname());
            intent.putExtra("img",student.getStudentDetails().getStudentprofilepicture());
            intent.putExtra("name",nameOfStudent.getText().toString());
            startActivity(intent);
        }
        if(v.getId() == R.id.notice){
            Intent intent = new Intent(StudentActivity.this,TeacherNotice.class);
            intent.putExtra("school",student.getSchoolcode());
            intent.putExtra("class",student.getCurrentclass());
            startActivity(intent);

        }
        if(v.getId() == R.id.mrks){
            Intent intent = new Intent(StudentActivity.this,StudentMarkActivity.class);
            intent.putExtra("Id",CurrentStudUniqueId);
            intent.putExtra("school",student.getSchoolcode());
            intent.putExtra("class",student.getCurrentclass());
            intent.putExtra("roll",student.getCurrentid());
            intent.putExtra("section",student.getCurrentsection());
            intent.putExtra("SchoolName",schoolDetails.getSchoolname());
            intent.putExtra("img",student.getStudentDetails().getStudentprofilepicture());
            intent.putExtra("name",nameOfStudent.getText().toString());
            startActivity(intent);
        }
        if(v.getId() == R.id.teachers){
            Intent intent = new Intent(StudentActivity.this,TeachersInSchool.class);
            intent.putExtra("SchoolName",schoolDetails.getSchoolname());
            intent.putExtra("SchoolId",student.getSchoolcode());
            startActivity(intent);
        }
        if(v.getId() == R.id.payments){

            openDialogS("It will be added on further updated.");
        }if(v.getId() == R.id.student){
            finish();
            Intent intent = new Intent(StudentActivity.this,AboutStudent.class);
            intent.putExtra("Id",CurrentStudUniqueId);
            intent.putExtra("school",student.getSchoolcode());
            intent.putExtra("class",student.getCurrentclass());
            intent.putExtra("roll",student.getCurrentid());
            intent.putExtra("section",student.getCurrentsection());
            intent.putExtra("SchoolName",schoolDetails.getSchoolname());
            intent.putExtra("img",student.getStudentDetails().getStudentprofilepicture());
            intent.putExtra("name",nameOfStudent.getText().toString());
            intent.putExtra("code",Code);

            startActivity(intent);

        }
        if(v.getId() == R.id.options){
            showPopup(v);
        }
        if(v.getId() == R.id.profilePic){
            Intent intent = new Intent(this, ZoomAbleImageActivity.class);
            intent.putExtra("url",student.getStudentDetails().getStudentprofilepicture());
            startActivity(intent);
        }
        if(v.getId() == R.id.noticeboard){
            Intent intent = new Intent(StudentActivity.this,AuthorityNotice.class);
            intent.putExtra("SchoolName",schoolDetails.getSchoolname());
            intent.putExtra("SchoolId",student.getSchoolcode());
            intent.putExtra("type","2");
            startActivity(intent);
        }

    }
    private void openDialogS(String msg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(msg);
        alertDialogBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    private void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this,v, Gravity.RIGHT);

        popup.getMenuInflater().inflate(R.menu.logout,popup.getMenu());

        popup.getMenu().findItem(R.id.update).setVisible(false);
        popup.getMenu().findItem(R.id.update).setEnabled(false);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()){
                    case R.id.logout:
                       openDialogBox();
                        break;
                }
                return true;
            }
        });

        popup.show();
    }
    private void openDialogBox() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setMessage("Are You Sure?");
        alertDialogBuilder.setPositiveButton("ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        logouts();
                    }
                });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void  logouts(){

        FirebaseMessaging.getInstance().unsubscribeFromTopic(student.getSchoolcode());
        FirebaseMessaging.getInstance().unsubscribeFromTopic(student.getSchoolcode()+student.getCurrentclass());
        finish();
        updateToken("");
        SharedPreferences sharedPreferences = getSharedPreferences("Users", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLogIn",false);
        editor.putString("UserId","");
        editor.putString("code","");
        editor.putString("UserType", "");
        editor.commit();

        Intent intent = new Intent(StudentActivity.this, LoginActivity.class);
        startActivity(intent);
    }

}
