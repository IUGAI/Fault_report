package com.example.faultreport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminMidCheck extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;

    String filter = "single";
    private static  String[] rank = {"신고","진행중","처리완료"};
    String id;
    JSONArray jsonArray;
    Spinner spinner;
    Button button,save;
    private static ImageView imageView_result;
    private static LinearLayout linearLayout;
    private static TextView textView_title, textView_name, location_text, datetime,notice_desc,textView_type;
    private static EditText processdetail;
    private static ImageView imageView;

    TextView number,name;
    String username,ranks,mtype,exnumber;

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){

            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)){
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_report_check);

        getSupportActionBar().setDisplayShowCustomEnabled(true);
        LayoutInflater inflator = (LayoutInflater) this .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.custom_imageview, null);
        getSupportActionBar().setCustomView(v);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigatorview);
        navigationView.bringToFront();
        drawerToggle  = new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.report_contens: {
                                Intent intent = new Intent(AdminMidCheck.this, ReportContent.class);
                                startActivity(intent);
                                break;
                            }
                            case R.id.logout:{
                                Intent intent1 = new Intent(AdminMidCheck.this,login.class);
                                startActivity(intent1);
                                break;
                            }
                            case R.id.reportwrite:{
                                Intent intent2 = new Intent(AdminMidCheck.this,Main.class);
                                startActivity(intent2);
                                break;
                            }
                            case R.id.notice:{
                                Intent intent3 = new Intent(AdminMidCheck.this,notice_content.class);
                                startActivity(intent3);
                                break;
                            }
                            default: {
                                break;
                            }
                        }
                        return false;
                    }
                });

        spinner = findViewById(R.id.belongsp) ;
        number = findViewById(R.id.number);
        name = findViewById(R.id.name1);
        setspinneradapter(rank, spinner);
        processdetail = findViewById(R.id.process_detail);
        imageView_result  = findViewById(R.id.result_img);
        save = findViewById(R.id.save);

       getuser();
        textView_title = findViewById(R.id.title_reports);
        textView_name = findViewById(R.id.name);
        location_text = findViewById(R.id.location);
        datetime = findViewById(R.id.datetime);
        linearLayout = findViewById(R.id.medicine);
        notice_desc = findViewById(R.id.notice_desr);
        button = findViewById(R.id.check);
        imageView = findViewById(R.id.image_view);
        textView_type = findViewById(R.id.text);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMidCheck.this, ReportContent.class);
                startActivity(intent);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String spinner_Values = spinner.getSelectedItem().toString();
                String description = processdetail.getText().toString();
                Intent intent = getIntent();
                if (intent != null && intent.hasExtra("id")) {
                    id = intent.getStringExtra("id");
                    if (description.equals("")){
                        Toast.makeText(AdminMidCheck.this, "저리 내용 입력해주세요", Toast.LENGTH_SHORT).show();
                    }
                    updateContent(AdminMidCheck.this,id, spinner_Values,description);
                    Toast.makeText(AdminMidCheck.this, "성공적으로 저리되었습니다", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(AdminMidCheck.this, ReportContent.class);
                    startActivity(intent1);

                }
            }
        });

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("id")){
            id = intent.getStringExtra("id");
            getContent(AdminMidCheck.this,id);
        }

    }


     public void updateContent(Context context, String id,String description, String result){
         RetrofitApi retrofitApi = RetrofitUrl.retrofit.create(RetrofitApi.class);
         Call<ResponseBody> call = retrofitApi.update(id,description, result);
         call.enqueue(new Callback<ResponseBody>() {
             @Override
             public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
             }

             @Override
             public void onFailure(Call<ResponseBody> call, Throwable t) {
                 Log.d("Failure", t.toString());
             }
         });
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
                    String image = jsonObject.getString("image");
                    String date = jsonObject.getString("Datatime");
                    String name = jsonObject.getString("Name");
                    String id = jsonObject.getString("id");
                    String title = jsonObject.getString("Title");
                    String detail = jsonObject.getString("Detail");
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
                            break;
                        case "진행중" :
                            imageView_result.setImageResource(R.drawable.status_proceed);

                    }

                    if (detail.equals("null")){
                        return;
                    } else {
                        processdetail.setText(detail);
                    }
                    textView_title.setText(title);
                    textView_name.setText(name);
                    location_text.setText(location);
                    datetime.setText(date);
                    notice_desc.setText(description);
                    spinner.setSelection(1);

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
            ranks = mPreferences.getString("rank", "");
            name.setText(username + ranks);
        }
    }

    public void setspinneradapter(String[] ranks, Spinner spinner){
        ArrayAdapter<String> arrayAdapterbelong = new ArrayAdapter<>(this, R.layout.spineritems, R.id.txtsp, ranks);
        spinner.setAdapter(arrayAdapterbelong);
    }
}