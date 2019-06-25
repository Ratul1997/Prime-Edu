package com.primeedu.rat.primeedu.AuthorityHomeActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.primeedu.rat.primeedu.Class.ListOfStudentsIdentity;
import com.primeedu.rat.primeedu.R;
import com.primeedu.rat.primeedu.Teacher.TeacherActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class AddSection extends AppCompatActivity implements View.OnClickListener {

    ImageButton add;
    LinearLayout ll;
    Button done;
    String SchoolId;
    List<EditText>studentNo = new ArrayList<>();
    List<Integer> position = new ArrayList<Integer>();
    List<Integer>deletediTEM   = new ArrayList<>();
    List<EditText>sectionName = new ArrayList<>();
    final private String classUrl = "SchoolName/Class/";
    ProgressDialog dialog;
    Spinner selectClass;
    String ClassNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_section2);

        getIntentData();

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

        getDatas();
    }

    private void getIntentData() {
        SchoolId = getIntent().getStringExtra("SchoolId").toString();
    }

    public void  init(){
        ll = (LinearLayout)findViewById(R.id.ll);
        add = (ImageButton)findViewById(R.id.add);
        add.setOnClickListener(this);
        done = (Button)findViewById(R.id.done);
        done.setOnClickListener(this);
        selectClass = (Spinner)findViewById(R.id.selectClass);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.classtype, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectClass.setAdapter(adapter);

    }

    private void onAddField(View v) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout rowView = (LinearLayout) inflater.inflate(R.layout.add_section, null);


        final ImageButton button = (ImageButton)rowView.findViewById(R.id.delete);
        button.setId(ll.getChildCount()-1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletediTEM.add(button.getId());
                ViewGroup vg = (ViewGroup)v.getParent();
                vg.removeAllViews();
            }
        });

        EditText editText = (EditText)rowView.findViewById(R.id.stdntNo);
        editText.setId(ll.getChildCount()-1);
        studentNo.add(editText);
        position.add(ll.getChildCount()-1);


        EditText editText2 = (EditText)rowView.findViewById(R.id.sectionNo);
        editText2.setId(ll.getChildCount()-1);
        sectionName.add(editText2);

        ll.addView(rowView, ll.getChildCount() - 1);
    }

    private void delteRemoveDatas(){
        Collections.sort(deletediTEM);
        int j = 0;
        for(int i =0 ;i<studentNo.size() && j<deletediTEM.size();i++){
            if(deletediTEM.get(j).equals(position.get(i))){
                studentNo.remove(i);
                sectionName.remove(i);
                continue;
            }
        }

//        for(int i = 0;i<sectionName.size();i++){
//            System.out.println(sectionName.get(i).getText().toString());
//        }
    }

    private void getDatas(){
        delteRemoveDatas();
        savedata();
    }
    private void savedata(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        for(int i =  0;i<sectionName.size();i++){
            if(sectionName.get(i).getText().toString().equals("") || studentNo.get(i).getText().toString().equals("")){
                Toast.makeText(this, "Some TextField is empty", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        String Class = ClassNo;
        int secCount = 0;
        for(int i = 0 ;i<sectionName.size();i++){
            System.out.println(sectionName.get(i).getText().toString());

            String sec = sectionName.get(i).getText().toString();

            final String stdnt = studentNo.get(i).getText().toString();

            secCount++;

            int studentCount = 0;
            for(int k = 1 ;k<=Integer.parseInt(stdnt);k++){

                DatabaseReference databaseReference = database.getReference("Classes/"+SchoolId+"/class/"+Class+"/Section/"+sec+"/");
                final int finalSecCount = secCount;
                final int finalK = k;
                studentCount++;
                final int finalStudentCount = studentCount;
                databaseReference.child(Integer.toString(k)).child("Created").setValue(new Date())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    if(finalSecCount == sectionName.size() && finalStudentCount == Integer.parseInt(stdnt)){

                                        dialog.dismiss();
                                        openDialogS("Done");
                                    }
                                    else{
                                        dialog.dismiss();
                                     //   openDialogS("Please check your internet connection and submit again.");
                                    }
                                    System.out.println(finalSecCount+" "+finalStudentCount);
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                dialog.dismiss();
                                openDialogS("Something is wrong.Please Check your internet connection and submit again.");
                            }
                        });
            }
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.add){

            onAddField(v);
        }
        if(v.getId() == R.id.done)
        {
            if(selectClass.getSelectedItemPosition() == 0){
                Toast.makeText(this, "Select a class", Toast.LENGTH_SHORT).show();
                return;
            }
            else{
                ClassNo = selectClass.getSelectedItem().toString();
                showLoading();
            }

        }

    }

    private void backButton(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Your file will not be saved." +
                "Are you sure, You wanted to make decision");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {


                        finish();
                    }
                });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK){
            backButton();
            return true;
        }
        return false;
    }
    private void openDialogS(String msg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(msg);
        alertDialogBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        finish();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
