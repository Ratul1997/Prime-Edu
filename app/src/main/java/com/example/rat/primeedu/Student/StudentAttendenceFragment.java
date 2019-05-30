package com.example.rat.primeedu.Student;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CalendarView;
import android.widget.TextView;

import com.example.rat.primeedu.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
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

import sun.bob.mcalendarview.MCalendarView;
import sun.bob.mcalendarview.MarkStyle;
import sun.bob.mcalendarview.vo.DateData;


@SuppressLint("ValidFragment")
public class StudentAttendenceFragment extends Fragment {


    private String url = "SchoolName/Class/";
    ProgressDialog dialog;
    List<String> presentDays = new ArrayList<>();
    List<String> absentDays = new ArrayList<>();
    TextView month;
    private String CurrentSection,Currentclass,CurrentStudent,SchoolId;
    private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMM - yyyy", Locale.getDefault());


    public StudentAttendenceFragment(String currentStudent, String currentclass, String currentSection, String schoolId) {
        CurrentSection = currentSection;
        Currentclass = currentclass;
        CurrentStudent = currentStudent;
        this.SchoolId = schoolId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_student_attendence, container, false);
        //showLoading();
        init(v);

        return v;
    }

    private void init(View v) {
        getAttendenceData(v);

    }

    private void showLoading() {

        dialog = new ProgressDialog(getContext());
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Please Wait ...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();
        dialog.show();
    }

    private void getAttendenceData(final View v) {
        showLoading();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        String path = "Classes/"+SchoolId+"/class/"+Currentclass+"/Section/"+CurrentSection+"/"+CurrentStudent+"/Attendence/";
        System.out.println(path);
        DatabaseReference databaseReference = firebaseDatabase.getReference(path);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                presentDays.clear();
                absentDays.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){

                    String status = snapshot.getValue(String.class);
                    if(status.equals("P"))presentDays.add(snapshot.getKey());
                    else absentDays.add(snapshot.getKey());
                }
                dialog.dismiss();
                createPieChart(v);
                createCalender(v);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void createPieChart(View v) {

        TextView attendenceMsg = (TextView)v.findViewById(R.id.attendenceMsg);
        TextView totalAttandence = (TextView)v.findViewById(R.id.totalAttandence);
        PieChart pieChart = (PieChart) v.findViewById(R.id.piechart);
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

    private void createCalender(View v) {



        month = (TextView)v.findViewById(R.id.month);
        month.setText(dateFormatForMonth.format(new Date()));

        final CompactCalendarView compactCalendarView = (CompactCalendarView) v.findViewById(R.id.compactcalendar_view);
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
