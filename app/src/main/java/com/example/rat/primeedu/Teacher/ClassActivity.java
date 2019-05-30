package com.example.rat.primeedu.Teacher;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.rat.primeedu.R;
import com.example.rat.primeedu.Asapter.SectionAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClassActivity extends AppCompatActivity implements View.OnClickListener{

    TextView classNo;
    private RecyclerView recyclerView;
    final private String classUrl = "SchoolName/Class/";
    List<String>Secion = new ArrayList<>();
    List<Integer> Student = new ArrayList<Integer>();
    SectionAdapter adapter;
    ImageButton back;
    ProgressDialog dialog;
    String SchoolName;
    String SchoolId,type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);
        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.actionbar));

        init();
        showLoading();

        getSections();
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

    private void getSections() {
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);


        String clsss = getIntent().getStringExtra("clsname");
        classNo.setText(clsss);
        System.out.println(clsss);

        adapter = new SectionAdapter(Secion,Student,this,clsss,type,SchoolId,SchoolName);
        final LinearLayoutManager mLayoutManager= new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Classes/"+SchoolId+"/class/"+classNo.getText()+"/Section/");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Secion.clear();
                Student.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
//                    System.out.println(snapshot.getKey()+" "+snapshot.getChildrenCount());
                    Secion.add(snapshot.getKey());
                    Student.add(Integer.parseInt(String.valueOf(snapshot.getChildrenCount())));
                }
                dialog.dismiss();
                Collections.reverse(Secion);
                Collections.reverse(Student);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void init(){
        classNo = (TextView)findViewById(R.id.classNo);

        back = (ImageButton)findViewById(R.id.back);
        back.setOnClickListener(this);
        SchoolId = getIntent().getStringExtra("id").toString();
        type = getIntent().getStringExtra("type").toString();
        SchoolName = getIntent().getStringExtra("schoolname").toString();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.back){
            finish();
        }
    }

}
