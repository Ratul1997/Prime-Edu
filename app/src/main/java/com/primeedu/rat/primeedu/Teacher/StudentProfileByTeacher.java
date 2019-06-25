package com.primeedu.rat.primeedu.Teacher;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.primeedu.rat.primeedu.Class.FatherDetails;
import com.primeedu.rat.primeedu.Class.GurdianDetails;
import com.primeedu.rat.primeedu.Class.MotherDetails;
import com.primeedu.rat.primeedu.Class.Student;
import com.primeedu.rat.primeedu.Class.StudentDetails;
import com.primeedu.rat.primeedu.Student.AboutStudentFragment;
import com.primeedu.rat.primeedu.R;
import com.primeedu.rat.primeedu.Student.StudentAttendenceFragment;
import com.primeedu.rat.primeedu.Student.StudentMarkDragment;
import com.primeedu.rat.primeedu.Asapter.ViewPagerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentProfileByTeacher extends AppCompatActivity implements View.OnClickListener,AboutStudentFragment.OnFragmentInteractionListener{

    TabLayout tablayout;
    ViewPager viewPager;
    private String CurrentSection,Currentclass,CurrentStudent,SchoolId,StudentUniqueId;
    private String url = "SchoolName/Class/";
    TextView nameOfStudent;
    String SchoolName;
    ProgressDialog dialog;
    Student student;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loadingprogressbar);
        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.actionbar));

        //showLoading();
        getIntentDatas();
        getFireBaseData();

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

    private void getFireBaseData() {
        final String path =  "Classes/"+SchoolId+"/class/"+Currentclass+"/Section/"+CurrentSection+"/"+CurrentStudent+"/StudentCode/";
        System.out.println(path);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(path);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() == null){
                    setContentView(R.layout.activity_student_profile_by_teacher);
                    init();

                    nameOfStudent.setText("Student Name");
                    TextView ids = findViewById(R.id.ids);
                    ids.setText(rollNumber(CurrentStudent));
                    openDialogBox();

                    FatherDetails fatherDetails = new FatherDetails("","","","");
                    MotherDetails motherDetails = new MotherDetails("","","","");
                    GurdianDetails gurdianDetails = new GurdianDetails("","","","","");



                    StudentDetails studentDetails = new StudentDetails("Student Name","","","",
                            "","",gurdianDetails,fatherDetails,motherDetails);


                    Student stD =  new Student(SchoolId,"Code",CurrentStudent,CurrentSection,
                            Currentclass,"",studentDetails,"");
                    student = stD;

                    createTabbar();
                }
                else if(dataSnapshot.getValue().toString().equals("")){

                    setContentView(R.layout.activity_student_profile_by_teacher);
                    init();

                    nameOfStudent.setText("Student Name");

                    TextView ids = findViewById(R.id.ids);
                    ids.setText(rollNumber(CurrentStudent));
                    openDialogBox();

                        FatherDetails fatherDetails = new FatherDetails("","","","");
                        MotherDetails motherDetails = new MotherDetails("","","","");
                        GurdianDetails gurdianDetails = new GurdianDetails("","","","","");



                        StudentDetails studentDetails = new StudentDetails("Student Name","","","",
                                "","",gurdianDetails,fatherDetails,motherDetails);


                        Student stD =  new Student(SchoolId,"Code",CurrentStudent,CurrentSection,
                                Currentclass,"",studentDetails,"");
                        student = stD;

                    createTabbar();
                }
                else {
                    String Id = dataSnapshot.getValue(String.class);
                    StudentUniqueId = Id;
                    collectStudentInformation(Id);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                dialog.dismiss();
            }
        });
    }

    private void collectStudentInformation(String id) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users/Student/"+id+"/");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    setContentView(R.layout.activity_student_profile_by_teacher);
                    init();

                    if(dataSnapshot.getValue() == null){
                        FatherDetails fatherDetails = new FatherDetails("","","","");
                        MotherDetails motherDetails = new MotherDetails("","","","");
                        GurdianDetails gurdianDetails = new GurdianDetails("","","","","");



                        StudentDetails studentDetails = new StudentDetails("Student Name","","","",
                                "","",gurdianDetails,fatherDetails,motherDetails);


                        Student stD =  new Student(SchoolId,"",CurrentStudent,CurrentSection,
                                Currentclass,"",studentDetails,"");
                        student = stD;
                    }
                    else{
                        student = dataSnapshot.getValue(Student.class);

                    }


                    setName(student);
                    createTabbar();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    dialog.dismiss();
                }

            });
    }

    private void setName(Student student) {

        String s = student.getStudentDetails().getStudentfname()+ " "+ student.getStudentDetails().getStudenlname();
        nameOfStudent.setText(s);

        System.out.println("asss"+ nameOfStudent.getText().toString());
        TextView ids = findViewById(R.id.ids);
        ids.setText(rollNumber(student.getCurrentid()));

        CircleImageView img = findViewById(R.id.img);
        Glide.with(getApplicationContext())
                .load(student.getStudentDetails().getStudentprofilepicture())
                .into(img);


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

    private void getIntentDatas() {

        SchoolId = getIntent().getStringExtra("schoolId").toString();
        Currentclass = getIntent().getStringExtra("class").toString();
        CurrentSection = getIntent().getStringExtra("section").toString();
        CurrentStudent = getIntent().getStringExtra("id").toString();
        SchoolName = getIntent().getStringExtra("schoolName").toString();
    }

    private void init() {
        nameOfStudent = (TextView)findViewById(R.id.nameOfStudent);
        findViewById(R.id.back).setOnClickListener(this);
    }

    private void createTabbar() {
        tablayout = (TabLayout)findViewById(R.id.tablayout);
        viewPager = (ViewPager)findViewById(R.id.viewPager);


        TextView school = findViewById(R.id.school_name);
        school.setText(SchoolName);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        System.out.println(StudentUniqueId);
        adapter.AddFragment(new StudentAttendenceFragment(CurrentStudent,Currentclass,CurrentSection,SchoolId),"Attendence");
        adapter.AddFragment(new StudentMarkDragment(CurrentStudent,Currentclass,CurrentSection,SchoolId, "2",nameOfStudent.getText().toString()),"Marks");
        adapter.AddFragment(new AboutStudentFragment(StudentUniqueId,student,"2"),"About");

        viewPager.setAdapter(adapter);
        tablayout.setupWithViewPager(viewPager);
    }
    private void openDialogBox() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setMessage("Student Has not registered yet.");
        alertDialogBuilder.setPositiveButton("ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.back){
            finish();
        }
    }

    @Override
    public void onFragmentInteraction(String fNames, String lNames) {

    }
}
