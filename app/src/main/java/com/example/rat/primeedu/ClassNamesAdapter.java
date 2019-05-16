package com.example.rat.primeedu;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ClassNamesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<ClassName> listClassName = new ArrayList<>();
    Context context;

    public ClassNamesAdapter(List<ClassName> listClassName, Context context) {
        this.listClassName = listClassName;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ClassName currentItem = listClassName.get(position);

        ((ViewHolder) holder).txt.setText("Class : " + currentItem.getClasNaMEs());
        ((ViewHolder) holder).txt1.setText("Total Section: " + currentItem.getTotalSection());


        ((ViewHolder) holder).lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(currentItem.getClasNaMEs());
                Intent intent = new Intent(context,ClassActivity.class);

                intent.putExtra("clsname",currentItem.getClasNaMEs());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listClassName.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView txt, txt1;
        CardView lin;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lin = (CardView) itemView.findViewById(R.id.lin);
            txt = (TextView) itemView.findViewById(R.id.txtId);
            txt1 = (TextView) itemView.findViewById(R.id.txtId2);
        }
    }
}
