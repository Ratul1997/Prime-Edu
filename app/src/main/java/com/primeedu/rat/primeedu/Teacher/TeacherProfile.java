package com.primeedu.rat.primeedu.Teacher;

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
import com.primeedu.rat.primeedu.Class.SchoolDetails;
import com.primeedu.rat.primeedu.Class.TeacherDetails;
import com.primeedu.rat.primeedu.Login.LoginActivity;
import com.primeedu.rat.primeedu.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class TeacherProfile extends AppCompatActivity implements View.OnClickListener{

    EditText tname,temail,tadd,tNo,tSub;
    TeacherDetails teacherDetails;
    SchoolDetails schoolDetails;
    String TeacherCode;
    String Code;
    ProgressDialog dialog;
    TextView names;


    CheckBox checkBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_profile);

        getIntentData();
        init();

    }


    private void init() {
        TextView schoolName = findViewById(R.id.school_name);
        schoolName.setText(schoolDetails.getSchoolname());

        names = findViewById(R.id.nameOfteacher);
        names.setText(teacherDetails.getTeachername());

        TextView id = findViewById(R.id.ids);
        id.setText(Code);

        ImageView img = (ImageView)findViewById(R.id.img);
        Glide.with(getApplicationContext())
                .load(teacherDetails.getTeacherimage())
                .into(img);

        tname = findViewById(R.id.tName);
        temail = findViewById(R.id.tEmail);
        tadd = findViewById(R.id.tAdd);
        tNo = findViewById(R.id.tNo);
        tSub = findViewById(R.id.tsub);

        tname.setText(teacherDetails.getTeachername());
        temail.setText(teacherDetails.getTeacheremail());
        tadd.setText(teacherDetails.getTeacheraddress());
        tSub.setText(teacherDetails.getTeachersubject());
        tNo.setText(teacherDetails.getTeachercontactno());

        checkBox = findViewById(R.id.checkEnable);
        checkBox.setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
    }

    private void getIntentData() {
        teacherDetails = (TeacherDetails) getIntent().getSerializableExtra("details");
        schoolDetails = (SchoolDetails)getIntent().getSerializableExtra("school");
        TeacherCode = getIntent().getStringExtra("id").toString();
        Code = getIntent().getStringExtra("code").toString();
    }

    private void setData(boolean flag){
        tname.setEnabled(flag);
        temail.setEnabled(flag);
        tadd.setEnabled(flag);
        tNo.setEnabled(flag);
        tSub.setEnabled(flag);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.back){
            finish();
            Intent intent = new Intent(TeacherProfile.this, TeacherActivity.class);
            intent.putExtra("code",Code);
            intent.putExtra("Id",TeacherCode);
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

        final String name = tname.getText().toString();
        String subject = tSub.getText().toString();
        String status = teacherDetails.getTeacherstatus();
        String contact = tNo.getText().toString();
        String email = temail.getText().toString();
        String address = tadd.getText().toString();
        String image = teacherDetails.getTeacherimage();

        final TeacherDetails tD = new TeacherDetails(name,subject,status,contact,teacherDetails.getTeachercurrentschool(),
                email,address,image,"");
        System.out.println(tD.getTeachercontactno());

        String path = "Users/Teachers/"+TeacherCode+"/Details";

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(path);
        databaseReference.setValue(tD)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            dialog.dismiss();
                            names.setText(tD.getTeachername());
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
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK){
            finish();
            Intent intent = new Intent(TeacherProfile.this, TeacherActivity.class);
            intent.putExtra("code",Code);
            intent.putExtra("Id",TeacherCode);
            startActivity(intent);
            return true;
        }
        return false;
    }

}
