package com.example.rat.primeedu.Teacher;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rat.primeedu.Asapter.AnnounceMentAdapter;
import com.example.rat.primeedu.Class.AnnouncementWithClass;
import com.example.rat.primeedu.Asapter.HomeWorkAdapter;
import com.example.rat.primeedu.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class TeacherPostFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private String path="SchoolName/Class/";
    Button post;
    List<AnnouncementWithClass> Homework = new ArrayList<>();
    List<AnnouncementWithClass> Announcement = new ArrayList<>();
    EditText newPostWritten;
    RecyclerView recyclerView1, recyclerView2;
    AnnounceMentAdapter adapter1;
    HomeWorkAdapter adapter2;
    public TeacherPostFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_teacher_post, container, false);
        init(view);
        return view;
    }
    private void init(View view){
        recyclerView1 = (RecyclerView)view.findViewById(R.id.recyclerView1);
        recyclerView2 = (RecyclerView)view.findViewById(R.id.recyclerView2);

        newPostWritten = (EditText)view.findViewById(R.id.newPostWritten);


        adapter1 = new AnnounceMentAdapter(Announcement,getActivity());
        final LinearLayoutManager mLayoutManager= new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerView1.setLayoutManager(mLayoutManager);
        recyclerView1.setAdapter(adapter1);



        adapter2 = new HomeWorkAdapter(Homework,getActivity());
        final LinearLayoutManager mLayoutManagers= new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true);
        mLayoutManagers.setReverseLayout(true);
        mLayoutManagers.setStackFromEnd(true);
        recyclerView2.setLayoutManager(mLayoutManagers);
        recyclerView2.setAdapter(adapter2);

        post = (Button)view.findViewById(R.id.post);
        post.setOnClickListener(this);
        getAllAnnounceMents();
    }

    private void getAllAnnounceMents(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(path);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Announcement.clear();
                Homework.clear();

                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
//                    System.out.println(snapshot.getKey());

                    for( DataSnapshot snapshot1 : snapshot.child("AnnounceMents").child("AnnounceMent").getChildren()){


                        Announcement.add(new AnnouncementWithClass(snapshot1.getValue().toString(),snapshot.getKey()));
                    }

                    for( DataSnapshot snapshot1 : snapshot.child("AnnounceMents").child("HomeWork").getChildren()){
                        Homework.add(new AnnouncementWithClass(snapshot1.getValue().toString(),snapshot.getKey()));
                    }
                }
                for(int i = 0 ;i<Announcement.size();i++){
                    System.out.println(Announcement.get(i));
                }
                adapter1.notifyDataSetChanged();
                adapter2.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        if(R.id.post == v.getId()){
            final Dialog dialog = new Dialog(getContext());

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setContentView(R.layout.selectpostoption);

            final EditText clas = (EditText)dialog.findViewById(R.id.clas);

            Button announcement = (Button) dialog.findViewById(R.id.announcement);
            Button hw = (Button) dialog.findViewById(R.id.hw);
            hw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(clas.getText().toString().equals("")){
                        Toast.makeText(getContext(), "Enter The Class", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    post("HomeWork",clas.getText().toString());
                    dialog.dismiss();
                }
            });
            announcement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(clas.getText().toString().equals("")){
                        Toast.makeText(getContext(), "Enter The Class", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    post("AnnounceMent",clas.getText().toString());
                    dialog.dismiss();
                }
            });

            dialog.show();
        }
    }

    private void post(String Destination, String Clas){

        String msg = newPostWritten.getText().toString();
         String url= path+"/"+Clas+"/AnnounceMents/"+Destination;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(url);

        myRef.child(makeKey()).setValue(msg);
        newPostWritten.setText("");
    }
    private String makeKey(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");
        Date date = new Date();

        return  formatter.format(date);
    }
}
