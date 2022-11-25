package com.example.faultreport;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.HolderView> {

    ArrayList notices = new ArrayList<>();

    public Adapter(ArrayList notices) {
        this.notices = notices;
    }

    @NonNull
    @Override
    public HolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notice_recycler_layout,parent,false);
        return new HolderView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderView holder, int position) {
         Notice notice = (Notice) notices.get(position);
         holder.count.setText(notice.getId());
         holder.content.setText(notice.getContent().substring(0,10) + "...");
         holder.date.setText(notice.getDate());
    }

    @Override
    public int getItemCount() {
        return notices.size();
    }

    class HolderView extends RecyclerView.ViewHolder{

         TextView count,content,date;
        public HolderView(@NonNull View itemView) {
            super(itemView);
            count = itemView.findViewById(R.id.count);
            content = itemView.findViewById(R.id.content);
            date = itemView.findViewById(R.id.date);
        }
    }
}
