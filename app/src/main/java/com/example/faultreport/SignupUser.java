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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupUser extends AppCompatActivity {

    private static Spinner spinner;
    private static boolean error;
    private static final String apicall = "signupUser";
    private static final String mtype = "사용자";
    private static String belong_text= "선택하세요",exnumber;
    private static Button button;
    private static TextView textView;
    private static EditText editText;
    public static String belong[] = {"선택하세요", "병원장실", "교수실","교수실5","교수실6","교수실7","교육수련부","간호국","외래파트","응급진죠파트","인공신장파트"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_user);
        spinner = findViewById(R.id.belongsp);
        button = findViewById(R.id.regist);
        editText = findViewById(R.id.serialnumberinput);
        textView = findViewById(R.id.clicksignupadmin);



        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupUser.this,SignupAdmin.class);
                startActivity(intent);
            }
        });
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.spineritems, R.id.txtsp, belong);
        spinner.setAdapter(arrayAdapter);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                belong_text = spinner.getSelectedItem().toString();
                exnumber = editText.getText().toString();
                if (exnumber.equals(" ")){
                    Toast.makeText(SignupUser.this, "내선번호 입력해주세요", Toast.LENGTH_SHORT).show();
                }
                 else if (belong_text.equals("선택하세요")) {
                    Toast.makeText(SignupUser.this, "소속은 선택해주세요", Toast.LENGTH_SHORT).show();
                }else if (exnumber.length() != 11){
                     Toast.makeText(SignupUser.this,"내선번호 다시 확인해주세요", Toast.LENGTH_LONG).show();
                }
                 else {
                    executeSignUpUser(belong_text,exnumber,mtype);
                }


            }
        });
    }

    private void executeSignUpUser(String belong_text, String exnumber, String mtype) {
        RetrofitApi retrofitApi = RetrofitUrl.retrofit.create(RetrofitApi.class);
        Call<ResponseBody> call = retrofitApi.signinuser(apicall,exnumber,belong_text,mtype);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                 RetrofitUrl retrofitUrl = new RetrofitUrl();
                 retrofitUrl.response(response,SignupUser.this);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(com.example.faultreport.SignupUser.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}