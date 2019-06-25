package com.primeedu.rat.primeedu.Student;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


import com.primeedu.rat.primeedu.Class.ExamDetails;
import com.primeedu.rat.primeedu.Class.SchoolDetails;
import com.primeedu.rat.primeedu.R;
import com.primeedu.rat.primeedu.Class.TermWise;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


@SuppressLint("ValidFragment")
public class StudentMarkDragment extends Fragment {

    ProgressDialog dialog;
    private float[] yData = {50f, 50f};
    private String[] xData = {"A", "P"};
    SchoolDetails schoolDetails;
    private String CurrentSection, Currentclass, CurrentStudent, SchoolId;
    private String url = "SchoolName/Class/";
    String type;
    List<TermWise> terms = new ArrayList<>();
    final int PERMISSION_CODE = 5000;
    int currentPosition;
    List<TermWise>currentExam = new ArrayList<>();
    List<TermWise> upcoming = new ArrayList<>();
    String StudentName;
    LineChart linechart;
    View view;
    List<TermWise> ct = new ArrayList<>();
    TableLayout table;
    TextView serial;

    List<Pair<String,Pair<String, List<TermWise>>>>sectionwise = new ArrayList<>();
    List<Pair<String,List<TermWise>>>classwise = new ArrayList<>();
    Map<String, ExamDetails> allExams = new HashMap<String, ExamDetails>();
    Map<String,List<Pair<String,List<TermWise>>>>allresults = new HashMap<>();

    private int[] colors = {Color.RED, Color.GREEN, Color.YELLOW, Color.BLUE, Color.MAGENTA, Color.parseColor("#A93226"),
            Color.parseColor("#154EF2"), Color.parseColor("#154EF2"), Color.parseColor("#154EF2"), Color.parseColor("#63211B"),
            Color.parseColor("#154EF2"), Color.parseColor("#154EF2"), Color.parseColor("#154EF2"), Color.parseColor("#1B631F"),
            Color.parseColor("#154EF2"), Color.parseColor("#154EF2"), Color.parseColor("#154EF2"), Color.parseColor("#15D4F2"),
            Color.parseColor("#154EF2"), Color.parseColor("#154EF2"), Color.parseColor("#154EF2"), Color.parseColor("#154EF2"),
            Color.parseColor("#154EF2"), Color.parseColor("#154EF2"), Color.parseColor("#154EF2"), Color.parseColor("#154EF2"),
    };

    public StudentMarkDragment(String currentStudent, String currentclass, String currentSection, String SchoolId, String s,String StudentName) {
        // Required empty public constructor
        CurrentSection = currentSection;
        Currentclass = currentclass;
        CurrentStudent = currentStudent;
        this.SchoolId = SchoolId;
        type = s;
        this.StudentName = StudentName;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_student_mark_dragment, container, false);

        System.out.println(StudentName+"  555555555");
        view = v;
       // linechart = (LineChart)v.findViewById(R.id.linecharts);
        table = v.findViewById(R.id.table);
        init(v);
        getSchool();
        return v;
    }

    private void getSchool() {
        showLoading();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Schools/" + SchoolId + "/Information/");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    schoolDetails = dataSnapshot.getValue(SchoolDetails.class);
                    getExams();
                } else {
                    dialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                dialog.dismiss();
            }
        });

    }

    private void getExams() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Exams/" + SchoolId + "/");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    dialog.dismiss();
                    Toast.makeText(getActivity(), "No Term Exams", Toast.LENGTH_SHORT).show();
                    getAllResults();
//                    getDataFromFirebase();

                } else {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        ExamDetails examDetails = snapshot.getValue(ExamDetails.class);
                        allExams.put(snapshot.getKey(), examDetails);
                    }
                    getAllResults();
