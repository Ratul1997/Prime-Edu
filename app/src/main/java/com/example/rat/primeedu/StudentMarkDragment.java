package com.example.rat.primeedu;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@SuppressLint("ValidFragment")
public class StudentMarkDragment extends Fragment {

    Dialog dialog;
    private float[] yData = {50f,50f};
    private String[] xData= {"A","P"};
    private String CurrentSection,Currentclass,CurrentStudent;
    private String url = "SchoolName/Class/";
    List<TermWise> terms = new ArrayList<>();
    TextView serial;
    private int[] colors = {Color.RED,Color.GREEN,Color.YELLOW,Color.BLUE,Color.MAGENTA,Color.parseColor("#A93226"),
            Color.parseColor("#154EF2"),Color.parseColor("#154EF2"),Color.parseColor("#154EF2"),Color.parseColor("#63211B"),
            Color.parseColor("#154EF2"),Color.parseColor("#154EF2"),Color.parseColor("#154EF2"),Color.parseColor("#1B631F"),
            Color.parseColor("#154EF2"),Color.parseColor("#154EF2"),Color.parseColor("#154EF2"),Color.parseColor("#15D4F2"),
            Color.parseColor("#154EF2"),Color.parseColor("#154EF2"),Color.parseColor("#154EF2"),Color.parseColor("#154EF2"),
            Color.parseColor("#154EF2"),Color.parseColor("#154EF2"),Color.parseColor("#154EF2"),Color.parseColor("#154EF2"),
    };

    public StudentMarkDragment(String currentStudent, String currentclass, String currentSection) {
        // Required empty public constructor
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

        View v = inflater.inflate(R.layout.fragment_student_mark_dragment, container, false);
       // showLoading();
        init(v);
        getDataFromFirebase();
       // linechart(v);

        return v;
    }
    private void init(View v){
        serial = (TextView)v.findViewById(R.id.serial);
    }

    private void showLoading() {

        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.loadingprogressbar);
        dialog.show();
    }
    private void getDataFromFirebase() {
        String path = url+Currentclass+"/Section/"+CurrentSection+"/"+CurrentStudent+"/Marks/";
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//        System.out.println(path);
        DatabaseReference databaseReference = firebaseDatabase.getReference(path);

        final Map subjcets = new HashMap();
        final int[] countt = {0};
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){

//                    System.out.println(snapshot.getKey());
                    ArrayList< Pair<String,Pair<String,String>>>sList = new ArrayList<>();
                    for(DataSnapshot snapshot1 : snapshot.child("Subject").getChildren()){
//                        System.out.println(snapshot1.getKey());
                        String num = snapshot1.child("number").getValue(String.class);
                        String maxNumber = snapshot1.child("maxNumber").getValue(String.class);
                        if(subjcets.get(snapshot1.getKey()) == null){
                            subjcets.put(snapshot1.getKey(), ++countt[0]);
                        }

                        Pair<String,Pair<String,String>> pp = new Pair<>(snapshot1.getKey(),new Pair<>(num,maxNumber));
                        sList.add(pp);
                    }
                    TermWise termWise = new TermWise(snapshot.getKey(),sList);
                    terms.add(termWise);
                }
