package com.example.rat.primeedu.Student;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rat.primeedu.Class.FatherDetails;
import com.example.rat.primeedu.Class.GurdianDetails;
import com.example.rat.primeedu.Class.MotherDetails;
import com.example.rat.primeedu.Class.Student;
import com.example.rat.primeedu.Class.StudentDetails;
import com.example.rat.primeedu.Login.SignupStudent;
import com.example.rat.primeedu.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


@SuppressLint("ValidFragment")
public class AboutStudentFragment extends Fragment implements View.OnClickListener{

    private OnFragmentInteractionListener mListener;
    private String CurrentSection,Currentclass,CurrentStudent;
    TextView sName, rollNo,fName,fOccu,fNo,mName,mOccu,mNo,lName,sSec,sAdd,sNo,gName,gNo,gOccu,gRel;
    ProgressDialog dialog;
    Spinner spinner;
    CheckBox checkEnable;
    private String url = "SchoolName/Class/";
    String type;
    Student student;
    String StudentId ;

    View v;
    public AboutStudentFragment(String currentStudent, Student student, String type) {
        this.student = student;
        this.type = type;
        StudentId = currentStudent;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_about_student, container, false);

        //showLoading();
        init();
        System.out.println(student.getStudentDetails().getGender());
        spinner.setEnabled(false);
        loadData();
        return v;
    }

    private void loadData() {
        sName.setText(student.getStudentDetails().getStudentfname());
        lName.setText(student.getStudentDetails().getStudenlname());
        sAdd.setText(student.getStudentDetails().getStudentaddress());
        sSec.setText(student.getCurrentsection());
        spinner.setSelection(giveItemNumber(student.getCurrentclass()));
        rollNo.setText(student.getCurrentid());
        sNo.setText(student.getStudentDetails().getContactnumber());

        gName.setText(student.getStudentDetails().getGurdianDetails().getStudentgurdianname());
        gNo.setText(student.getStudentDetails().getGurdianDetails().getStudentgurdiancontactno());
        gRel.setText(student.getStudentDetails().getGurdianDetails().getRelation());
        gOccu.setText(student.getStudentDetails().getGurdianDetails().getStudentgurdianoccupation());

        fName.setText(student.getStudentDetails().getFatherDetails().getStudentfathername());
        fOccu.setText(student.getStudentDetails().getFatherDetails().getStudentfatheroccupation());
        fNo.setText(student.getStudentDetails().getFatherDetails().getStudentfathercontactno());

        mName.setText(student.getStudentDetails().getMotherDetails().getStudentmothername());
        mOccu.setText(student.getStudentDetails().getMotherDetails().getStudentmotheroccupation());
        mNo.setText(student.getStudentDetails().getMotherDetails().getStudentmothercontactno());

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

    private int giveItemNumber(String s){
        int num = 0;
        switch (s){
            case "Play":
                num =  1;
                break;
            case "Nursery":
                num =  2;
            break;
            case "KG":
                num =  3;
            break;
            case "1":
                num =  4;
            break;
            case "2":
                num =  5;
            break;
            case "3":
                num =  6;
            break;
            case "4":
                num =  7;
            break;
            case "5":
                num =  8;
            break;
            case "6":
                num =  9;
            break;
            case "7":
                num =  10;
            break;
            case "8":
                num =  11;
            break;
            case "9":
                num =  12;
            break;
            case "10":
                num =  13;
            break;
            case "11":
                num =  14;
            break;
            case "12":
                num =  15;
            break;
        }
        return  num;
    }

    private void setDataTOfields(boolean flag) {

        if(type.equals("1")){


            sName.setEnabled(flag);
            rollNo.setEnabled(flag);
            lName.setEnabled(flag);
            sAdd.setEnabled(flag);
            sSec.setEnabled(flag);
            sNo.setEnabled(flag);
            spinner.setEnabled(flag);


            fName.setEnabled(flag);
            fOccu.setEnabled(flag);
            mName.setEnabled(flag);
            mOccu.setEnabled(flag);

            gName.setEnabled(flag);
            gOccu.setEnabled(flag);

            gRel.setEnabled(flag);
        }
        else {

            sName.setEnabled(flag);
            rollNo.setEnabled(flag);
            lName.setEnabled(flag);
            sAdd.setEnabled(flag);
            sSec.setEnabled(flag);
            sNo.setEnabled(flag);
            spinner.setEnabled(flag);


            fName.setEnabled(flag);
            fOccu.setEnabled(flag);
            mName.setEnabled(flag);
            mOccu.setEnabled(flag);
            mNo.setEnabled(flag);
            fNo.setEnabled(flag);


            gName.setEnabled(flag);
            gOccu.setEnabled(flag);

            gRel.setEnabled(flag);
            gNo.setEnabled(flag);
        }
    }

    private void init() {
        sName = (TextView)v.findViewById(R.id.sName);
        lName = (TextView)v.findViewById(R.id.lastName);
        sAdd = (TextView)v.findViewById(R.id.sAddress);
        sSec = (TextView)v.findViewById(R.id.sSec);
        sNo = (TextView)v.findViewById(R.id.sNo);

        spinner = (Spinner)v.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.classtype, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        rollNo = (TextView)v.findViewById(R.id.rollNo);
        fName = (TextView)v.findViewById(R.id.fName);
        fOccu = (TextView)v.findViewById(R.id.fOccu);
        fNo = (TextView)v.findViewById(R.id.fNo);
        mName = (TextView)v.findViewById(R.id.mName);
        mOccu = (TextView)v.findViewById(R.id.mOccu);
        mNo = (TextView)v.findViewById(R.id.mNo);


        gName = (TextView)v.findViewById(R.id.gName);
        gNo = (TextView)v.findViewById(R.id.gNo);
        gOccu = (TextView)v.findViewById(R.id.gOccu);
        gRel = (TextView)v.findViewById(R.id.gRelation);


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
            setDataTOfields(true);

        }else{
            openDialogBox();
            setDataTOfields(false);
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
        showLoading();

        String Currentclass = spinner.getSelectedItem().toString();
        String CurrentSection = sSec.getText().toString();
        String CurrentStudentId = rollNo.getText().toString();

        String gurDianName = gName.getText().toString();
        String gurDianOccu = gOccu.getText().toString();
        String gurDianContact = gNo.getText().toString();
        String gurDianRelation = gRel.getText().toString();

        String studentFName = sName.getText().toString();
        String studentLName = lName.getText().toString();

        String studentContact = sNo.getText().toString();
        String studentAddress = sAdd.getText().toString();

        String fNme = fName.getText().toString();
        String fOcu = fOccu.getText().toString();
        String fN = fNo.getText().toString();

        String mNme = mName.getText().toString();
        String mOcu = mOccu.getText().toString();
        String mN = mNo.getText().toString();

        FatherDetails fatherDetails = new FatherDetails(fNme,fOcu,fN,"");
        MotherDetails motherDetails = new MotherDetails(mNme,mOcu,mN,"");
        GurdianDetails gurdianDetails = new GurdianDetails(gurDianName,gurDianOccu,gurDianContact,student.getStudentDetails().getGurdianDetails().getGurdianprofilepic(),gurDianRelation);



        StudentDetails studentDetails = new StudentDetails(studentFName,studentLName,studentAddress,student.getStudentDetails().getStudentprofilepicture(),
                student.getStudentDetails().getGender(),studentContact,gurdianDetails,fatherDetails,motherDetails);


        Student stD =  new Student(student.getSchoolcode(),student.getStudentcode(),CurrentStudentId,CurrentSection,
               Currentclass,student.getSutdentpass(),studentDetails,"");


        String path = "Users/Student/"+StudentId;
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(path);
        ref.setValue(stD).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    storeinCLass();
                }
                else{
                    dialog.dismiss();
                    openDialogBoxes("Something is wrong.");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                openDialogBoxes("Something is wrong.");
            }
        });

    }

    private void storeinCLass(){

        String Currentclass = spinner.getSelectedItem().toString();
        String CurrentSection = sSec.getText().toString();
        String CurrentStudentId = rollNo.getText().toString();
        final String studentFName = sName.getText().toString();
        final String studentLName = lName.getText().toString();


        String path = "Classes/"+student.getSchoolcode()+"/class/"+Currentclass+"/Section/"+CurrentSection+"/"+CurrentStudentId+"/StudentCode/";
        DatabaseReference classref = FirebaseDatabase.getInstance().getReference(path);
        classref.setValue(StudentId)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dialog.dismiss();
                        if (mListener != null) {
                            System.out.println("aaaaaaaaaaaaaaaaaa");
                            mListener.onFragmentInteraction(studentFName,studentLName);
                        }
                        openDialogBoxes("Data has been saved");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        openDialogBoxes("Please Check Internet Connection.");
                    }
                });
    }

    private void openDialogBoxes(String s) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setMessage(s);
        alertDialogBuilder.setPositiveButton("ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(String fNames, String lNames);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
