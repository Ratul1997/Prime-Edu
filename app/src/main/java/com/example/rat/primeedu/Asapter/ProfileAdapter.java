package com.example.rat.primeedu.Asapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rat.primeedu.Class.TeacherDetails;
import com.example.rat.primeedu.R;
import com.futuremind.recyclerviewfastscroll.SectionTitleProvider;

import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileAdapter  extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> implements SectionTitleProvider{

    SortedList<TeacherDetails>list ;
    Context context;
    public ProfileAdapter(Context context){
        list = new SortedList<TeacherDetails>(TeacherDetails.class, new SortedList.Callback<TeacherDetails>() {
            @Override
            public int compare(TeacherDetails teacherDetails, TeacherDetails t21) {
                return t21.getTeachername().compareTo(teacherDetails.getTeachername());
            }

            @Override
            public void onChanged(int i, int i1) {
                notifyItemRangeChanged(i,i1);
            }

            @Override
            public boolean areContentsTheSame(TeacherDetails teacherDetails, TeacherDetails t21) {
                return t21.getTeachername().equals(teacherDetails.getTeachername());
            }

            @Override
            public boolean areItemsTheSame(TeacherDetails teacherDetails, TeacherDetails t21) {
                return t21.getTeachername().equals(teacherDetails.getTeachername());
            }

            @Override
            public void onInserted(int i, int i1) {
                notifyItemRangeChanged(i,i1);
            }

            @Override
            public void onRemoved(int i, int i1) {
                notifyItemRangeRemoved(i,i1);
            }

            @Override
            public void onMoved(int i, int i1) {
                notifyItemMoved(i,i1);
            }
        });
        this.context = context;
    }

    //conversation helpers
    public void addAll(List<TeacherDetails>teachers) {
        System.out.println("Adapter: "+teachers.size());
        list.beginBatchedUpdates();
        for (int i = 0; i < teachers.size(); i++) {
            list.add(teachers.get(i));
        }
        list.endBatchedUpdates();
    }

    public TeacherDetails get(int position) {
        return list.get(position);
    }

    public void clear() {
        list.beginBatchedUpdates();
        while (list.size() > 0) {
            list.removeItemAt(list.size() - 1);
        }
        list.endBatchedUpdates();
    }


    @NonNull
    @Override
    public ProfileAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.profile, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileAdapter.ViewHolder viewHolder, int i) {


        Glide.with(context)
                .load(list.get(i).getTeacherimage())
                .into(viewHolder.profile);
        viewHolder.name.setText(list.get(i).getTeachername());
        viewHolder.subject.setText(list.get(i).getTeachersubject());
        viewHolder.contact.setText(list.get(i).getTeachercontactno());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public String getSectionTitle(int position) {
        return list.get(position).getTeachername().substring(0, 1);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name,subject,contact;
        CircleImageView profile;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profile = (CircleImageView)itemView.findViewById(R.id.profileImg);
            name = (TextView)itemView.findViewById(R.id.name);
            subject = (TextView)itemView.findViewById(R.id.subject);
            contact = (TextView)itemView.findViewById(R.id.contactNo);
        }
    }
}
