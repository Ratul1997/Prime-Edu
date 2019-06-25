package com.primeedu.rat.primeedu.Student;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.primeedu.rat.primeedu.R;

public class StudentNotices extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_notices);
        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.colorAccent));


    }
}
