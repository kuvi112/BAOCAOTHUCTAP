package com.example.ongnauvi.nhacviec;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class TabTKB extends AppCompatActivity {

    private TKBDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_tkb);

        database = new TKBDatabase(this);




    }
}
