package com.example.faultreport;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class login extends AppCompatActivity {
   private EditText editText_num;
   public static final String Sharedprefname = "User_Info";
   public static Context mctx;
   public static SharedPreferences minstance;
   private Button login;
   private TextView Signup;
   private String edit_text_get,
                  error,
                  apicall = "login";
   private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        getSupportActionBar().hide();
        editText_num = findViewById(R.id.edit_text_login);
        login = findViewById(R.id.submit);
        Signup = findViewById(R.id.textview_signup);

        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(com.example.faultreport.login.this, com.example.faultreport.SignupUser.class);
                startActivity(intent);
            }
        });




        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_text_get = editText_num.getText().toString();
                if (edit_text_get.isEmpty()){
                    Toast.makeText(login.this, "내선번호 입력해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    executeSendFeedbackForm(edit_text_get);
                }
            }
        });
    }

    private void executeSendFeedbackForm(String edit_text_get) {
        RetrofitApi retrofitApi = RetrofitUrl.retrofit.create(RetrofitApi.class);
        Call<ResponseBody> call = retrofitApi.sendUserFeedback(edit_text_get,apicall);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String s = null;
                    if (response.code() == 200) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            error = jsonObject.getString("error");
                            if (error.equals("true")){
                                Toast.makeText(login.this, "내선 번호다시 확인 해 주세요", Toast.LENGTH_SHORT).show();
                            } else  {
                                SharedPrefrences(jsonObject);
                                Intent intent = new Intent(com.example.faultreport.login.this, WelcomeActivity.class);
                                startActivity(intent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
            }


            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(com.example.faultreport.login.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void SharedPrefrences(JSONObject jsonObject) throws JSONException {
        SharedPreferences sharedPreferences = getSharedPreferences("data",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("number",jsonObject.getString("number"));
        editor.putString("mtype",jsonObject.getString("mtype"));
        if (jsonObject.getString("Name") != null){
            editor.putString("name",jsonObject.getString("Name"));
        } else {
            return;
        }
        if (jsonObject.getString("Rank") != null){
            editor.putString("rank",jsonObject.getString("Rank"));
        } else {
            return;
        }
        editor.apply();
    }


}