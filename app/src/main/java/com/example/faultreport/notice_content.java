package com.example.faultreport;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class notice_content extends AppCompatActivity {
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice_content);

        spinner = findViewById(R.id.belongsp);
        String belong[] = {"선택", "병원장실", "교수실"};

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.spineritems, R.id.txtsp, belong);
        spinner.setAdapter(arrayAdapter);
    }
}