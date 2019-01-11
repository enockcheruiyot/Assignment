package com.example.app.sampletest.CommonUtils;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.app.sampletest.R;


/*
 * Created by Prabhakaran on 10/1/2019
 */
public class CustomProgressDialog extends android.app.ProgressDialog {
    private Context context;
    public Animation innerProgress;
    public ImageView ivProgress;

    public static CustomProgressDialog getInstance(Context context) {
        CustomProgressDialog customProgressDialog = new CustomProgressDialog(context, R.style.progress_style);
        customProgressDialog.setIndeterminate(true);
        customProgressDialog.setCancelable(false);
        return customProgressDialog;
    }

    public CustomProgressDialog(Context context) {
        super(context);
        this.context = context;
    }

    private CustomProgressDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_custom_progress_dialog);
         ivProgress = findViewById(R.id.ivProgress);

        innerProgress = AnimationUtils.loadAnimation(context, R.anim.anim_inner_progress);
        innerProgress.setRepeatCount(Animation.INFINITE);
        ivProgress.startAnimation(innerProgress);

        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().setDimAmount(0.0f);
    }




}