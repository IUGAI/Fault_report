package com.example.faultreport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportContent extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;

    String filter = "all", exnumber, mtype, filter_s = "all";
    JSONArray jsonArray;
    TextView textView;
    ArrayList<Report> arrayList = new ArrayList<>();
    Button button;
    RecyclerView recyclerView;
    AdapterReport adapter;
    RadioButton radioButton1, radioButton2, radioButton3,radioButton4,radioButton5;

    TextView number, name;
    String username, rank;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)){
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){

            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_content);


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
                                Intent intent = new Intent(ReportContent.this, ReportContent.class);
                                startActivity(intent);
                                break;
                            }
                            case R.id.logout:{
                                Intent intent1 = new Intent(ReportContent.this,login.class);
                                startActivity(intent1);
                                break;
                            }
                            case R.id.reportwrite:{
                                Intent intent2 = new Intent(ReportContent.this,Main.class);
                                startActivity(intent2);
                                break;
                            }
                            case R.id.notice:{
                                Intent intent3 = new Intent(ReportContent.this,notice_content.class);
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


        recyclerView = findViewById(R.id.recyclerivew);
        button = findViewById(R.id.delete);
        number = findViewById(R.id.number);
        name = findViewById(R.id.name);
        textView = findViewById(R.id.textview_ff);
        radioButton1 = findViewById(R.id.broke_radio);
        radioButton2 = findViewById(R.id.medicine_radio);
        radioButton3 = findViewById(R.id.repair_radio);
        radioButton4 = findViewById(R.id.safe_radio);
        radioButton5 = findViewById(R.id.clean_radio);
        textView.setVisibility(View.INVISIBLE);
        getuser();
        getContent(arrayList, ReportContent.this, filter,filter_s);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReportContent.this, Main.class);
                startActivity(intent);
            }
        });
    }

    private void getContent(ArrayList arrayList, Context context, String filter_fun, String filter_funs) {
        arrayList.clear();
        RetrofitApi retrofitApi = RetrofitUrl.retrofit.create(RetrofitApi.class);
        Call<ResponseBody> call = retrofitApi.getreports(filter_fun,filter_funs);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    jsonArray = new JSONArray(response.body().string());
                    if (jsonArray.length() == 0){
                         adapter.notifyDataSetChanged();
                         textView.setVisibility(View.VISIBLE);
                    } else {
                           textView.setVisibility(View.INVISIBLE);
                          recyclerView.setVisibility(View.VISIBLE);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String image = jsonObject.getString("image");
                            String date = jsonObject.getString("Datatime");
                            String id = jsonObject.getString("id");
                            String title = jsonObject.getString("Title");
                            String location = jsonObject.getString("Location");
                            String report_type = jsonObject.getString("Report_type");
                            String result = jsonObject.getString("Result");
                            Log.i("Array", jsonArray.toString());
                            arrayList.add(new Report(id, image, location, date, title, report_type, result));
                            adapter = new AdapterReport(arrayList);
                            recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
                            recyclerView.setAdapter(adapter);
                            adapter.setOnitemclicklistnerl(new AdapterReport.Onitemclicklistner() {
                                @Override
                                public void onitemclick(int postition) {
                                    Report report = adapter.getReports().get(postition);
                                    SharedPreferences mPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
                                    exnumber = mPreferences.getString("number", "");
                                    mtype = mPreferences.getString("mtype", "");
                                    Log.i("TASK", mtype);
                                    if (mtype.equals("사용자")) {
                                        Intent intent = new Intent(ReportContent.this, ReportDeatailContent.class);
                                        intent.putExtra("id", report.getId());
                                        startActivity(intent);
                                    } else {
                                        Intent intent = new Intent(ReportContent.this, AdminMidCheck.class);
                                        intent.putExtra("id", report.getId());
                                        startActivity(intent);
                                    }
                                }
                            });
                        }
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void getuser() {
        SharedPreferences mPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        exnumber = mPreferences.getString("number", "");
        mtype = mPreferences.getString("mtype", "");
        Log.i("TASK", mtype);
        if (mtype.equals("사용자")) {
            number.setText(exnumber);
            name.setText("");
        } else {
            number.setText(exnumber);
            username = mPreferences.getString("name", "");
            rank = mPreferences.getString("rank", "");
            name.setText(username + rank);
        }
    }


    public void onclickbuttonradio(View view) {
        boolean isslected = ((AppCompatRadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.all:
                if (isslected) {
                    filter = "all";
                    filter_s = "all";
                    radioButton1.setChecked(false);
                    radioButton2.setChecked(false);
                    radioButton3.setChecked(false);
                    radioButton4.setChecked(false);
                    radioButton5.setChecked(false);
                    getContent(arrayList, ReportContent.this, filter, filter_s);
                }
                break;
            case R.id.call:
                if (isslected) {
                   filter = "call";
                    getContent(arrayList, ReportContent.this, filter, filter_s);
                }
                break;
            case R.id.process:
                if (isslected) {
                    filter = "process";
                    getContent(arrayList, ReportContent.this, filter, filter_s);
                }
                break;
            case R.id.done:
                if (isslected) {
                    filter = "done";
                    getContent(arrayList, ReportContent.this, filter, filter_s);
                }
                break;

            default:
                break;
        }
    }

    public void onclickbuttonradios(View view) {
        boolean isselected = ((AppCompatRadioButton) view).isChecked();
        switch (view.getId()){
            case  R.id.broke_radio:
                if (isselected){
                    filter_s = "broke";
                    getContent(arrayList, ReportContent.this, filter, filter_s);
                }
                break;
            case  R.id.medicine_radio:
                filter_s = "medecine";
                getContent(arrayList, ReportContent.this, filter, filter_s);
                break;
            case  R.id.repair_radio:
                filter_s = "radio";
                getContent(arrayList, ReportContent.this, filter, filter_s);
                break;
            case  R.id.safe_radio:
                filter_s = "repair";
                getContent(arrayList, ReportContent.this, filter, filter_s);
                break;
            case  R.id.clean_radio:
                filter_s = "clean";
                getContent(arrayList, ReportContent.this, filter, filter_s);
                break;
            default:
                break;
        }
    }
}