package com.example.faultreport;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportDeatailContent extends AppCompatActivity {

    String filter = "single";
    String id;
    JSONArray jsonArray;
    Button button;


    TextView number,name;
    String username,rank,mtype,exnumber;

    private static LinearLayout linearLayout;
    private static TextView textView_title, textView_name, location_text, datetime,notice_desc,textView_type;
    private static ImageView imageView,imageView_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_final_check);
        textView_title = findViewById(R.id.title_reports);
        textView_name = findViewById(R.id.name);
        location_text = findViewById(R.id.location);
        datetime = findViewById(R.id.datetime);
        number = findViewById(R.id.number);
        name = findViewById(R.id.name1);
        imageView_result = findViewById(R.id.report_img);
        linearLayout = findViewById(R.id.medicine);
        notice_desc = findViewById(R.id.notice_desr);
        button = findViewById(R.id.check);
        imageView = findViewById(R.id.image_view);
        textView_type = findViewById(R.id.text);
         getuser();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReportDeatailContent.this, ReportContent.class);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("id")){
            id = intent.getStringExtra("id");
            getContent(ReportDeatailContent.this,id);
        }
    }



    public  void getContent(Context context, String id) {
        RetrofitApi retrofitApi = RetrofitUrl.retrofit.create(RetrofitApi.class);
        Call<ResponseBody> call = retrofitApi.getreportssingle(filter, id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    jsonArray = new JSONArray(response.body().string());
                    Log.d("asdasd", jsonArray.toString());
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String date = jsonObject.getString("Datatime");
                    String name = jsonObject.getString("Name");
                    String title = jsonObject.getString("Title");
                    String location = jsonObject.getString("Location");
                    String report_type = jsonObject.getString("Report_type");
                    String description = jsonObject.getString("Description");
                    String result = jsonObject.getString("Result");

                    switch (result){
                        case "신고":
                            imageView_result.setImageResource(R.drawable.status_call);
                            break;
                        case "처리완료":
                            imageView_result.setImageResource(R.drawable.status_done);

                    }
                    textView_title.setText(title);
                    textView_name.setText(name);
                    location_text.setText(location);
                    datetime.setText(date);
                    notice_desc.setText(description);
                    switch (report_type){
                        case "시설 고장":
                            imageView.setImageResource(R.drawable.ic_baseline_phone_missed_24);
                            textView_type.setText("시설 고장");
                            break;
                        case "의료 장비":
                            imageView.setImageResource(R.drawable.ic_baseline_medical_services_24);
                            textView_type.setText("의료 장비");
                            break;
                        case "하자 신청":
                            imageView.setImageResource(R.drawable.ic_baseline_build_24);
                            textView_type.setText("하자 신청 ");
                            break;
                        case "보안 신고":
                            imageView.setImageResource(R.drawable.ic_baseline_videocam_24);
                            textView_type.setText("보안 신고");
                            break;
                        case "청소 요청":
                            imageView.setImageResource(R.drawable.ic_baseline_cleaning_services_24);
                            textView_type.setText("청소 요청");
                            break;
                        default:
                            linearLayout.setVisibility(View.INVISIBLE);
                            break;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                 Log.d("Failure", t.toString());
            }
        });
    }

    public  void getuser(){

        SharedPreferences mPreferences = getSharedPreferences("data",Context.MODE_PRIVATE);
        exnumber = mPreferences.getString("number","");
        mtype = mPreferences.getString("mtype","");
        Log.i("TASK", mtype);
        if (mtype.equals("사용자")){
            number.setText(exnumber);
            name.setText("");
        } else {
            number.setText(exnumber);
            username = mPreferences.getString("name","");
            rank = mPreferences.getString("rank", "");
            name.setText(username + rank);
        }
    }
}