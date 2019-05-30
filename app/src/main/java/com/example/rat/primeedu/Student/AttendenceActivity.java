package com.example.rat.primeedu.Student;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rat.primeedu.Class.Student;
import com.example.rat.primeedu.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class AttendenceActivity extends AppCompatActivity implements View.OnClickListener {

    private String url = "SchoolName/Class/";

    List<String> presentDays = new ArrayList<>();
    List<String> absentDays = new ArrayList<>();
    String Schoolname;
    TextView month;
    private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMM - yyyy", Locale.getDefault());

    private TextView nameOfStudent;
    CircleImageView profilePic;
    private String CurrentSection,Currentclass,CurrentStudent,SchoolCode;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendence);



        getIntentData();
        getAttendenceData();
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
    private void getAttendenceData() {
        showLoading();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        String path = "Classes/"+SchoolCode+"/class/"+Currentclass+"/Section/"+CurrentSection+"/"+CurrentStudent+"/Attendence/";
        System.out.println(path);
        DatabaseReference databaseReference = firebaseDatabase.getReference(path);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){

                    String status = snapshot.getValue(String.class);
                    if(status.equals("P"))presentDays.add(snapshot.getKey());
                    else absentDays.add(snapshot.getKey());
                }
                dialog.dismiss();
                createPieChart();
                createCalender();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getIntentData() {


        Currentclass = getIntent().getStringExtra("class");
        CurrentSection = getIntent().getStringExtra("section");
        CurrentStudent = getIntent().getStringExtra("roll");
        Schoolname = getIntent().getStringExtra("SchoolName").toString();
        SchoolCode = getIntent().getStringExtra("school");
        String img = getIntent().getStringExtra("img");
        String name = getIntent().getStringExtra("name");

        nameOfStudent = (TextView)findViewById(R.id.nameOfStudent);
        profilePic = findViewById(R.id.profilePic);



        TextView scl = findViewById(R.id.school_name);
        scl.setText(Schoolname);

        nameOfStudent.setText(name);
        Glide.with(getApplicationContext())
                .load(img)
                .into(profilePic);

        TextView ids = findViewById(R.id.ids);
        ids.setText(rollNumber(CurrentStudent));

        findViewById(R.id.back).setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        if(R.id.back == v.getId()){
            finish();
        }
    }
    private void createPieChart() {

        TextView attendenceMsg = (TextView)findViewById(R.id.attendenceMsg);
        TextView totalAttandence = (TextView)findViewById(R.id.totalAttandence);
        PieChart pieChart = (PieChart) findViewById(R.id.piechart);
        ArrayList NoOfEmp = new ArrayList();
        ArrayList NoOfDays = new ArrayList();

        int totalClass = presentDays.size()+absentDays.size();

        totalAttandence.setText("Present: "+presentDays.size()+"  Absent: "+absentDays.size());
        float percentOfPresentDay = (float) (((float)presentDays.size()/(float)totalClass)*100.0);
        float percentOfAbcentDay = (float) (((float)absentDays.size()/(float)totalClass)*100.0);

        if(percentOfPresentDay>80.0){
            attendenceMsg.setText("Excellent! Keep it Up .");
            attendenceMsg.setTextColor(Color.parseColor("#008000"));

        }
        else if(percentOfPresentDay<=80.0 && percentOfPresentDay>=60.0){
            attendenceMsg.setText("Your Attendance is not fascinating.");
            attendenceMsg.setTextColor(Color.BLUE);
        }
        else {
            attendenceMsg.setTextColor(Color.parseColor("#FF0000"));
            attendenceMsg.setText("Attendance is too low!!.");
        }

        NoOfDays.add(new Entry(percentOfPresentDay,0));
        NoOfDays.add(new Entry(percentOfAbcentDay,1));

        System.out.println(totalClass+" "+presentDays.size()+" "+absentDays.size()+" "+percentOfAbcentDay);
        PieDataSet dataSet = new PieDataSet(NoOfDays, "Absent");

        ArrayList status = new ArrayList();


        final  int[] MY_COLORS = {

                Color. rgb(47,127,202),
                Color.  rgb(220,220,220)
        };

        ArrayList<Integer> colors = new ArrayList<>();

        for(int c: MY_COLORS) colors.add(c);

        status.add("Present");
        status.add("Absent");
        PieData data = new PieData(status, dataSet);
        pieChart.setCenterText(Float.toString(percentOfPresentDay)+"%");
        pieChart.setCenterTextSize(12f);
        pieChart.setCenterTextColor(Color.parseColor("#2F7FCA"));
        pieChart.setData(data);
        dataSet.setColors(colors);
        pieChart.animateXY(5000, 5000);
    }

    private void createCalender() {

        month = (TextView)findViewById(R.id.month);
        month.setText(dateFormatForMonth.format(new Date()));

        final CompactCalendarView compactCalendarView = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        compactCalendarView.setFirstDayOfWeek(Calendar.MONDAY);

        compactCalendarView.setUseThreeLetterAbbreviation(false);
        compactCalendarView.setFirstDayOfWeek(Calendar.MONDAY);
        compactCalendarView.setIsRtl(false);
        compactCalendarView.displayOtherMonthDays(false);

        compactCalendarView.invalidate();
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {

            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {

                month.setText(dateFormatForMonth.format(firstDayOfNewMonth));
            }
        });

        SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy");
        showPresentDays(myFormat,compactCalendarView);
        showAbsentDays(myFormat,compactCalendarView);
    }

    private void showAbsentDays(SimpleDateFormat myFormat, CompactCalendarView compactCalendarView) {
        for(int i = 0 ;i<absentDays.size();i++){

            Date date = null;
            try {
                date = myFormat.parse(absentDays.get(i));
                long millis = date.getTime();
                Event ev1 = new Event(Color.RED, millis);
                compactCalendarView.addEvent(ev1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private void showPresentDays(SimpleDateFormat myFormat, CompactCalendarView compactCalendarView) {
        for(int i = 0 ;i<presentDays.size();i++){

            Date date = null;
            try {
                date = myFormat.parse(presentDays.get(i));
                long millis = date.getTime();
                Event ev1 = new Event(Color.GREEN, millis);
                compactCalendarView.addEvent(ev1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}
