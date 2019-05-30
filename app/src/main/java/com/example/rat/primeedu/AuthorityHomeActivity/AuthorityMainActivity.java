package com.example.rat.primeedu.AuthorityHomeActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rat.primeedu.Class.SchoolDetails;
import com.example.rat.primeedu.Class.Student;
import com.example.rat.primeedu.Login.LoginActivity;
import com.example.rat.primeedu.R;
import com.example.rat.primeedu.Student.StudentActivity;
import com.example.rat.primeedu.Teacher.TeacherActivity;
import com.example.rat.primeedu.Teacher.TeacherProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Method;

public class AuthorityMainActivity extends AppCompatActivity implements View.OnClickListener {

    private String SchoolId,Code;
    ProgressDialog dialog;
    SchoolDetails school ;
    TextView school_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authority_main);

        getIntentData();
        sharedPreferrence();
        showLoading();
        init();
    }

    private void sharedPreferrence() {
        SharedPreferences sharedPreferences = getSharedPreferences("Users", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();


        editor.putBoolean("isLogIn",true);
        editor.putString("UserId",SchoolId);
        editor.putString("code",Code);
        editor.putString("UserType", String.valueOf(3));

        editor.commit();
    }

    private void showLoading() {

        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Please Wait ...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();


        fetchingData();
    }

    private void fetchingData(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Schools/"+SchoolId+"/Information/");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dialog.dismiss();
                school = dataSnapshot.getValue(SchoolDetails.class);
                setData();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        })
        ;
    }

    private void setData() {
        school_name.setText(school.getSchoolname());

        TextView code = findViewById(R.id.idOfSchool);
        code.setText(Code);
    }

    private void getIntentData() {

        school_name = (TextView)findViewById(R.id.school_name);
        SchoolId = getIntent().getStringExtra("Id");
        Code = getIntent().getStringExtra("code");
    }

    private void init() {
        findViewById(R.id.viewClass).setOnClickListener(this);
        findViewById(R.id.viewTeacher).setOnClickListener(this);
        findViewById(R.id.viewNotice).setOnClickListener(this);
        findViewById(R.id.viewPayments).setOnClickListener(this);
        findViewById(R.id.viewExams).setOnClickListener(this);
        findViewById(R.id.options).setOnClickListener(this);
        findViewById(R.id.about).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.viewClass){
            Intent intent = new Intent(AuthorityMainActivity.this,AuthorityCreateClass.class);
            intent.putExtra("SchoolName",school_name.getText().toString());
            intent.putExtra("SchoolId",SchoolId);
            startActivity(intent);
        }
        if(v.getId() == R.id.viewTeacher){
            Intent intent = new Intent(AuthorityMainActivity.this,TeachersInSchool.class);
            intent.putExtra("SchoolName",school_name.getText().toString());
            intent.putExtra("SchoolId",SchoolId);
            startActivity(intent);
        }
        if(v.getId() == R.id.viewNotice){

            Intent intent = new Intent(AuthorityMainActivity.this,AuthorityNotice.class);
            intent.putExtra("SchoolName",school_name.getText().toString());
            intent.putExtra("SchoolId",SchoolId);
            intent.putExtra("type","3");
            startActivity(intent);
        }
        if(v.getId() == R.id.options){
            showPopup(v);
        }
        if(v.getId() == R.id.viewExams){
            Intent intent = new Intent(AuthorityMainActivity.this,UpComingExamAuthority.class);
            intent.putExtra("SchoolName",school_name.getText().toString());
            intent.putExtra("SchoolId",SchoolId);

            startActivity(intent);
        }
        if(v.getId() == R.id.about){
            finish();
            Intent intent = new Intent(AuthorityMainActivity.this,AuthorityAbout.class);
            intent.putExtra("school",school);
            intent.putExtra("id",SchoolId);
            intent.putExtra("code",Code);
            startActivity(intent);
        }
        if(v.getId() == R.id.viewPayments){

            openDialogS("It will be added on further updated.");
        }
    }



    private void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this,v, Gravity.RIGHT);

        popup.getMenuInflater().inflate(R.menu.logout,popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()){
                    case R.id.logout:
                        openDialogBox();
                        break;
                    case R.id.update:
                        updateDatabase();
                        break;
                }
                return true;
            }
        });

        popup.show();
    }

    private void updateDatabase() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setMessage("Are You Sure? You want to remove all data?");
        alertDialogBuilder.setPositiveButton("ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        deletClasses();
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

    private void deletTeacherClasses() {
        String path= "Schools/"+SchoolId+"/Teachers";
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(path);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() == null){
                    dialog.dismiss();
                }
                else{
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        String key = snapshot.getValue(String.class);

                        String url = "Users/Teachers/"+key+"/Class";
                        System.out.println(url);

                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(url);
                        databaseReference.removeValue();
                    }
                    dialog.dismiss();
                    openDialogS("Successfully removed data. Thank you to stay with us. Best of luck with your new databases.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void deletClasses() {
        dialog.show();
        String path = "Classes/"+SchoolId+"/class/";
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(path);

        ref.removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            String url = "Exams/"+SchoolId;
                            DatabaseReference reff = FirebaseDatabase.getInstance().getReference(url);
                            reff.removeValue();

                            String url2 = "Schools/"+SchoolId+"/Notice/";
                            DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference(url2);
                            ref2.removeValue();
                            deletTeacherClasses();

                        }
                        else{
                            dialog.dismiss();
                            openDialogS("Something is wrong.");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        openDialogS("Something is wrong");
                    }
                });
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
                        finish();
                        SharedPreferences sharedPreferences = getSharedPreferences("Users", 0);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("isLogIn",false);
                        editor.putString("UserId","");
                        editor.putString("UserType", "");
                        editor.putString("code","");
                        editor.commit();

                        Intent intent = new Intent(AuthorityMainActivity.this, LoginActivity.class);
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

}
