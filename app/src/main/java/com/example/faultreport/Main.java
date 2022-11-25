package com.example.faultreport;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.faultreport.DataModel.JsonUnits;

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

    public static JSONArray jsonArray;
    Button button_report_contens,button_report_go;
    TextView textView;
    String mode = "broke";
    RecyclerView recyclerView;
    AppCompatRadioButton broke,repair,medicine,safe,clean;
    public static ArrayList<Notice> notices = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        button_report_contens = findViewById(R.id.button_report_contens);
        button_report_go = findViewById(R.id.button_report_go);
        recyclerView = findViewById(R.id.recyclerivew);
        textView = findViewById(R.id.broke_report);
        broke = findViewById(R.id.broke_radio);
        repair = findViewById(R.id.repair_radio);
        medicine = findViewById(R.id.medicine_radio);
        safe = findViewById(R.id.safe_radio);
        clean = findViewById(R.id.clean_radio);
        getnotice(this,notices,recyclerView);
   button_report_go.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {
           Intent intent = new Intent(Main.this, ReportWrite.class);
           intent.putExtra("mode", mode);
           startActivity(intent);
       }
   });

    }

    public static void getnotice(Context context,ArrayList<Notice> arrayList,RecyclerView recyclerView){
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
                        Log.d("fsfsfss", arrayList.get(i).getDate());
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
}
