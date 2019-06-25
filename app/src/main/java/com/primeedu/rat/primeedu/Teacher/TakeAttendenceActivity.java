package com.primeedu.rat.primeedu.Teacher;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.primeedu.rat.primeedu.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

public class TakeAttendenceActivity extends AppCompatActivity implements View.OnClickListener{

    private int row ;
    private int col = 5;
    private int studentNo ;
    private boolean present[];
    final private String classUrl = "SchoolName/Class/";
    TextView date,className,sectionName;
    String SchoolName;
    ImageButton save;
    private String Currentclass ,CurrentSection ;
    String CurrentDate = "";
    ImageButton back;
    private String SchoolId ;
    ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_attendence);

        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.actionbar));


        getIntentData();
        init();
        populateButton();
    }

    private void getIntentData() {
        SchoolId = getIntent().getStringExtra("schoolId").toString();
        Currentclass = getIntent().getStringExtra("classNo").toString();
        CurrentSection = getIntent().getStringExtra("sectionNo").toString();
        SchoolName = getIntent().getStringExtra("schoolName").toString();
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


    private void init(){

        TextView scl = findViewById(R.id.school_name);
        scl.setText(SchoolName);
        date = (TextView)findViewById(R.id.date);
        date.setOnClickListener(this);

        save = (ImageButton)findViewById(R.id.save);
        save.setOnClickListener(this);

        back = (ImageButton)findViewById(R.id.back);
        back.setOnClickListener(this);


//        className = (TextView)findViewById(R.id.className);
//        className.setText("Class: "+Currentclass);

//        sectionName = (TextView)findViewById(R.id.sectionName);
//        sectionName.setText("Section: "+CurrentSection);

    }

    private void populateButton(){
        TableLayout table = (TableLayout)findViewById(R.id.table);
        table.setStretchAllColumns(true);

        String no = getIntent().getStringExtra("stdNo").toString();

        studentNo = Integer.parseInt(no);
        int e = studentNo/col;
        if(e*col == studentNo)row = e;
        else row = e+1;

        boolean arr[] = new boolean[studentNo+1];
        present = arr;

        for(int i = 1 ;i<=studentNo;i++){
            present[i] = true;
        }
        int countt = 0;
        for(int i = 0 ;i<row   && countt<=studentNo ;i++){
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            table.addView(tableRow);
            for(int j = 0 ;j<5 ;j++){
                countt += 1;
                if(countt > studentNo)break;
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                LinearLayout rowView = (LinearLayout) inflater.inflate(R.layout.activity_rows, null);
                final int finalCountt = countt;

                final TextView roll = (TextView)rowView.findViewById(R.id.roll);
                final ImageView presentId = (ImageView)rowView.findViewById(R.id.presentId);
                rowView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(present[finalCountt]){
                            roll.setTextColor(Color.RED);
                            presentId.setImageResource(R.drawable.at7);
                            present[finalCountt] = false;
                        }
                        else if(!present[finalCountt]){
                            roll.setTextColor(Color.parseColor("#2F7FCA"));
                            presentId.setImageResource(R.drawable.at6);
                            present[finalCountt] = true;
                        }
                    }
                });
                roll.setText(rollNumber(countt));
                tableRow.addView(rowView);
            }
        }
    }
    private String rollNumber(int count){
        String s = Integer.toString(count);
        if(s.length() ==1 ){
            s = "00"+s;
        }
        else if(s.length() ==2 ){
            s = "0"+s;
        }

        return s;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.date){
            datePick();
        }
        if(v.getId() == R.id.save){

            if(CurrentDate.equals("")){
                Toast.makeText(this, "Please Select A Date", Toast.LENGTH_SHORT).show();
                return;
            }
            saveAttendence();
        }
        if(v.getId() == R.id.back){
            backButton();
        }

    }
    private void backButton(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure, You wanted to make decision");
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

    private void saveAttendence() {
        showLoading();
        System.out.println(Currentclass+" "+CurrentSection);
        //String path = classUrl+Currentclass+"/Section/"+CurrentSection;
        String path = "Classes/"+SchoolId+"/class/"+Currentclass+"/Section/"+CurrentSection;

        System.out.println(path);

        final int[] flag = {1};
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        for(int i = 1 ;i<=studentNo; i++){


            DatabaseReference myRef = database.getReference(path+"/"+i+"/Attendence/");

            if(present[i]){
                final int finalI = i;
                myRef.child(CurrentDate).setValue("P")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            if(finalI ==studentNo){
                                dialog.dismiss();
                                openDialogBox();
                            }
                            else{
                                flag[0] = 0;
                            }
                        }
                    }
                })
                ;
            }
            else{
                final int finalI1 = i;
                myRef.child(CurrentDate).setValue("A")
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    if(finalI1 ==studentNo){
                                        dialog.dismiss();
                                        openDialogBox();
                                    }
                                }
                                else{
                                    flag[0] = 0;
                                }
                            }
                        })
                ;
            }

            if(flag[0] == 0){
                return;
            }
        }

    }
    private void datePick() {
        Calendar c = Calendar.getInstance();
        DatePickerDialog datePickerDialog ;
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        Date today = c.getTime();

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                int mnth = month+1;
                date.setText("Date: " +dayOfMonth+ "/"+ mnth +"/" + year);
                CurrentDate = dayOfMonth+"-"+mnth+"-"+year;
            }
        },day,month,year );

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
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
        alertDialogBuilder.setMessage("Attendance has been saved.");
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
