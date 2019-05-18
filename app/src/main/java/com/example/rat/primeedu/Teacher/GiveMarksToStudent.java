package com.example.rat.primeedu.Teacher;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GiveMarksToStudent extends AppCompatActivity implements View.OnClickListener {

    Button subj,marksGiven;
    ImageButton back;
    int subj_number;
    TextView classNo;
    String Class,Section,StudentId;
    EditText id,maximumMark,examName;
    CheckBox checkBox;
    private String url = "SchoolName/Class/";

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
        StudentId = getIntent().getStringExtra("id").toString();


        classNo.setText(Class+"("+Section+")");
        id.setText(StudentId);
    }

    private void init() {

        examName = (EditText)findViewById(R.id.examName);
        maximumMark = (EditText)findViewById(R.id.maximumMark);
        id = (EditText)findViewById(R.id.id);

        checkBox = (CheckBox)findViewById(R.id.checkbox);

        classNo = (TextView)findViewById(R.id.classNo);

        subj = (Button) findViewById(R.id.subj);
        subj.setOnClickListener(this);

        marksGiven  = (Button)findViewById(R.id.marksGiven);
        marksGiven.setOnClickListener(this);

        back = (ImageButton)findViewById(R.id.back);
        back.setOnClickListener(this);
    }

    private void openNumberPicker() {
        RelativeLayout linearLayout = new RelativeLayout(this);
        final NumberPicker aNumberPicker = new NumberPicker(this);
        aNumberPicker.setMaxValue(50);
        aNumberPicker.setMinValue(1);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(50, 50);
        RelativeLayout.LayoutParams numPicerParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        numPicerParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

        linearLayout.setLayoutParams(params);
        linearLayout.addView(aNumberPicker, numPicerParams);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Select the number");
        alertDialogBuilder.setView(linearLayout);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                subj_number = aNumberPicker.getValue();

                                createView(aNumberPicker.getValue());
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    private void createView(int subjNumber){

        System.out.println(subjNumber);
        TableLayout table = (TableLayout)findViewById(R.id.table);
        table.setStretchAllColumns(true);

        for(int i = 0 ;i<subjNumber ;i++){
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            table.addView(tableRow);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            CardView rowView = (CardView) inflater.inflate(R.layout.marks, null);
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

    private void check(){
        if(id.getText().toString().equals("")){
            Toast.makeText(this, "Please Enter Student Id", Toast.LENGTH_SHORT).show();
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
            TableRow row = (TableRow) table.getChildAt(i);
            for(int j = 0; j<row.getChildCount() ;j++){
                CardView cardView = (CardView)row.getChildAt(j);
                EditText sub = (EditText)cardView.findViewById(R.id.sub);
                EditText mark = (EditText)cardView.findViewById(R.id.marks);

                if(mark.getText().equals("") || sub.getText().equals("")){
                    Toast.makeText(this, "Some Textfield Remains Empty. Please Fill it.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(Float.parseFloat(mark.getText().toString())> Float.parseFloat(maximumMark.getText().toString())){
                    Toast.makeText(this, "Subject Marks is greater than maximum mark. Please Check", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }
        getDataFromTable();
    }
    private void getDataFromTable() {

        String studentId = id.getText().toString();
        String exam = examName.getText().toString();
        String nums = maximumMark.getText().toString();


        String path = url+Class+"/Section/"+Section+"/"+studentId+"/Marks/"+exam;

        System.out.println(path);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
//
//        If needed wil add
//        DatabaseReference databaseReference = database.getReference(path+"/Status");
//
//        if(checkBox.isChecked() == true){
//            databaseReference.child("Status").setValue("P");
//        }else{
//            databaseReference.child("Status").setValue("A");
//        }

        TableLayout table = (TableLayout)findViewById(R.id.table);
        for(int i = 0 ;i<table.getChildCount();i++){
            TableRow row = (TableRow) table.getChildAt(i);
            for(int j = 0; j<row.getChildCount() ;j++){
                CardView cardView = (CardView)row.getChildAt(j);
                EditText sub = (EditText)cardView.findViewById(R.id.sub);
                final EditText mark = (EditText)cardView.findViewById(R.id.marks);

                final boolean[] check = {false};
                DatabaseReference myref = database.getReference(path+"/Subject/"+sub.getText().toString()+"/");
                myref.child("maxNumber").setValue(nums).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(GiveMarksToStudent.this, "SuccessFully Entered", Toast.LENGTH_SHORT).show();
                            check[0] = true;
                            mark.setText("");
                        }
                        if(task == null){
                            Toast.makeText(GiveMarksToStudent.this, "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                if(mark.getText().equals("")){

                }else{
                    Toast.makeText(GiveMarksToStudent.this, "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
                    return;
                }
                myref.child("number").setValue(mark.getText().toString());
            }
        }
        id.setText("");
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.subj){
            openNumberPicker();
        }
        if(v.getId() == R.id.marksGiven){
            gotoTheMarkingPage();
        }
        if(v.getId() == R.id.back){
            backButton();
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
}
