package com.example.faultreport;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.faultreport.DataModel.RealPathUtil;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportWrite extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;

   public static String mode;
   public static String mode_post = "add",report_type;
    public static ImageView imageView;
    public static TextView textView_image;
    public static LinearLayout button_chose_image,linearLayout_image;
    public static CheckBox checkBox;
    public static ImageView imageView_Type;
    public static  Call<ResponseBody> call;
    public static String path = "";
    public static File file;
    public static TextView textView;
    private  static Button send,check,update,canel;

    Boolean error;

    TextView number,names;
    String username,rank,mtype,exnumber;


    String  name = null,location = null,title = null,descr = null;
    EditText editText_name,editText_location,edittext_tile,editText_description;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)){
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){

            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_write);

        getSupportActionBar().setDisplayShowCustomEnabled(true);
        LayoutInflater inflator = (LayoutInflater) this .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.custom_imageview, null);
        getSupportActionBar().setCustomView(v);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigatorview);
        navigationView.bringToFront();
        drawerToggle  = new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.report_contens: {
                                Intent intent = new Intent(ReportWrite.this, ReportContent.class);
                                startActivity(intent);
                                break;
                            }
                            case R.id.logout:{
                                Intent intent1 = new Intent(ReportWrite.this,login.class);
                                startActivity(intent1);
                                break;
                            }
                            case R.id.reportwrite:{
                                Intent intent2 = new Intent(ReportWrite.this,Main.class);
                                startActivity(intent2);
                                break;
                            }
                            case R.id.notice:{
                                Intent intent3 = new Intent(ReportWrite.this,notice_content.class);
                                startActivity(intent3);
                                break;
                            }
                            default: {
                                break;
                            }
                        }
                        return false;
                    }
                });
        button_chose_image = findViewById(R.id.choose_image);
        linearLayout_image = findViewById(R.id.layout_image);
        imageView = findViewById(R.id.image_view);
        checkBox = findViewById(R.id.checked_box);
        number = findViewById(R.id.number);
        names = findViewById(R.id.name1);
        imageView_Type = findViewById(R.id.img_type);
        textView = findViewById(R.id.text_type);
        send = findViewById(R.id.send);
        textView_image = findViewById(R.id.text_image);
        check = findViewById(R.id.check);
        update = findViewById(R.id.update);
        canel = findViewById(R.id.cancel);

        Bitmap bitmap;

        editText_name = findViewById(R.id.edit_name);
        editText_description = findViewById(R.id.notice_desr_edit);
        editText_location = findViewById(R.id.edit_location);
        edittext_tile = findViewById(R.id.edit_title);
getuser();


        Intent intent = getIntent();
        mode = intent.getStringExtra("mode");
        button_chose_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED){
                   Intent intent1 = new Intent();
                   intent1.setType("image/*");
                   intent1.setAction(Intent.ACTION_GET_CONTENT);
                   intent1.putExtra("path",path);
                   startActivityForResult(intent1,10);
                } else {
                    ActivityCompat.requestPermissions(ReportWrite.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }
            }
        });

        SetType(mode);



        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              GetData();
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
                    checkBox.setVisibility(View.INVISIBLE);
                    button_chose_image.setVisibility(View.INVISIBLE);
                    textView_image.setVisibility(View.INVISIBLE);
                    update.setVisibility(View.VISIBLE);
                    check.setVisibility(View.INVISIBLE);
                    canel.setVisibility(View.INVISIBLE);
                }


                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editText_name.setEnabled(true);
                        edittext_tile.setEnabled(true);
                        checkBox.setVisibility(View.VISIBLE);
                        button_chose_image.setVisibility(View.VISIBLE);
                        textView_image.setVisibility(View.VISIBLE);
                        editText_description.setEnabled(true);
                        editText_location.setEnabled(true);
                        update.setVisibility(View.INVISIBLE);
                        check.setVisibility(View.VISIBLE);
                        canel.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
      canel.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        finish();
    }
});
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetData();
                if (checkBox.isChecked()){
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
               else {
                     setretrofitcheckboxed();
                }
                } else {
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
                    else if (path.equals("")){
                        Toast.makeText(ReportWrite.this,"이미지 선택 해주세요", Toast.LENGTH_SHORT).show();
                    } else {
                        setretrofituncheckboxed();
                    }
                }

            }
        });
    }



    private void WriteNotice( String uri) {
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200){
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        error = jsonObject.getBoolean("error");
                        JSONObject jsonObject1 = jsonObject.getJSONObject("user");
                        String id = jsonObject1.getString("ID_RE");
                        Log.d("Report" , response.body().toString());
                        if (error){
                            Toast.makeText(ReportWrite.this, "Error", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ReportWrite.this, "시설 신고 성공적으로 등록 완료되었습니다", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ReportWrite.this, ReportDeatailContent.class);
                            intent.putExtra("id", id);
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
              Log.d("ERROR",t.toString());
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
            Context context = ReportWrite.this;
            path = RealPathUtil.getRealPath(context,selectedimage);
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            imageView.setImageBitmap(bitmap);
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

    public void GetData(){
        name = editText_name.getText().toString();
        title = edittext_tile.getText().toString();
        location = editText_location.getText().toString();
        report_type = textView.getText().toString();
        descr = editText_description.getText().toString();
    }


    public void setretrofitcheckboxed(){
        RequestBody moder = RequestBody.create(MediaType.parse("text/plain"), mode_post);
        RequestBody titler = RequestBody.create(MediaType.parse("text/plain"), title);
        RequestBody locationr = RequestBody.create(MediaType.parse("text/plain"), location);
        RequestBody report_typer = RequestBody.create(MediaType.parse("text/plain"), report_type);
        RequestBody report_descr = RequestBody.create(MediaType.parse("text/plain"), descr);
        RequestBody namer = RequestBody.create(MediaType.parse("text/plain"), name);
        RetrofitApi retrofitApi = RetrofitUrl.retrofit.create(RetrofitApi.class);
        call = retrofitApi.addreport_flsimage(moder, titler, locationr, report_typer, report_descr, namer);
        WriteNotice(path);
    }

    public void setretrofituncheckboxed(){
        RequestBody moder = RequestBody.create(MediaType.parse("text/plain"), mode_post);
        RequestBody titler = RequestBody.create(MediaType.parse("text/plain"), title);
        RequestBody locationr = RequestBody.create(MediaType.parse("text/plain"), location);
        RequestBody report_typer = RequestBody.create(MediaType.parse("text/plain"), report_type);
        RequestBody report_descr = RequestBody.create(MediaType.parse("text/plain"), descr);
        RequestBody namer = RequestBody.create(MediaType.parse("text/plain"), name);
        RetrofitApi retrofitApi = RetrofitUrl.retrofit.create(RetrofitApi.class);
        File file1 = new File(path);
        RequestBody fileimgr = RequestBody.create(MediaType.parse("multipart /form-data"), file1);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file1.getName(), fileimgr);
        call = retrofitApi.addreport(moder, titler, locationr, report_typer, report_descr, namer, body);
        WriteNotice(path);
    }

    public  void getuser(){

        SharedPreferences mPreferences = getSharedPreferences("data",Context.MODE_PRIVATE);
        exnumber = mPreferences.getString("number","");
        mtype = mPreferences.getString("mtype","");
        Log.i("TASK", mtype);
        if (mtype.equals("사용자")){
            number.setText(exnumber);
            names.setText("");
        } else {
            number.setText(exnumber);
            username = mPreferences.getString("name","");
            rank = mPreferences.getString("rank", "");
            names.setText(username + rank);
        }
    }


}