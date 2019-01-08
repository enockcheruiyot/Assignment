package com.example.app.sampletest.Activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.app.sampletest.Adapters.RelatedVideoListAdapter;
import com.example.app.sampletest.CommonUtils.Constants;
import com.example.app.sampletest.CommonUtils.Utilities;
import com.example.app.sampletest.ModelViewPresenter.Model.VideoListPojo;
import com.example.app.sampletest.R;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VideoDetailsActivity extends BaseActivity {

    @BindView(R.id.ivVideoThumb)
    ImageView ivVideoThumb;
    @BindView(R.id.tvVideoTitle)
    TextView tvVideoTitle;
    @BindView(R.id.tvVideoDescription)
    TextView tvVideoDescription;
    @BindView(R.id.rvRelatedVideos)
    RecyclerView rvRelatedVideos;
    @BindView(R.id.appbarlayout_tool_bar)
    Toolbar toolbar;
    @BindView(R.id.video_view)
    SimpleExoPlayerView simpleExoPlayerView;
    @BindView(R.id.iv_control)
    ImageView ivControl;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    SimpleExoPlayer player;
    private ComponentListener componentListener;
    boolean isPlayBackFinish = false,pageOpened = false;
    int currentWindow;
    long playbackPosition;

    public List<VideoListPojo> videoListPojoList;
    public VideoListPojo videoListPojo;
    public int videoPosition;
    public RelatedVideoListAdapter relatedVideoListAdapter;
    public MediaSource mediaSource;
    public Uri videoUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_details);
        ButterKnife.bind(this);
        init();
    }

    public void init() {
        videoListPojoList = new ArrayList<>(Arrays.asList(new Gson().fromJson(getIntent().getStringExtra(Constants.VideoListPojo), VideoListPojo[].class)));
        videoListPojo = new Gson().fromJson(getIntent().getStringExtra(Constants.VideoDetails), VideoListPojo.class);
        videoPosition = getIntent().getIntExtra((Constants.VideoPosition), 0);
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.default_thumbnail)
                .error(R.drawable.default_thumbnail);
        Glide.with(this).load(videoListPojo.getThumb()).apply(options).
                into(ivVideoThumb);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        toolbar.setTitle(getString(R.string.empty));
        componentListener = new ComponentListener();
        postVideoDetails();
        setVideoListAdapter();
        signConfig();

    }

    private void initializePlayer() {
        DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
        TrackSelection.Factory adaptiveTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(this),
                new DefaultTrackSelector(adaptiveTrackSelectionFactory), new DefaultLoadControl());
        player.addListener(componentListener);
        player.prepare(mediaSource, true, false);
        simpleExoPlayerView.setPlayer(player);
        boolean haveResumePosition = currentWindow != C.INDEX_UNSET;
        if (haveResumePosition) {
            player.seekTo(currentWindow, playbackPosition);
            player.setPlayWhenReady(true);
        }
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("exoplayer-codelab")).
                createMediaSource(uri);
    }

    public void postVideoDetails() {
        videoUri = Uri.parse(videoListPojo.getUrl());
        mediaSource = buildMediaSource(videoUri);
        tvVideoTitle.setText(videoListPojo.getTitle());
        tvVideoDescription.setText(videoListPojo.getDescription());
    }

    public void setVideoListAdapter() {
        videoListPojoList.remove(videoPosition);
        relatedVideoListAdapter = new RelatedVideoListAdapter(this, videoListPojoList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvRelatedVideos.setLayoutManager(layoutManager);
        rvRelatedVideos.setAdapter(relatedVideoListAdapter);
        relatedVideoListAdapter.setOnVideoClickListener(position -> {
            releasePlayer();
            videoListPojoList.add(videoListPojo);
            videoListPojo = videoListPojoList.get(position);
            videoListPojoList.remove(position);
            relatedVideoListAdapter.notifyDataSetChanged();
            postVideoDetails();
            currentWindow = databaseHelper.getVideo(videoListPojo.getId()).getCurrentWindow();
            playbackPosition =  databaseHelper.getVideo(videoListPojo.getId()).getPlayBackPosition();
            playVideo();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @OnClick({R.id.iv_control, R.id.ivVideoThumb})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.iv_control:
                playVideo();
                break;
            case R.id.ivVideoThumb:
                playVideo();
                break;
        }

    }

    public void playVideo() {
        if(!pageOpened) {
            currentWindow = databaseHelper.getVideo(videoListPojo.getId()).getCurrentWindow();
            playbackPosition =  databaseHelper.getVideo(videoListPojo.getId()).getPlayBackPosition();
        }
        pageOpened = true;
        initializePlayer();
        ivControl.setVisibility(View.GONE);
        ivVideoThumb.setVisibility(View.GONE);
        appBarLayout.setVisibility(View.GONE);
    }

    private void releasePlayer() {
        if (player != null) {
            if (isPlayBackFinish) {
                playbackPosition = 0;
                currentWindow = 0;
                videoListPojo.setCurrentWindow(currentWindow);
                videoListPojo.setPlayBackPosition(playbackPosition);
                databaseHelper.updateVideo(videoListPojo);

            } else {
                playbackPosition = player.getCurrentPosition();
                currentWindow = player.getCurrentWindowIndex();
                videoListPojo.setCurrentWindow(currentWindow);
                videoListPojo.setPlayBackPosition(playbackPosition);
                databaseHelper.updateVideo(videoListPojo);
            }
            player.removeListener(componentListener);
            player.release();

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(pageOpened)
        playVideo();
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

    private class ComponentListener extends Player.DefaultEventListener {

        @Override
        public void onPlayerStateChanged(boolean playWhenReady,
                                         int playbackState) {
            switch (playbackState) {
                case ExoPlayer.STATE_BUFFERING:
                    progressBar.setVisibility(View.VISIBLE);
                    break;
                case ExoPlayer.STATE_READY:
                    progressBar.setVisibility(View.GONE);
                    break;
                case ExoPlayer.STATE_ENDED:
                    ivControl.setVisibility(View.VISIBLE);
                    appBarLayout.setVisibility(View.VISIBLE);
                    ivVideoThumb.setVisibility(View.VISIBLE);
                    ivControl.setImageResource(R.drawable.ic_replay);
                    isPlayBackFinish = true;
                    releasePlayer();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        videoListPojo.setCurrentWindow(currentWindow);
        videoListPojo.setPlayBackPosition(playbackPosition);
        databaseHelper.updateVideo(videoListPojo);

    }

}
