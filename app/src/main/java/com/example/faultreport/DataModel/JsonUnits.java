package com.example.faultreport.DataModel;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.faultreport.Notice;
import com.example.faultreport.RetrofitApi;
import com.example.faultreport.RetrofitUrl;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JsonUnits {


    public static ArrayList<Notice> getjsonunits(String json) throws JSONException {
        ArrayList<Notice> notices = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(json);
        for (int i = 0; i < jsonArray.length();i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String id = jsonObject.getString("id");
            String date = jsonObject.getString("data");
            String content = jsonObject.getString("title");
            notices.add(new Notice(id,content,date));
        }

        return notices;
    }
}
