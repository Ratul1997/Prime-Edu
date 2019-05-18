package com.example.rat.primeedu;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.rat.primeedu.Class.UploadImage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class UploadPicture extends AppCompatActivity implements View.OnClickListener{

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PERMISSION_CODE = 0;
    private Button upload,chossefile;
    ProgressBar progress;
    ImageView imageView;
    Uri mUri;
    private StorageReference mStorageRef;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_picture);

        upload = (Button)findViewById(R.id.upload);
        upload.setOnClickListener(this);
        chossefile = (Button)findViewById(R.id.choosefile);
        chossefile.setOnClickListener(this);

        imageView = (ImageView)findViewById(R.id.imageView);

        String s= "https://firebasestorage.googleapis.com/v0/b/primeedu-7f9bc.appspot.com/o/1558032720244.jpg?alt=media&token=183010b5-86f9-4d10-8413-a2d79205062c";

        Glide.with(getApplicationContext())
                .load(s)
                .into(imageView);
        progress = (ProgressBar)findViewById(R.id.progress);

        //StorageReference mStorageRef;
        mStorageRef = FirebaseStorage.getInstance().getReference();
//        mStorageRef = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("uploads");

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.upload){
            upoloadImage();
        }
        if(v.getId() == R.id.choosefile){
            openFileChooser();
        }
    }

    private String getFileExtention(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    private void upoloadImage() {
        if(mUri!=null){
            final StorageReference fileRef = mStorageRef.child(System.currentTimeMillis()+"."+getFileExtention(mUri));
            fileRef.putFile(mUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progress.setProgress(0);
                                }
                            },500);

                            fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // getting image uri and converting into string
//                                    File f = new File(String.valueOf(uri));
//                                    Picasso.get().load(f).into(imageView);
                                    UploadImage uploadImage = new UploadImage("Hi",uri.toString());
                                    String upId = databaseReference.push().getKey();
                                    databaseReference.child(upId).setValue(uploadImage);
                                }
                            });

                          //  Picasso.get().load(taskSnapshot.getUploadSessionUri()).into(imageView);

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
                            progress.setProgress((int)pr);
                        }
                    });
        }
    }

    private void openFileChooser() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_GRANTED){
                String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permissions,PERMISSION_CODE);
            }
            else {
                System.out.println("asas");
                pickFromGallery();
            }
        }
    }

    private void pickFromGallery() {
        System.out.println("asasaaaaaSSSS");
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,10000);
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

        if(resultCode == RESULT_OK && requestCode == 10000){
            mUri = data.getData();

            //Picasso.get().load(mUri).into(imageView);
        }
    }
}
