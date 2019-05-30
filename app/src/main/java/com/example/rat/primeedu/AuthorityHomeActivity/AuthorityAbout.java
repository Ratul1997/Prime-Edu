package com.example.rat.primeedu.AuthorityHomeActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rat.primeedu.Class.SchoolDetails;
import com.example.rat.primeedu.Class.TeacherDetails;
import com.example.rat.primeedu.R;
import com.example.rat.primeedu.Teacher.TeacherActivity;
import com.example.rat.primeedu.Teacher.TeacherProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AuthorityAbout extends AppCompatActivity implements View.OnClickListener {

    EditText tname,temail,tadd,tNo,tSub;
    TeacherDetails teacherDetails;
    SchoolDetails schoolDetails;
    String TeacherCode;
    String SchoolCode;
    String Code;
    ProgressDialog dialog;
    TextView names;


    CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authority_about);

        getIntentData();
        init();
    }


    private void init() {
        names = findViewById(R.id.school_name);
        names.setText(schoolDetails.getSchoolname());

        TextView id = findViewById(R.id.ids);
        id.setText(Code);

        tname = findViewById(R.id.tName);
        temail = findViewById(R.id.tEmail);
        tadd = findViewById(R.id.tAdd);
        tNo = findViewById(R.id.tNo);

        tname.setText(schoolDetails.getSchoolname());
        temail.setText(schoolDetails.getSchoolemail());
        tadd.setText(schoolDetails.getSchooladdress());
        tNo.setText(schoolDetails.getSchoolcontactnumber());

        checkBox = findViewById(R.id.checkEnable);
        checkBox.setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
    }

    private void getIntentData() {
        schoolDetails = (SchoolDetails)getIntent().getSerializableExtra("school");
        Code = getIntent().getStringExtra("code").toString();
        SchoolCode = getIntent().getStringExtra("id").toString();
    }

    private void setData(boolean flag){
        tname.setEnabled(flag);
        temail.setEnabled(flag);
        tadd.setEnabled(flag);
        tNo.setEnabled(flag);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.back){
            finish();
            Intent intent = new Intent(AuthorityAbout.this, AuthorityMainActivity.class);
            intent.putExtra("code",Code);
            intent.putExtra("Id",SchoolCode);
            startActivity(intent);
        }
        if(v.getId() == R.id.checkEnable){

            if(checkBox.isChecked()){
                setData(true);
            }
            else{
                setData(false);
                openDialogBox();
            }
        }
    }
    private void openDialogBox() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure, You wanted to change your data!");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        updateData();
//                        dialog.show();
                    }
                });

        alertDialogBuilder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
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

    private void updateData() {
        showLoading();

        String SchoolName = tname.getText().toString();
        String SchoolEmail = temail.getText().toString();
        String SchoolAddress = tadd.getText().toString();
        String SchoolContact = tNo.getText().toString();
        String SchoolTeacher = schoolDetails.getSchoolteachernumbers();
        String SchoolStaff = schoolDetails.getSchoolstaffnumbers();
        String SchoolStudent = schoolDetails.getSchoolstudentnumber();

        final SchoolDetails school = new SchoolDetails(SchoolName,SchoolEmail,
                SchoolStudent,SchoolStaff,SchoolTeacher,SchoolAddress,SchoolContact);


        String path = "Schools/"+SchoolCode+"/Information/";

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(path);
        databaseReference.setValue(school)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            dialog.dismiss();
                            names.setText(school.getSchoolname());
                            openDialogBoxes("Data hase been saved");
                        }
                        else{
                            dialog.dismiss();
                            openDialogBoxes("Something is wrong.");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                openDialogBoxes("Something is wrong.");
            }
        });


    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK){
            finish();
            Intent intent = new Intent(AuthorityAbout.this, AuthorityMainActivity.class);
            intent.putExtra("code",Code);
            intent.putExtra("Id",SchoolCode);
            startActivity(intent);
            return true;
        }
        return false;
    }
    private void openDialogBoxes(String s) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setMessage(s);
        alertDialogBuilder.setPositiveButton("ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
