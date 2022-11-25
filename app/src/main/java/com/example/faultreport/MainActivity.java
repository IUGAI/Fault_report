package com.example.faultreport;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRadioButton;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {
    AppCompatRadioButton rbleft, rbright;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice_write);
        rbleft = findViewById(R.id.rbleft);
        rbright = findViewById(R.id.rbright);
    }

    public void onRadiobuttonClick(View view) {
        boolean isslected = ((AppCompatRadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.rbleft:
                if (isslected) {
                    rbleft.setTextColor(Color.WHITE);
                    rbright.setTextColor(Color.parseColor("#00397e"));
                }
                break;
            case R.id.rbright:
                if (isslected) {
                    rbright.setTextColor(Color.WHITE);
                    rbleft.setTextColor(Color.parseColor("#00397e"));
                }
                break;
        }
    }

}

