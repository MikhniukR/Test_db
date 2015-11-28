package com.mikhniuk.test_db;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity {
    public static EditText id;
    public static String s_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        id = (EditText) findViewById(R.id.id);
    }

    public void Get(View v){
        s_id = id.getText().toString();
        Intent intent = new Intent(this,CreateActivity.class);
        intent.putExtra("id",s_id);
        startActivity(intent);
    }
    public void Create_new(View v){
        Intent intent = new Intent(this,CreateActivity.class);
        startActivity(intent);
    }
}