//                    getDataFromFirebase();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void init(View v) {
        serial = (TextView) v.findViewById(R.id.serial);
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



    private void getAllResults(){

        String path = "Classes/" + SchoolId + "/class/" + Currentclass + "/Section/";
        System.out.println(path);
        DatabaseReference ref =  FirebaseDatabase.getInstance().getReference(path);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.getValue() != null) {
                    Map<String,List<Pair<String,List<TermWise>>>> mp = new HashMap<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        //System.out.println("A = "+ snapshot.getKey().toString());
//                        sectionwise.clear();
                        List<Pair<String,List<TermWise>>> results = new ArrayList<>();
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                             System.out.println(snapshot1.getKey().toString());

                            List<TermWise> tmwse = new ArrayList<>();
                            for (DataSnapshot snapshot2 : snapshot1.child("Marks").child("Terms").getChildren()) {

                                ArrayList<Pair<String, Pair<String, String>>> sList = new ArrayList<>();
                                for (DataSnapshot snapshot3 : snapshot2.getChildren()) {
                                    String num = snapshot3.child("number").getValue(String.class);
                                    String maxNumber = snapshot3.child("maxNumber").getValue(String.class);

                                    Pair<String, Pair<String, String>> pp = new Pair<>(snapshot3.getKey(), new Pair<>(num, maxNumber));

                                    sList.add(pp);
                                }
                                TermWise termWise = new TermWise(snapshot2.getKey(), sList);
                                tmwse.add(termWise);
                            }
                            Pair<String, List<TermWise>> p = new Pair<>(snapshot1.getKey().toString(), tmwse);
                            results.add(p);
                            Pair<String,Pair<String, List<TermWise>>> al = new Pair<>(snapshot.getKey().toString(),p);
                            sectionwise.add(al);
                        }
                        mp.put(snapshot.getKey().toString(),results);
                    }

                    allresults = mp;

                    getDataFromFirebase();
                }else{
                    Toast.makeText(getActivity(), "No Section Aded.", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private String calculatePositioninClass(){
        System.out.println(sectionwise.size());
        Collections.sort(sectionwise, new Comparator<Pair<String, Pair<String, List<TermWise>>>>() {
            @Override
            public int compare(Pair<String, Pair<String, List<TermWise>>> o1, Pair<String, Pair<String, List<TermWise>>> o2) {
                float total1 = calculateTotal(o1.second.second);
                float total2 = calculateTotal(o2.second.second);
                return Float.compare(total2,total1);
            }
        });

        String Id = "";
        for(int i = 0 ;i<sectionwise.size();i++){
            System.out.println(sectionwise.get(i).first+" "+sectionwise.get(i).second.first);
            if(sectionwise.get(i).first.equals(CurrentSection) && sectionwise.get(i).second.first.equals(CurrentStudent)){
                Id = Integer.toString(i+1);
                break;
            }
        }
        return Id;
    }
    private String calculatePosition() {
        List<Pair<String,List<TermWise>>> results = allresults.get(CurrentSection);


        Collections.sort(results, new Comparator<Pair<String, List<TermWise>>>() {
            @Override
            public int compare(Pair<String, List<TermWise>> o1, Pair<String, List<TermWise>> o2) {

                float total1 = calculateTotal(o1.second);
                float total2 = calculateTotal(o2.second);
                return Float.compare(total2,total1);
            }
        });
        String Id = "";
        for(int i = 0;i<results.size();i++){

            System.out.println(results.get(i).first);
            if(results.get(i).first.equals(CurrentStudent)){
                Id = Integer.toString(i+1);
                break;
            }
        }
        return Id;
    }

    private void getDataFromFirebase() {


        //String path = url+Currentclass+"/Section/"+CurrentSection+"/"+CurrentStudent+"/Marks/";
        String path = "Classes/" + SchoolId + "/class/" + Currentclass + "/Section/" + CurrentSection + "/" + CurrentStudent + "/Marks/";
        System.out.println("aaaaaaaaaaaaaaaaaa");
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//        System.out.println(path);
        DatabaseReference databaseReference = firebaseDatabase.getReference(path);

        final Map subjcets = new HashMap();
        final Map termSubj = new HashMap();
        final int[] countt = {0};
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                System.out.println(dataSnapshot.getValue());
                for (DataSnapshot snapshot : dataSnapshot.child("Terms").getChildren()) {

                    ArrayList<Pair<String, Pair<String, String>>> sList = new ArrayList<>();
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        String num = snapshot1.child("number").getValue(String.class);
                        String maxNumber = snapshot1.child("maxNumber").getValue(String.class);
                        if (subjcets.get(snapshot1.getKey()) == null) {
                            subjcets.put(snapshot1.getKey(), ++countt[0]);
                        }
                        Pair<String, Pair<String, String>> pp = new Pair<>(snapshot1.getKey(), new Pair<>(num, maxNumber));

                        sList.add(pp);
                    }
                    TermWise termWise = new TermWise(snapshot.getKey(), sList);
                    if (type.equals("1")) {
                        ExamDetails exm;
                        if (allExams.get(snapshot.getKey()) != null) {
                            exm = allExams.get(snapshot.getKey());
                            try {
                                Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(exm.getPublishresultdate());

                                Date date = new SimpleDateFormat("dd/MM/yyyy").parse(String.valueOf(new Date()));

                                if (date.compareTo(date1) > 0) {
                                    upcoming.add(termWise);
                                } else {
                                    terms.add(termWise);
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        } else {
                            terms.add(termWise);
                        }
                    } else {
                        terms.add(termWise);
                    }


                }
                for (DataSnapshot snapshot : dataSnapshot.child("Ct").getChildren()) {

                    ArrayList<Pair<String, Pair<String, String>>> sList = new ArrayList<>();
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        String num = snapshot1.child("number").getValue(String.class);
                        String maxNumber = snapshot1.child("maxNumber").getValue(String.class);
                        Pair<String, Pair<String, String>> pp = new Pair<>(snapshot1.getKey(), new Pair<>(num, maxNumber));
                        sList.add(pp);
                    }
                    TermWise termWise = new TermWise(snapshot.getKey(), sList);
                    ct.add(termWise);
                }

                System.out.println();

                //makeLine(getView(), subjcets);
                makeBarChart();
                generateAvailableResult();

                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                dialog.dismiss();
            }
        });
    }


    private String calculatetotalHighestMarks(){
        String num = "";

        List<TermWise>term = sectionwise.get(0).second.second;

        return  Float.toString(calculateTotal(term));
    }

    private float calculateTotal(List<TermWise> ss) {

        String terms = currentExam.get(currentPosition).getTerm();
        float a = 0;
        for(int i = 0 ;i<ss.size() ;i++){

            if(ss.get(i).getTerm().equals(terms)){
                float total = 0;

                for(int j = 0;j<ss.get(i).getSubjects().size();j++){
                    if(ss.get(i).getSubjects().get(j).second.first!=null){
                        total += Float.parseFloat(ss.get(i).getSubjects().get(j).second.first);

                    }
                }
                a = total;
                break;
            }
        }
        return a;
    }

    private Map<String,Float> calculateSubjWiseMarks(){
        String terms = currentExam.get(currentPosition).getTerm();

        Map<String,Float>subjectHigestMarks = new HashMap<>();
        for(int i = 0 ;i<sectionwise.size();i++){
            List<TermWise>ss = sectionwise.get(i).second.second;
            for(int j = 0 ;j<ss.size(); j++){
                System.out.println(ss.get(j).getTerm());
                if(ss.get(j).getTerm().equals(terms)){

                    for(int k = 0 ;k<ss.get(j).getSubjects().size();k++){
                        if(ss.get(j).getSubjects().get(k).first !=null){

                            if(subjectHigestMarks.get(ss.get(j).getSubjects().get(k).first)==null){
                                float ff = Float.parseFloat(ss.get(j).getSubjects().get(k).second.first);
                                subjectHigestMarks.put(ss.get(j).getSubjects().get(k).first,ff);
                            }
                            else{
                                if(ss.get(j).getSubjects().get(k).second.first != null){

                                    float ff = Float.parseFloat(ss.get(j).getSubjects().get(k).second.first);
                                    if(ff>subjectHigestMarks.get(ss.get(j).getSubjects().get(k).first)){
                                        subjectHigestMarks.put(ss.get(j).getSubjects().get(k).first,ff);
                                    }
                                }
                            }
                        }

                    }
                }
            }

        }

        return subjectHigestMarks;
    }


    private void generateAvailableResult() {

        View v = this.getView();

        table.setStretchAllColumns(true);

        table.removeAllViews();
        Collections.reverse(terms);
        Collections.reverse(ct);
        for (int i = 0; i < terms.size(); i++) {
            TableRow tableRow = new TableRow(view.getContext());
            tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            table.addView(tableRow);
            LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LinearLayout rowView = (LinearLayout) inflater.inflate(R.layout.available_result, null);

            TextView examNames = (TextView) rowView.findViewById(R.id.examNames);
            if (allExams.get(terms.get(i).getTerm()) != null) {
                examNames.setText(allExams.get(terms.get(i).getTerm()).getExamname());
            } else {
                examNames.setText((terms.get(i).getTerm()));
            }

            Button viewResult = (Button) rowView.findViewById(R.id.viewResult);
            ImageButton imageButton = (ImageButton)rowView.findViewById(R.id.download);
            final int finalI1 = i;
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentPosition = finalI1;
                    currentExam = terms;
                    try {
                        check();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            });
            final int finalI = i;
            viewResult.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    System.out.println(finalI);
                    openDialogMenu(finalI, terms);
                }
            });

            tableRow.addView(rowView);
        }
        for (int i = 0; i < ct.size(); i++) {
            TableRow tableRow = new TableRow(view.getContext());
            tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            table.addView(tableRow);
            LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LinearLayout rowView = (LinearLayout) inflater.inflate(R.layout.available_result, null);

            TextView examNames = (TextView) rowView.findViewById(R.id.examNames);
            examNames.setText(ct.get(i).getTerm());
            ImageButton imageButton = (ImageButton)rowView.findViewById(R.id.download);
            final int finalI1 = i;
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentPosition = finalI1;
                    currentExam = ct;
                    try {
                        check();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            Button viewResult = (Button) rowView.findViewById(R.id.viewResult);
            final int finalI = i;
            viewResult.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    System.out.println(finalI);
                    openDialogMenu(finalI, ct);
                }
            });

            tableRow.addView(rowView);
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        String FIleName = schoolDetails.getSchoolname()+"_"+Currentclass+"_"+CurrentSection+"_"+CurrentStudent+"_"
                +currentExam.get(currentPosition).getTerm()+new SimpleDateFormat("yyyyMMdd_HHss", Locale.getDefault())
                .format(System.currentTimeMillis());
        File image = File.createTempFile(
                FIleName,  /* prefix */
                ".pdf",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        return image;
    }


    private void createPdf() throws IOException {


        Document document = new Document();
        String FIleName = schoolDetails.getSchoolname()+"_"+Currentclass+"_"+CurrentSection+"_"+CurrentStudent+"_"
                +currentExam.get(currentPosition).getTerm()+new SimpleDateFormat("yyyyMMdd_HHss", Locale.getDefault())
                .format(System.currentTimeMillis());


        File pdfilepath = createImageFile();

        try {
            creatingParagraph(document,pdfilepath);
            openDialogBox(pdfilepath);
            Toast.makeText(getContext(), pdfilepath.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    private void openDialogBox(final File pdfFile) {
        dialog.dismiss();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setMessage("Do you want to open file?");
        alertDialogBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        if (pdfFile.exists()) //Checking if the file exists or not
                        {
                            pdfFile.setReadable(true);
                            Uri path =Uri.fromFile(pdfFile);
                            System.out.println(path);
                            Intent objIntent = new Intent(Intent.ACTION_VIEW);

                            Uri apkURI = FileProvider.getUriForFile(
                                    getContext(),
                                    getContext().getApplicationContext()
                                            .getPackageName() + ".provider", pdfFile);

                            objIntent.setDataAndType(apkURI, "application/pdf");
                            objIntent.setFlags(Intent. FLAG_ACTIVITY_CLEAR_TOP);
                            objIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            try {
                                startActivity(objIntent);
                            } catch (ActivityNotFoundException e) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setTitle("No Application Found");
                                builder.setMessage("Download one from Android Market?");
                                builder.setPositiveButton("Yes, Please",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent marketIntent = new Intent(Intent.ACTION_VIEW);
                                                marketIntent.setData(Uri.parse("market://details?id=com.adobe.reader"));
                                                startActivity(marketIntent);
                                            }
                                        });
                                builder.setNegativeButton("No, Thanks", null);
                                builder.create().show();
                            }

                        } else {

                            Toast.makeText(getActivity(), "The file not exists! ", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void creatingParagraph(Document document, File pdfilepath) throws FileNotFoundException, DocumentException {

        System.out.println(calculatePosition());
        String sec_position = calculatePosition();
        String cls_position = calculatePositioninClass();
        String highest_marks = calculatetotalHighestMarks();

        Map<String,Float> subjWiseMarks= calculateSubjWiseMarks();

        for(String s : subjWiseMarks.keySet()){
            System.out.println(s+ " "+subjWiseMarks.get(s));
        }


        PdfWriter.getInstance(document, new FileOutputStream(pdfilepath.getAbsolutePath()));
        document.open();
        document.setPageSize(PageSize.A4);
        document.addCreationDate();
        document.setMargins(0, 0, 0, 0);
        document.addAuthor("Prime Edu");
        document.addCreator("Ratul Bhowmick");

        LineSeparator lineSeparator = new LineSeparator();
        lineSeparator.setLineColor(new BaseColor(0, 0, 0, 68));


        PdfPTable tt = new PdfPTable(2);


        Drawable d = getResources().getDrawable(R.drawable.logo);
        BitmapDrawable bitDw = ((BitmapDrawable) d);
        Bitmap bmp = bitDw.getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);


        Drawable d2 = getResources().getDrawable(R.drawable.school_radiant);
        BitmapDrawable bitDw2 = ((BitmapDrawable) d2);
        Bitmap bmp2 = bitDw2.getBitmap();
        ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
        bmp2.compress(Bitmap.CompressFormat.PNG, 100, stream2);
        try {



            Image image1 = Image.getInstance(stream.toByteArray());
//            Image image1 = Image.getInstance(new URL("http://www.javatips.net/static/images/create-pdf-with-itext-java.jpg"));

            Image image2 = Image.getInstance(stream2.toByteArray());
            image2.setAlignment(PdfPCell.ALIGN_LEFT);
            image2.scaleToFit(50, 50);
            image1.setAlignment(PdfPCell.ALIGN_RIGHT);
            image1.scaleToFit(50, 50);


            PdfPTable ppp = new PdfPTable(2);
            ppp.setWidthPercentage(100);
            if(SchoolId.equals("j0fPt6J9qEaFqh5FSISkll3thwp2")){
                ppp.addCell(getCell(image2, PdfPCell.ALIGN_LEFT));
            }else{
                ppp.addCell(getCell("", PdfPCell.ALIGN_LEFT));
            }
            ppp.addCell(getCell(image1, PdfPCell.ALIGN_RIGHT));
            document.add(ppp);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //School Title
        Font mOrderDetailsTitleFont = new Font(Font.FontFamily.COURIER, 20.0f, Font.NORMAL, BaseColor.BLACK);
        Chunk mOrderDetailsTitleChunk = new Chunk(schoolDetails.getSchoolname(), mOrderDetailsTitleFont);
        Paragraph mOrderDetailsTitleParagraph = new Paragraph(mOrderDetailsTitleChunk);
        mOrderDetailsTitleParagraph.setAlignment(Element.ALIGN_CENTER);
        document.add(mOrderDetailsTitleParagraph);

        document.add(new Paragraph(""));

        Font schooladdFont = new Font(Font.FontFamily.COURIER, 15.0f, Font.NORMAL, BaseColor.BLACK);
        Chunk schooladdChunk = new Chunk(schoolDetails.getSchooladdress(),schooladdFont);
        Paragraph schooladdPara = new Paragraph(schooladdChunk);
        schooladdPara.setAlignment(Element.ALIGN_CENTER);
        schooladdPara.setSpacingAfter(10f);

        document.add(schooladdPara);
        document.add(new Paragraph(""));


        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(80);
        table.setSpacingAfter(5f);
        table.addCell(getCell("Email: "+schoolDetails.getSchoolemail(), PdfPCell.ALIGN_LEFT));
        table.addCell(getCell("Contact No: "+schoolDetails.getSchoolcontactnumber(), PdfPCell.ALIGN_RIGHT));
        document.add(table);

        document.add(new Paragraph(""));

        document.add(new Paragraph(""));


        document.add(new Chunk(lineSeparator));


        String exmName = "";
        if(allExams.get(currentExam.get(currentPosition).getTerm()) == null){
            exmName = currentExam.get(currentPosition).getTerm();
        }
        else{
            exmName = allExams.get(currentExam.get(currentPosition).getTerm()).getExamname();
        }
        Font schooleFont = new Font(Font.FontFamily.COURIER, 15.0f, Font.NORMAL, BaseColor.BLACK);
        Chunk schooleChunk = new Chunk(exmName,schooleFont);
        Paragraph schoolePara = new Paragraph(schooleChunk);
        schoolePara.setAlignment(Element.ALIGN_CENTER);
        document.add(schoolePara);


        document.add(new Paragraph(""));
        Font font = new Font(Font.FontFamily.COURIER, 14.0f, Font.NORMAL, BaseColor.BLACK);
        Chunk nChunk = new Chunk(StudentName,font);
        Paragraph n = new Paragraph(nChunk);
        n.setAlignment(Element.ALIGN_CENTER);
        document.add(n);

        Paragraph paragraph1 = new Paragraph("Class: "+Currentclass);
        paragraph1.setSpacingAfter(2f);
        paragraph1.setAlignment(Element.ALIGN_CENTER);
        document.add(paragraph1);


        Paragraph paragraph2 = new Paragraph("Section: "+CurrentSection);
        paragraph2.setSpacingAfter(2f);
        paragraph2.setAlignment(Element.ALIGN_CENTER);
        document.add(paragraph2);

        Paragraph paragraph3 = new Paragraph("Roll: "+CurrentStudent);
        paragraph3.setAlignment(Element.ALIGN_CENTER);
        paragraph3.setSpacingAfter(5f);

        document.add(paragraph3);

        document.add(new Paragraph(""));

        document.add(new Paragraph(""));
        int total = 0;
        float obtain_marks = (float) 0.0;

        PdfPTable tabless = new PdfPTable(3);
        tabless.setWidthPercentage(60);
        tabless.addCell(getCell("Subject", PdfPCell.ALIGN_LEFT));
        tabless.addCell(getCell("Marks(Highest)", PdfPCell.ALIGN_CENTER));
        tabless.addCell(getCell("Grades", PdfPCell.ALIGN_RIGHT));
        document.add(tabless);

        document.add(new Paragraph(""));


        PdfPTable line = new PdfPTable(3);
        line.setWidthPercentage(65);
        line.setSpacingAfter(5f);
        line.addCell(getCell("-------------------", PdfPCell.ALIGN_LEFT));
        line.addCell(getCell("------------------", PdfPCell.ALIGN_CENTER));
        line.addCell(getCell("------------------", PdfPCell.ALIGN_RIGHT));
        document.add(line);

        document.add(new Paragraph(""));

        float gpaTotal = (float) 0.0;
        ArrayList<Pair<String, Pair<String, String>>> subjects = currentExam.get(currentPosition).getSubjects();
        for (int i = 0; i < subjects.size(); i++) {

            PdfPTable tables = new PdfPTable(3);
            tables.setWidthPercentage(60);
            tables.setSpacingAfter(3f);
            tables.addCell(getCell(subjects.get(i).first, PdfPCell.ALIGN_LEFT));
//
            tables.addCell(getCell(subjects.get(i).second.first+"("+subjWiseMarks.get(subjects.get(i).first)+")", PdfPCell.ALIGN_CENTER));

            Pair<String,String> p = percentages(subjects.get(i).second) ;
            String s = p.first+" ("+p.second+")";
            gpaTotal += Float.parseFloat(p.second);

            tables.addCell(getCell(s,PdfPCell.ALIGN_RIGHT));
            document.add(tables);

            document.add(new Paragraph(""));

            obtain_marks += Float.parseFloat(subjects.get(i).second.first);

            total += Integer.parseInt(subjects.get(i).second.second);
        }



        PdfPTable line2 = new PdfPTable(3);
        line2.setWidthPercentage(65);
        line2.setSpacingAfter(5f);
        line2.addCell(getCell("-------------------", PdfPCell.ALIGN_LEFT));
        line2.addCell(getCell("------------------", PdfPCell.ALIGN_CENTER));
        line2.addCell(getCell("------------------", PdfPCell.ALIGN_RIGHT));
        document.add(line2);
        PdfPTable tables = new PdfPTable(3);
        tables.setWidthPercentage(60);
        tables.setSpacingBefore(1f);
        tables.addCell(getCell("Total: ", PdfPCell.ALIGN_LEFT));
        int sizes = subjects.size();
        tables.addCell(getCell(String.format("%.2f", obtain_marks) , PdfPCell.ALIGN_CENTER));
        Float m = (gpaTotal/(float) sizes);
        tables.addCell(getCell(String.format("%.2f", m), PdfPCell.ALIGN_RIGHT));
        document.add(tables);

        document.add(new Paragraph(""));


        PdfPTable pp1 = new PdfPTable(1);
        pp1.setWidthPercentage(65);
        pp1.setSpacingBefore(15f);
        pp1.setSpacingAfter(5f);
        pp1.addCell(getCell("Total highest in the class: "+highest_marks,PdfPCell.ALIGN_LEFT));
        document.add(pp1);


        PdfPTable pp2 = new PdfPTable(1);
        pp2.setWidthPercentage(65);
        pp2.setSpacingAfter(5f);
        pp2.addCell(getCell("Section Position: "+sec_position,PdfPCell.ALIGN_LEFT));
        document.add(pp2);

        PdfPTable pp3 = new PdfPTable(1);
        pp3.setWidthPercentage(65);
        pp3.addCell(getCell("Class position: "+cls_position,PdfPCell.ALIGN_LEFT));
        document.add(pp3);

        Font red = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL, BaseColor.RED);
        Chunk redText = new Chunk("We Tech", red);

        Font normal = new Font(Font.FontFamily.COURIER, 7, Font.NORMAL, BaseColor.BLACK);
        Chunk normalText = new Chunk("Powered By ",normal);

        Paragraph p1 = new Paragraph();
        p1.setSpacingBefore(30f);
        p1.setAlignment(PdfPCell.ALIGN_RIGHT);
        p1.add(normalText);


        p1.add(redText);
        document.add(p1);


        document.close();

    }

    private Pair<String,String> percentages(Pair<String, String> second) {

        Pair<String,String> grade;

        Float total = Float.parseFloat(second.second);
        Float num = Float.parseFloat(second.first);

        Float per = (num*100)/total;

        if(per>=80.0){
            grade = new Pair<>("A+","5.00");
        }
        else if( per<80.0 && per>=70.0){
            grade = new Pair<>("A","4.00");
        }
        else if( per<70.0  && per>=60.0){
            grade = new Pair<>("A-","3.50");

        }else if(per<60.0  && per>=50.0){

            grade = new Pair<>("B","3.00");
        }else if(per<50.0  && per>=40.0){
            grade = new Pair<>("C","2.50");
        }
        else if(per<40.0  && per>=33.0){
            grade = new Pair<>("D","2.00");
        }
        else {
            grade  = new Pair<>("F","0.00");
        }

        return grade;
    }

    private void check() throws IOException {
        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(), new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, PERMISSION_CODE);
        }else{
            createPdf();
        }
    }

    private void openDialogMenu(int i, List<TermWise> sb) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.term_marks_show);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView textView = (TextView) dialog.findViewById(R.id.exams);
        TextView scl = (TextView)dialog.findViewById(R.id.schlName);
        TextView cls = (TextView)dialog.findViewById(R.id.cls);
        TextView sec = (TextView)dialog.findViewById(R.id.sec);

        scl.setText(schoolDetails.getSchoolname());
        cls.setText("Class: "+Currentclass);
        sec.setText("Section: "+CurrentSection);

        textView.setText(sb.get(i).getTerm());
        generateTablesForSubject(dialog, sb.get(i).getSubjects());
        Button done = (Button) dialog.findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void generateTablesForSubject(Dialog v, ArrayList<Pair<String, Pair<String, String>>> subjects) {
        TableLayout table = (TableLayout) v.findViewById(R.id.table);
        table.setStretchAllColumns(true);


        int total = 0;
        float obtain_marks = (float) 0.0;
        for (int i = 0; i < subjects.size(); i++) {
            TableRow tableRow = new TableRow(view.getContext());
            tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            table.addView(tableRow);
            LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LinearLayout rowView = (LinearLayout) inflater.inflate(R.layout.termmark_details, null);

            TextView subjectName = (TextView) rowView.findViewById(R.id.subjectName);
            subjectName.setText(subjects.get(i).first);

            TextView mrks = (TextView) rowView.findViewById(R.id.mrks);
            mrks.setText(subjects.get(i).second.first + "/" + subjects.get(i).second.second);

            obtain_marks += Float.parseFloat(subjects.get(i).second.first);

            total += Integer.parseInt(subjects.get(i).second.second);
            tableRow.addView(rowView);
        }

        TextView totalmarks = (TextView) v.findViewById(R.id.totalmarks);
        totalmarks.setText(String.format("%.2f", obtain_marks) + "/" + total);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        createPdf();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(getContext(), "denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    public PdfPCell getCell(String text, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setPadding(0);
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }

    public  PdfPCell getCell(Image img ,int alignment){
        PdfPCell cell = new PdfPCell();
        cell.addElement(img);
        cell.setPadding(0);
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }

    private void makeBarChart(){
        BarChart chart = view.findViewById(R.id.barchart);


        ArrayList TermNames = new ArrayList();

        ArrayList termMarks = new ArrayList();

        for (int i = 0; i < terms.size(); i++) {
            ArrayList numbers = new ArrayList<>();
            if (allExams.get(terms.get(i).getTerm()) != null){
                TermNames.add(allExams.get(terms.get(i).getTerm()).getExamname());
            }
            else{
                TermNames.add(terms.get(i).getTerm());
            }
            System.out.println(terms.get(i).getTerm());
            float totals = (float) 0.0;
            for (Pair<String, Pair<String, String>> sList : terms.get(i).getSubjects()) {
                System.out.println(sList.first+" "+sList.second.first+" "+sList.second.first);
                if(sList.second.first!=null){
                    totals += Float.parseFloat(sList.second.first);

                }
            }
            termMarks.add(new BarEntry(totals,i));
        }
        BarDataSet bardataset = new BarDataSet(termMarks, "No Of Terms");
        chart.animateY(3000);
        BarData data = new BarData(TermNames, bardataset);
        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
        chart.setData(data);

    }
}