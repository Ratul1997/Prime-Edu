package com.example.rat.primeedu.Teacher;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
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
import android.widget.Toast;

import com.example.rat.primeedu.Class.ClassName;
import com.example.rat.primeedu.Asapter.ClassNamesAdapter;
import com.example.rat.primeedu.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@SuppressLint("ValidFragment")
public class ClassFregments extends Fragment implements View.OnClickListener{

    final private String classUrl = "SchoolName/Class/";
    Button add_class;
    List<ClassName> listClassName = new ArrayList<>();
    RecyclerView recyclerview;
    ClassNamesAdapter adapter;

    public ClassFregments() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        final Dialog dialog = new Dialog(getContext());

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.loadingprogressbar);
        dialog.show();

        getClasses(dialog);

        View view = inflater.inflate(R.layout.fragment_class_fregments, container, false);
        init(view);
        return view;
    }

    private void init(View view){
        add_class = (Button)view.findViewById(R.id.add_class);
        add_class.setOnClickListener(this);

        recyclerview = (RecyclerView)view.findViewById(R.id.recyclerview);

        adapter = new ClassNamesAdapter(listClassName,getActivity());
        final LinearLayoutManager mLayoutManager= new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerview.setLayoutManager(mLayoutManager);
        recyclerview.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.add_class){
            navigate();
        }
    }

    private void navigate(){
        Intent intent  = new Intent(getActivity(), AddSection.class);
        startActivity(intent);
    }

    private void getClasses(final Dialog dialog) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(classUrl);

        int total = 0;
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int count = 0;
                listClassName.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    System.out.println(snapshot.getKey());
                    count = 0;
                    for(DataSnapshot snapshot1 :snapshot.child("Section").getChildren()){
                        count++;
//                        System.out.println(snapshot1.getKey());
                    }
                    listClassName.add(new ClassName(snapshot.getKey(),count));
                }
                dialog.dismiss();
                Collections.reverse(listClassName);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(getActivity(), "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
