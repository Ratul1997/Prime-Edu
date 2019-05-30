
package com.example.rat.primeedu.Teacher;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.rat.primeedu.AuthorityHomeActivity.AuthorityMainActivity;
import com.example.rat.primeedu.AuthorityHomeActivity.AuthorityNotice;
import com.example.rat.primeedu.Class.SchoolDetails;
import com.example.rat.primeedu.Class.TeacherDetails;
import com.example.rat.primeedu.Class.ZoomAbleImageActivity;
import com.example.rat.primeedu.Login.LoginActivity;
import com.example.rat.primeedu.R;
import com.example.rat.primeedu.Student.StudentActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

public class TeacherActivity extends AppCompatActivity implements View.OnClickListener{

    private ActionBar toolbar;
    private String TeacherId,Code;
    ProgressDialog dialog;
    TeacherDetails teacherDetails;
    TextView nameOfTeacher;
    SchoolDetails schoolDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_teacher);

        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.actionbar));

        getIntentData();
        sharedPreferrence();
        init();
        showLoading();
    }

    private void sharedPreferrence() {
        SharedPreferences sharedPreferences = getSharedPreferences("Users", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();


        editor.putBoolean("isLogIn",true);
        editor.putString("UserId",TeacherId);
        editor.putString("code",Code);
        editor.putString("UserType", String.valueOf(2));

        editor.commit();
    }

    private void showLoading() {

        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Please Wait ...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();
        dialog.show();

        fetchDatas();
    }

    private void fetchDatas() {
        DatabaseReference data = FirebaseDatabase.getInstance().getReference("Users/Teachers/"+TeacherId+"/Details/");

        data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() == null){
                    dialog.dismiss();
                    Toast.makeText(TeacherActivity.this, "Something wong", Toast.LENGTH_SHORT).show();
                }
                else {

                    teacherDetails = dataSnapshot.getValue(TeacherDetails.class);


                    nameOfTeacher.setText(teacherDetails.getTeachername().toString());
                    System.out.println(teacherDetails.getTeachername().toString());

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Schools/"+teacherDetails.getTeachercurrentschool()+"/Information/");
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            dialog.dismiss();
                            schoolDetails = dataSnapshot.getValue(SchoolDetails.class);
                            setData();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            dialog.dismiss();
                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                dialog.dismiss();
            }
        });
    }

    private void setData() {
        nameOfTeacher.setText(teacherDetails.getTeachername());

        ImageView profilePic = findViewById(R.id.profilePic);
        Glide.with(getApplicationContext())
                .load(teacherDetails.getTeacherimage())
                .into(profilePic);



        String token = FirebaseInstanceId.getInstance().getToken();
        FirebaseMessaging.getInstance().subscribeToTopic(teacherDetails.getTeachercurrentschool());
        updateToken(token);
    }
    private void updateToken(String token) {
        DatabaseReference Reference = FirebaseDatabase.getInstance().getReference("Users/Teachers/"+TeacherId+"/");
        Reference.child("token").setValue(token);
    }

    private void getIntentData(){
        TeacherId = getIntent().getStringExtra("Id");
        Code = getIntent().getStringExtra("code").toString();
        System.out.println(Code);
    }

    private void init() {
        findViewById(R.id.viewClass).setOnClickListener(this);
        nameOfTeacher = (TextView)findViewById(R.id.nameOfTeacher);
        findViewById(R.id.options).setOnClickListener(this);
        findViewById(R.id.viewNotice).setOnClickListener(this);

        TextView idd = findViewById(R.id.idOfTeacher);
        idd.setText(Code);

        findViewById(R.id.options).setOnClickListener(this);
        findViewById(R.id.about).setOnClickListener(this);

        findViewById(R.id.noticeboard).setOnClickListener(this);
        findViewById(R.id.profilePic).setOnClickListener(this);
        findViewById(R.id.viewPayments).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.viewClass){
            Intent intent = new Intent(TeacherActivity.this,TeacherClassActivity.class);
            intent.putExtra("Id",TeacherId);
            intent.putExtra("SchoolId",teacherDetails.getTeachercurrentschool());
            System.out.println(schoolDetails.getSchoolname());
            intent.putExtra("SchoolName",schoolDetails.getSchoolname());
            startActivity(intent);
        }
        if(v.getId() == R.id.options){
            showPopup(v);
        }
        if(v.getId() == R.id.viewNotice){
            Intent intent = new Intent(TeacherActivity.this,TeacherPost.class);
            intent.putExtra("Id",TeacherId);
            intent.putExtra("SchoolId",teacherDetails.getTeachercurrentschool());
            intent.putExtra("SchoolName",schoolDetails.getSchoolname());
            intent.putExtra("TeacherName",teacherDetails.getTeachername());
            startActivity(intent);
        }
        if(v.getId() == R.id.noticeboard){
            Intent intent = new Intent(TeacherActivity.this,AuthorityNotice.class);
            intent.putExtra("SchoolName",schoolDetails.getSchoolname());
            intent.putExtra("SchoolId",teacherDetails.getTeachercurrentschool());
            intent.putExtra("type","2");
            startActivity(intent);
        }
        if(v.getId() == R.id.about){
            finish();
            Intent intent = new Intent(TeacherActivity.this,TeacherProfile.class);
            intent.putExtra("details",teacherDetails);
            intent.putExtra("school",schoolDetails);
            intent.putExtra("id",TeacherId);
            intent.putExtra("code",Code);
            startActivity(intent);
        }
        if(v.getId() == R.id.profilePic){
            Intent intent = new Intent(this, ZoomAbleImageActivity.class);
            intent.putExtra("url",teacherDetails.getTeacherimage());
            startActivity(intent);
        }
        if(v.getId() == R.id.viewPayments){

            openDialogS("It will be added on further updated.");
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
                        finish();

                        FirebaseMessaging.getInstance().unsubscribeFromTopic(teacherDetails.getTeachercurrentschool());
                        updateToken("");
                        SharedPreferences sharedPreferences = getSharedPreferences("Users", 0);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("isLogIn",false);
                        editor.putString("UserId","");
                        editor.putString("UserType", "");
                        editor.putString("code","");
                        editor.commit();

                        Intent intent = new Intent(TeacherActivity.this, LoginActivity.class);
                        startActivity(intent);
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
}
