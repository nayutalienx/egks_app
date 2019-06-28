package com.example.demochat;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface JsonPlaceHolderApi {
    @GET("pop.php")
    Call<List<Post>> getPosts();

    @GET("push.php")
    Call<List<Post>> addPost(
            @Query("user") String user,
            @Query("text") String text
    );



}
