package com.example.app.sampletest.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.example.app.sampletest.CommonUtils.Utilities;
import com.example.app.sampletest.ModelViewPresenter.Model.VideoListPojo;
import com.example.app.sampletest.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RelatedVideoListAdapter extends RecyclerView.Adapter<RelatedVideoListAdapter.ViewHolder> {
    private Context context;
    private List<VideoListPojo> videoListPojoList;
    private VideoListClickListener videoListClickListener;

    public RelatedVideoListAdapter(Context videoListActivity, List<VideoListPojo> videoListPojoList) {
        this.context = videoListActivity;
        this.videoListPojoList = videoListPojoList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_related_videos, parent, false);

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
            if(Utilities.isNetworkAvailable(context))
                videoListClickListener.getListPosition(position);
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
        @BindView(R.id.iv_thumb)
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
    public void setOnVideoClickListener(VideoListClickListener videoClickListener){
        this.videoListClickListener = videoClickListener;
    }
    public interface VideoListClickListener
    {
        void getListPosition(int position);
    }
}
