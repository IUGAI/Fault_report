package com.example.faultreport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.faultreport.DataModel.JsonUnits;
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

public class Main extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;

    public static String filter = "all";
    public static JSONArray jsonArray;
    RecyclerView recyclerView;
    Button button_report_contens,button_report_go,logout;
    TextView textView;
    String mode = "";

   TextView number,name;
   String username,rank;

    String exnumber,mtype;

    RadioButton broke,repair,medicine,safe,clean;
    ArrayList<Notice> notices = new ArrayList<>();

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
        setContentView(R.layout.main);

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
                               Intent intent = new Intent(Main.this, ReportContent.class);
                               startActivity(intent);
                                break;
                            }
                            case R.id.logout:{
                                Intent intent1 = new Intent(Main.this,login.class);
                                startActivity(intent1);
                                break;
                            }
                            case R.id.reportwrite:{
                                Intent intent2 = new Intent(Main.this,Main.class);
                                startActivity(intent2);
                                break;
                            }
                            case R.id.notice:{
                                Intent intent = new Intent(Main.this,notice_content.class);
                                startActivity(intent);
                                break;
                            }
                            default: {
                                break;
                            }
                        }
                        return false;
                    }
                });


        number = findViewById(R.id.number);
        name = findViewById(R.id.name);
        button_report_contens = findViewById(R.id.button_report_contens);
        logout = findViewById(R.id.button_do_report);
        button_report_go = findViewById(R.id.button_report_go);
        recyclerView = findViewById(R.id.recyclerivew);
        textView = findViewById(R.id.broke_report);
        broke = findViewById(R.id.broke_radio);
        repair = findViewById(R.id.repair_radio);
        medicine = findViewById(R.id.medicine_radio);
        safe = findViewById(R.id.safe_radio);
        clean = findViewById(R.id.clean_radio);
        getnotice(Main.this,notices);
    getuser();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

   button_report_go.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {
           if (mode.equals("")){
               AlertDialog.Builder msg = new AlertDialog.Builder(Main.this)
                       .setTitle("경고")
                       .setMessage("고장 신고 내용 선택해주세요")
                       .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                           }
                       });
               AlertDialog alert = msg.create();
               alert.show();
           } else {
               Intent intent = new Intent(Main.this, ReportWrite.class);
               intent.putExtra("mode", mode);
               startActivity(intent);
           }
       }
   });
   button_report_contens.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {
           Intent intent = new Intent(Main.this, ReportContent.class);
           startActivity(intent);
       }
   });


    }

    public void getnotice(Context context, ArrayList arrayList){
        RetrofitApi retrofitApi = RetrofitUrl.retrofit.create(RetrofitApi.class);
        Call<ResponseBody> call = retrofitApi.getnotice();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    jsonArray = new JSONArray(response.body().string());
                    for (int i = 0; i < jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String id = jsonObject.getString("id");
                        String date = jsonObject.getString("data");
                        String content = jsonObject.getString("title");
                        arrayList.add(new Notice(id,content,date));
                        Adapter adapter = new Adapter(arrayList);
                        recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
                        recyclerView.setAdapter(adapter);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void onclickbuttonradio(View view) {
        boolean isslected = ((AppCompatRadioButton) view).isChecked();
        switch (view.getId()){
            case  R.id.clean_radio:
                if (isslected) {
                    mode = "clean";
                    clean.setChecked(true);
                }
                break;
            case R.id.broke_radio:
                if (isslected) {
                    mode = "broke";
                    broke.setChecked(true);
                }
                break;
            case R.id.medicine_radio:
                if (isslected) {
                    mode = "medicine";
                    medicine.setChecked(true);
                }
                 break;
            case  R.id.safe_radio:
                if (isslected) {
                    mode = "safe";
                    safe.setChecked(true);
                }
                break;
            case R.id.repair_radio:
                if (isslected) {
                    mode = "repair";
                    repair.setChecked(true);
                }
                break;
            default:
                break;
        }
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
