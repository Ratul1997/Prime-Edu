package com.example.rat.primeedu;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.rat.primeedu.Login.SignUpTeacher;
import com.example.rat.primeedu.Login.SignupAuthority;
import com.example.rat.primeedu.Login.SignupStudent;

public class SignUp2 extends AppCompatActivity {
    String[] typeList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up2);
        typeList = new String[] {"Student", "Teacher", "Authority"};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(SignUp2.this);
        mBuilder.setTitle("Account type");
        mBuilder.setIcon(R.drawable.icon);
        mBuilder.setSingleChoiceItems(typeList, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0){
                    Intent intent = new Intent(SignUp2.this, SignupStudent.class);
                    startActivity(intent);
                    dialog.dismiss();
                }
                else if(which == 1){
                    Intent intent = new Intent(SignUp2.this, SignUpTeacher.class);
                    startActivity(intent);
                    dialog.dismiss();
                }
                else if(which == 2){
                    Intent intent = new Intent(SignUp2.this, SignupAuthority.class);
                    startActivity(intent);
                    dialog.dismiss();
                }
            }
        });
        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

}
