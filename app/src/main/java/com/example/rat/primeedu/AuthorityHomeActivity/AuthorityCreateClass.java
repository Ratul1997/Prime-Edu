package com.example.rat.primeedu.AuthorityHomeActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rat.primeedu.Asapter.ClassNamesAdapter;
import com.example.rat.primeedu.Class.ClassName;
import com.example.rat.primeedu.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AuthorityCreateClass extends AppCompatActivity implements View.OnClickListener{

    String classUrl = "";
    Button add_class;
    List<ClassName> listClassName = new ArrayList<>();
    RecyclerView recyclerview;
    ClassNamesAdapter adapter;
    TextView school_name;
    String Schoolname,SchoolId;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authority_create_class);

        getIntentData();
        showLoading();
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

        getClasses();
    }



    private void getIntentData() {
        Schoolname = getIntent().getStringExtra("SchoolName").toString();
        SchoolId = getIntent().getStringExtra("SchoolId").toString();
    }

    private void init(){
        add_class = (Button)findViewById(R.id.add_class);
        add_class.setOnClickListener(this);

        school_name = (TextView)findViewById(R.id.school_name);
        school_name.setText(Schoolname);

        recyclerview = (RecyclerView)findViewById(R.id.recyclerview);

        findViewById(R.id.back).setOnClickListener(this);
        adapter = new ClassNamesAdapter(listClassName,this,SchoolId,Schoolname);
        final LinearLayoutManager mLayoutManager= new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerview.setLayoutManager(mLayoutManager);
        recyclerview.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.add_class){
            navigate();
        }
        if(v.getId() == R.id.back){
            finish();
        }
    }
    private void navigate(){
        Intent intent  = new Intent(this, AddSection.class);
        intent.putExtra("SchoolId",SchoolId);
        startActivity(intent);
    }

    private void getClasses() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Classes/"+SchoolId+"/class/");

        int total = 0;
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int count = 0;
                listClassName.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    count = 0;
                    for(DataSnapshot snapshot1 :snapshot.child("Section").getChildren()){
                        count++;

                    }
                    listClassName.add(new ClassName(snapshot.getKey(),count));
                }
                dialog.dismiss();
                Collections.reverse(listClassName);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(AuthorityCreateClass.this, "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
