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
import android.widget.Toast;

import com.example.ningli.signindemo.database.DBHelper;
import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import static com.example.ningli.signindemo.database.DBHelper.TABLE_NAME;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    SQLiteDatabase database;
    private GoogleApiClient googleApiClient;
    private int RC_SIGN_IN = 007;
    private CallbackManager callbackManager;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);
        }
    }

    private void handleResult(GoogleSignInResult result) {
        Intent intent = new Intent(MainActivity.this, SuccessActivity.class);
        if (result.isSuccess()) {
            GoogleSignInAccount acc = result.getSignInAccount();
            String email = acc.getEmail();
            Toast.makeText(this, email, Toast.LENGTH_SHORT).show();

            signOut();


            Cursor cursor = database.query(TABLE_NAME, new String[]{"Id","UserName","Password"},
                    "UserName = ? and Type = ?", new String[]{email, "googlePlus"}, null, null,null);
            String id;
            if (!cursor.moveToNext()) {
                ContentValues values = new ContentValues();
                values.put("UserName", email);
                values.put("Type", "googlePlus");


                id = String.valueOf(database.insert(TABLE_NAME, null, values));
                String sql_creat = "create table if not exists " + "_" + id +
                        " (Id integer primary key AUTOINCREMENT, item text, num integer, state integer)";
                database.execSQL(sql_creat);
            }
            else {
                id = cursor.getString(cursor.getColumnIndex("Id"));
            }
                intent.putExtra("UserId", "_" + id);
                startActivity(intent);

        }

    }
    private void signOut() {
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
