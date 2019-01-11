/*
 * Created by Prabhakaran on 10/1/2019
 */
package com.example.app.sampletest.ModelViewPresenter.View;

import com.example.app.sampletest.ModelViewPresenter.Model.VideoListPojo;

import java.util.List;

public interface VideoListView {
    void setVideoLists(List<VideoListPojo> videoListPojo);
    void showProgress();
    void hideProgress();
}
