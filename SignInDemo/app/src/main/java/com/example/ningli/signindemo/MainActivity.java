package com.example.ningli.signindemo;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ningli.signindemo.database.DBHelper;

public class MainActivity extends AppCompatActivity {
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SQLiteOpenHelper db = DBHelper.getInstance(this);
        database = db.getWritableDatabase();


        //database.execSQL("insert into " + DBHelper.TABLE_NAME + " (Id, UserName, Password) values (null, 'admin', 'admin')");
        Toast.makeText(this, "database", Toast.LENGTH_LONG).show();

        findViewById(R.id.SignUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.SignIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SuccessActivity.class);
                database = db.getReadableDatabase();

                EditText txtname = (EditText)findViewById(R.id.UserName);
                String name_input      =  txtname.getText().toString();

                EditText textpassword = (EditText)findViewById(R.id.Password);
                String password_input      =  textpassword.getText().toString();

                Cursor cursor = database.query(DBHelper.TABLE_NAME, new String[]{"id","UserName","Password"}, "UserName = ?", new String[]{name_input}, null, null,null);

                while(cursor.moveToNext()){
                    String password = cursor.getString(cursor.getColumnIndex("Password"));
                if (password.equals(password_input))
                    startActivity(intent);
                }

            }
        });
    }
}
