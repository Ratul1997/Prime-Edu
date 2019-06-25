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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.primeedu.rat.primeedu.Class.FatherDetails;
import com.primeedu.rat.primeedu.Class.GurdianDetails;
import com.primeedu.rat.primeedu.Class.MotherDetails;
import com.primeedu.rat.primeedu.Class.Student;
import com.primeedu.rat.primeedu.Class.StudentDetails;
import com.primeedu.rat.primeedu.Class.UploadImage;
import com.primeedu.rat.primeedu.GMailSender;
import com.primeedu.rat.primeedu.R;
import com.primeedu.rat.primeedu.Teacher.ViewStudentActivity;
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

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignupStudent extends AppCompatActivity implements View.OnClickListener{

    private EditText stuFname,stuLnam,stuEmail,stuAddress,stuContact,stuClass,stuSec,stuRoll,stuPass,stuConfirmPass;
    private EditText gurdianName,gurdianRelation,gurdianEmail,gurdianOccu,gurdianContact;
    private Button signUp;
    private TextView login_id,stuGender;
    private EditText stuSchoolCode;
    private CheckBox male,female,other;
    private static final int PICK_IMAGE_REQUEST = 1;
    Uri studentmUri= null,gurdianMuri=null;
    private static final int PERMISSION_CODE = 0;
    LinearLayout students,gurdian;
    FirebaseAuth mAuth;
    private CircleImageView studentImage,gurdianImage;
    private StorageReference mStorageRef;
    int imageTypes = 0;
    int result;
    ProgressDialog dialog;
    int pp1 = 0,pp2=0;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_student);

        init();
    }

    private void init() {
        student();
        gurdian();
        buttons();
        images();
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


    private void images() {
        studentImage = (CircleImageView)findViewById(R.id.studentImage);
        gurdianImage = (CircleImageView)findViewById(R.id.gurdianImage);
    }

    private void buttons() {
        signUp = (Button)findViewById(R.id.stuSignup);
        signUp.setOnClickListener(this);

        login_id = (TextView)findViewById(R.id.log_in_id);
        login_id.setOnClickListener(this);

        gurdian = (LinearLayout)findViewById(R.id.gurdian);
        gurdian.setOnClickListener(this);

        students = (LinearLayout)findViewById(R.id.students);
        students.setOnClickListener(this);

        findViewById(R.id.showPass1).setOnClickListener(this);
        findViewById(R.id.showPass2).setOnClickListener(this);

    }

    private void gurdian() {
        gurdianName = (EditText)findViewById(R.id.gurdianName);
        gurdianRelation = (EditText)findViewById(R.id.gurdianRelation);
        gurdianEmail = (EditText)findViewById(R.id.gurdianEmail);
        gurdianOccu = (EditText)findViewById(R.id.gurdianOccu);
        gurdianContact = (EditText)findViewById(R.id.gurdianContact);
    }

    private void student() {

        male = (CheckBox)findViewById(R.id.male);
        female = (CheckBox)findViewById(R.id.female);
        other = (CheckBox)findViewById(R.id.other);

        spinner = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.classtype, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);



        stuFname = (EditText)findViewById(R.id.stuFname);
        stuLnam = (EditText)findViewById(R.id.stuLname);
        stuEmail = (EditText)findViewById(R.id.stuEmail);
        stuGender = (TextView) findViewById(R.id.stuGender);
        stuAddress = (EditText)findViewById(R.id.stuAddress);
        stuContact = (EditText)findViewById(R.id.stuContact);
        stuSec = (EditText)findViewById(R.id.stuSec);
        stuRoll = (EditText)findViewById(R.id.stuRoll);
        stuPass = (EditText)findViewById(R.id.stuPass);
        stuConfirmPass = (EditText)findViewById(R.id.stuConfirmPass);
        stuSchoolCode = (EditText)findViewById(R.id.stuSchoolCode);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.stuSignup:
                createStudent();
                break;
            case R.id.log_in_id:
                finish();
                break;
            case R.id.students:
                imageTypes = 1;
                uploadImage(1,v);
                break;
            case R.id.gurdian:
                imageTypes = 2;
                uploadImage(1,v);
                break;
            case R.id.showPass1:
                pp1++;
                if(pp1%2 !=0){
                    stuPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }else{
                    stuPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                break;
            case  R.id.showPass2:
                pp2++;
                if(pp2 %2 !=0){
                    stuPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }else{
                    stuConfirmPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                break;
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

    private boolean conditions() {
        String Currentclass = spinner.getSelectedItem().toString();
        String CurrentSection = stuSec.getText().toString();
        String CurrentStudentId = stuRoll.getText().toString();

        String gurDianName = gurdianName.getText().toString();
        String gurDianOccu = gurdianOccu.getText().toString();
        String gurDianContact = gurdianContact.getText().toString();
        String gurDianRelation = gurdianRelation.getText().toString();

        String studentFName = stuFname.getText().toString();
        String studentLName = stuLnam.getText().toString();
        String studentGender = "";
        if(male.isChecked()){
            studentGender = "Male";
        }
        else if(female.isChecked()){
            studentGender = "Female";
        }
        else{
            studentGender = "Other";
        }
        String studentAddress = stuAddress.getText().toString();

        if(Currentclass.equals("") || CurrentSection.equals("") || CurrentStudentId.equals("")
                || gurDianName.equals("") || gurDianOccu.equals("") || gurDianContact.equals("")
                || gurDianRelation.equals("") || studentFName.equals("")|| studentLName.equals("")
                || studentGender.equals("") || studentGender.equals("") || studentAddress.equals("")
                || studentmUri==null || gurdianMuri ==null || spinner.getSelectedItemPosition() == 0){

            return false;
        }
        return true;
    }

    private void createStudent(){
        if(conditions()){
            String pass = stuPass.getText().toString();
            String confirm = stuConfirmPass.getText().toString();

            System.out.println(confirm +" "+pass);

            if(pass.equals("")){
                Toast.makeText(this, "Empty Password", Toast.LENGTH_SHORT).show();
                return;
            }
            if(confirm.equals("")){
                Toast.makeText(this, "Empty Password", Toast.LENGTH_SHORT).show();
                return;
            }
            if(pass.length()<=8){
                Toast.makeText(this, "Password should be greater than 8 length", Toast.LENGTH_SHORT).show();
                return;
            }
            if(pass.equals(confirm)){
                checkInSchoolDataBase();
            }else{
                Toast.makeText(this, "Password Does not Matched", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this, "Some Fields are empty", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkInSchoolDataBase() {
        showLoading();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

        String schoolCode = stuSchoolCode.getText().toString();

        Query query = rootRef.child("Schools").orderByChild("School Code").equalTo(schoolCode);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println(dataSnapshot.getKey());
                if(dataSnapshot.getValue() == null){
                    dialog.dismiss();
                    Toast.makeText(SignupStudent.this, "School Code Is Wrong.Please Check Again", Toast.LENGTH_SHORT).show();
                }
                else{
                    for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                        System.out.println(childSnapshot.child("Information").child("schoolname").getValue().toString());
                        openDialog(childSnapshot);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                dialog.dismiss();
                Toast.makeText(SignupStudent.this, "Please, Check Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openDialog(final DataSnapshot dataSnapshot) {
        dialog.dismiss();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        String s = dataSnapshot.child("Information").child("schoolname").getValue().toString();
        alertDialogBuilder.setTitle("School: "+s);
        alertDialogBuilder.setMessage("Name: "+stuFname.getText().toString()+" "+stuLnam.getText().toString()+"\n"
        +"Class: "+spinner.getSelectedItem().toString()+"\n"+"Section: "+stuSec.getText().toString()+"\n"+"Roll: "+stuRoll.getText().toString());
        alertDialogBuilder.setPositiveButton("SignUp",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        generateStudentCode(dataSnapshot.getKey());
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

    private void generateStudentCode(final String Schoolcode){

        showLoading();
        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference("Users/Student/count/");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String number = dataSnapshot.getValue().toString();
                int serial = Integer.parseInt(number)+1;
                databaseReference.setValue(serial);

                authenticationForStudent(Integer.toString(serial),Schoolcode);
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
    private void authenticationForStudent(final String code, final String Schoolcode){
        dialog.dismiss();
        uploadImage(makeKey(),code,Schoolcode);
    }

    private void createStudent(final String user,final String StudentCode,final String SchoolCode, final List<String>Uri ){

        showLoading();
        String Currentclass = spinner.getSelectedItem().toString();
        String CurrentSection = stuSec.getText().toString();
        String CurrentStudentId = stuRoll.getText().toString();

        String gurDianName = gurdianName.getText().toString();
        String gurDianOccu = gurdianOccu.getText().toString();
        String gurDianContact = gurdianContact.getText().toString();
        String gurDianRelation = gurdianRelation.getText().toString();

        String studentFName = stuFname.getText().toString();
        String studentLName = stuLnam.getText().toString();
        String studentGender = "";
        if(male.isChecked()){
            studentGender = "Male";
        }
        else if(female.isChecked()){
            studentGender = "Female";
        }
        else{
            studentGender = "Other";
        }
        String studentContact = stuContact.getText().toString();
        String studentAddress = stuAddress.getText().toString();


        FatherDetails fatherDetails = new FatherDetails("","","","");
        MotherDetails motherDetails = new MotherDetails("","","","");
        GurdianDetails gurdianDetails = new GurdianDetails(gurDianName,gurDianOccu,gurDianContact,Uri.get(0),gurDianRelation);



        StudentDetails studentDetails = new StudentDetails(studentFName,studentLName,studentAddress,Uri.get(1),
        studentGender,studentContact,gurdianDetails,fatherDetails,motherDetails);


        Student student =  new Student(SchoolCode,StudentCode,CurrentStudentId,CurrentSection,
                Currentclass,stuPass.getText().toString(),studentDetails,"");

        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        DatabaseReference databaseReference = firebaseDatabase.getReference("Users/Student/");
        databaseReference.child(user).setValue(student);

        DatabaseReference createinSchool = firebaseDatabase.getReference("Schools/"+SchoolCode+"/Students/");
        createinSchool.child(createinSchool.push().getKey()).setValue(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(SignupStudent.this, "SuccessFully added to database", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(SignupStudent.this, "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                });

        String path = "Classes/"+SchoolCode+"/class/"+Currentclass+"/Section/"+CurrentSection+"/"+CurrentStudentId+"/StudentCode/";
        DatabaseReference classref = firebaseDatabase.getReference(path);
        classref.setValue(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(SignupStudent.this, "SuccessFully added to database", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignupStudent.this, "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                });

        openDialogBox(StudentCode);
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
        final List<Uri> muri = new ArrayList<>();
        muri.add(studentmUri);
        muri.add(gurdianMuri);

        final List<String>downloadUri = new ArrayList<>();

        final int[] countt = {0};
        for(int i = 0 ;i<muri.size();i++){
            System.out.println(getFileExtention(muri.get(i)));
            mStorageRef = FirebaseStorage.getInstance().getReference();
            final StorageReference fileRef = mStorageRef.child(user+"/"+System.currentTimeMillis()+"."+getFileExtention(muri.get(i)));

            final int finalI = i;

            fileRef.putFile(muri.get(i))
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            //progressBar.dismiss();
                            fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    countt[0]++;
                                    System.out.println("asas:"+countt[0]);
                                    progressBar.setProgress(0);
                                    downloadUri.add(uri.toString());

                                    if(countt[0] == muri.size()){
                                        progressBar.dismiss();
                                        System.out.println(downloadUri);

                                        createStudent(user,code,schoolcode,downloadUri);
                                    }
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.dismiss();
                            Toast.makeText(SignupStudent.this, "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double pr = (100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            progressBar.setProgress((int)pr);
                        }
                    });
            System.out.println(i);
        }

    }
    private void openDialogBox(String Code) {
        dialog.dismiss();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("STudent Id: "+Code);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setMessage("This is your Student ID. Please Remember this and login with this School Id and your password.");
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
            if(imageTypes == 1){
                studentmUri = data.getData();

                Picasso.get().load(studentmUri).into(studentImage);

            }
            if(imageTypes == 2){
                gurdianMuri = data.getData();

                Picasso.get().load(gurdianMuri).into(gurdianImage);

            }
        }
    }
    public int randomNumber(){
        Random r = new Random();
        int low = 3000;
        int high = 4000;
        return r.nextInt(high-low) + low;
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
                            "ratulbhowmick65@gmail.com", stuEmail.getText().toString());
                } catch (Exception e) {
                    Log.e("SendMail", e.getMessage(), e);

                }
            }

        }).start();
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
                                    generateStudentCode(key);
                                }else{
                                    Toast.makeText(SignupStudent.this, "Code dose not matched.", Toast.LENGTH_SHORT).show();
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
