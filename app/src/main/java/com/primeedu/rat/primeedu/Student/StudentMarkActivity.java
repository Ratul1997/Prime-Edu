package com.primeedu.rat.primeedu.Student;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.primeedu.rat.primeedu.Asapter.ViewPagerAdapter;
import com.primeedu.rat.primeedu.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentMarkActivity extends AppCompatActivity implements View.OnClickListener{

    TabLayout tablayout;
    ViewPager viewPager;
    Dialog dialog;

    private TextView nameOfStudent;
    String Schoolname;
    CircleImageView profilePic;
    private String CurrentSection,Currentclass,CurrentStudent,SchoolCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_mark);



        getIntentData();
        createTabbar();
    }

    private void getIntentData() {

        Schoolname = getIntent().getStringExtra("SchoolName").toString();
        Currentclass = getIntent().getStringExtra("class");
        CurrentSection = getIntent().getStringExtra("section");
        CurrentStudent = getIntent().getStringExtra("roll");
        SchoolCode = getIntent().getStringExtra("school");
        String img = getIntent().getStringExtra("img");
        String name = getIntent().getStringExtra("name");

        nameOfStudent = (TextView)findViewById(R.id.nameOfStudent);
        profilePic = findViewById(R.id.profilePic);

        nameOfStudent.setText(name);
        Glide.with(getApplicationContext())
                .load(img)
                .into(profilePic);

        TextView scl = findViewById(R.id.school_name);
        scl.setText(Schoolname);

        TextView ids = findViewById(R.id.ids);
        ids.setText(rollNumber(CurrentStudent));

        findViewById(R.id.back).setOnClickListener(this);

        findViewById(R.id.back).setOnClickListener(this);
    }

    private String rollNumber(String s){
        if(s.length() ==1 ){
            s = "00"+s;
        }
        else if(s.length() ==2 ){
            s = "0"+s;
        }

        return s;
    }
    private void showLoading() {

        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.loadingprogressbar);
        dialog.show();
    }

    private void createTabbar() {

        tablayout = (TabLayout)findViewById(R.id.tablayout);
        viewPager = (ViewPager)findViewById(R.id.viewPager);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.AddFragment(new StudentMarkDragment(CurrentStudent,Currentclass,CurrentSection,SchoolCode,"1",nameOfStudent.getText().toString()),"");

        viewPager.setAdapter(adapter);
        tablayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.back){
            finish();
        }
    }
}
