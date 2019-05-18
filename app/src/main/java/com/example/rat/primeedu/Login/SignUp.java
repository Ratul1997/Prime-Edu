package com.example.rat.primeedu.Login;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rat.primeedu.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUp extends AppCompatActivity implements View.OnClickListener{

    EditText login,pass;
    Button dones;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        init();
    }
    private void init() {
        login = (EditText)findViewById(R.id.login);
        pass = (EditText)findViewById(R.id.pass);

        dones = (Button)findViewById(R.id.dones);
        dones.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(R.id.dones == v.getId()){
            System.out.println(login.getText().toString()+" "+pass.getText().toString());
            String email = login.getText().toString();
            String password = pass.getText().toString();
            loginF("abc2@gmail.com", "123456");
        }
    }

    private void loginF(String email, String password) {
        System.out.println(email +" "+password);
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();

                            System.out.println(user.getUid());
                        } else {
                            // If sign in fails, display a message to the user.

                        }

                        // ...
                    }
                });
    }
}
