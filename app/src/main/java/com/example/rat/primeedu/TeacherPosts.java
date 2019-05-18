package com.example.rat.primeedu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.rat.primeedu.Class.TeachersNotice;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TeacherPosts extends AppCompatActivity {

    EditText text,Class;
    Button Hw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_posts);


        text = (EditText)findViewById(R.id.texts);
        Class = (EditText)findViewById(R.id.Class);

        Hw = (Button)findViewById(R.id.Hw);
        Hw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = database.getReference("Classes/24sOoni50iRw2tDa2Du0mhmswrG3/class/3/Notice");

                TeachersNotice teachersNotice = new TeachersNotice("Testing","20000003");
                databaseReference.child("Announcement").setValue(teachersNotice);



            }
        });
    }
}
