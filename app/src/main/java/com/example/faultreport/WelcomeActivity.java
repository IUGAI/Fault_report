package com.example.faultreport;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {


    private Handler handler;
    private Intent intent;
    private static TextView textViewph,textViewname;
    String exnumber,mtype,name,rank;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        textViewph = findViewById(R.id.phone);
        textViewname = findViewById(R.id.name);
        SharedPreferences_get();
        Setdatatottext();
        gotomain();

    }

    public void SharedPreferences_get(){
        SharedPreferences mPreferences = getSharedPreferences("data",Context.MODE_PRIVATE);
         exnumber = mPreferences.getString("number","");
         mtype = mPreferences.getString("mtype","");
         name = mPreferences.getString("name","");
         rank = mPreferences.getString("rank","");
    }


    public void Setdatatottext(){
        if (mtype.equals("사용자")){
            textViewname.setText("");
        } else {
            textViewname.setText(String.format("%s %s님",rank,name));
        }
        textViewph.setText(exnumber + "님");
    }

    public  void gotomain(){
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                intent = new Intent(WelcomeActivity.this, Main.class);
                startActivity(intent);
            }
        }, 1000);
    }
}