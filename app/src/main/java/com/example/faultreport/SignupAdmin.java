package com.example.faultreport;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupAdmin extends AppCompatActivity {
    private static Spinner spinnerbelong, spinnerrank;
    private static boolean error;
    private static  String[] rank = {"선택하세요","VIP서버","사원","파트장","팀장","사무국장"};
    private static final String apicall = "signUpAdmin", mtype="관리자";
    private static String belong_get_text, rang_get_text,phnumbertext,nametext;
    private static EditText phnumber,name;
    private static Button regist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_admin);
        spinnerbelong = findViewById(R.id.belongsp);
        spinnerrank = findViewById(R.id.ranksp);
        regist = findViewById(R.id.regist);
        phnumber = findViewById(R.id.phnumber);
        getSupportActionBar().hide();
        name = findViewById(R.id.nameedtext);

       setspinneradapter(rank,spinnerrank);
       setspinneradapter(SignupUser.belong,spinnerbelong);


       regist.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               get_Text_fromitems();
               check_input();
               executesigninadmin();
           }
       });
    }


    private  void get_Text_fromitems(){
        belong_get_text = spinnerbelong.getSelectedItem().toString();
        rang_get_text = spinnerrank.getSelectedItem().toString();
        phnumbertext = phnumber.getText().toString();
        nametext = name.getText().toString();
    }

    private void executesigninadmin() {
        RetrofitApi retrofitApi = RetrofitUrl.retrofit.create(RetrofitApi.class);
        Call<ResponseBody> call = retrofitApi.signinadmin(apicall,belong_get_text,rang_get_text,phnumbertext,nametext,mtype);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                  RetrofitUrl retrofitUrl = new RetrofitUrl();
                  retrofitUrl.response(response, SignupAdmin.this);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(com.example.faultreport.SignupAdmin.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setspinneradapter(String[] ranks,Spinner spinner){
        ArrayAdapter<String> arrayAdapterbelong = new ArrayAdapter<>(this, R.layout.spineritems, R.id.txtsp, ranks);
        spinner.setAdapter(arrayAdapterbelong);
    }

    public void check_input(){
        if (belong_get_text.equals("선택하세요")){
            Toast.makeText(SignupAdmin.this, "소속을 선택해주세요", Toast.LENGTH_SHORT).show();
        } else if (rang_get_text.equals("선택하세요")){
            Toast.makeText(SignupAdmin.this, "지급 선택해주세요", Toast.LENGTH_SHORT).show();
        } else if (phnumbertext.equals("")){
            Toast.makeText(SignupAdmin.this, "전화번호 입력해주세요", Toast.LENGTH_SHORT).show();
        } else if (phnumbertext.length() != 11){
            Toast.makeText(SignupAdmin.this, "전화번호 다시 확인 해주세요", Toast.LENGTH_SHORT).show();
        } else if (name.equals("")){
            Toast.makeText(SignupAdmin.this, "이름 다시 확인 해주세요", Toast.LENGTH_SHORT).show();
        }
    }
}