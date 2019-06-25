package com.primeedu.rat.primeedu;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.primeedu.rat.primeedu.AuthorityHomeActivity.AuthorityMainActivity;
import com.primeedu.rat.primeedu.Login.LoginActivity;
import com.primeedu.rat.primeedu.Student.StudentActivity;
import com.primeedu.rat.primeedu.Teacher.TeacherActivity;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    private static int Splash_time_out = 3000;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                init();

            }
        }, Splash_time_out);
    }

    private void init(){
        FirebaseMessaging.getInstance().subscribeToTopic("PrimeEdu");
        SharedPreferences sharedPreferences = getSharedPreferences("Users", 0);

        if(sharedPreferences.getBoolean("isLogIn",false)) {

            String Id = sharedPreferences.getString("UserId", "").toString();
            String type = sharedPreferences.getString("UserType", "").toString();
            String code = sharedPreferences.getString("code", "").toString();
            System.out.println(type);


            if(type.equals("") || Id.equals("") || code.equals("")){
                finish();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);

                return;
            }
            switch (type){
                case "1":
                    finish();
                    Intent intent1 = new Intent(MainActivity.this, StudentActivity.class);
                    intent1.putExtra("Id",Id);
                    intent1.putExtra("code",code);
                    startActivity(intent1);
                    break;
                case "2":
                    finish();
                    Intent intent2 = new Intent(MainActivity.this, TeacherActivity.class);
                    intent2.putExtra("Id",Id);
                    intent2.putExtra("code",code);
                    startActivity(intent2);
                    break;
                case "3":
                    finish();
                    Intent intent = new Intent(MainActivity.this, AuthorityMainActivity.class);
                    intent.putExtra("Id",Id);
                    intent.putExtra("code",code);
                    startActivity(intent);
                    break;
            }
        }
        else{
            finish();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

    }
}
///https://myaccount.google.com/lesssecureapps