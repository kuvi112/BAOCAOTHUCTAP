package com.example.ongnauvi.nhacviec;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity {
    TabHost TH ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TH = (TabHost)findViewById(android.R.id.tabhost);

        TabHost.TabSpec TabDS = TH.newTabSpec("TabDS");
        TabHost.TabSpec TabTKB = TH.newTabSpec("TabTKB");

        TabDS.setIndicator("Danh Sách");
        TabDS.setContent(new Intent(this,TabDS.class));

        TabTKB.setIndicator("Thời Khóa Biểu");
        TabTKB.setContent(new Intent(this, TabTKB.class));

        TH.addTab(TabDS);
        TH.addTab(TabTKB);
    }
}
