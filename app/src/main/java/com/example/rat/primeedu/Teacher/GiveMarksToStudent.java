package com.example.rat.primeedu.Teacher;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rat.primeedu.R;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GiveMarksToStudent extends AppCompatActivity implements View.OnClickListener {

    ImageButton marksGiven;
    ImageButton back;
    int subj_number;
    TextView classNo;
    String Class,Section,StudentId;
    EditText id,maximumMark,examName;
    CheckBox checkBox;
    private String url = "SchoolName/Class/";
    String SchoolId;
    CheckBox terms,ct;
    ProgressDialog dialog;
    String TotalStudentNo,SchoolName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_marks_to_student);
        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.colorAccent));

        init();
        getDataFromIntent();
    }

    private void getDataFromIntent() {
        Class = getIntent().getStringExtra("class").toString();
        Section = getIntent().getStringExtra("section").toString();
//        StudentId = getIntent().getStringExtra("id").toString();

        SchoolName = getIntent().getStringExtra("schoolName").toString();
        TotalStudentNo = getIntent().getStringExtra("stdNo").toString();
        classNo.setText(Class+"("+Section+")");
        //id.setText(StudentId);

        SchoolId = getIntent().getStringExtra("schoolId").toString();
        TextView school = findViewById(R.id.school_name);
        school.setText(SchoolName);
    }

    private void init() {
        terms = (CheckBox)findViewById(R.id.terms);
        terms.setOnClickListener(this);
        ct = (CheckBox)findViewById(R.id.ct);
        ct.setOnClickListener(this);
        examName = (EditText)findViewById(R.id.examName);
        maximumMark = (EditText)findViewById(R.id.maximumMark);
        id = (EditText)findViewById(R.id.id);



        classNo = (TextView)findViewById(R.id.classNo);


        marksGiven  = (ImageButton)findViewById(R.id.marksGiven);
        marksGiven.setOnClickListener(this);

        back = (ImageButton)findViewById(R.id.back);
        back.setOnClickListener(this);
    }


    private void createView(){

        TableLayout table = (TableLayout)findViewById(R.id.table);
        table.setStretchAllColumns(true);
        int studentNo = Integer.parseInt(TotalStudentNo);
        for(int i = 1 ;i<=studentNo ;i++){
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            table.addView(tableRow);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            CardView rowView = (CardView) inflater.inflate(R.layout.marks, null);
            TextView ids = (TextView)rowView.findViewById(R.id.ids);
            ids.setText(Integer.toString(i));
            tableRow.addView(rowView);
        }

    }

    public void gotoTheMarkingPage(){
        check();
    }

    private void backButton(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Your file will not be saved." +
                "Are you sure, You wanted to make decision");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {


                        finish();
                    }
                });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
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


    private void check(){
        if(!terms.isChecked() && !ct.isChecked() ){
            Toast.makeText(this, "Please Enter Exam Type", Toast.LENGTH_SHORT).show();
            return;
        }
        if(id.getText().toString().equals("")){
            Toast.makeText(this, "Please Enter Subject", Toast.LENGTH_SHORT).show();
            return;
        }

        if(maximumMark.getText().toString().equals("")){
            Toast.makeText(this, "Please Enter Maximum Mark of each subject", Toast.LENGTH_SHORT).show();
            return;
        }

        if(examName.getText().toString().equals("")){
            Toast.makeText(this, "Please Enter A Examination Name", Toast.LENGTH_SHORT).show();
            return;
        }
        TableLayout table = (TableLayout)findViewById(R.id.table);
        for(int i = 0 ;i<table.getChildCount();i++){
            final TableRow row = (TableRow) table.getChildAt(i);

            CardView cardView = (CardView)row.getChildAt(0);
            EditText mark = (EditText)cardView.findViewById(R.id.marks);

            if(mark.getText().toString().equals("")){
                Toast.makeText(this, i+1 +"th field Remains Empty. Please Fill it.", Toast.LENGTH_SHORT).show();
                return;
            }
            if(Float.parseFloat(mark.getText().toString())> Float.parseFloat(maximumMark.getText().toString())){
                Toast.makeText(this, i+1 + "th fields Subject Marks is greater than maximum mark. Please Check", Toast.LENGTH_SHORT).show();
                return;
            }
        }

         getDataFromTable();
    }
    private void getDataFromTable() {

        showLoading();
        String SubjectName = id.getText().toString();
        String exam = examName.getText().toString();
        String nums = maximumMark.getText().toString();


        String path = "";


        System.out.println(path);
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        final int[] col = {0};
        final int[] rw = {0};
        path = "Classes/"+SchoolId+"/class/"+Class+"/Section/"+Section+"/";
        final TableLayout table = (TableLayout)findViewById(R.id.table);
        for(int i = 0 ;i<table.getChildCount();i++){
            final TableRow row = (TableRow) table.getChildAt(i);

            CardView cardView = (CardView)row.getChildAt(0);
            final EditText mark = (EditText)cardView.findViewById(R.id.marks);

            String Url = "";
            if(terms.isChecked()){
                Url = path+(i+1)+"/Marks/Terms/"+exam+"/"+SubjectName;
            }
            else{
                Url = path+(i+1)+"/Marks/Ct/"+exam+"/"+SubjectName;
            }
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference(Url);
            ref.child("maxNumber").setValue(nums);
            final int finalI = i;
            ref.child("number").setValue(mark.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                mark.setText("");
                                if( finalI == table.getChildCount()-1){
                                    dialog.dismiss();
                                    openDialogBox();
                                    id.setText("");
                                }
                            }else {
                                dialog.dismiss();
                                Toast.makeText(GiveMarksToStudent.this, "Something is wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                            Toast.makeText(GiveMarksToStudent.this, "Something is wrong", Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnCanceledListener(new OnCanceledListener() {
                        @Override
                        public void onCanceled() {
                            dialog.dismiss();
                            Toast.makeText(GiveMarksToStudent.this, "Something is wrong", Toast.LENGTH_SHORT).show();

                        }
                    });
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.marksGiven){
            gotoTheMarkingPage();
        }
        if(v.getId() == R.id.back){
            backButton();
        }
        if(v.getId() == R.id.terms){
            if(terms.isChecked()){
                ct.setChecked(false);
                ct.setEnabled(false);
                examName.setInputType(InputType.TYPE_CLASS_NUMBER);
                TableLayout table = (TableLayout)findViewById(R.id.table);
                System.out.println(table.getChildCount());

                if(table.getChildCount()==0){
                    createView();
                }

            }
            else{
                ct.setEnabled(true);
                examName.setInputType(InputType.TYPE_CLASS_TEXT);
            }
        }
        if(v.getId() == R.id.ct){
            if(ct.isChecked()){
                terms.setChecked(false);
                terms.setEnabled(false);
                examName.setInputType(InputType.TYPE_CLASS_TEXT);

                TableLayout table = (TableLayout)findViewById(R.id.table);
                System.out.println(table.getChildCount());

                if(table.getChildCount()==0){
                    createView();
                }
            }
            else{
                terms.setEnabled(true);
                examName.setInputType(InputType.TYPE_CLASS_TEXT);

            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            backButton();
            return true;
        }
        return false;
    }
    private void openDialogBox() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setMessage("Marks has been saved.");
        alertDialogBuilder.setPositiveButton("ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
