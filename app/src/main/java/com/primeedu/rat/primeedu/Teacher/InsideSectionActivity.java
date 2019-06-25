package com.primeedu.rat.primeedu.Teacher;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.primeedu.rat.primeedu.R;

public class InsideSectionActivity extends AppCompatActivity implements View.OnClickListener {

    String Currentclass,CurrentSection,CurrentStudent,type;
    ImageButton attendence, gMarks,vStudent;
    LinearLayout lGM,lVs,lAt;
    ProgressDialog dialog;
    String SchoolId,SchoolName;
    TextView studentNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inside_section);
        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.actionbar));


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
    }

    private void getIntentData() {
        Currentclass = getIntent().getStringExtra("classNo").toString();
        CurrentSection = getIntent().getStringExtra("sectionNo").toString();
        CurrentStudent =  getIntent().getStringExtra("stdNo").toString();
        type = getIntent().getStringExtra("type").toString();
        SchoolId = getIntent().getStringExtra("schollId").toString();
        SchoolName = getIntent().getStringExtra("schoolname").toString();
    }
    private void takeAttendence(){

        Intent intent = new Intent(this, TakeAttendenceActivity.class);

        intent.putExtra("stdNo",CurrentStudent);
        intent.putExtra("sectionNo",CurrentSection);
        intent.putExtra("classNo",Currentclass);
        intent.putExtra("schoolId",SchoolId);
        intent.putExtra("schoolName",SchoolName);
        startActivity(intent);
    }
    private void viewMarks(){

        System.out.println("aaa+"+type);
        Intent intent = new Intent(this, ViewStudentActivity.class);
        intent.putExtra("stdNo",CurrentStudent);
        intent.putExtra("sectionNo",CurrentSection);
        intent.putExtra("clsname",Currentclass);
        intent.putExtra("schoolId",SchoolId);
        intent.putExtra("type",type);

        intent.putExtra("schoolName",SchoolName);
        startActivity(intent);
    }
    private void giveMarks(){
        Intent intent = new Intent(this, GiveMarksToStudent.class);
        intent.putExtra("section",CurrentSection);
        intent.putExtra("class",Currentclass);
        intent.putExtra("stdNo",CurrentStudent);
        intent.putExtra("schoolId",SchoolId);
        intent.putExtra("schoolName",SchoolName);
        startActivity(intent);
    }

    private void init() {
      lAt = (LinearLayout)findViewById(R.id.lAt);
      lAt.setOnClickListener(this);

      lGM = (LinearLayout)findViewById(R.id.lGM);
      lGM.setOnClickListener(this);

      lVs = (LinearLayout)findViewById(R.id.lVs);
      lVs.setOnClickListener(this);

      findViewById(R.id.back).setOnClickListener(this);

      studentNumber = (TextView)findViewById(R.id.studentNumber);
      studentNumber.setText(CurrentStudent);

      TextView cls = (TextView)findViewById(R.id.cls);
      cls.setText("CLASS: "+Currentclass);

      TextView sec = (TextView)findViewById(R.id.sec);
      sec.setText("SECTION: "+CurrentSection);

      TextView school_name = (TextView)findViewById(R.id.school_name);
      school_name.setText(SchoolName);
      switch (type){
          case "2":

              lAt.setEnabled(true);
              lGM.setEnabled(true);
              lAt.setVisibility(View.VISIBLE);
              lGM.setVisibility(View.VISIBLE);
              break;
          case "3":
              lAt.setEnabled(false);
              lGM.setEnabled(false);
              lAt.setVisibility(View.INVISIBLE);
              lGM.setVisibility(View.INVISIBLE);

              LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(0,0);
              lAt.setLayoutParams(parms);
              lGM.setLayoutParams(parms);
              break;
      }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.lAt ){
            takeAttendence();

        }if(v.getId() == R.id.lGM){
            giveMarks();
        }if(v.getId() == R.id.lVs ){
            viewMarks();
        }
        if(v.getId() == R.id.back){
            finish();
        }
    }
}