//                dialog.dismiss();
               makeLine(getView(),subjcets);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void generateAvailableResult(View v){
        TableLayout table = (TableLayout)v.findViewById(R.id.table);
        table.setStretchAllColumns(true);

        for(int i = 0 ;i<terms.size() ;i++){
            TableRow tableRow = new TableRow(v.getContext());
            tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            table.addView(tableRow);
            LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LinearLayout rowView = (LinearLayout) inflater.inflate(R.layout.available_result, null);

            TextView examNames = (TextView)rowView.findViewById(R.id.examNames);
            examNames.setText(terms.get(i).getTerm());

            Button viewResult = (Button)rowView.findViewById(R.id.viewResult);
            final int finalI = i;
            viewResult.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    System.out.println(finalI);
                    openDialogMenu(finalI);
                }
            });

            tableRow.addView(rowView);
        }
    }
    private void openDialogMenu(int i){
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.term_marks_show);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView textView = (TextView)dialog.findViewById(R.id.exams);
        textView.setText(terms.get(i).getTerm());
        generateTablesForSubject(dialog,terms.get(i).getSubjects());
        Button done = (Button)dialog.findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void generateTablesForSubject(Dialog v,ArrayList< Pair<String,Pair<String,String>>> subjects) {
        TableLayout table = (TableLayout)v.findViewById(R.id.table);
        table.setStretchAllColumns(true);

        int total = 0;
        float obtain_marks = (float) 0.0;
        for(int i = 0 ;i<subjects.size() ;i++){
            TableRow tableRow = new TableRow(v.getContext());
            tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            table.addView(tableRow);
            LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LinearLayout rowView = (LinearLayout) inflater.inflate(R.layout.termmark_details, null);

            TextView subjectName = (TextView)rowView.findViewById(R.id.subjectName);
            subjectName.setText(subjects.get(i).first);

            TextView mrks = (TextView)rowView.findViewById(R.id.mrks);
            mrks.setText(subjects.get(i).second.first+"/"+subjects.get(i).second.second);

            obtain_marks += Float.parseFloat(subjects.get(i).second.first);

            total += Integer.parseInt(subjects.get(i).second.second);
            tableRow.addView(rowView);
        }

        TextView totalmarks = (TextView)v.findViewById(R.id.totalmarks);
        totalmarks.setText(String.format("%.2f",obtain_marks)+"/"+total);
    }

    private void makeLine(View v,Map subjectsSerial){
        LineChart linechart = (LineChart) v.findViewById(R.id.linechart);
        ArrayList<String>Subject = new ArrayList<>();
        ArrayList<String>SubjectSerial = new ArrayList<>();
        ArrayList<String>Term = new ArrayList<>();
        Map trackingSubject = new HashMap();


        ArrayList<LineDataSet> datas = new ArrayList<>();


        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        Subject.add("");
        SubjectSerial.add("");

        for(int i = 0;i<terms.size();i++){
            ArrayList numbers = new ArrayList<>();
//            System.out.println(terms.get(i).getTerm());
            for( Pair<String,Pair<String,String>> sList: terms.get(i).getSubjects()){
                if(trackingSubject.get(sList.first) == null){
                    trackingSubject.put(sList.first,true);
                    Subject.add(sList.first);
                    SubjectSerial.add(Integer.toString((Integer) subjectsSerial.get(sList.first)));
                }
//                System.out.println(sList.first+" "+sList.second.first+" "+sList.second.first);
                numbers.add(new BarEntry(Float.parseFloat(sList.second.first), (Integer) subjectsSerial.get(sList.first)));
            }
            LineDataSet bardataset1 = new LineDataSet(numbers, terms.get(i).getTerm());
            bardataset1.setColor(colors[i]);

            dataSets.add(bardataset1);
        }

        String s = "";
        for(int i = 1 ;i<Subject.size();i++){
            s +=Subject.get(i)+"-"+subjectsSerial.get(Subject.get(i))+"\t";

        }
//        System.out.println(s);
        serial.setText(s);


        LineData data = new LineData(SubjectSerial, dataSets);
        linechart.setData(data);
        linechart.setVisibleXRangeMaximum(12);
        linechart.getXAxis().setLabelsToSkip(0);
        linechart.invalidate();

        generateAvailableResult(v);
    }
//    private void piecharts(View v) {
//        PieChart pieChart = (PieChart) v.findViewById(R.id.piechart);
//        ArrayList NoOfEmp = new ArrayList();
//
//        NoOfEmp.add(new Entry(945f, 0));
//        NoOfEmp.add(new Entry(1040f, 1));
//        NoOfEmp.add(new Entry(1133f, 2));
//        NoOfEmp.add(new Entry(1240f, 3));
//        NoOfEmp.add(new Entry(1369f, 4));
//        NoOfEmp.add(new Entry(1487f, 5));
//        NoOfEmp.add(new Entry(1501f, 6));
//        NoOfEmp.add(new Entry(1645f, 7));
//        NoOfEmp.add(new Entry(1578f, 8));
//        NoOfEmp.add(new Entry(1695f, 9));
//        PieDataSet dataSet = new PieDataSet(NoOfEmp, "Number Of Employees");
//
//        ArrayList year = new ArrayList();
//
//        year.add("2008");
//        year.add("2009");
//        year.add("2010");
//        year.add("2011");
//        year.add("2012");
//        year.add("2013");
//        year.add("2014");
//        year.add("2015");
//        year.add("2016");
//        year.add("2017");
//        PieData data = new PieData(year, dataSet);
//        pieChart.setData(data);
//        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
//        pieChart.animateXY(5000, 5000);
//    }

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
//    }


}
