package com.example.app.sampletest.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.app.sampletest.Activities.VideoDetailsActivity;
import com.example.app.sampletest.CommonUtils.Constants;
import com.example.app.sampletest.CommonUtils.Utilities;
import com.example.app.sampletest.ModelViewPresenter.Model.VideoListPojo;
import com.example.app.sampletest.R;
import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.ViewHolder> {
    private Context context;
    private List<VideoListPojo> videoListPojoList;

    public VideoListAdapter(Context videoListActivity, List<VideoListPojo> videoListPojoList) {
        this.context = videoListActivity;
        this.videoListPojoList = videoListPojoList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_video_list, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        VideoListPojo videoListPojo = videoListPojoList.get(position);
        holder.tvVideoTitle.setText(videoListPojo.getTitle());
        holder.tvVideoDescription.setText(videoListPojo.getDescription());
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.default_thumbnail)
                .error(R.drawable.default_thumbnail);
        Glide.with(context).load(videoListPojo.getThumb()).apply(options).
                into(holder.ivVideoThumb);
        holder.constraintLayout.setOnClickListener(v -> {
            if(Utilities.isNetworkAvailable(context)) {
                Intent intent = new Intent(context, VideoDetailsActivity.class);
                intent.putExtra(Constants.VideoDetails, new Gson().toJson(videoListPojoList.get(position)));
                intent.putExtra(Constants.VideoPosition, position);
                intent.putExtra(Constants.VideoListPojo, new Gson().toJson(videoListPojoList));
                context.startActivity(intent);
            }
            else {
                Toast.makeText(context, "Check your Network Connection!", Toast.LENGTH_SHORT).show();
            }
        });
        }


    @Override
    public int getItemCount() {
        return videoListPojoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivVideoThumb)
        ImageView ivVideoThumb;
        @BindView(R.id.tvVideoTitle)
        TextView tvVideoTitle;
        @BindView(R.id.tvVideoDescription)
        TextView tvVideoDescription;
        @BindView(R.id.ct_parent)
        ConstraintLayout constraintLayout;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

    }
}
