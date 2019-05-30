package com.example.rat.primeedu.AuthorityHomeActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.rat.primeedu.Class.ExamDetails;
import com.example.rat.primeedu.Class.NoticeDetails;
import com.example.rat.primeedu.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class CreatePost extends AppCompatActivity implements View.OnClickListener{

    Button upload,back;
    CheckBox terms,announcement;
    EditText msg,starting,ending,publishing;
    private static final int PERMISSION_CODE = 0;
    Uri muri;
    private String SchoolCode,Schoolname;
    private static final int PICK_IMAGE_REQUEST = 1;
    ImageView imageFile;
    Spinner spinner_id;
    NoticeDetails noticeDetails;
    ProgressDialog dialog;
    TextView school_name;

    final List<String> token = new ArrayList<>();
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        getIntentData();
        init();
    }

    private void getIntentData() {
        Schoolname = getIntent().getStringExtra("SchoolName").toString();
        SchoolCode = getIntent().getStringExtra("SchoolId").toString();
    }

    private void init() {
        findViewById(R.id.upload_image).setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.postButton).setOnClickListener(this);
        spinner_id = (Spinner)findViewById(R.id.spinner_id);
        requestQueue = Volley.newRequestQueue(this);

        imageFile = (ImageView)findViewById(R.id.imageFile);


        school_name = (TextView)findViewById(R.id.school_name);
        school_name.setText(Schoolname);
        terms = (CheckBox)findViewById(R.id.terms);
        terms.setOnClickListener(this);
        announcement = (CheckBox)findViewById(R.id.announcements);
        announcement.setOnClickListener(this);

        msg = (EditText)findViewById(R.id.msg);
        starting = (EditText)findViewById(R.id.start);
        ending = (EditText)findViewById(R.id.end);
        publishing = (EditText)findViewById(R.id.publish);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.examtype, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_id.setAdapter(adapter);
    }

    private void state(boolean flag){
        terms.setChecked(false);
        starting.setEnabled(flag);
        spinner_id.setEnabled(flag);
        ending.setEnabled(flag);
        publishing.setEnabled(flag);

        terms.setEnabled(flag);
    }


    @Override
    public void onClick(View v) {
        if(v.getId() ==  R.id.terms){
            if(terms.isChecked()){
                announcement.setEnabled(false);
                announcement.setChecked(false);
            }else{
                announcement.setEnabled(true);
                announcement.setChecked(false);
            }
        }
        if(v.getId() ==  R.id.announcements){
            if(announcement.isChecked()){
                state(false);
            }else{
                state(true);
            }
        }
        if(v.getId() ==  R.id.postButton)
        {
            //sendNotifications("111222333");
            post();
            //createExamsSideForEachStudent(SchoolCode,"101");
        }
        if(v.getId() ==  R.id.upload_image){
            openFileChooser();
        }
        if(v.getId() ==  R.id.back){
            finish();
        }
    }

    private void post() {
        if(!terms.isChecked() && !announcement.isChecked()){
            openDialogS("Please Select A PostType");
            return;
        }
        else if(terms.isChecked()){
            postExam();
        }
        else {
            postNotices();
        }
    }

    private void postNotices() {
        if(msg.getText().toString().equals("") && muri == null){
            openDialogS("Please Write An Massage or Upload An Image");
            return;
        }

        if(msg.getText().toString().equals("") && muri!= null){
            openDialogS("Please give a caption.");
            return;
        }

        if(muri == null){
            postAnnouncementWithoutImage();
        }
        else{
            showLoading();
            uploadAnnouncement();
        }

    }

    private void postAnnouncementWithImage(String Uri) {
        showLoading();
        String msgg = msg.getText().toString();
        noticeDetails = new NoticeDetails(msgg,Uri);
        DatabaseReference schoolRef2 = FirebaseDatabase.getInstance().getReference("Schools/"+SchoolCode+"/Notice/");
        final String Id = makeKey();
        schoolRef2.child(Id).setValue(noticeDetails)
        .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    dialog.dismiss();
                    openDialogBox(Id);
                }
            }
        });
    }

    private void uploadAnnouncement() {
        dialog.dismiss();
        final ProgressDialog progressBar;
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(false);
        progressBar.setMessage("File Uploading ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressBar.show();

        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        final StorageReference fileRef = mStorageRef.child(SchoolCode+"/Notice/"+System.currentTimeMillis()+"."+getFileExtention(muri));
        fileRef.putFile(muri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                progressBar.dismiss();
                                postAnnouncementWithImage(uri.toString());
                            }
                        });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
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

    private void postAnnouncementWithoutImage() {
        showLoading();
        String msgg = msg.getText().toString();
        noticeDetails = new NoticeDetails(msgg,"");
        DatabaseReference schoolRef2 = FirebaseDatabase.getInstance().getReference("Schools/"+SchoolCode+"/Notice/");
        final String Id = makeKey();
        schoolRef2.child(Id).setValue(noticeDetails)
        .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    dialog.dismiss();
                    openDialogBox(Id);
                }
            }
        });

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


    private void postExam() {
        if(spinner_id.getSelectedItemPosition() == 0){
            openDialogS("Please Select An Exam Type.");
            return;
        }
        else if(starting.getText().toString().equals("") || ending.getText().toString().equals("")
                || publishing.getText().toString().equals("")){
            openDialogS("Some Exams Filed Are Empty");
            return;
        }
        else if(muri == null){
            openDialogS("Please Upload Routine.");
            return;
        }
        else{

            showLoading();
            createExamcode();
        }
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
            muri = data.getData();
            Picasso.get().load(muri).into(imageFile);
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

    private void createExamcode(){
        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference("Exams/count/");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String number = dataSnapshot.getValue().toString();
                int serial = Integer.parseInt(number)+1;
                databaseReference.setValue(serial);

                uploadImages(Integer.toString(serial));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                dialog.dismiss();
            }
        });
    }

    private String getFileExtention(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImages(final String s) {
        dialog.dismiss();
        final ProgressDialog progressBar;
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(false);
        progressBar.setMessage("File Uploading ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressBar.show();

        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        final StorageReference fileRef = mStorageRef.child(SchoolCode+"/"+s+"/"+System.currentTimeMillis()+"."+getFileExtention(muri));
        fileRef.putFile(muri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                progressBar.dismiss();
                                showLoading();
                                createExam(s,SchoolCode,uri.toString());
                            }
                        });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
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

    private void createExam(final String examcode, final String SchoolCode,String Uri){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("Exams/"+SchoolCode+"/"+examcode);

        String eXName = spinner_id.getSelectedItem().toString();
        String sDate = starting.getText().toString();
        String eDate = ending.getText().toString();
        String pDate = publishing.getText().toString();
        ExamDetails examDetails = new ExamDetails(eXName,sDate,eDate,pDate,Uri);

        String msgg = msg.getText().toString();
        msgg += "\n"+ "Starting Date: "+sDate+"\nEnding Date:"+eDate+"\nPossible Publish Date:"+pDate;
        noticeDetails = new NoticeDetails(msgg,Uri);


        databaseReference.setValue(examDetails)
        .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //  String Id = makeKey();
//                    schoolRef2.child(Id).setValue(examcode);
//                    schoolRef2.child(Id).setValue(noticeDetails);
//
//                    System.out.println(noticeDetails.getMsg());
                    noticeExam();
                }
                else{
                    Toast.makeText(CreatePost.this, "Something is wrong.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void noticeExam() {
        DatabaseReference schoolRef2 = FirebaseDatabase.getInstance().getReference("Schools/"+SchoolCode+"/Notice/");
        final String Id = makeKey();
        schoolRef2.child(Id).setValue(noticeDetails)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            dialog.dismiss();
                            openDialogBox(Id);
                        }
                    }
                });
    }

    private String makeKey(){
        SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyyHHmmssS");
        Date date = new Date();

        return  formatter.format(date);
    }


    private void openDialogBox(final String Id) {
        String Ids = Id;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setMessage("Your massage has been posted");
        alertDialogBuilder.setPositiveButton("ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        sendNotifications(Id);
                        finish();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

     private void sendNotifications(final String Id) {
        notieKey(Id);
    }

     private void notieKey(String noticeId){

        JSONObject data = new JSONObject();
        try {
            System.out.println(SchoolCode);
            String s = SchoolCode;
            String ttl = "Authority posts a new post";
            data.put("to","/topics/"+s);
            JSONObject notify = new JSONObject();
            notify.put("title",ttl);
            notify.put("body",noticeDetails.getMsg());

            data.put("notification",notify);
            String Url = "https://fcm.googleapis.com/fcm/send";
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Url,
                    data,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                                System.out.println("Hi");
                                System.out.println(response);

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String>header = new HashMap<>();
                    header.put("content-type","application/json");
                    header.put("authorization","key=AIzaSyCBietxW6-cybuRGw9W5IKCqB9m4fH1iUQ");
                    return  header;
                }
            };
            requestQueue.add(request);
        }
        catch (JSONException e){
            e.printStackTrace();
        }
    }
}
