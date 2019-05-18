package com.example.rat.primeedu.Teacher;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.rat.primeedu.R;

public class InsideSectionActivity extends AppCompatActivity implements View.OnClickListener {

    String Currentclass,CurrentSection,CurrentStudent;
    ImageButton attendence, gMarks,vStudent;
    LinearLayout lGM,lVs,lAt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inside_section);

        getIntentData();
        init();
    }

    private void getIntentData() {
        Currentclass = getIntent().getStringExtra("classNo").toString();
        CurrentSection = getIntent().getStringExtra("sectionNo").toString();
        CurrentStudent =  getIntent().getStringExtra("stdNo").toString();
    }
    private void takeAttendence(){
        Intent intent = new Intent(this, TakeAttendenceActivity.class);

        intent.putExtra("stdNo",CurrentStudent);
        intent.putExtra("sectionNo",CurrentSection);
        intent.putExtra("classNo",Currentclass);
        startActivity(intent);
    }
    private void viewMarks(){
        System.out.println(CurrentStudent);
        Intent intent = new Intent(this, ViewStudentActivity.class);
        intent.putExtra("stdNo",CurrentStudent);
        intent.putExtra("sectionNo",CurrentSection);
        intent.putExtra("clsname",Currentclass);
        startActivity(intent);
    }
    private void giveMarks(){
        Intent intent = new Intent(this, GiveMarksToStudent.class);
        intent.putExtra("section",CurrentSection);
        intent.putExtra("class",Currentclass);
        intent.putExtra("id","");
        startActivity(intent);
    }

    private void init() {
      lAt = (LinearLayout)findViewById(R.id.lAt);
      lAt.setOnClickListener(this);

      lGM = (LinearLayout)findViewById(R.id.lGM);
      lGM.setOnClickListener(this);

      lVs = (LinearLayout)findViewById(R.id.lVs);
      lVs.setOnClickListener(this);

      attendence = (ImageButton)findViewById(R.id.attendence);
      attendence.setOnClickListener(this);

      vStudent = (ImageButton)findViewById(R.id.vStudent);
      vStudent.setOnClickListener(this);

      gMarks = (ImageButton)findViewById(R.id.gMarks);
      gMarks.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.lAt || v.getId() == R.id.attendence){
            takeAttendence();

        }if(v.getId() == R.id.lGM || v.getId() == R.id.gMarks){
            giveMarks();
        }if(v.getId() == R.id.lVs || v.getId() == R.id.vStudent){
            viewMarks();
        }
    }
}
