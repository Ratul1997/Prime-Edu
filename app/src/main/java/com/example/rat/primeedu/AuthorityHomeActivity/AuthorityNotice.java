package com.example.rat.primeedu.AuthorityHomeActivity;

import android.app.ProgressDialog;
import android.arch.paging.PagedList;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rat.primeedu.Asapter.ClassNamesAdapter;
import com.example.rat.primeedu.Asapter.NoticeAdapter;
import com.example.rat.primeedu.Class.NoticeDetails;
import com.example.rat.primeedu.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.shreyaspatil.firebase.recyclerpagination.DatabasePagingOptions;
import com.shreyaspatil.firebase.recyclerpagination.FirebaseRecyclerPagingAdapter;
import com.shreyaspatil.firebase.recyclerpagination.LoadingState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AuthorityNotice extends AppCompatActivity implements View.OnClickListener{

    TextView moreOptions,school_name;
    private String SchoolId,Schoolname,type;
    RecyclerView mRecyclerView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    FirebaseRecyclerPagingAdapter maapter;
    NoticeAdapter adapter;
    List<Pair<String,NoticeDetails>> list = new ArrayList<>();
    private String last="";
    ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authority_notice);

        getIntentData();
        init();
    }

    private void getIntentData() {
        Schoolname = getIntent().getStringExtra("SchoolName").toString();
        SchoolId = getIntent().getStringExtra("SchoolId").toString();
        type = getIntent().getStringExtra("type").toString();
    }
    private void showLoading() {
   System.out.println("easas");

        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Please Wait ...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();
        dialog.show();

    }

    private void init() {

        Button addpost = (Button)findViewById(R.id.add_post);

        System.out.println(type);
        if(type.equals("2"))addpost.setVisibility(View.INVISIBLE);
        else addpost.setOnClickListener(this);


        findViewById(R.id.back).setOnClickListener(this);

        school_name = (TextView)findViewById(R.id.school_name);
        school_name.setText(Schoolname);

        //Initialize RecyclerView


        mRecyclerView = findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);

        if(type.equals("3")){
            adapter = new NoticeAdapter(list,this,SchoolId,"3");
        }
        else {
            adapter = new NoticeAdapter(list,this,SchoolId,"2");
        }
        LinearLayoutManager mManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mManager);
        createViews();
        mRecyclerView.setAdapter(adapter);
    }
    private void createViews() {
        showLoading();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Schools/" + SchoolId + "/Notice");

        Query query = mDatabase;
        query.limitToLast(60)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //NoticeDetails noticeDetails;
                        if(dataSnapshot.getValue()== null){
                            dialog.dismiss();

                            adapter.notifyDataSetChanged();
                        }else{
                            dialog.dismiss();
                            list.clear();
                            for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                                list.add(new Pair<String, NoticeDetails>(snapshot.getKey(),snapshot.getValue(NoticeDetails.class)));
                                System.out.println(snapshot.getKey());
                                System.out.println("sss"+snapshot.getValue(NoticeDetails.class).getMsgUrl());
                                last = snapshot.getKey();
                            }
                            System.out.println(list.size());

                            Collections.reverse(list);
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.add_post){
            Intent intent = new Intent(AuthorityNotice.this,CreatePost.class);
            intent.putExtra("SchoolId",SchoolId);
            intent.putExtra("SchoolName",school_name.getText().toString());
            startActivity(intent);
        }
        if(v.getId() == R.id.back){
            finish();
        }
    }
}
