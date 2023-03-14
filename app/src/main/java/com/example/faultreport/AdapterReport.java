package com.example.faultreport;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public  class AdapterReport extends  RecyclerView.Adapter<AdapterReport.HolderView>{



    List<Report> reports;

    private Onitemclicklistner Onitemclicklistner;


    public AdapterReport(ArrayList reports) {
        this.reports = reports;
    }


    interface  Onitemclicklistner {
       void onitemclick(int postition);
    }


    public void  setOnitemclicklistnerl(AdapterReport.Onitemclicklistner Onitemclicklistner){
        this.Onitemclicklistner = Onitemclicklistner;
    }

    @NonNull
    @Override
    public HolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_recycler_layout,parent,false);
        return new HolderView(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull HolderView holder, int position) {
          Report report =  (Report) reports.get(position);
          holder.location.setText(report.getLocation());
          holder.date.setText(report.getDate().substring(5,16));
          holder.title.setText(report.getTitle());
          holder.report_type.setText(report.getReport_type());
          String text = report.getResult();
          holder.result.setText(text);
          if (text.equals("처리완료")){
              holder.result.setTextColor(Color.parseColor("#27485a"));
          } else if (text.equals("진행중")){
              holder.result.setTextColor(Color.parseColor("#d6858e"));
          }
          if (report.getImg().equals("none")){

          }  else {
              Picasso.get().load(report.getImg()).into(holder.image);
          }
    }

    @Override
    public int getItemCount() {
        return reports.size();
    }


    class HolderView extends RecyclerView.ViewHolder{
                ImageView image;
                ConstraintLayout constraintLayout;
        TextView location,date,title,report_type,result;
        public HolderView(@NonNull View itemView) {
            super(itemView);
            image =  itemView.findViewById(R.id.image);
            location = itemView.findViewById(R.id.location);
            date = itemView.findViewById(R.id.date);
            report_type = itemView.findViewById(R.id.report_type);
            title = itemView.findViewById(R.id.title);
            result = itemView.findViewById(R.id.result);
            constraintLayout = itemView.findViewById(R.id.constraint_layout);
            constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Onitemclicklistner != null){
                        Onitemclicklistner.onitemclick(getAdapterPosition());
                    }
                }
            });
        }
//        ImageView image;
//        TextView location,date,title,report_type,result;
//        public HolderView(@NonNull View itemView) {
//            super(itemView);
//            image =  itemView.findViewById(R.id.image_view);
//            location = itemView.findViewById(R.id.location);
//            date = itemView.findViewById(R.id.date);
//            report_type = itemView.findViewById(R.id.report_type);
//            title = itemView.findViewById(R.id.title);
//            result = itemView.findViewById(R.id.result);
//
        }



    public List<Report> getReports() {
        return reports;
    }

    public void setReports(List<Report> reports) {
        this.reports = reports;
    }
}
