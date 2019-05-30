package com.example.rat.primeedu.Teacher;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rat.primeedu.Asapter.ClassNamesAdapter;
import com.example.rat.primeedu.AuthorityHomeActivity.AuthorityCreateClass;
import com.example.rat.primeedu.Class.ClassName;
import com.example.rat.primeedu.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TeacherClassActivity extends AppCompatActivity implements View.OnClickListener {

    ProgressDialog dialog;
    String classUrl = "";
    Button add_class;
    List<String> listClassName = new ArrayList<>();
    RecyclerView recyclerview;
    TeacherClasses adapter;
    TextView school_name;

    String Schoolname,SchoolId,TeacherId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_class);
        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.actionbar));

        getIntentData();
        init();
        showLoading();

    }

    private void getIntentData() {
        TeacherId = getIntent().getStringExtra("Id").toString();
        Schoolname = getIntent().getStringExtra("SchoolName").toString();
        SchoolId = getIntent().getStringExtra("SchoolId").toString();
    }

    private void showLoading() {

        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Please Wait ...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();
        dialog.show();

        getClasseses();
    }



    private void init(){
        add_class = (Button)findViewById(R.id.add_class);
        add_class.setOnClickListener(this);

        school_name = (TextView)findViewById(R.id.school_name);
        school_name.setText(Schoolname);

        recyclerview = (RecyclerView)findViewById(R.id.recyclerview);

        findViewById(R.id.back).setOnClickListener(this);
        adapter = new TeacherClasses(listClassName,this,SchoolId,Schoolname,TeacherId);
        final LinearLayoutManager mLayoutManager= new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerview.setLayoutManager(mLayoutManager);
        recyclerview.setAdapter(adapter);
    }


    private void getClasseses() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users/Teachers/"+TeacherId+"/Class/");

        int total = 0;
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listClassName.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    listClassName.add(snapshot.getKey());
                }
                dialog.dismiss();
                Collections.reverse(listClassName);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(TeacherClassActivity.this, "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.add_class){
            openNumberPicker();
        }
        if(v.getId() == R.id.back){
            finish();
        }
    }
    private void openNumberPicker() {
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.prompts, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final Spinner spinner_id = (Spinner) promptsView
                .findViewById(R.id.spinner_id);

        // set dialog message
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.classtype, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_id.setAdapter(adapter);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                if(spinner_id.getSelectedItemPosition() == 0){

                                }
                                else{
                                    saveData(spinner_id.getSelectedItem().toString());
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

    private void saveData(String s) {
        showLoading();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users/Teachers/"+TeacherId+"/Class/");
        ref.child(s).setValue("")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            dialog.dismiss();
                        }
                    }
                });
    }
}
