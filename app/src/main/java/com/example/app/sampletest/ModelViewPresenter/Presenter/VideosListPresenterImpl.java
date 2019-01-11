/*
 * Created by Prabhakaran on 10/1/2019
 */
package com.example.app.sampletest.ModelViewPresenter.Presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.app.sampletest.ModelViewPresenter.Model.VideoListPojo;
import com.example.app.sampletest.ModelViewPresenter.View.VideoListView;
import com.example.app.sampletest.WebService.ApiClient;
import com.example.app.sampletest.WebService.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideosListPresenterImpl implements VideosListPresenter {
    private Context context;
    private VideoListView videoListView;

    public VideosListPresenterImpl(Context context, VideoListView videoListView) {
        this.context = context;
        this.videoListView = videoListView;

    }

    @Override
    public void getVideoList() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<List<VideoListPojo>> call = apiService.getPostList();
        videoListView.showProgress();
        call.enqueue(new Callback<List<VideoListPojo>>() {
            @Override
            public void onResponse(@NonNull Call<List<VideoListPojo>> call, @NonNull Response<List<VideoListPojo>> response) {
                try {
                    if (response.isSuccessful()) {
                        if (response.code() == 200) {
                            videoListView.hideProgress();
                            videoListView.setVideoLists(response.body());

                        } else {
                            videoListView.hideProgress();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    videoListView.hideProgress();
                }

            }

            @Override
            public void onFailure(@NonNull Call<List<VideoListPojo>> call, @NonNull Throwable t) {
                videoListView.hideProgress();
            }

        });


    }
}
