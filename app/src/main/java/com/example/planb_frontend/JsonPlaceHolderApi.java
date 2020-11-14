package com.example.planb_frontend;

import retrofit2.Call;
import retrofit2.http.GET;

import java.util.List;

public interface JsonPlaceHolderApi {

    @GET("api/v1/video/list")
    Call<List<Post>> getPosts();

}
