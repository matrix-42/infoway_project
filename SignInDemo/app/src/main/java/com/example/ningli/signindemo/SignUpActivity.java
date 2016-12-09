package com.example.ningli.signindemo;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ningli.signindemo.database.DBHelper;

public class SignUpActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        final SQLiteOpenHelper db = DBHelper.getInstance(this);

        findViewById(R.id.Done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                EditText txtname = (EditText)findViewById(R.id.InputUserName);
                String name      =  txtname.getText().toString();

                EditText txtpassword = (EditText)findViewById(R.id.InputPassword);
                String password      =  txtpassword.getText().toString();

                SQLiteDatabase database = db.getWritableDatabase();

                Cursor cursor = database.query(DBHelper.TABLE_NAME, new String[]{"id","UserName","Password"}, "UserName = ?", new String[]{name}, null, null,null);

                if (cursor.moveToNext()){
                    Toast.makeText(SignUpActivity.this, "Username used!", Toast.LENGTH_LONG).show();
                }
                else if (name.isEmpty() || password.isEmpty())
                    Toast.makeText(SignUpActivity.this, "Invalid Username or Password!", Toast.LENGTH_LONG).show();
                else {
                    database.beginTransaction();
                    ContentValues values = new ContentValues();
                    values.put("UserName", name);
                    values.put("Password", password);

                    database.insert(DBHelper.TABLE_NAME, null, values);
                    database.setTransactionSuccessful();
                    database.endTransaction();

                    startActivity(intent);
                }
            }
        });

    }
}
