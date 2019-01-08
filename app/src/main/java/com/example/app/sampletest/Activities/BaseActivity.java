package com.example.app.sampletest.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.TextView;

import com.example.app.sampletest.CommonUtils.DatabaseHelper;
import com.example.app.sampletest.CommonUtils.SharedPrefManager;
import com.example.app.sampletest.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.ButterKnife;

public class BaseActivity extends AppCompatActivity {
    public static final int RequestSignInCode = 7;
    public DatabaseHelper databaseHelper;
    public FirebaseAuth firebaseAuth;
    public GoogleApiClient googleApiClient;
    public SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPrefManager = new SharedPrefManager(this);
        databaseHelper = new DatabaseHelper(this);
    }

    public void signConfig() {
        firebaseAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, connectionResult -> {

                }).addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();

    }
    public void logOut() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (dialog.getWindow() != null)
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_logout);
        ButterKnife.bind(dialog);
        dialog.setCancelable(true);
        TextView no = dialog.findViewById(R.id.no);
        TextView yes = dialog.findViewById(R.id.yes);
        yes.setOnClickListener(v -> {
            dialog.dismiss();
            closeSession();
        });
        no.setOnClickListener(v -> dialog.dismiss());
        dialog.show();

    }
    public void closeSession() {
        firebaseAuth.signOut();
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                (ResultCallback) result -> {
                    sharedPrefManager.saveIsLoggedIn(BaseActivity.this, false);
                    sharedPrefManager.clear();
                    finish();
                    Intent intent = new Intent(BaseActivity.this, MainActivity.class);
                    startActivity(intent);

                });
    }
}
