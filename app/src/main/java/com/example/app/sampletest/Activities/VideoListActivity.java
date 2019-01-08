package com.example.app.sampletest.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.app.sampletest.Adapters.VideoListAdapter;
import com.example.app.sampletest.CommonUtils.CustomProgressDialog;
import com.example.app.sampletest.CommonUtils.Utilities;
import com.example.app.sampletest.ModelViewPresenter.Model.VideoListPojo;
import com.example.app.sampletest.ModelViewPresenter.Presenter.VideosListPresenter;
import com.example.app.sampletest.ModelViewPresenter.Presenter.VideosListPresenterImpl;
import com.example.app.sampletest.ModelViewPresenter.View.VideoListView;
import com.example.app.sampletest.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoListActivity extends BaseActivity implements VideoListView {
    @BindView(R.id.rv_video_list)
    RecyclerView rvVideoList;
    @BindView(R.id.appbarlayout_tool_bar)
    Toolbar toolbar;
    public VideoListAdapter videoListAdapter;
    public List<VideoListPojo> videoListPojoList = new ArrayList<>();
    public VideosListPresenter videosListPresenter;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        ButterKnife.bind(this);
        init();
    }

    public void init() {
        videosListPresenter = new VideosListPresenterImpl(VideoListActivity.this, this);
        videosListPresenter.getVideoList();
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        toolbar.setTitle(getString(R.string.empty));
        signConfig();

    }

    public void setVideoListAdapter() {
        videoListAdapter = new VideoListAdapter(this, videoListPojoList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvVideoList.setLayoutManager(layoutManager);
        rvVideoList.setAdapter(videoListAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                if(Utilities.isNetworkAvailable(this))
                logOut();
                else {
                Toast.makeText(this, "Check your Network Connection!", Toast.LENGTH_SHORT).show();
            }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void setVideoLists(List<VideoListPojo> videoListPojo) {
        videoListPojoList = videoListPojo;
        if (databaseHelper.getVideoCount() == 0) {
            for (VideoListPojo videoListPojo1 : videoListPojoList) {
                databaseHelper.insertVideo(videoListPojo1);
            }
        } else if (videoListPojoList.size() != databaseHelper.getVideoCount()) {
            for (VideoListPojo videoListPojo1 : videoListPojoList) {
                if (databaseHelper.getVideo(videoListPojo1.getId()) == null) {
                    databaseHelper.insertVideo(videoListPojo1);
                }
            }
        }
        setVideoListAdapter();
    }

    @Override
    public void showProgress() {
        progressDialog = CustomProgressDialog.getInstance(this);
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }
}
