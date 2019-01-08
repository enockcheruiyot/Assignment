package com.example.app.sampletest.ModelViewPresenter.View;

import com.example.app.sampletest.ModelViewPresenter.Model.VideoListPojo;

import java.util.List;

public interface VideoListView {
    void setVideoLists(List<VideoListPojo> videoListPojo);
    void showProgress();
    void hideProgress();
}
