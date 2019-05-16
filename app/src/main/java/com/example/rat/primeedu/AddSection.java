package com.example.rat.primeedu;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AddSection extends AppCompatActivity implements View.OnClickListener {

    ImageButton add;
    LinearLayout ll;
    ImageButton done;
    EditText classNo;
    List<EditText>studentNo = new ArrayList<>();
    List<Integer> position = new ArrayList<Integer>();
    List<Integer>deletediTEM   = new ArrayList<>();
    List<EditText>sectionName = new ArrayList<>();
    final private String classUrl = "SchoolName/Class/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_section2);
        init();
    }
    public void  init(){
        ll = (LinearLayout)findViewById(R.id.ll);
        add = (ImageButton)findViewById(R.id.add);
        add.setOnClickListener(this);
        done = (ImageButton)findViewById(R.id.done);
        done.setOnClickListener(this);

        classNo = (EditText)findViewById(R.id.classNo);
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
    }
    private void getDatas(){
        delteRemoveDatas();
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        for(int i =  0;i<sectionName.size();i++){
            if(sectionName.get(i).getText().toString().equals("") || studentNo.get(i).getText().toString().equals("")){
                Toast.makeText(this, "Some TextField is empty", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        String Class = classNo.getText().toString();
        for(int i = 0 ;i<sectionName.size();i++){
            System.out.println(sectionName.get(i).getText().toString());

            String sec = sectionName.get(i).getText().toString();

            String stdnt = studentNo.get(i).getText().toString();



            for(int k = 1 ;k<=Integer.parseInt(stdnt);k++){

                DatabaseReference myRef = database.getReference(classUrl+"/"+Class+"/Section/"+sec+"/"+k);

                String id = myRef.push().getKey();
                myRef.child("Details").setValue(new ListOfStudentsIdentity("","","","","","",""));
            }
        }
        Intent intent = new Intent(this,TeacherActivity.class);
        navigate(intent);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.add){

            onAddField(v);
        }
        if(v.getId() == R.id.done)
        {
            if(classNo.getText().toString().equals("")){
                Toast.makeText(this, "Have to Give Class Num", Toast.LENGTH_SHORT).show();
            }
            else{
                getDatas();

            }

        }

    }

    private void navigate(Intent intent){
        startActivity(intent);
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
}
