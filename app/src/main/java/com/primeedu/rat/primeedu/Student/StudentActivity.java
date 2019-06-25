package com.primeedu.rat.primeedu.Student;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.primeedu.rat.primeedu.Asapter.ViewPagerAdapter;
import com.primeedu.rat.primeedu.AuthorityHomeActivity.AuthorityMainActivity;
import com.primeedu.rat.primeedu.AuthorityHomeActivity.AuthorityNotice;
import com.primeedu.rat.primeedu.AuthorityHomeActivity.TeachersInSchool;
import com.primeedu.rat.primeedu.Class.SchoolDetails;
import com.primeedu.rat.primeedu.Class.Student;
import com.primeedu.rat.primeedu.Class.ZoomAbleImageActivity;
import com.primeedu.rat.primeedu.Login.LoginActivity;
import com.primeedu.rat.primeedu.R;
import com.primeedu.rat.primeedu.SharedPred;
import com.primeedu.rat.primeedu.Teacher.TeacherActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentActivity extends AppCompatActivity implements View.OnClickListener,View.OnLongClickListener {

    TabLayout tablayout;
    ViewPager viewPager;
    private String CurrentSection,Currentclass,CurrentStudent;
    TextView nameOfStudent;
    ProgressDialog dialog;
    CircleImageView profilePic;
    private String CurrentStudUniqueId,Code;
    private Student student;
    SchoolDetails schoolDetails;
    private static final int PICK_IMAGE_REQUEST = 1;

    private static final int PERMISSION_CODE = 0;
    Uri mUri ;

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
        profilePic.setOnLongClickListener(this);

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
            intent.putExtra("type","1");
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
    private void changePass() {

        LayoutInflater li = LayoutInflater.from(this);
        final View promptsView = li.inflate(R.layout.changepass, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);


        alertDialogBuilder.setView(promptsView);


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        final int[] pp1 = {0};
        final int[] pp2 = {0};
        final EditText ps = promptsView.findViewById(R.id.Pass);
        final EditText cps = promptsView.findViewById(R.id.ConfirmPass);

        promptsView.findViewById(R.id.showPass1).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pp1[0]++;
                        if(pp1[0] %2 !=0){
                            ps.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        }else{
                            ps.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        }
                    }
                }
        );
        promptsView.findViewById(R.id.showPass2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pp2[0]++;
                if(pp2[0] %2 !=0){
                    cps.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }else{
                    cps.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });

        promptsView.findViewById(R.id.done).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(ps.getText().toString().equals("")|| cps.getText().toString().equals("")){
                            Toast.makeText(StudentActivity.this, "Some field are empty", Toast.LENGTH_SHORT).show();

                        }
                        else if(ps.getText().toString().length()<=8){
                            Toast.makeText(StudentActivity.this, "Password should be more than 8 letters.", Toast.LENGTH_SHORT).show();

                        }
                        else if(ps.getText().toString().equals(cps.getText().toString())){
                            setPassword(CurrentStudUniqueId,"1",ps.getText().toString());
                        }else{
                            Toast.makeText(StudentActivity.this, "Password Does not Matched", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

    }

    private void setPassword(String s, String type, String ps) {
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
        ref.setValue(ps)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            dialog.dismiss();
                            openDialogS("Successfully changed.");
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
                    case R.id.cPass:
                        changePass();
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
    @Override
    public boolean onLongClick(View v) {

        if(v.getId() == R.id.profilePic){
            uploadImage(1,v);
        }
        return true;
    }
    private void uploadImage(int type,View v){
        PopupMenu popup = new PopupMenu(v.getContext(), v, Gravity.RIGHT);
        popup.getMenuInflater().inflate(R.menu.upload,popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.uploadPic:
                        openFileChooser();
                        break;
                }
                return true;
            }
        });
        popup.show();
    }
    private void openFileChooser() {

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.READ_EXTERNAL_STORAGE }, PERMISSION_CODE);

        }else{
            pickFromGallery();
        }
    }
    private void pickFromGallery() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickFromGallery();
                }
                else{
                    Toast.makeText(this, "denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE_REQUEST){

            mUri = data.getData();
            openUploaddImageDialogBox();
        }
    }
    private void openUploaddImageDialogBox() {
        dialog.dismiss();
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Uplaod Image");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setMessage("Are you sure? You want to change profile picture!!");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        if(mUri == null){
                            Toast.makeText(StudentActivity.this, "File does not picked correctly.Please try again.", Toast.LENGTH_SHORT).show();
                            arg0.dismiss();
                        }else{
                            uploadImage("","","");
                        }
                    }
                });
        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    private String getFileExtention(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    private  void  uploadImage(final String user, final String code, final String schoolcode) {

        final ProgressDialog progressBar;
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(false);
        progressBar.setMessage("File Uploading ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressBar.show();


        System.out.println(mUri);
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        final StorageReference files = mStorageRef.child(CurrentStudUniqueId+"/"+System.currentTimeMillis()+"."+getFileExtention(mUri));
        files.putFile(mUri)
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if(task.isSuccessful()){
                            Toast.makeText(StudentActivity.this, "Done", Toast.LENGTH_SHORT).show();
                            files.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {


                                    Glide.with(getApplicationContext())
                                            .load(uri)
                                            .into(profilePic);
                                    saveImage(uri);
                                    progressBar.dismiss();
                                    Toast.makeText(StudentActivity.this, "Successfully upload.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else{
                            progressBar.dismiss();
                            Toast.makeText(StudentActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.dismiss();
                        Toast.makeText(StudentActivity.this, "Something is wrong.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double pr = (100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                        progressBar.setProgress((int)pr);
                    }
                });
    }

    private void saveImage(final Uri uri) {
        dialog.show();
        System.out.println(uri);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users/Student/"+CurrentStudUniqueId+"/studentDetails/");
        ref.child("studentprofilepicture").setValue(uri.toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        dialog.dismiss();
                        if(task.isSuccessful()){
                            student.getStudentDetails().setStudentprofilepicture(uri.toString());

                            Toast.makeText(StudentActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(StudentActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
