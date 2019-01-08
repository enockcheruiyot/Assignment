package com.example.app.sampletest.WebService;

import com.example.app.sampletest.ModelViewPresenter.Model.VideoListPojo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

    @GET("media.json?print=pretty")
    Call<List<VideoListPojo>> getPostList();

}