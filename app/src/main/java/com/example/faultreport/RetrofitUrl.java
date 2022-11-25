package com.example.faultreport;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUrl {

   public static String Urls = "http://10.50.113.218/php1.1/";
   boolean error;
   public static Retrofit.Builder builder = new Retrofit.Builder()
           .baseUrl(RetrofitUrl.Urls)
           .addConverterFactory(GsonConverterFactory.create());

   public static Retrofit retrofit = builder.build();


   public void response(Response<ResponseBody> response, Context context){
      if (response.code() == 200){
         try {
            JSONObject jsonObject = new JSONObject(response.body().string());
            error = jsonObject.getBoolean("error");
            if (error){
               Toast.makeText(context, "이미 등록된 사용자", Toast.LENGTH_SHORT).show();
            } else {
               Toast.makeText(context, "사용자 성공적으로 등록되었습니다", Toast.LENGTH_SHORT).show();
               Intent intent = new Intent(context, login.class);
               context.startActivity(intent);
            }
         } catch (JSONException e) {
            e.printStackTrace();
         } catch (IOException e) {
            e.printStackTrace();
         }
      }



   }
}
