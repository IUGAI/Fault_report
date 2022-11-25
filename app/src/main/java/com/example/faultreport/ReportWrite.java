package com.example.faultreport;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportWrite extends AppCompatActivity {
   public static String mode;
   public static String mode_post = "add",report_type;
    public static ImageView imageView;
    public static LinearLayout button_chose_image,linearLayout_image;
    public static CheckBox checkBox;
    public static ImageView imageView_Type;
    public static TextView textView;
    private  static Button send,check,update,canel;

    Boolean error;


    String  name = null,location = null,title = null,descr = null;
    EditText editText_name,editText_location,edittext_tile,editText_description;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_write);
        button_chose_image = findViewById(R.id.choose_image);
        linearLayout_image = findViewById(R.id.layout_image);
        imageView = findViewById(R.id.image_view);
        checkBox = findViewById(R.id.checked_box);
        imageView_Type = findViewById(R.id.img_type);
        textView = findViewById(R.id.text_type);
        send = findViewById(R.id.send);
        check = findViewById(R.id.check);
        update = findViewById(R.id.update);
        canel = findViewById(R.id.cancel);

        editText_name = findViewById(R.id.edit_name);
        editText_description = findViewById(R.id.notice_desr_edit);
        editText_location = findViewById(R.id.edit_location);
        edittext_tile = findViewById(R.id.edit_title);



        Intent intent = getIntent();
        mode = intent.getStringExtra("mode");
        button_chose_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent1,3);
            }
        });

        SetType(mode);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetDatas();
                WriteNotice();
            }
        });

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = editText_name.getText().toString();
                title = edittext_tile.getText().toString();
                location = editText_location.getText().toString();
                report_type = textView.getText().toString();
                descr = editText_description.getText().toString();
                if (name.equals("")){
                    Toast.makeText(ReportWrite.this, "이름 입력해주세요", Toast.LENGTH_SHORT).show();
                } else if (title.equals("")) {
                    Toast.makeText(ReportWrite.this, "제목 입력해주세요", Toast.LENGTH_SHORT).show();
                }
                else if (location.equals("")) {
                    Toast.makeText(ReportWrite.this, "장소 입력해주세요", Toast.LENGTH_SHORT).show();
                }
                else if (descr.equals("")) {
                    Toast.makeText(ReportWrite.this, "신고 내용 입력해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    editText_name.setEnabled(false);
                    edittext_tile.setEnabled(false);
                    editText_description.setEnabled(false);
                    editText_location.setEnabled(false);
                    update.setVisibility(View.VISIBLE);
                    check.setVisibility(View.INVISIBLE);
                    canel.setVisibility(View.INVISIBLE);
                }


                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editText_name.setEnabled(true);
                        edittext_tile.setEnabled(true);
                        editText_description.setEnabled(true);
                        editText_location.setEnabled(true);
                        update.setVisibility(View.INVISIBLE);
                        check.setVisibility(View.VISIBLE);
                        canel.setVisibility(View.VISIBLE);
                    }
                });
            }
        });

    }

    private void GetDatas() {
        name = editText_name.getText().toString();
        title = edittext_tile.getText().toString();
        location = editText_location.getText().toString();
        report_type = textView.getText().toString();
        descr = editText_description.getText().toString();

        if (name.equals("")){
            Toast.makeText(ReportWrite.this, "이름 입력해주세요", Toast.LENGTH_SHORT).show();
        } else if (title.equals("")) {
            Toast.makeText(ReportWrite.this, "제목 입력해주세요", Toast.LENGTH_SHORT).show();
        }
        else if (location.equals("")) {
            Toast.makeText(ReportWrite.this, "장소 입력해주세요", Toast.LENGTH_SHORT).show();
        }
        else if (descr.equals("")) {
            Toast.makeText(ReportWrite.this, "신고 내용 입력해주세요", Toast.LENGTH_SHORT).show();
        }
    }

    private void WriteNotice() {
        RetrofitApi retrofitApi = RetrofitUrl.retrofit.create(RetrofitApi.class);
        Call<ResponseBody> call = retrofitApi.addreport(mode_post,title,report_type,location,descr,name);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200){
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        error = jsonObject.getBoolean("error");
                        if (error){
                            Toast.makeText(ReportWrite.this, "Error", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ReportWrite.this, "시설 신고 성공적으로 등록 완료되었습니다", Toast.LENGTH_SHORT).show();
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

            }
        });
    }


    public void SetType(String mode){
        switch (mode){
            case "broke":
                imageView_Type.setImageResource(R.drawable.ic_baseline_phone_missed_24);
                textView.setText("시설 고장");
                break;
            case "medicine":
                imageView_Type.setImageResource(R.drawable.ic_baseline_medical_services_24);
                textView.setText("의료 장비");
                break;
            case "repair":
                imageView_Type.setImageResource(R.drawable.ic_baseline_build_24);
                textView.setText("하자 신청");
                break;
            case "safe":
                imageView_Type.setImageResource(R.drawable.ic_baseline_videocam_24);
                textView.setText("보안 신고");
                break;
            case "clean":
                imageView_Type.setImageResource(R.drawable.ic_baseline_cleaning_services_24);
                textView.setText("청소 요청");
                break;
            default:
                break;
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Uri selectedimage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedimage);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        switch(view.getId()) {
            case R.id.checked_box:
                if (checked) {
                     linearLayout_image.setVisibility(View.INVISIBLE);
                     button_chose_image.setVisibility(View.INVISIBLE);
                } else {
                    linearLayout_image.setVisibility(View.VISIBLE);
                    button_chose_image.setVisibility(View.VISIBLE);
                }
                break;
        }
    }
}