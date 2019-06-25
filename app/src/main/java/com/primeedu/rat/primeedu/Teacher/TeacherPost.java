package com.primeedu.rat.primeedu.Teacher;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.primeedu.rat.primeedu.Asapter.AnnounceMentAdapter;
import com.primeedu.rat.primeedu.Asapter.HomeWorkAdapter;
import com.primeedu.rat.primeedu.Class.AnnouncementWithClass;
import com.primeedu.rat.primeedu.Class.TeachersPostClass;
import com.primeedu.rat.primeedu.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeacherPost extends AppCompatActivity implements View.OnClickListener {

    ImageButton post;
    RequestQueue requestQueue;
    List<Pair<String,AnnouncementWithClass>> Homework = new ArrayList<>();
    List<Pair<String,AnnouncementWithClass>> Announcement = new ArrayList<>();
    EditText newPostWritten;
    RecyclerView recyclerView1, recyclerView2;
    AnnounceMentAdapter adapter1;
    HomeWorkAdapter adapter2;
    String TeacherId,Schoolname,SchoolId,TeacherName;
    TextView school_name;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_post);
        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.actionbar));


        getIntentDatat();
        init();
    }

    private void getIntentDatat() {
        TeacherId = getIntent().getStringExtra("Id").toString();
        Schoolname = getIntent().getStringExtra("SchoolName").toString();
        SchoolId = getIntent().getStringExtra("SchoolId").toString();
        TeacherName = getIntent().getStringExtra("TeacherName").toString();
    }
    private void showLoading() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait ...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }

    private void init(){

        requestQueue = Volley.newRequestQueue(this);
        school_name = (TextView)findViewById(R.id.school_name);
        school_name.setText(Schoolname);
        findViewById(R.id.back).setOnClickListener(this);


        recyclerView1 = (RecyclerView)findViewById(R.id.recyclerView1);
        recyclerView2 = (RecyclerView)findViewById(R.id.recyclerView2);

        newPostWritten = (EditText)findViewById(R.id.newPostWritten);


        adapter1 = new AnnounceMentAdapter(Announcement,TeacherPost.this,"2",SchoolId);
        final LinearLayoutManager mLayoutManager= new LinearLayoutManager(TeacherPost.this, LinearLayoutManager.VERTICAL, true);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerView1.setLayoutManager(mLayoutManager);
        recyclerView1.setAdapter(adapter1);



        adapter2 = new HomeWorkAdapter(Homework,TeacherPost.this,"2",SchoolId);
        final LinearLayoutManager mLayoutManagers= new LinearLayoutManager(TeacherPost.this, LinearLayoutManager.VERTICAL, true);
        mLayoutManagers.setReverseLayout(true);
        mLayoutManagers.setStackFromEnd(true);
        recyclerView2.setLayoutManager(mLayoutManagers);
        recyclerView2.setAdapter(adapter2);

        findViewById(R.id.post).setOnClickListener(this);
        getAllAnnounceMents();
    }

    private void getAllAnnounceMents(){
        showLoading();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final String path = "Classes/"+SchoolId+"/class";
        DatabaseReference myRef = database.getReference(path);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Announcement.clear();
                Homework.clear();

                for(final DataSnapshot snapshot: dataSnapshot.getChildren()){


                    for(DataSnapshot snapshot1: snapshot.child("AnnounceMents").child("HomeWork").getChildren()){
                        System.out.println(snapshot1.getKey());
                        TeachersPostClass postClass = snapshot1.getValue(TeachersPostClass.class);
                        if(postClass.getTeacherid().equals(TeacherId)){
                            AnnouncementWithClass withClass = new AnnouncementWithClass(postClass.getMsg(),snapshot.getKey());
                            Homework.add(new Pair<String, AnnouncementWithClass>(snapshot1.getKey(),withClass));
                        }
                    }
                    for(DataSnapshot snapshot1: snapshot.child("AnnounceMents").child("AnnounceMent").getChildren()){
                        System.out.println(snapshot1.getKey());
                        TeachersPostClass postClass = snapshot1.getValue(TeachersPostClass.class);
                        if(postClass.getTeacherid().equals(TeacherId)){
                            AnnouncementWithClass withClass = new AnnouncementWithClass(postClass.getMsg(),snapshot.getKey());
                            Announcement.add(new Pair<String, AnnouncementWithClass>(snapshot1.getKey(),withClass));
                        }
                    }
                }
                Collections.reverse(Homework);
                Collections.reverse(Announcement);
                adapter1.notifyDataSetChanged();
                adapter2.notifyDataSetChanged();
                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        if(R.id.back == v.getId()){
            finish();
        }
        if(R.id.post == v.getId()){
            if(newPostWritten.getText().toString().equals(""))return;

            final Dialog dialog = new Dialog(TeacherPost.this);

            dialog.setContentView(R.layout.selectpostoption);


            final Spinner selectClass = dialog.findViewById(R.id.selectClass);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.classtype, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            selectClass.setAdapter(adapter);


            Button announcement = (Button) dialog.findViewById(R.id.announcement);
            Button hw = (Button) dialog.findViewById(R.id.hw);
            hw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(selectClass.getSelectedItemPosition() == 0){
                        Toast.makeText(TeacherPost.this, "Choose A Class", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    post("HomeWork",selectClass.getSelectedItem().toString());
                    dialog.dismiss();
                }
            });
            announcement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(selectClass.getSelectedItemPosition() == 0){
                        Toast.makeText(TeacherPost.this, "Choose A Class", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    post("AnnounceMent",selectClass.getSelectedItem().toString());
                    dialog.dismiss();
                }
            });

            dialog.show();
        }
    }

    private void post(String Destination, String Clas){

        String msg = newPostWritten.getText().toString();
        String path = "Classes/"+SchoolId+"/class/"+Clas+"/AnnounceMents/"+Destination;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(path);

        notieKey(Clas);
        TeachersPostClass post = new TeachersPostClass(msg,TeacherId,TeacherName);
        myRef.child(makeKey()).setValue(post);
        newPostWritten.setText("");

    }
    private String makeKey(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");
        Date date = new Date();

        return  formatter.format(date);
    }

    private void notieKey(String Class){

        JSONObject data = new JSONObject();
        try {
            String s = SchoolId+Class;
            String ttl = TeacherName+"has posted a new announcement";
            data.put("to","/topics/"+s);
            JSONObject notify = new JSONObject();
            notify.put("title",ttl);
            notify.put("body",newPostWritten.getText().toString());

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
