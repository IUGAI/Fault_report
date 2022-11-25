package com.example.faultreport;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
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

    @FormUrlEncoded
    @POST("apireport.php")
    Call<ResponseBody> addreport(
      @Field("mode") String mode,
      @Field("title") String title,
      @Field("location") String location,
      @Field("report_type") String report_type,
      @Field("report_desc") String report_desc,
      @Field("name") String name
    );

    @GET("apireport.php")
    Call<ResponseBody> getreports(
    @Query("filter") String mode
    );

}
