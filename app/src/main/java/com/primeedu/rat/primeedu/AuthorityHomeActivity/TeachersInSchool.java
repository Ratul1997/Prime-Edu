package com.primeedu.rat.primeedu.AuthorityHomeActivity;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.primeedu.rat.primeedu.Asapter.ClassNamesAdapter;
import com.primeedu.rat.primeedu.Asapter.ProfileAdapter;
import com.primeedu.rat.primeedu.Class.SortByName;
import com.primeedu.rat.primeedu.Class.TeacherDetails;
import com.primeedu.rat.primeedu.R;
import com.futuremind.recyclerviewfastscroll.FastScroller;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TeachersInSchool extends AppCompatActivity implements View.OnClickListener {

    ProgressDialog dialog;
    private String SchoolId,SchoolName,type;
    ProfileAdapter adapter ;
    RecyclerView recyclerview;
    TextView school_name;
    FastScroller fastScroller;

    List<Pair<String,TeacherDetails>>list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teachers_in_school);


        getIntentData();
        init();
        showLoading();
    }

    private void init() {

        school_name = (TextView)findViewById(R.id.school_name);
        school_name.setText(SchoolName);

        recyclerview = (RecyclerView)findViewById(R.id.recyclerview);
        fastScroller = (FastScroller) findViewById(R.id.fastscroll);

        findViewById(R.id.back).setOnClickListener(this);

        adapter = new ProfileAdapter(list,this,SchoolId,type);
        final LinearLayoutManager mLayoutManager= new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerview.setLayoutManager(mLayoutManager);
        recyclerview.setAdapter(adapter);

        fastScroller.setRecyclerView(recyclerview);
    }

    private void getIntentData() {
        SchoolId = getIntent().getStringExtra("SchoolId").toString();
        SchoolName = getIntent().getStringExtra("SchoolName").toString();
        type = getIntent().getStringExtra("type").toString();
    }

    private void showLoading() {

        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Please Wait ...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();
        dialog.show();

        getDatas();
    }

    private void getDatas() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Schools/"+SchoolId+"/Teachers/");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() == 0){


                    list.clear();
                    adapter.notifyDataSetChanged();
                    dialog.dismiss();
                    return;
                }
                list.clear();
                final TeacherDetails[] teacherDetails = {null};

                final int countt = (int) dataSnapshot.getChildrenCount();
                final int[] k = {0};
                for(final DataSnapshot snapshot : dataSnapshot.getChildren()){
                   System.out.println(snapshot.getValue().toString());
                   String pth = "Users/Teachers/"+snapshot.getValue().toString()+"/Details/";
                   System.out.println(pth);
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference(pth);
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if(dataSnapshot.getValue() == null){
                                dialog.dismiss();
                                Toast.makeText(TeachersInSchool.this, "No Teacher.", Toast.LENGTH_SHORT).show();
                            }
                            else{

                                k[0]++;
                                System.out.println(dataSnapshot.getValue(TeacherDetails.class));
                                TeacherDetails teacherDetails1 = dataSnapshot.getValue(TeacherDetails.class);
                                System.out.println(teacherDetails1.getTeachername());

                                list.add(new Pair<String, TeacherDetails>(snapshot.getValue().toString(),teacherDetails1));
                                System.out.println(k[0]+","+countt);
                                if(k[0] == countt){
                                    System.out.println(k[0]+","+countt);
                                    dialog.dismiss();
                                    System.out.println(list.size());

                                    Collections.sort(list, new SortByName());

                                    for(int i = 0 ;i<list.size();i++){
                                        System.out.println(list.get(i).second.getTeachername());
                                    }
                                    adapter.notifyDataSetChanged();
                                }

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.back){
            finish();
        }
    }
}

