/*
 * Created by Prabhakaran on 10/1/2019
 */
package com.example.app.sampletest.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.app.sampletest.CommonUtils.Utilities;
import com.example.app.sampletest.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        signConfig();
    }
    @OnClick({R.id.sign_in_button,R.id.tvSignIn})
            public void OnClick(View view){
        switch (view.getId())
        {
            case R.id.sign_in_button:
                if(Utilities.isNetworkAvailable(this)) {
                    UserSignInMethod();
                }else {
                    Toast.makeText(this, "Check your Network Connection!", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }
    public void UserSignInMethod(){
        Intent AuthIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(AuthIntent, RequestSignInCode);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RequestSignInCode){
            GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (googleSignInResult.isSuccess()){
                GoogleSignInAccount googleSignInAccount = googleSignInResult.getSignInAccount();
                FirebaseUserAuth(googleSignInAccount);
            }
        }
    }

    public void FirebaseUserAuth(GoogleSignInAccount googleSignInAccount) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
        firebaseAuth.signInWithCredential(authCredential)
                .addOnCompleteListener(MainActivity.this, (OnCompleteListener) AuthResultTask -> {
                    if (AuthResultTask.isSuccessful()){
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                       Intent intent = new Intent(MainActivity.this,VideoListActivity.class);
                       startActivity(intent);
                       finish();
                        assert firebaseUser != null;
                        Toast.makeText(MainActivity.this,"Hi "+firebaseUser.getDisplayName()+"!",Toast.LENGTH_LONG).show();
                        sharedPrefManager.saveIsLoggedIn(MainActivity.this,true);
                    }else {
                        Toast.makeText(MainActivity.this,"Something Went Wrong",Toast.LENGTH_LONG).show();
                    }
                });
    }

}
