package com.example.rat.primeedu.Login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rat.primeedu.Class.ExamDetails;
import com.example.rat.primeedu.Class.SchoolDetails;
import com.example.rat.primeedu.Class.StudentDetails;
import com.example.rat.primeedu.Class.TeacherDetails;
import com.example.rat.primeedu.R;
import com.example.rat.primeedu.Teacher.AddSection;
import com.example.rat.primeedu.Teacher.TakeAttendenceActivity;
import com.example.rat.primeedu.TeacherPosts;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity implements View.OnClickListener {


    EditText login,pass,schoolname,Address,teacherTotal,totalStudent,totalStaff;
    Button done;
    private FirebaseAuth mAuth;
    private String Currentclass = "3";
    private String CurrentSection = "A";
    private String CurrentStudentId = "6";

    String teacherEmail = "teststudent7@gmail.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        init();
    }

    private void init() {
        login = (EditText)findViewById(R.id.login);
        pass = (EditText)findViewById(R.id.pass);
        schoolname = (EditText)findViewById(R.id.schoolname);
        Address = (EditText)findViewById(R.id.Address);
        teacherTotal = (EditText)findViewById(R.id.teacherTotal);
        totalStudent = (EditText)findViewById(R.id.totalStudent);
        totalStaff = (EditText)findViewById(R.id.totalStudent);

        done = (Button)findViewById(R.id.done);
        done.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(R.id.done == v.getId()){
           // finish();
            //checkInSchoolDataBase();
            Intent intent = new Intent(Login.this,TeacherPosts.class);
            startActivity(intent);
//         //createClasss();
//        //checkInSchoolDataBase();
//          createExamcode();
//            //createExamsSideForEachStudent("24sOoni50iRw2tDa2Du0mhmswrG3","1004");

        }
    }

    private void authenticationForStudent(final String code, final String Schoolcode){
        mAuth = FirebaseAuth.getInstance();

        mAuth.createUserWithEmailAndPassword(teacherEmail, "123456")
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(Login.this, "Success", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();

                            createStudent(user,code,Schoolcode);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
    private void generateStudentCode(final String Schoolcode){
        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference("Users/Student/count/");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String number = dataSnapshot.getValue().toString();
                int serial = Integer.parseInt(number)+1;
                databaseReference.setValue(serial);

                authenticationForStudent(Integer.toString(serial),Schoolcode);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void createStudent(final FirebaseUser user,final String StudentCode,final String SchoolCode ){


        StudentDetails studentDetails = new StudentDetails("Ratul","Ratan","Ratna","Teacher",
                "Teacher","01818419162","01917457387","Dhaka");
        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Users/Student/");
        databaseReference.child(user.getUid()).child("Details").setValue(studentDetails);

        DatabaseReference studentcode2 = firebaseDatabase.getReference("Users/Student/");
        studentcode2.child(user.getUid()).child("StudentCode").setValue(StudentCode);
        studentcode2.child(user.getUid()).child("CurrentClass").setValue(Currentclass);
        studentcode2.child(user.getUid()).child("CurrentSection").setValue(CurrentSection);
        studentcode2.child(user.getUid()).child("CurrentId").setValue(CurrentStudentId);

        studentcode2.child(user.getUid()).child("SchoolCode").setValue(SchoolCode);

        DatabaseReference createinSchool = firebaseDatabase.getReference("Schools/"+SchoolCode+"/Students/");
        createinSchool.child(createinSchool.push().getKey()).setValue(StudentCode);


        String path = "Classes/24sOoni50iRw2tDa2Du0mhmswrG3/class/"+Currentclass+"/Section/"+CurrentSection+"/"+CurrentStudentId+"/StudentCode/";
        DatabaseReference classref = firebaseDatabase.getReference(path);
        classref.setValue(StudentCode);

    }
    private void createClasss(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Classes/24sOoni50iRw2tDa2Du0mhmswrG3/class/9/Section/A");

        for(int i = 1 ;i<=63;i++){
            databaseReference.child(Integer.toString(i)).child("StudentCode").setValue("");
        }

    }

    private void createExam(String examcode, String SchoolCode){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("Exams/"+SchoolCode+"/"+examcode);

        ExamDetails examDetails = new ExamDetails("Term1","10/3/19","13/4/19","15/2/19");

        databaseReference.setValue(examDetails);

        createExamsSideForEachStudent(SchoolCode,examcode);


        DatabaseReference schoolRef = database.getReference("Schools/"+SchoolCode+"/Exams");
        schoolRef.child(schoolRef.push().getKey()).setValue(examcode);

    }
    private void setExamCode(int className, String sec, int totalStudent,  String schoolCode,String examcode){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String path = "Classes/"+schoolCode+"/class/"+className+"/Section/"+sec+"/";
        for(int i = 1 ;i<=totalStudent; i++){
            DatabaseReference databaseReference = database.getReference(path+i+"/Marks/Terms/");
            databaseReference.child(examcode).setValue("");
        }
    }

    private void createExamsSideForEachStudent(final String schoolCode, final String examCode) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("Classes/"+schoolCode+"/class");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){

                    for(DataSnapshot snapshot1 : snapshot.child("Section").getChildren()){

                        System.out.println(snapshot1.getKey()+" "+snapshot1.getChildrenCount());
                        setExamCode(Integer.parseInt(snapshot.getKey()),snapshot1.getKey(), (int) snapshot1.getChildrenCount(),schoolCode,examCode);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void createExamcode(){
        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference("Exams/count/");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String number = dataSnapshot.getValue().toString();
                int serial = Integer.parseInt(number)+1;
                databaseReference.setValue(serial);

                createExam(Integer.toString(serial),"24sOoni50iRw2tDa2Du0mhmswrG3");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void generateSchoolCode(String clubkey) {
        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference("Schools/count/");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String number = dataSnapshot.getValue().toString();
                int serial = Integer.parseInt(number)+1;
                databaseReference.setValue(serial);
                authSchool(Integer.toString(serial));
                //String padded = String.format("00001"+"2"+"%07d" , serial);

                //System.out.println(padded);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void authSchool(final String code) {
        mAuth = FirebaseAuth.getInstance();

        mAuth.createUserWithEmailAndPassword("testschool9@gmail.com", "123456")
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(Login.this, "Success", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();

                           createSchoolDataBase(user,code);
//                    createTeacherDataBase(user);
//                            createTeacherDataBase(user);
                            //System.out.println(user);
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }
    private void auth(final String code,final String schoolCode) {
        mAuth = FirebaseAuth.getInstance();

        mAuth.createUserWithEmailAndPassword(teacherEmail, "123456")
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(Login.this, "Success", Toast.LENGTH_SHORT).show();
                    FirebaseUser user = mAuth.getCurrentUser();

                    ///createSchoolDataBase(user);
//                    createTeacherDataBase(user);
                    createTeacherDataBase(user,code,schoolCode);
                    //System.out.println(user);
                    //updateUI(user);
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(Login.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                    //updateUI(null);
                }

                // ...
            }
        });
    }

    private void createTeacherDataBase(final FirebaseUser user, final String code, final String ScollCode){

        TeacherDetails teacherDetails = new TeacherDetails("Rahman Mia","Bangla","Present","01515279018",ScollCode,
                teacherEmail,"Badda Dhaka");

        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Users/Teachers/");


        databaseReference.child(user.getUid()).child("Details").setValue(teacherDetails)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            DatabaseReference sircode2 = firebaseDatabase.getReference("Users/Teachers/");
                            sircode2.child(user.getUid()).child("TeacherCode").setValue(code);

                            DatabaseReference createinSchool = firebaseDatabase.getReference("Schools/"+ScollCode+"/Teachers");
                            createinSchool.child(createinSchool.push().getKey()).setValue(code)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(Login.this, "Succ", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                        else{
                            Toast.makeText(Login.this, "ASASASASAS", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void generateTeacherCode(final String SchoolCode) {
        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference("Users/Teachers/count/");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String number = dataSnapshot.getValue().toString();
                int serial = Integer.parseInt(number)+1;
                databaseReference.setValue(serial);

                auth(Integer.toString(serial),SchoolCode);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void checkInSchoolDataBase() {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

        String schoolCode = "10005";

        Query query = rootRef.child("Schools").orderByChild("School Code").equalTo(schoolCode);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println(dataSnapshot.getKey());
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    String clubkey = childSnapshot.getKey();

//                    generateTeacherCode(clubkey);
                  //  generateSchoolCode(clubkey);
                    generateStudentCode(clubkey);
                    //System.out.println(clubkey);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        System.out.println(schoolCode);


    }

    private void createSchoolDataBase(final FirebaseUser user, final String code){

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference("Schools/");
        final DatabaseReference schoolref = firebaseDatabase.getReference("Schools/");

        SchoolDetails schoolDetails = new SchoolDetails("Abc High School","abc2@gmail.com",
                "3000","45","50","Dhaka","01515297025");
       databaseReference.child(user.getUid()).child("Information").setValue(schoolDetails)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            databaseReference.child(user.getUid()).child("School Code").setValue(code);
                            finish();
                            Intent intent = new Intent(Login.this,SignUp.class);
                            startActivity(intent);
                        }

                    }
                });
    }


    @Override
    protected void onStart() {
        super.onStart();


    }
}
