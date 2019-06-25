package com.primeedu.rat.primeedu.Login;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.primeedu.rat.primeedu.Class.TeacherDetails;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpTeacher extends AppCompatActivity implements View.OnClickListener{

    EditText teacherName,schoolCode, teacherEmail,teacherContact,teacherAddress,teacherPass,teacherConfirmPass,teacherSubject;
    Button signup;
    TextView login;
    LinearLayout image;
    CircleImageView img;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PERMISSION_CODE = 0;
    FirebaseAuth mAuth;
    Uri teacherUri = null;
    ProgressDialog dialog;
    int result;
    int pp1 = 0,pp2=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_teacher);

        init();
    }

    private void init() {

        teacherName = findViewById(R.id.teacheName);
        teacherEmail = findViewById(R.id.teacherEmail);
        teacherAddress = findViewById(R.id.teacherAddress);
        teacherContact = findViewById(R.id.teacherContact);
        teacherConfirmPass = findViewById(R.id.teacherConfirmPass);
        teacherPass = findViewById(R.id.teacherPass);
        login = (TextView)findViewById(R.id.log_in_id);
        schoolCode = findViewById(R.id.schoolCode);
        teacherSubject =findViewById(R.id.teacherSubject);


        img = findViewById(R.id.img);


        findViewById(R.id.showPass1).setOnClickListener(this);
        findViewById(R.id.showPass2).setOnClickListener(this);
        signup = (Button)findViewById(R.id.teacherSignUp);
        signup.setOnClickListener(this);

        findViewById(R.id.log_in_id).setOnClickListener(this);
        image = findViewById(R.id.teacherImage);
        image.setOnClickListener(this);
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
        if(v.getId() == R.id.teacherImage){
            uploadImage(1,v);
        }
        if(v.getId() == R.id.teacherSignUp){
            check();
        }
        if(v.getId() == R.id.log_in_id){
            finish();
        }
        if(v.getId() == R.id.showPass1){
            pp1++;
            if(pp1%2 !=0){
                teacherPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }else{
                teacherPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
        }
        if(v.getId() == R.id.showPass2){
            pp2++;
            if(pp2 %2 !=0){
                teacherConfirmPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }else{
                teacherConfirmPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
        }
    }

    private void check() {
        if(teacherName.getText().toString().equals("") || teacherPass.getText().toString().equals("")
                || teacherConfirmPass.getText().toString().equals("") || teacherContact.getText().toString().equals("")
                || teacherAddress.getText().toString().equals("") || teacherSubject.getText().toString().equals("")
                || teacherEmail.getText().toString().equals("") || teacherUri == null || schoolCode.getText().toString().equals("")){
            Toast.makeText(this, "Some Field is empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if(teacherPass.getText().toString().length()<=8){
            Toast.makeText(this, "Password should be 8 or more letters", Toast.LENGTH_SHORT).show();
            return;
        }
        if(teacherPass.getText().toString().equals(teacherConfirmPass.getText().toString())){
            checkInSchoolDataBase();
        }else{
            Toast.makeText(this, "Password does not matched", Toast.LENGTH_SHORT).show();
            return;
        }

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
            teacherUri = data.getData();
            Picasso.get().load(teacherUri).into(img);
        }
    }

    private void checkInSchoolDataBase() {
        showLoading();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

        String schoolcode = schoolCode.getText().toString();

        Query query = rootRef.child("Schools").orderByChild("School Code").equalTo(schoolcode);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() == null){
                    dialog.dismiss();
                    Toast.makeText(SignUpTeacher.this, "School Code Is Wrong.Please Check Again", Toast.LENGTH_SHORT).show();
                }
                else{
                    for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                        openDialog(childSnapshot);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                dialog.dismiss();
                Toast.makeText(SignUpTeacher.this, "Something is Wrong", Toast.LENGTH_SHORT).show();
            }
        });
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


        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        final StorageReference fileRef = mStorageRef.child(user+"/"+System.currentTimeMillis()+"."+getFileExtention(teacherUri));
         fileRef.putFile(teacherUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                progressBar.dismiss();
                                createTeacherDataBase(user,code,schoolcode,uri.toString());
                              }
                        });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignUpTeacher.this, "Something is wrong", Toast.LENGTH_SHORT).show();
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


    private void openDialog(final DataSnapshot dataSnapshot) {
        dialog.dismiss();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        String s = dataSnapshot.child("Information").child("schoolname").getValue().toString();
        alertDialogBuilder.setTitle("School: "+s);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setMessage("Name: "+teacherName.getText().toString());
        alertDialogBuilder.setPositiveButton("SignUp",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        generateTeacherCode(dataSnapshot.getKey());
                    }
                });
        alertDialogBuilder.setNegativeButton("Cancle",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    private void generateTeacherCode(final String SchoolCode) {
        showLoading();
        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference("Users/Teachers/count/");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String number = dataSnapshot.getValue().toString();
                int serial = Integer.parseInt(number)+1;
                databaseReference.setValue(serial);

                auth(Integer.toString(serial),SchoolCode);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                dialog.dismiss();
            }
        });
    }

    private String makeKey(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyddMMHHmmssSSSSSS");
        Date date = new Date();

        return  formatter.format(date);
    }

    private void auth(final String code,final String schoolCode) {
        uploadImage(makeKey(),code,schoolCode);
    }
    private void createTeacherDataBase(final String user, final String code, final String ScollCode,String Uri){

        showLoading();
        String name = teacherName.getText().toString();
        String subject = teacherSubject.getText().toString();
        String status = "present";
        String contact = teacherContact.getText().toString();
        String email = teacherEmail.getText().toString();
        String address = teacherAddress.getText().toString();
        String image = Uri;

        TeacherDetails teacherDetails = new TeacherDetails(name,subject,status,contact,ScollCode,
                email,address,image,"");

        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Users/Teachers/");

        databaseReference.child(user).child("Details").setValue(teacherDetails)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            DatabaseReference sircode2 = firebaseDatabase.getReference("Users/Teachers/");
                            sircode2.child(user).child("TeacherCode").setValue(code);
                            sircode2.child(user).child("TeacherPass").setValue(teacherPass.getText().toString());

                            DatabaseReference createinSchool = firebaseDatabase.getReference("Schools/"+ScollCode+"/Teachers");
                            String id = createinSchool.push().getKey();
                            createinSchool.child(id).setValue(user)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            dialog.dismiss();
                                            Toast.makeText(SignUpTeacher.this, "Successfully added", Toast.LENGTH_SHORT).show();
                                            openDialogBox(code);
                                        }
                                    });
                        }
                        else{
                            dialog.dismiss();
                            Toast.makeText(SignUpTeacher.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void openDialogBox(String Code) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Teacher Id: "+Code);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setMessage("This is your Teacher ID. Please Remember this and login with this School Id and your password.");
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
    public void passwordConfirmation(final String key){

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
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                if(editText.getText().toString().equals(Integer.toString(result))){
                                    dialog.dismiss();
                                }else{
                                    Toast.makeText(SignUpTeacher.this, "Code dose not matched.", Toast.LENGTH_SHORT).show();
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

    private void generateConfirmationCode(final String result, View view) {



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
                            "ratulbhowmick65@gmail.com", teacherEmail.getText().toString());
                } catch (Exception e) {
                    Log.e("SendMail", e.getMessage(), e);

                }
            }

        }).start();
    }


}
