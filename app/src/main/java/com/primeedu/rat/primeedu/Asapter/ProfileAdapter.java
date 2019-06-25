package com.primeedu.rat.primeedu.Asapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.primeedu.rat.primeedu.Class.TeacherDetails;
import com.primeedu.rat.primeedu.R;
import com.futuremind.recyclerviewfastscroll.SectionTitleProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileAdapter  extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> implements SectionTitleProvider{

    List<Pair<String,TeacherDetails>>list ;
    Context context;
    String SchoolId;
    ProgressDialog dialog;
    String type;
    public ProfileAdapter(List<Pair<String, TeacherDetails>> list, Context context, String schoolId,String type) {
        this.list = list;
        this.context = context;
        SchoolId = schoolId;
        this.type = type;
    }

    @NonNull
    @Override
    public ProfileAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.profile, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileAdapter.ViewHolder viewHolder, final int i) {


        Glide.with(context)
                .load(list.get(i).second.getTeacherimage())
                .into(viewHolder.profile);
        viewHolder.name.setText(list.get(i).second.getTeachername());
        viewHolder.subject.setText(list.get(i).second.getTeachersubject());
        viewHolder.contact.setText(list.get(i).second.getTeachercontactno());

        if(type.equals("1"))viewHolder.prof.setEnabled(false);

        viewHolder.prof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(v.getContext(), v, Gravity.RIGHT);

                popup.getMenuInflater().inflate(R.menu.addclass,popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        if(item.getItemId() == R.id.addclass){
                            openNumberPicker(list.get(i).first);
                        }
                        if(item.getItemId() == R.id.deletes){
                            deleteInSchool(list.get(i).first);
                        }

                        return true;
                    }
                });

                popup.show();
            }
        });
    }

    private void deleteInSchool(final String id){
        showLoading();
        String path= "Schools/"+SchoolId+"/Teachers/";
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(path);
        Query query = databaseReference;

        query.orderByValue().equalTo(id)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.getValue() == null){
                            dialog.dismiss();
                            Toast.makeText(context, "Teacher Id does not found.", Toast.LENGTH_SHORT).show();
                        }else{

                            for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                                String key = snapshot.getKey();

                                String url = "Schools/"+SchoolId+"/Teachers/"+key+"/";
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(url);
                                databaseReference.removeValue();
                            }
                            deleteTeacherId(id);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        dialog.dismiss();
                        Toast.makeText(context, "Something is wrong.", Toast.LENGTH_SHORT).show();
                    }
                });

    }
    private void deleteTeacherId(final String id) {
        String path= "Schools/"+SchoolId+"/Teachers/"+id+"/";
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(path);

        ref.removeValue();
        String url = "Users/Teachers/"+id+"/";
        System.out.println(url);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(url);
        databaseReference.removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        dialog.dismiss();
                        if(task.isSuccessful()){
                            Toast.makeText(context, "Successfully removed.", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(context, "Something is wrong.", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Toast.makeText(context, "Something is wrong.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void openNumberPicker(final String TeacherId) {
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.classsection, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(false);

        final Spinner spinner_id = (Spinner) promptsView
                .findViewById(R.id.spinner_id);



        final AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.setCancelable(false);
        final EditText section  = promptsView.findViewById(R.id.section);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.classtype, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_id.setAdapter(adapter);

        promptsView.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(spinner_id.getSelectedItemPosition() == 0){
                    Toast.makeText(context, "Empty Field", Toast.LENGTH_SHORT).show();
                }
                else if(section.getText().toString().equals("")){
                    Toast.makeText(context, "Empty field.", Toast.LENGTH_SHORT).show();
                }
                else{
                    alertDialog.dismiss();
                    saveData(TeacherId,spinner_id.getSelectedItem().toString(),section.getText().toString());
                }
            }
        });
        promptsView.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });


        alertDialog.show();

    }



    private void saveData(String TeacherId,String s,String Section) {
        showLoading();
        System.out.println(TeacherId);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users/Teachers/"+TeacherId+"/Class/");
        ref.child(s).child(Section).setValue("")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        dialog.dismiss();
                        if(task.isSuccessful()){
                            Toast.makeText(context, "Successfully added.", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(context, "Check internet connection.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void showLoading() {

        dialog = new ProgressDialog(context);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Please Wait ...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public String getSectionTitle(int position) {
        return list.get(position).second.getTeachername().substring(0, 1);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name,subject,contact;
        CircleImageView profile;
        LinearLayout prof;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            prof = itemView.findViewById(R.id.prof);
            profile = (CircleImageView)itemView.findViewById(R.id.profileImg);
            name = (TextView)itemView.findViewById(R.id.name);
            subject = (TextView)itemView.findViewById(R.id.subject);
            contact = (TextView)itemView.findViewById(R.id.contactNo);
        }
    }
}
