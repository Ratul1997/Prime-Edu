package com.example.rat.primeedu.Teacher;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rat.primeedu.R;

import java.lang.reflect.Method;

public class ViewStudentActivity extends AppCompatActivity implements View.OnClickListener{

    private int row ;
    private int col = 5;
    private int studentNo ;
    final private String classUrl = "SchoolName/Class/";
    TextView className,sectionName;
    String SchoolId;
    private String Currentclass,CurrentSection,type;
    String CurrentDate = "",SchoolName;
    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_student);
        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.actionbar));

        getIntentDatas();
        init();
        populateButton();
    }
    private void getIntentDatas(){
        type = getIntent().getStringExtra("type").toString();
        System.out.println(type);

        SchoolName = getIntent().getStringExtra("schoolName").toString();
        Currentclass = getIntent().getStringExtra("clsname").toString();
        SchoolId = getIntent().getStringExtra("schoolId").toString();
        CurrentSection = getIntent().getStringExtra("sectionNo").toString();
    }
    private void init(){

        back = (ImageButton)findViewById(R.id.back);
        back.setOnClickListener(this);

        className = (TextView)findViewById(R.id.className);
        className.setText("Class: "+Currentclass);

        sectionName = (TextView)findViewById(R.id.sectionName);
        sectionName.setText("Section: "+CurrentSection);

    }

    private void populateButton(){
        TableLayout table = (TableLayout)findViewById(R.id.table);
        table.setStretchAllColumns(true);

        String no = getIntent().getStringExtra("stdNo").toString();

        studentNo = Integer.parseInt(no);
        int e = studentNo/col;
        if(e*col == studentNo)row = e;
        else row = e+1;

        int countt = 0;
        for(int i = 0 ;i<row   && countt<=studentNo ;i++){
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            table.addView(tableRow);
            for(int j = 0 ;j<5 ;j++){
                countt += 1;
                if(countt > studentNo)break;
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                LinearLayout rowView = (LinearLayout) inflater.inflate(R.layout.student, null);
                final int finalCountt = countt;

                final TextView roll = (TextView)rowView.findViewById(R.id.roll);
                final ImageView presentId = (ImageView)rowView.findViewById(R.id.presentId);

                final int finalCountt1 = countt;

                rowView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PopupMenu popup = new PopupMenu(v.getContext(), v, Gravity.RIGHT);
                        try {
                            Method method = popup.getMenu().getClass().getDeclaredMethod("setOptionalIconsVisible", boolean.class);
                            method.setAccessible(true);
                            method.invoke(popup.getMenu(), true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        final int p = finalCountt1;

                        popup.getMenuInflater().inflate(R.menu.student,popup.getMenu());

                        if(type.equals("3")){
                            System.out.println("Yes");
                            popup.getMenu().findItem(R.id.giveMarks).setVisible(false);
                            popup.getMenu().findItem(R.id.giveMarks).setEnabled(false);
                        }
                        if(type.equals("2")){

                            popup.getMenu().findItem(R.id.giveMarks).setVisible(false);
                            popup.getMenu().findItem(R.id.giveMarks).setEnabled(false);
                        }
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {

                                switch (item.getItemId()){
                                    case R.id.viewprf:
                                        Toast.makeText(ViewStudentActivity.this, "asasasas", Toast.LENGTH_SHORT).show();
                                        viewProfile(p);
                                        break;
                                    case R.id.giveMarks:
                                        giveMarks(p);
                                        break;
                                }
                                return true;
                            }
                        });

                        popup.show();
                    }
                });

                rowView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(final View v) {

                        return true;
                    }
                });
                roll.setText(rollNumber(countt));
                tableRow.addView(rowView);
            }
        }
    }
    private void viewProfile(int p){

        System.out.println(p);
        Intent intent = new Intent(this,StudentProfileByTeacher.class);
        System.out.println(sectionName.getText().toString());
        intent.putExtra("section",CurrentSection);
        intent.putExtra("class",Currentclass);
        intent.putExtra("id",Integer.toString(p));
        intent.putExtra("schoolId",SchoolId);
        intent.putExtra("schoolName",SchoolName);
        startActivity(intent);
    }
    private void giveMarks(int p){

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
        if(v.getId() == R.id.back){
            backButton();
        }

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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            backButton();
            return true;
        }
        return false;
    }
}
