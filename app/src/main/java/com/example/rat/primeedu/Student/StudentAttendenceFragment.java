package com.example.rat.primeedu.Student;

import android.annotation.SuppressLint;
import android.app.Dialog;
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
    Dialog dialog;
    List<String> presentDays = new ArrayList<>();
    List<String> absentDays = new ArrayList<>();
    TextView month;
    private String CurrentSection,Currentclass,CurrentStudent;
    private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMM - yyyy", Locale.getDefault());


    public StudentAttendenceFragment(String currentStudent, String currentclass, String currentSection) {
        CurrentSection = currentSection;
        Currentclass = currentclass;
        CurrentStudent = currentStudent;
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
        //createPieChart(v);
       // createCalender(v);

    }
    private void showLoading() {

        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.loadingprogressbar);
        dialog.show();
    }

    private void getAttendenceData(final View v) {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        String path = url+Currentclass+"/Section/"+CurrentSection+"/"+CurrentStudent+"/Attendence";
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
//                dialog.dismiss();
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
            attendenceMsg.setTextColor(Color.YELLOW);
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

        status.add("Present");
        status.add("Absent");
        PieData data = new PieData(status, dataSet);
        pieChart.setCenterText("Attendance in percentage");
        pieChart.setCenterTextSize(10f);
        pieChart.setCenterTextColor(Color.parseColor("#0097A7"));
        pieChart.setData(data);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieChart.animateXY(5000, 5000);
    }

//    private void barcharts(View v) {
//        BarChart chart = (BarChart) v.findViewById(R.id.barchart);
//        ArrayList NoOfEmp = new ArrayList();
//
//
//        NoOfEmp.add(new BarEntry(945f, 0));
//        NoOfEmp.add(new BarEntry(1040f, 1));
//
//
//        ArrayList NoOfEmp1 = new ArrayList();
//
//
//        NoOfEmp1.add(new BarEntry(900f, 0));
//        NoOfEmp1.add(new BarEntry(1000f, 1));
//
//        ArrayList year = new ArrayList();
//
//        ArrayList year1 = new ArrayList();
//
//        year.add("1");
//        year.add("2");
//        year.add("3");
//
//        year1.add("2006");
//        year1.add("2004");
//        BarDataSet bardataset1 = new BarDataSet(NoOfEmp, "No Of Employee1");
//        BarDataSet bardataset2 = new BarDataSet(NoOfEmp1, "No Of Employee");
//
//
//        bardataset1.setColor(Color.GREEN);
//        bardataset2.setColor(Color.RED);
//
//
////
////        chart.animateY(5000);
//
//        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
//        dataSets.add(bardataset1);
//        dataSets.add(bardataset2);
//
//        BarData data = new BarData(year, dataSets);
//
//        chart.setData(data);
//
//
//        chart.invalidate();
//
//
//    }

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
