package com.example.rat.primeedu.Login;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rat.primeedu.AuthorityHomeActivity.AuthorityMainActivity;
import com.example.rat.primeedu.R;
import com.example.rat.primeedu.Student.StudentActivity;
import com.example.rat.primeedu.Teacher.TeacherActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LoginActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,View.OnClickListener {

    private TextView signUp;
    private Spinner spinner;
    private EditText userId,userPass;
    private ImageButton logIn;
    ProgressDialog progressBar;
    CheckBox rememberMe;
    LinearLayout student,management,teacher;
    int ss = 0,tt=0,mm = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);


        init();
        getSharedPref();
        signUp = (TextView) findViewById(R.id.sign_up_id);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity();
            }
        });

    }

    private void init() {
        userId = findViewById(R.id.userId);
        userPass = findViewById(R.id.userPass);

        logIn = findViewById(R.id.logIn);
        logIn.setOnClickListener(this);

        student = findViewById(R.id.student);
        teacher = findViewById(R.id.teacher);
        management = findViewById(R.id.management);

        findViewById(R.id.student).setOnClickListener(this);
        findViewById(R.id.management).setOnClickListener(this);
        findViewById(R.id.teacher).setOnClickListener(this);

        rememberMe = findViewById(R.id.rememberMe);
        findViewById(R.id.rememberMe).setOnClickListener(this);

        progressBar = new ProgressDialog(this);
        progressBar.setIndeterminate(true);
        progressBar.setCancelable(false);
        progressBar.setMessage("Please Wait ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        findViewById(R.id.forgetPass).setOnClickListener(this);
    }

    public void openActivity(){
        openDialog();

    }
    private void getSharedPref() {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginDetails", 0);

        if (sharedPreferences.getBoolean("isRememberd", false)) {

            String Id = sharedPreferences.getString("UserId", "").toString();
            String pass = sharedPreferences.getString("Password", "").toString();
            userId.setText(Id);
            userPass.setText(pass);
            rememberMe.setChecked(true);
        }else{

        }
    }

    private void openDialog() {
        String[] typeList;
        typeList = new String[] {"Student", "Teacher", "Authority"};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(LoginActivity.this);
        mBuilder.setTitle("Account type");
        mBuilder.setIcon(R.drawable.icon);
        mBuilder.setSingleChoiceItems(typeList, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0){

                    Intent intent = new Intent(LoginActivity.this, SignupStudent.class);
                    startActivity(intent);
                    dialog.dismiss();
                }
                else if(which == 1){

                    Intent intent = new Intent(LoginActivity.this, SignUpTeacher.class);
                    startActivity(intent);
                    dialog.dismiss();
                }
                else if(which == 2){

                    Intent intent = new Intent(LoginActivity.this, SignupAuthority.class);
                    startActivity(intent);
                    dialog.dismiss();
                }
            }
        });
        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (position){
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void loginStudent(){

         final String ID = userId.getText().toString();
        final String Pass = userPass.getText().toString();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users/Student/");
        databaseReference.orderByChild("studentcode").equalTo(ID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        progressBar.dismiss();
                        if(dataSnapshot.getValue() == null){
                            String msg = "User Id doesn't exist. Please check again.";
                            openDialogS(msg);
                        }
                        else{
                            for( DataSnapshot snapshot : dataSnapshot.getChildren()){
                                if(snapshot.child("sutdentpass").getValue().toString().equals(Pass)){
                                    if(rememberMe.isChecked()){
                                        sharedPreferrence();
                                    }
                                    finish();
                                    Intent intent = new Intent(LoginActivity.this, StudentActivity.class);
                                    intent.putExtra("Id",snapshot.getKey());
                                    intent.putExtra("code",ID);
                                    startActivity(intent);
                                }
                                else{
                                    String msg = "Password is wrong.";
                                    openDialogS(msg);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

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

    private void loginTeacher(){

        final String ID = userId.getText().toString();
        final String Pass = userPass.getText().toString();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users/Teachers/");
        databaseReference.orderByChild("TeacherCode").equalTo(ID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        progressBar.dismiss();
                        if(dataSnapshot.getValue() == null){
                            String msg = "User Id doesn't exist. Please check again.";
                            openDialogS(msg);
                        }
                        else{
                            for( DataSnapshot snapshot : dataSnapshot.getChildren()){
                                if(snapshot.child("TeacherPass").getValue().toString().equals(Pass)){
                                    if(rememberMe.isChecked()){
                                        sharedPreferrence();
                                    }
                                    finish();
                                    Intent intent = new Intent(LoginActivity.this, TeacherActivity.class);
                                    intent.putExtra("code",ID);
                                    intent.putExtra("Id",snapshot.getKey());
                                    startActivity(intent);
                                }
                                else{
                                    String msg = "Password is wrong.";
                                    openDialogS(msg);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
    private void loginAuthority(){

        final String ID = userId.getText().toString();
        final String Pass = userPass.getText().toString();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Schools/");
        databaseReference.orderByChild("School Code").equalTo(ID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //System.out.println(dataSnapshot.getValue());

                        progressBar.dismiss();
                        if(dataSnapshot.getValue() == null){
                            String msg = "School Id doesn't exist. Please check again.";
                            openDialogS(msg);
                        }
                        else{
                            for( DataSnapshot snapshot : dataSnapshot.getChildren()){
                                if(snapshot.child("School Pass").getValue().toString().equals(Pass)){
                                    if(rememberMe.isChecked()){
                                        sharedPreferrence();
                                    }
                                    finish();
                                    Intent intent = new Intent(LoginActivity.this, AuthorityMainActivity.class);
                                    intent.putExtra("Id",snapshot.getKey());
                                    intent.putExtra("code",ID);
                                    startActivity(intent);
                                }
                                else{
                                    String msg = "Password is wrong.";
                                    openDialogS(msg);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.logIn){
            int num;
            if(userId.getText().toString().equals("") || userPass.getText().toString().equals("")){
                Toast.makeText(this, "TextFiles is empty", Toast.LENGTH_SHORT).show();
                return;
            }

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
                    userPass.onEditorAction(EditorInfo.IME_ACTION_DONE);

                    progressBar.show();
                    loginStudent();
                    break;
                case 2:

                    userPass.onEditorAction(EditorInfo.IME_ACTION_DONE);

                    progressBar.show();
                    loginTeacher();
                    break;
                case 3:
                    userPass.onEditorAction(EditorInfo.IME_ACTION_DONE);

                    progressBar.show();
                    loginAuthority();
                    break;
            }
        }
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
        if(v.getId() == R.id.rememberMe){
            if(rememberMe.isChecked()){
                sharedPreferrence();
            }
            else{
                clearShared();
            }
        }
        if(v.getId() == R.id.forgetPass){

            Intent intent = new Intent(LoginActivity.this,ForgetPass.class);
            startActivity(intent);
        }
    }

    private void clearShared() {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginDetails", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean("isRememberd",false);
        editor.putString("UserId","");
        editor.putString("Password", "");

        editor.commit();
    }

    private void sharedPreferrence() {

        SharedPreferences sharedPreferences = getSharedPreferences("LoginDetails", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();


        editor.putBoolean("isRememberd",true);
        editor.putString("UserId",userId.getText().toString());
        editor.putString("Password", userPass.getText().toString());

        editor.commit();
    }
}

