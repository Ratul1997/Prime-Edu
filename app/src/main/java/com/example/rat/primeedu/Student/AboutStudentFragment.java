package com.example.rat.primeedu.Student;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rat.primeedu.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


@SuppressLint("ValidFragment")
public class AboutStudentFragment extends Fragment implements View.OnClickListener{
    private String CurrentSection,Currentclass,CurrentStudent;
    TextView sName, rollNo,fName,fOccu,fNo,mName,mOccu,mNo;
    Dialog dialog;
    CheckBox checkEnable;
    private String url = "SchoolName/Class/";
    public AboutStudentFragment(String currentStudent, String currentclass, String currentSection) {

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

        View v = inflater.inflate(R.layout.fragment_about_student, container, false);

        //showLoading();
        init(v);
        getDataFromFirebase(v);
        return v;
    }

    private void showLoading() {

        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.loadingprogressbar);
        dialog.show();
    }
    private void getDataFromFirebase(View v) {

        String path = url+Currentclass+"/Section/"+CurrentSection+"/"+CurrentStudent+"/Details/";

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference(path);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot: dataSnapshot.getChildren()){

                    System.out.println(snapshot.getValue()+" "+snapshot.getKey());
                    if(snapshot.getKey().equals("fathersNo")){
                        fNo.setText(snapshot.getValue().toString());
                    }else if(snapshot.getKey().equals("fathersWork")){
                        fOccu.setText(snapshot.getValue().toString());
                    }
                    else if(snapshot.getKey().equals("fathers_name")){
                        fName.setText(snapshot.getValue().toString());
                    }
                    else if(snapshot.getKey().equals("mothersNo")){
                        mNo.setText(snapshot.getValue().toString());
                    }
                    else if(snapshot.getKey().equals("mothers_Name")){
                        mName.setText(snapshot.getValue().toString());
                    }
                    else if(snapshot.getKey().equals("mothers_Work")){
                        mOccu.setText(snapshot.getValue().toString());
                    }
                    else if(snapshot.getKey().equals("studentsName")){
                        sName.setText(snapshot.getValue().toString());
                    }
                 }
//                 dialog.dismiss();
                setDataTOfields();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void setDataTOfields() {
        rollNo.setText(CurrentStudent);
        sName.setEnabled(false);
        rollNo.setEnabled(false);
        fName.setEnabled(false);
        fOccu.setEnabled(false);
        mName.setEnabled(false);
        mOccu.setEnabled(false);
        mNo.setEnabled(false);
        fNo.setEnabled(false);

    }

    private void init(View v) {
        sName = (TextView)v.findViewById(R.id.sName);
        rollNo = (TextView)v.findViewById(R.id.rollNo);
        fName = (TextView)v.findViewById(R.id.fName);
        fOccu = (TextView)v.findViewById(R.id.fOccu);
        fNo = (TextView)v.findViewById(R.id.fNo);
        mName = (TextView)v.findViewById(R.id.mName);
        mOccu = (TextView)v.findViewById(R.id.mOccu);
        mNo = (TextView)v.findViewById(R.id.mNo);

        checkEnable = (CheckBox)v.findViewById(R.id.checkEnable);
        checkEnable.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.checkEnable){
            setDataFieldsAfterEnbleIt();
        }
    }

    private void setDataFieldsAfterEnbleIt() {
        System.out.println();

        if(checkEnable.isChecked()){
            sName.setEnabled(true);
            fName.setEnabled(true);
            fOccu.setEnabled(true);
            mName.setEnabled(true);
            mOccu.setEnabled(true);
            mNo.setEnabled(true);
            fNo.setEnabled(true);

        }else{
            openDialogBox();
            setDataTOfields();
        }
    }

    private void openDialogBox() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage("Are you sure, You wanted to change your data!");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        updateData();
//                        dialog.show();
                    }
                });

        alertDialogBuilder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void updateData() {
        String path = url+Currentclass+"/Section/"+CurrentSection+"/"+CurrentStudent+"/Details/";
        checkEnable.setEnabled(false);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference(path);

        databaseReference.child("fathers_name").setValue(fName.getText().toString());
        databaseReference.child("fathersNo").setValue(fNo.getText().toString());
        databaseReference.child("fathersWork").setValue(fOccu.getText().toString());
        databaseReference.child("mothersNo").setValue(mNo.getText().toString());
        databaseReference.child("mothers_Name").setValue(mName.getText().toString());
        databaseReference.child("mothers_Work").setValue(mOccu.getText().toString());
        databaseReference.child("studentsName").setValue(sName.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getContext(), "SuccessFully Entered", Toast.LENGTH_SHORT).show();
                            checkEnable.setEnabled(true);
                        }
                        else{
                            Toast.makeText(getContext(), "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });

        if(!checkEnable.isEnabled()){
            Toast.makeText(getContext(), "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
            checkEnable.setEnabled(true);
        }
    }


}
