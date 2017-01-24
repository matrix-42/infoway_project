package com.example.ningli.signindemo;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.crashlytics.android.Crashlytics;
import com.example.ningli.signindemo.database.DBHelper;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import io.fabric.sdk.android.Fabric;

import static com.example.ningli.signindemo.database.DBHelper.TABLE_NAME;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "Dw2H26JbXaZuFsexVE61Tblp2";
    private static final String TWITTER_SECRET = "DdI3lsdd6E9B2w9kBMdbpddwssnKnj98HHAdPZ6A2mfVWDkULo";

    SQLiteDatabase database;
    private GoogleApiClient googleApiClient;
    private int RC_SIGN_IN = 007;
    private CallbackManager callbackManager;

    private TwitterLoginButton twitterLoginButton;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount acc = result.getSignInAccount();
                String email = acc.getEmail();
                signOutGoogle();
                handleSignIn(email, "googlePlus");
            }
        }
        else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
            twitterLoginButton.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleSignIn(String ID, String type) {
        Intent intent = new Intent(MainActivity.this, SuccessActivity.class);
        Cursor cursor = database.query(TABLE_NAME, new String[]{"Id","UserName","Password"},
                "UserName = ? and Type = ?", new String[]{ID, type}, null, null,null);
        String id;
        if (!cursor.moveToNext()) {
            ContentValues values = new ContentValues();
            values.put("UserName", ID);
            values.put("Type", type);


            id = String.valueOf(database.insert(TABLE_NAME, null, values));
            String sql_creat = "create table if not exists " + "_" + id +
                    " (Id integer primary key AUTOINCREMENT, item text, num integer, state integer, image text)";
            database.execSQL(sql_creat);
        }
        else {
            id = cursor.getString(cursor.getColumnIndex("Id"));
        }
            intent.putExtra("UserId", "_" + id);
            startActivity(intent);



    }

    private void signOutGoogle() {
        if (googleApiClient.isConnected()) {
            Auth.GoogleSignInApi.revokeAccess(googleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            Log.d(MainActivity.class.getSimpleName(), "Revoke access using Google Api.");
                        }
                    });
        } else {
            Log.d(MainActivity.class.getSimpleName(), "Can not Revoke access using Google Api.");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig), new Crashlytics());

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_main);

        SQLiteOpenHelper db = DBHelper.getInstance(this);
        database = db.getWritableDatabase();

        findViewById(R.id.SignUp).setOnClickListener(this);
        findViewById(R.id.SignIn).setOnClickListener(this);
        findViewById(R.id.SignInGoogle).setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        LoginButton loginButton = (LoginButton)findViewById(R.id.SignInFacebook);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                String facebookID = loginResult.getAccessToken().getUserId();
                LoginManager.getInstance().logOut();
                handleSignIn(facebookID, "facebook");
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        twitterLoginButton = (TwitterLoginButton) findViewById(R.id.SignInTwitter);
        twitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // The TwitterSession is also available through:
                // Twitter.getInstance().core.getSessionManager().getActiveSession()
                TwitterSession session = result.data;
                // TODO: Remove toast and use the TwitterSession's userID
                // with your app's user model
                //String msg = "@" + session.getUserName() + " logged in! (#" + session.getUserId() + ")";
                //Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                String twitterID = String.valueOf(session.getUserId());
                handleSignIn(twitterID, "twitter");
            }
            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.SignUp:
                signUp();
                break;
            case R.id.SignIn:
                signIn();
                break;
            case R.id.SignInGoogle:
                signInGoogle();
                break;
        }

    }

    private void signInGoogle() {
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent, RC_SIGN_IN);
    }

    private void signIn() {
        Intent intent = new Intent(MainActivity.this, SuccessActivity.class);

        EditText txtname = (EditText)findViewById(R.id.UserName);
        String name_input      =  txtname.getText().toString();

        EditText textpassword = (EditText)findViewById(R.id.Password);
        String password_input      =  textpassword.getText().toString();

        Cursor cursor = database.query(TABLE_NAME, new String[]{"Id","UserName","Password","Type"},
                "UserName = ? and Type = ?", new String[]{name_input, "Local"}, null, null,null);

        while(cursor.moveToNext()){
            String id = cursor.getString(cursor.getColumnIndex("Id"));
            String password = cursor.getString(cursor.getColumnIndex("Password"));

            if (password.equals(password_input))
                intent.putExtra("UserId", "_" + id);
            startActivity(intent);
        }
    }

    private void signUp() {
        Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(MainActivity.class.getSimpleName(), "onConnectionFailed:" + connectionResult);
    }
}
