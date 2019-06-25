package com.primeedu.rat.primeedu.Login;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.primeedu.rat.primeedu.Class.SchoolDetails;
import com.primeedu.rat.primeedu.Class.UploadImage;
import com.primeedu.rat.primeedu.GMailSender;
import com.primeedu.rat.primeedu.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SignupAuthority extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;
    private EditText schoolName,schoolAddress,schoolEmail,schoolContact,schoolTeacher;
    private EditText schollStudents,schoolStaff,schoolPass,confirmPass;
    private Button schoolSignup;
    private  TextView log_in_id;
    boolean Timer;
    ProgressDialog dialog;
    int result;
    int pp1 = 0,pp2=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_authority);


        init();
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

    private void init() {
        schoolName = (EditText)findViewById(R.id.schoolName);
        schoolAddress = (EditText)findViewById(R.id.schoolAddress);
        schoolEmail = (EditText)findViewById(R.id.schoolEmail);
        schoolContact = (EditText)findViewById(R.id.schoolContact);
        schoolTeacher = (EditText)findViewById(R.id.schoolTeacher);
        schollStudents = (EditText)findViewById(R.id.schoolStudents);
        schoolStaff = (EditText)findViewById(R.id.schoolStaff);
        schoolPass = (EditText)findViewById(R.id.schoolPassword);
        confirmPass = (EditText)findViewById(R.id.confirmPass);


        schoolSignup = (Button)findViewById(R.id.schoolSignup);
        schoolSignup.setOnClickListener(this);


        log_in_id = (TextView)findViewById(R.id.log_in_id);
        log_in_id.setOnClickListener(this);

        findViewById(R.id.showPass1).setOnClickListener(this);
        findViewById(R.id.showPass2).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.schoolSignup){
            check();

        }
        if(v.getId() == R.id.log_in_id){
            finish();
        }
        if(v.getId() == R.id.showPass1){
            pp1++;
            if(pp1%2 !=0){
                schoolPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }else{
                schoolPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
        }
        if(v.getId() == R.id.showPass2){
            pp2++;
            if(pp2 %2 !=0){
                confirmPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }else{
                confirmPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
        }
    }

    private void check() {
        if(schoolEmail.getText().toString().equals("")|| schoolAddress.getText().toString().equals("")
                || schoolContact.getText().toString().equals("")|| schoolTeacher.getText().toString().equals("")
                || schoolStaff.getText().toString().equals("") || schollStudents.getText().toString().equals("")
                || schoolName.getText().toString().equals("") || schoolPass.getText().toString().equals("")
                || confirmPass.getText().toString().equals("")){
            Toast.makeText(this, "Some field is empty", Toast.LENGTH_SHORT).show();

            return;
        }
        if(schoolPass.getText().toString().length()<=8){
            Toast.makeText(this, "Password Should be more than 8 letters", Toast.LENGTH_SHORT).show();
            return;
        }
        if(schoolPass.getText().toString().equals(confirmPass.getText().toString())){
            passwordConfirmation();
        }
        else{
            Toast.makeText(this, "Password does not matched", Toast.LENGTH_SHORT).show();
            return;
        }

    }

    private void generateSchoolCode() {
        showLoading();
        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference("Schools/count/");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String number = dataSnapshot.getValue().toString();
                int serial = Integer.parseInt(number)+1;
                databaseReference.setValue(serial);
                authSchool(Integer.toString(serial));
               }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                dialog.dismiss();
            }
        });
    }

    private void authSchool(final String code) {
        mAuth = FirebaseAuth.getInstance();

        mAuth.createUserWithEmailAndPassword(schoolEmail.getText().toString(), schoolPass.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(SignupAuthority.this, "Success", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            createSchoolDataBase(user,code);

                        } else {
                            dialog.dismiss();
                            Toast.makeText(SignupAuthority.this, "Email Address is already used. Or Something is wrong",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                })
        ;
    }

    private void createSchoolDataBase(final FirebaseUser user, final String code){

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference("Schools/");


        String SchoolName = schoolName.getText().toString();
        String SchoolEmail = schoolEmail.getText().toString();
        String SchoolAddress = schoolAddress.getText().toString();
        String SchoolContact = schoolContact.getText().toString();
        String SchoolTeacher = schoolTeacher.getText().toString();
        String SchoolStaff = schoolStaff.getText().toString();
        String SchoolStudent = schollStudents.getText().toString();

        SchoolDetails schoolDetails = new SchoolDetails(SchoolName,SchoolEmail,
                SchoolStudent,SchoolStaff,SchoolTeacher,SchoolAddress,SchoolContact);
        databaseReference.child(user.getUid()).child("Information").setValue(schoolDetails)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            databaseReference.child(user.getUid()).child("School Code").setValue(code);
                            databaseReference.child(user.getUid()).child("School Pass").setValue(schoolPass.getText().toString());
                            Toast.makeText(SignupAuthority.this, "Successfully created an account! ", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            openDialogBox(code);
                     }
                    }
                })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Toast.makeText(SignupAuthority.this, "Please Check Your Internet Connection! ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openDialogBox(String Code) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setTitle("School Id: "+Code);
        alertDialogBuilder.setMessage("This is your School ID. Please Remember this and login with this School Id and your password.");
        alertDialogBuilder.setPositiveButton("Login",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        finish();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public int randomNumber(){
        Random r = new Random();
        int low = 3000;
        int high = 4000;
        return r.nextInt(high-low) + low;
    }
    private void generateConfirmationCode(final String result, View view) {

        System.out.println(Timer);


        final TextView time = view.findViewById(R.id.time);
        final TextView send = (TextView)view.findViewById(R.id.sendAgain);
        new CountDownTimer(1300000, 1000) {

            public void onTick(long millisUntilFinished) {
                System.out.println(millisUntilFinished);
                time.setText("Seconds remaining: " + millisUntilFinished / 1000);
            }

            @SuppressLint("ResourceAsColor")
            public void onFinish() {

                Timer = true;
                send.setEnabled(true);
                send.setTextColor(R.color.texts);
                System.out.println(Timer);
                time.setText("00");
            }
        }.start();



        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String s = "Your confirmation code is: "+result;
                    GMailSender sender = new GMailSender("ratulbhowmick65@gmail.com",
                            "makarovak47");
                    sender.sendMail("Confirmation Mail", s,
                            "ratulbhowmick65@gmail.com", schoolEmail.getText().toString());
                } catch (Exception e) {
                    Log.e("SendMail", e.getMessage(), e);

                }
            }

        }).start();
    }

    public void passwordConfirmation(){

        result = randomNumber();
        LayoutInflater li = LayoutInflater.from(this);
        final View promptsView = li.inflate(R.layout.confirmpass, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);
        generateConfirmationCode(Integer.toString(result),promptsView);

        final EditText editText = (EditText) promptsView
                .findViewById(R.id.confirmation);

        final TextView send = (TextView)promptsView.findViewById(R.id.sendAgain);
        send.setTextColor(Color.BLACK);
        send.setEnabled(false);


        send.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        send.setEnabled(false);
                        send.setTextColor(Color.BLACK);
                        System.out.println("aaa"+Timer);
                        result = randomNumber();
                        generateConfirmationCode(Integer.toString(result),promptsView);
                    }
                }
        );
        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                if(editText.getText().toString().equals(Integer.toString(result))){
                                    dialog.dismiss();
                                   // generateStudentCode(key);
                                    generateSchoolCode();
                                }else{
                                    Toast.makeText(SignupAuthority.this, "Code dose not matched.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }


}
