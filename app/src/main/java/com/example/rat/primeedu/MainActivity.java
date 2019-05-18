package com.example.rat.primeedu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.rat.primeedu.Login.Login;
import com.example.rat.primeedu.Login.LoginActivity;
import com.example.rat.primeedu.Login.SignUp;
import com.example.rat.primeedu.R;
import com.example.rat.primeedu.Teacher.TeacherActivity;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
