package com.primeedu.rat.primeedu;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.primeedu.rat.primeedu.AuthorityHomeActivity.AuthorityMainActivity;
import com.primeedu.rat.primeedu.Login.LoginActivity;
import com.primeedu.rat.primeedu.Student.StudentActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Admin extends AppCompatActivity implements View.OnClickListener{

    String icon_url="https://www.wetech.com";
    String Id ="WeTechCompany";
    String Pass ;
    ProgressDialog dialog;
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        init();
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
    private void init() {


        requestQueue = Volley.newRequestQueue(this);
        findViewById(R.id.admin).setOnClickListener(this);
        findViewById(R.id.link).setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.admin){

            getShared();
        }
        if(v.getId() == R.id.link){
            openLink();
        }
        if(v.getId() == R.id.back){
            finish();
        }
    }

    private void optionsPopUp(){

        LayoutInflater li = LayoutInflater.from(this);
        final View promptsView = li.inflate(R.layout.optionforadmin, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        alertDialogBuilder.setView(promptsView);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        promptsView.findViewById(R.id.announce).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPost();
            }
        });

        promptsView.findViewById(R.id.adminCpass).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeAdmin();
            }
        });

        promptsView.findViewById(R.id.cps).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSignUp();
            }
        });
        promptsView.findViewById(R.id.update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDatabases();
            }
        });

    }

    private void updateDatabases() {
        LayoutInflater li = LayoutInflater.from(this);
        final View promptsView = li.inflate(R.layout.schoolupdatedatabase, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);


        final EditText user = promptsView.findViewById(R.id.userId);
        final EditText pass = promptsView.findViewById(R.id.userPass);


        final int[] countt = {0};
        promptsView.findViewById(R.id.show).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countt[0]++;
                if(countt[0] %2 !=0){
                    pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }else{
                    pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });
        alertDialogBuilder.setView(promptsView);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        promptsView.findViewById(R.id.done).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String us = user.getText().toString();
                        String pas = pass.getText().toString();

                        if(us.equals("") && pas.equals("")){
                            Toast.makeText(Admin.this, "Field Is empty", Toast.LENGTH_SHORT).show();
                        }
                        else{

                            loginAuthority(us,pas,alertDialog);
                         }
                    }
                }
        );

    }

    private void loginAuthority(final String ID, final String Pass, final AlertDialog alertDialog){

        showLoading();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Schools/");
        databaseReference.orderByChild("School Code").equalTo(ID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //System.out.println(dataSnapshot.getValue());

                        dialog.dismiss();
                        if(dataSnapshot.getValue() == null){
                            String msg = "School Id doesn't exist. Please check again.";
                            openDialogS(msg);
                        }
                        else{
                            for( DataSnapshot snapshot : dataSnapshot.getChildren()){
                                if(snapshot.child("School Pass").getValue().toString().equals(Pass)){
                                    alertDialog.dismiss();
                                    updateDatabase(snapshot.getKey());
                                }
                                else{
                                    String msg = "Password is wrong.";
                                    openDialogS(msg);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }
    private void updateDatabase(final String Key) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setMessage("Are You Sure? You want to remove all data?");
        alertDialogBuilder.setPositiveButton("ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        deletClasses(Key);
                    }
                });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();


    }

    private void deletTeacherClasses(final String SchoolId) {
        String path= "Schools/"+SchoolId+"/Teachers";
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(path);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() == null){

                    deleteStudent(SchoolId);
                }
                else{
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        String key = snapshot.getValue(String.class);

                        String url = "Users/Teachers/"+key+"/Class";
                        System.out.println(url);

                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(url);
                        databaseReference.removeValue();
                    }
                    deleteStudent(SchoolId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                dialog.dismiss();
            }
        });


    }

    private void deleteStudent(final String SchoolId){
        String path = "Schools/"+SchoolId+"/Students/";

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(path);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() ==null){
                    dialog.dismiss();
                }
                else{
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        String url = "Users/Student/"+snapshot.getValue().toString()+"/";
                        System.out.println(url);
                        String url2 = "Schools/"+SchoolId+"/Students/"+snapshot.getKey()+"/";

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(url);
                        reference.removeValue();

                        FirebaseDatabase.getInstance().getReference(url2).removeValue();
                    }
                    dialog.dismiss();
                    openDialogS("Successfully removed data.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                dialog.dismiss();
            }
        });
    }

    private void deletClasses(final String SchoolId) {
        dialog.show();
        String path = "Classes/"+SchoolId+"/class/";
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(path);

        ref.removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            String url = "Exams/"+SchoolId;
                            DatabaseReference reff = FirebaseDatabase.getInstance().getReference(url);
                            reff.removeValue();

                            String url2 = "Schools/"+SchoolId+"/Notice/";
                            DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference(url2);
                            ref2.removeValue();
                            deletTeacherClasses(SchoolId);

                        }
                        else{
                            dialog.dismiss();
                            openDialogS("Something is wrong.");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        openDialogS("Something is wrong");
                    }
                });
    }

    private void openDialogS(String msg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(msg);
        alertDialogBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    private void changeAdmin(){
        LayoutInflater li = LayoutInflater.from(this);
        final View promptsView = li.inflate(R.layout.changepass, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        alertDialogBuilder.setView(promptsView);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();


        final EditText ps = promptsView.findViewById(R.id.Pass);
        final EditText cps = promptsView.findViewById(R.id.ConfirmPass);

        promptsView.findViewById(R.id.done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ps.getText().toString().equals("")|| cps.getText().toString().equals("")){
                    Toast.makeText(Admin.this, "Some field are empty", Toast.LENGTH_SHORT).show();

                }
                else if(ps.getText().toString().length()<=8){
                    Toast.makeText(Admin.this, "Password should be more than 8 letters.", Toast.LENGTH_SHORT).show();

                }
                else if(ps.getText().toString().equals(cps.getText().toString())){
                    setAdmin(ps.getText().toString(),alertDialog);
                    Toast.makeText(Admin.this, "Successfully changed.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(Admin.this, "Password Does not Matched", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setAdmin(String s, final AlertDialog alertDialog) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Admin");
        ref.child("adminPass").setValue(s)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        alertDialog.dismiss();
                        if(task.isSuccessful()){
                            Toast.makeText(Admin.this, "Successfully changed.", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(Admin.this, "Something is wrong.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void changeSignUp(){

        LayoutInflater li = LayoutInflater.from(this);
        final View promptsView = li.inflate(R.layout.changepass, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        alertDialogBuilder.setView(promptsView);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();


        final EditText ps = promptsView.findViewById(R.id.Pass);
        final EditText cps = promptsView.findViewById(R.id.ConfirmPass);

        promptsView.findViewById(R.id.done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ps.getText().toString().equals("")|| cps.getText().toString().equals("")){
                    Toast.makeText(Admin.this, "Some field are empty", Toast.LENGTH_SHORT).show();

                }
                else if(ps.getText().toString().length()<=8){
                    Toast.makeText(Admin.this, "Password should be more than 8 letters.", Toast.LENGTH_SHORT).show();

                }
                else if(ps.getText().toString().equals(cps.getText().toString())){
                    setSharedPref(ps.getText().toString(),alertDialog);
                    Toast.makeText(Admin.this, "Successfully changed.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(Admin.this, "Password Does not Matched", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void openPopUp() {
        LayoutInflater li = LayoutInflater.from(this);
        final View promptsView = li.inflate(R.layout.adminlogin, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);


        final EditText user = promptsView.findViewById(R.id.adminUser);
        final EditText pass = promptsView.findViewById(R.id.adminPass);


        final int[] countt = {0};
        promptsView.findViewById(R.id.show).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countt[0]++;
                if(countt[0] %2 !=0){
                    pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }else{
                    pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });
        alertDialogBuilder.setView(promptsView);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        promptsView.findViewById(R.id.logIn).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String us = user.getText().toString();
                        String pas = pass.getText().toString();

                        if(us.equals(Id) && pas.equals(Pass)){
                            alertDialog.dismiss();
                            optionsPopUp();
                        }
                        else{
                            Toast.makeText(Admin.this, "User Id or Password does not matched.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );


    }

    private void openPost() {
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.post, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);


        final EditText msg = promptsView.findViewById(R.id.newPostWritten);


        promptsView.findViewById(R.id.post).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(msg.getText().toString().equals("")){
                            Toast.makeText(Admin.this, "Empty Field.", Toast.LENGTH_SHORT).show();
                        }
                        else{

                            openDialogBox(msg.getText().toString());
                        }
                    }
                }
        );
        alertDialogBuilder.setView(promptsView);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void openLink() {
        goToUrl(icon_url);
    }
    private void goToUrl (String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }
    private void openDialogBox(final String msg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Your Massage: ");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setMessage(msg);
        alertDialogBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        sendMsg(msg);
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

    private void sendMsg(String msg) {
        notieKey(msg);
    }
    private void notieKey(String msg){

        JSONObject data = new JSONObject();
        try {
            String s = "PrimeEdu";
            String ttl = "Prime Edu has posted a new announcement";
            data.put("to","/topics/"+s);
            JSONObject notify = new JSONObject();
            notify.put("title",ttl);
            notify.put("body",msg);

            data.put("notification",notify);
            String Url = "https://fcm.googleapis.com/fcm/send";
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Url,
                    data,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println("Hi");
                            System.out.println(response);
                            Toast.makeText(Admin.this, "Massage has been posted.", Toast.LENGTH_SHORT).show();

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Admin.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String>header = new HashMap<>();
                    header.put("content-type","application/json");
                    header.put("authorization","key=AIzaSyCBietxW6-cybuRGw9W5IKCqB9m4fH1iUQ");
                    return  header;
                }
            };
            requestQueue.add(request);
        }
        catch (JSONException e){
            e.printStackTrace();
        }

    }
    private void setSharedPref(String s, final AlertDialog alertDialog){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Admin");
        ref.child("signUpPass").setValue(s)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        alertDialog.dismiss();
                        if(task.isSuccessful()){
                            Toast.makeText(Admin.this, "Successfully changed.", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(Admin.this, "Something is wrong.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void getShared(){
        showLoading();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Admin");
        ref.child("adminPass")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dialog.dismiss();
                      if(dataSnapshot.getValue() == null){

                      }else{

                          Pass = dataSnapshot.getValue().toString();
                          openPopUp();
                      }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}
