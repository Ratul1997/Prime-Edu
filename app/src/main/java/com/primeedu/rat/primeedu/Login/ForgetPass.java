package com.primeedu.rat.primeedu.Login;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.primeedu.rat.primeedu.AuthorityHomeActivity.AuthorityMainActivity;
import com.primeedu.rat.primeedu.GMailSender;
import com.primeedu.rat.primeedu.R;
import com.primeedu.rat.primeedu.Student.StudentActivity;
import com.primeedu.rat.primeedu.Teacher.TeacherActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class ForgetPass extends AppCompatActivity implements View.OnClickListener {

    private TextView signUp;
    private Spinner spinner;
    private EditText userId,usermail;
    private ImageButton logIn;
    ProgressDialog progressBar;
    EditText pass,cpass;
    CheckBox rememberMe;
    ProgressDialog dialog;
    LinearLayout student,management,teacher;
    int ss = 0,tt=0,mm = 0;
    int result;
    int pp1 = 0,pp2=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass);

        init();
    }

    private void init() {
        userId = findViewById(R.id.userId);
        usermail = findViewById(R.id.usermail);

        student = findViewById(R.id.student);
        teacher = findViewById(R.id.teacher);
        management = findViewById(R.id.management);

        pass = findViewById(R.id.pass);
        cpass = findViewById(R.id.confPass);
        findViewById(R.id.student).setOnClickListener(this);
        findViewById(R.id.management).setOnClickListener(this);
        findViewById(R.id.teacher).setOnClickListener(this);
        findViewById(R.id.done).setOnClickListener(this);
        findViewById(R.id.login_id).setOnClickListener(this);
        findViewById(R.id.showPass1).setOnClickListener(this);
        findViewById(R.id.showPass2).setOnClickListener(this);

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
        if(R.id.student == v.getId()){
            ss++;
            if(ss%2 !=0){
                student.setBackgroundResource(R.drawable.card);
                teacher.setEnabled(false);
                management.setEnabled(false);
            }
            else{
                student.setBackgroundResource(0);
                teacher.setEnabled(true);
                management.setEnabled(true);
            }
        }
        if(R.id.teacher == v.getId()){
            tt++;
            if(tt %2 !=0){
                teacher.setBackgroundResource(R.drawable.card);
                student.setEnabled(false);
                management.setEnabled(false);
            }
            else{
                teacher.setBackgroundResource(0);
                student.setEnabled(true);
                management.setEnabled(true);
            }

        }if(R.id.management == v.getId()){
            mm++;
            if(mm %2 !=0){
                management.setBackgroundResource(R.drawable.card);
                student.setEnabled(false);
                teacher.setEnabled(false);
            }
            else{
                management.setBackgroundResource(0);
                student.setEnabled(true);
                teacher.setEnabled(true);
            }
        }
        if(v.getId() == R.id.login_id){
            finish();
        }
        if(v.getId() == R.id.done){

            int num;
            if(userId.getText().toString().equals("") || usermail.getText().toString().equals("")
                    || pass.getText().toString().equals("") || cpass.getText().toString().equals("")){
                Toast.makeText(this, "TextFiles is empty", Toast.LENGTH_SHORT).show();
                return;
            }
            if(pass.getText().toString().length()<=8){
                Toast.makeText(this, "Should be more than 8 letters", Toast.LENGTH_SHORT).show();
                return;
            }
            if(pass.getText().toString().equals(cpass.getText().toString())){

                if(student.isEnabled() && teacher.isEnabled() && management.isEnabled())num = 0;
                else if(student.isEnabled())num = 1;
                else if(teacher.isEnabled())num = 2;
                else{
                    num = 3;
                }
                switch (num){
                    case 0:
                        Toast.makeText(this, "Select a Login Type", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        usermail.onEditorAction(EditorInfo.IME_ACTION_DONE);

                        loginStudent();
                        break;
                    case 2:

                        usermail.onEditorAction(EditorInfo.IME_ACTION_DONE);
                        teacherLogin();
                        break;
                    case 3:
                        usermail.onEditorAction(EditorInfo.IME_ACTION_DONE);

                        loginAuthority();
                        break;
                }
            }
            else {
                Toast.makeText(this, "Password does not matched", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if(v.getId() == R.id.showPass1){
            pp1++;
            if(pp1%2 !=0){
                pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }else{
                pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
        }
        if(v.getId() == R.id.showPass2){
            pp2++;
            if(pp2 %2 !=0){
                cpass.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }else{
                cpass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
        }
    }

    private void teacherLogin() {
        showLoading();
        final String ID = userId.getText().toString();


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users/Teachers/");
        databaseReference.orderByChild("TeacherCode").equalTo(ID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.getValue() == null){
                            dialog.dismiss();
                            String msg = "User Id doesn't exist. Please check again.";
                            openDialogS(msg);
                        }
                        else{
                            for( DataSnapshot snapshot: dataSnapshot.getChildren()){

                                dialog.dismiss();
                                passwordConfirmation(snapshot.getKey(),"2");
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        dialog.dismiss();
                        openDialogS("Something is wrong");
                    }
                });
    }

    private void loginAuthority() {
        showLoading();
        final String ID = userId.getText().toString();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Schools/");
        databaseReference.orderByChild("School Code").equalTo(ID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getValue() == null){
                            dialog.dismiss();
                            String msg = "School Id doesn't exist. Please check again.";
                            openDialogS(msg);
                        }
                        else{

                            for( DataSnapshot snapshot: dataSnapshot.getChildren()){

                                dialog.dismiss();
                                passwordConfirmation(snapshot.getKey(),"3");
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        dialog.dismiss();
                        openDialogS("Something is wrong");
                    }
                });

    }

    private void loginStudent() {
        showLoading();
        final String ID = userId.getText().toString();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users/Student/");
        databaseReference.orderByChild("studentcode").equalTo(ID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.getValue() == null){
                            dialog.dismiss();
                            String msg = "User Id doesn't exist. Please check again.";
                            openDialogS(msg);
                        }
                        else{


                            for( DataSnapshot snapshot: dataSnapshot.getChildren()){

                                dialog.dismiss();
                                passwordConfirmation(snapshot.getKey(),"1");
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        dialog.dismiss();
                        openDialogS("Something is wrong");
                    }
                });

    }
    private void generateConfirmationCode(final String result,View view) {

        final TextView time = view.findViewById(R.id.time);
        final TextView send = (TextView)view.findViewById(R.id.sendAgain);
        new CountDownTimer(180000, 1000) {

            public void onTick(long millisUntilFinished) {
                System.out.println(millisUntilFinished);
                time.setText("Seconds remaining: " + millisUntilFinished / 1000);
            }

            @SuppressLint("ResourceAsColor")
            public void onFinish() {
                send.setEnabled(true);
                send.setTextColor(R.color.texts);
                time.setText("Seconds remaining: 00");
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
                            "ratulbhowmick65@gmail.com", usermail.getText().toString());
                } catch (Exception e) {
                    Log.e("SendMail", e.getMessage(), e);

                }
            }

        }).start();
    }
    public int randomNumber(){
        Random r = new Random();
        int low = 3000;
        int high = 4000;
        return r.nextInt(high-low) + low;
    }

    public void passwordConfirmation(final String s, final String type){

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
                        result = randomNumber();
                        generateConfirmationCode(Integer.toString(result),promptsView);
                    }
                }
        );
        // set dialog message
        System.out.println(result);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                if(editText.getText().toString().equals(Integer.toString(result))){
                                    dialog.dismiss();

                                    setPassword(s,type);
                                    }else{
                                    Toast.makeText(ForgetPass.this, "Code dose not matched.", Toast.LENGTH_SHORT).show();
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

    private void setPassword(String s, String type) {
        showLoading();
        String path = "";
        if(type == "1"){

            path = "Users/Student/"+s+"/sutdentpass/";
        }else if(type == "2"){
            path = "Users/Teachers/"+s+"/TeacherPass/";
        }else if(type == "3"){
            path = "Schools/"+s+"/School Pass/";
        }
        System.out.println(path);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(path);
        ref.setValue(pass.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            dialog.dismiss();
                            openDialogBox("");
                        }else {
                            dialog.dismiss();
                            openDialogS("Something is wrong");
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

    private void openDialogBox(String Code) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setMessage("Password has been updated successfully.");
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
}
