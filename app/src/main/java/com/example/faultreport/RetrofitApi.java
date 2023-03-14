package com.example.faultreport;

import java.io.File;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface RetrofitApi {

    @FormUrlEncoded
    @POST("api.php")
    Call<ResponseBody> sendUserFeedback(
            @Field("exnumber")String exnumber,
            @Field("apicall")String login
    );

    @FormUrlEncoded
    @POST("api.php")
    Call<ResponseBody> signinuser(
            @Field("apicall")String signupUser,
            @Field("exnumber")String exnumber,
            @Field("belong")String belong,
            @Field("mtype")String mtype
    );

    @FormUrlEncoded
    @POST("api.php")
    Call<ResponseBody> signinadmin(
        @Field("apicall") String signupAdmin,
        @Field("belong") String belong,
        @Field("rank") String rank,
        @Field("phnumber") String phnumber,
        @Field("name") String name,
        @Field("mtype") String mtype
    );


    @GET("getnotice.php")
    Call<ResponseBody> getnotice();

//    @Multipart
//    @POST("apireport.php")
//    Call<ResponseBody> addreport(
//      @Part("mode") String mode,
//      @Part("title") String title,
//      @Part("location") String location,
//      @Part("report_type") String report_type,
//      @Part("report_desc") String report_desc,
//      @Part("name") String name
//


    @Multipart
    @POST("apireport.php")
    Call<ResponseBody> addreport(
            @Part("mode") RequestBody mode,
            @Part("title") RequestBody title,
            @Part("location") RequestBody location,
            @Part("report_type") RequestBody report_type,
            @Part("report_desc") RequestBody report_desc,
            @Part("name") RequestBody name,
            @Part MultipartBody.Part file
            );

    @Multipart
    @POST("apireport.php")
    Call<ResponseBody> addreport_flsimage(
            @Part("mode") RequestBody mode,
            @Part("title") RequestBody title,
            @Part("location") RequestBody location,
            @Part("report_type") RequestBody report_type,
            @Part("report_desc") RequestBody report_desc,
            @Part("name") RequestBody name
    );


    @GET("apireport.php")
    Call<ResponseBody> getreports(
    @Query("filter") String mode,
    @Query("filter_s") String modes
    );

    @GET("apireport.php")
    Call<ResponseBody> getreportssingle(
            @Query("filter") String mode,
            @Query("id") String id
    );

    @FormUrlEncoded
    @POST("update.php")
    Call<ResponseBody> update(
            @Field("id") String id,
            @Field("Result") String result,
            @Field("Description") String Description
    );


}
